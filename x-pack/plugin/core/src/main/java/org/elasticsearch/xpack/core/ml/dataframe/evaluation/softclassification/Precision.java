/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.core.ml.dataframe.evaluation.softclassification;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.xpack.core.ml.dataframe.evaluation.EvaluationMetricResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Precision extends AbstractConfusionMatrixMetric {

    public static final ParseField NAME = new ParseField("precision");

    private static final ConstructingObjectParser<Precision, Void> PARSER = new ConstructingObjectParser<>(NAME.getPreferredName(),
        a -> new Precision((List<Double>) a[0]));

    static {
        PARSER.declareDoubleArray(ConstructingObjectParser.constructorArg(), AT);
    }

    public static Precision fromXContent(XContentParser parser) {
        return PARSER.apply(parser, null);
    }

    public Precision(List<Double> at) {
        super(at.stream().mapToDouble(Double::doubleValue).toArray());
    }

    public Precision(StreamInput in) throws IOException {
        super(in);
    }

    @Override
    public String getWriteableName() {
        return NAME.getPreferredName();
    }

    @Override
    public String getName() {
        return NAME.getPreferredName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Precision that = (Precision) o;
        return Arrays.equals(thresholds, that.thresholds);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(thresholds);
    }

    @Override
    protected List<AggregationBuilder> aggsAt(String labelField, List<ClassInfo> classInfos, double threshold) {
        List<AggregationBuilder> aggs = new ArrayList<>();
        for (ClassInfo classInfo : classInfos) {
            aggs.add(buildAgg(classInfo, threshold, Condition.TP));
            aggs.add(buildAgg(classInfo, threshold, Condition.FP));
        }
        return aggs;
    }

    @Override
    public EvaluationMetricResult evaluate(ClassInfo classInfo, Aggregations aggs) {
        double[] precisions = new double[thresholds.length];
        for (int i = 0; i < precisions.length; i++) {
            double threshold = thresholds[i];
            Filter tpAgg = aggs.get(aggName(classInfo, threshold, Condition.TP));
            Filter fpAgg = aggs.get(aggName(classInfo, threshold, Condition.FP));
            long tp = tpAgg.getDocCount();
            long fp = fpAgg.getDocCount();
            precisions[i] = tp + fp == 0 ? 0.0 : (double) tp / (tp + fp);
        }
        return new ScoreByThresholdResult(NAME.getPreferredName(), thresholds, precisions);
    }
}
