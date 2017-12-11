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
package org.hatemile.util.css.phcss;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.writer.CSSWriterSettings;
import java.util.ArrayList;
import java.util.List;
import org.hatemile.util.css.StyleSheetDeclaration;
import org.hatemile.util.css.StyleSheetRule;

/**
 *
 * @author carlson
 */
public class PHCSSRule implements StyleSheetRule {

    /**
     * The ph-css rule.
     */
    protected final CSSStyleRule rule;

    /**
     * The settings of ph-css.
     */
    protected static CSSWriterSettings writerSettings;

    /**
     * Initializes a new object that encapsulate the ph-css rule.
     * @param styleRule The ph-css rule.
     */
    public PHCSSRule(final CSSStyleRule styleRule) {
        rule = styleRule;
        if (writerSettings == null) {
            writerSettings = new CSSWriterSettings(ECSSVersion.CSS30);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasProperty(final String propertyName) {
        for (CSSDeclaration declaration : rule.getAllDeclarations()) {
            if (declaration.getProperty().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasDeclarations() {
        return rule.hasDeclarations();
    }

    /**
     * {@inheritDoc}
     */
    public List<StyleSheetDeclaration> getDeclarations(
            final String propertyName) {
        List<StyleSheetDeclaration> sheetDeclarations =
                new ArrayList<StyleSheetDeclaration>();
        for (CSSDeclaration declaration : rule.getAllDeclarations()) {
            if (declaration.getProperty().equals(propertyName)) {
                sheetDeclarations.add(new PHCSSDeclaration(declaration));
            }
        }
        return sheetDeclarations;
    }

    /**
     * {@inheritDoc}
     */
    public String getSelector() {
        String selectorString = "";
        for (CSSSelector selector : rule.getAllSelectors()) {
            if (selectorString.isEmpty()) {
                selectorString = selector.getAsCSSString(writerSettings);
            } else {
                selectorString += "," + selector.getAsCSSString(writerSettings);
            }
        }
        return selectorString;
    }
}
