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
package org.elasticsearch.gradle.info;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;

import java.io.File;

public class JavaHome {
    private Integer version;
    private File javaHome;

    private JavaHome(int version, File javaHome) {
        this.version = version;
        this.javaHome = javaHome;
    }

    public static JavaHome of(int version, File javaHome) {
        return new JavaHome(version, javaHome);
    }

    @Input
    public Integer getVersion() {
        return version;
    }

    @InputDirectory
    @Optional
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getJavaHome() {
        return javaHome;
    }
}
