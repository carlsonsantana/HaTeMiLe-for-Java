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
package org.hatemile.util.css;

import java.util.List;

/**
 * The StyleSheetDeclaration interface contains the methods for access the CSS
 * declaration.
 */
public interface StyleSheetDeclaration {

    /**
     * Returns the value of declaration.
     * @return The value of declaration.
     */
    String getValue();

    /**
     * Returns a array with the values of declaration.
     * @return The array with the values of declaration.
     */
    List<String> getValues();

    /**
     * Returns the property of declaration.
     * @return The property of declaration.
     */
    String getProperty();
}
