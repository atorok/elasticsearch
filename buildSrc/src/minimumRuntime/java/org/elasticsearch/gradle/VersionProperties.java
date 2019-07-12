/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.gradle;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Accessor for shared dependency versions used by elasticsearch, namely the elasticsearch and lucene versions.
 */
public class VersionProperties {

    public static String getElasticsearch() {
        return elasticsearch;
    }

    public static String getLucene() {
        return lucene;
    }

    public static String getBundledJdk() {
        return bundledJdk;
    }

    public static Map<String, String> getVersions() {
        return versions;
    }

    private static final String elasticsearch;
    private static final String lucene;
    private static final String bundledJdk;
    private static final Map<String, String> versions = new HashMap<String, String>();

    static {
        Properties props = getVersionProperties();
        elasticsearch = props.getProperty("elasticsearch");
        lucene = props.getProperty("lucene");
        bundledJdk = props.getProperty("bundled_jdk");

        for (String property : props.stringPropertyNames()) {
            versions.put(property, props.getProperty(property));
        }
    }

    private static Properties getVersionProperties() {
        Properties props = new Properties();
        InputStream propsStream = VersionProperties.class.getResourceAsStream("/version.properties");
        if (propsStream == null) {
            throw new IllegalStateException("/version.properties resource missing");
        }
        try {
            props.load(propsStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load version properties", e);
        }
        return props;
    }

    public static boolean isElasticsearchSnapshot() {
        return elasticsearch.endsWith("-SNAPSHOT");
    }
}
