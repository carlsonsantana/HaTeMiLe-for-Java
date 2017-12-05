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
 * The StyleSheetRule interface contains the methods for access the CSS rule.
 */
public interface StyleSheetRule {

    /**
     * Returns that the rule has a declaration with the property.
     * @param propertyName The name of property.
     * @return True if the rule has a declaration with the property or false if
     * the rule not has a declaration with the property.
     */
    boolean hasProperty(String propertyName);

    /**
     * Returns that the rule has declarations.
     * @return True if the rule has the property or false if the rule not has
     * declarations.
     */
    boolean hasDeclarations();

    /**
     * Returns the declarations with the property.
     * @param propertyName The property.
     * @return The declarations with the property.
     */
    List<StyleSheetDeclaration> getDeclarations(String propertyName);

    /**
     * Returns the selector of rule.
     * @return The selector of rule.
     */
    String getSelector();
}
