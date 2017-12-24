/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.hatemile.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The Configure class contains the configuration of HaTeMiLe.
 */
public class Configure {

    /**
     * The parameters of configuration of HaTeMiLe.
     */
    protected final ResourceBundle resourceBundle;

    /**
     * Initializes a new object that contains the configuration of HaTeMiLe.
     */
    public Configure() {
        this(Locale.getDefault());
    }

    /**
     * Initializes a new object that contains the configuration of HaTeMiLe.
     * @param fileName The full path of file.
     */
    public Configure(final String fileName) {
        this(fileName, Locale.getDefault());
    }

    /**
     * Initializes a new object that contains the configuration of HaTeMiLe.
     * @param locale The locale of configuration.
     */
    public Configure(final Locale locale) {
        resourceBundle = ResourceBundle.getBundle("hatemile-configure",
                Objects.requireNonNull(locale));
    }

    /**
     * Initializes a new object that contains the configuration of HaTeMiLe.
     * @param fileName The full path of file.
     * @param locale The locale of configuration.
     */
    public Configure(final String fileName, final Locale locale) {
        try {
            File file = new File(fileName);
            URL[] urls = {file.getParentFile().toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            resourceBundle = ResourceBundle
                    .getBundle(Objects.requireNonNull(file.getName()),
                        Objects.requireNonNull(locale), loader);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the parameters of configuration.
     * @return The parameters of configuration.
     */
    public Map<String, String> getParameters() {
        Enumeration<String> keys;
        String key;
        Map<String, String> map = new HashMap<String, String>();
        for (keys = resourceBundle.getKeys(); keys.hasMoreElements();) {
            key = keys.nextElement().replace('.', '-');
            map.put(key, resourceBundle.getString(key));
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Check that the configuration has an parameter.
     * @param parameter The name of parameter.
     * @return True if the configuration has the parameter false if the
     * configuration not has the parameter.
     */
    public boolean hasParameter(final String parameter) {
        return resourceBundle.containsKey(parameter.replace('-', '.'));
    }

    /**
     * Returns the value of a parameter of configuration.
     * @param parameter The parameter.
     * @return The value of the parameter.
     */
    public String getParameter(final String parameter) {
        return resourceBundle.getString(parameter.replace('-', '.'));
    }

    @Override
    public boolean equals(final Object object) {
        if (this != object) {
            if (object == null) {
                return false;
            }
            if (!(object instanceof Configure)) {
                return false;
            }
            Configure configure = (Configure) object;
            if (!getParameters().equals(configure.getParameters())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.resourceBundle.hashCode();
    }
}
