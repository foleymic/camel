/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.runtimecatalog.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.CatalogCamelContext;
import org.apache.camel.runtimecatalog.JSonSchemaResolver;
import org.apache.camel.spi.ClassResolver;
import org.apache.camel.util.IOHelper;

/**
 * Uses runtime {@link CamelContext} to resolve the JSon schema files.
 */
public class CamelContextJSonSchemaResolver implements JSonSchemaResolver {

    private final CatalogCamelContext camelContext;

    public CamelContextJSonSchemaResolver(CamelContext camelContext) {
        this.camelContext = camelContext.adapt(CatalogCamelContext.class);
    }

    @Override
    public String getComponentJSonSchema(String name) {
        try {
            return camelContext.getComponentParameterJsonSchema(name);
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    @Override
    public String getDataFormatJSonSchema(String name) {
        try {
            return camelContext.getDataFormatParameterJsonSchema(name);
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    @Override
    public String getLanguageJSonSchema(String name) {
        try {
            return camelContext.getLanguageParameterJsonSchema(name);
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    @Override
    public String getOtherJSonSchema(String name) {
        // not supported
        return null;
    }

    @Override
    public String getModelJSonSchema(String name) {
        try {
            return camelContext.getEipParameterJsonSchema(name);
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    @Override
    public String getMainJsonSchema() {
        String path = "META-INF/camel-main-configuration-metadata.json";
        ClassResolver resolver = camelContext.getClassResolver();
        InputStream inputStream = resolver.loadResourceAsStream(path);
        if (inputStream != null) {
            try {
                return IOHelper.loadText(inputStream);
            } catch (IOException e) {
                // ignore
            } finally {
                IOHelper.close(inputStream);
            }
        }
        return null;
    }
}
