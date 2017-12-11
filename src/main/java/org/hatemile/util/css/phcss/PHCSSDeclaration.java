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
import com.helger.css.writer.CSSWriterSettings;
import java.util.Arrays;
import java.util.List;
import org.hatemile.util.css.StyleSheetDeclaration;

/**
 *
 * @author carlson
 */
public class PHCSSDeclaration implements StyleSheetDeclaration {

    /**
     * The ph-css declaration.
     */
    protected final CSSDeclaration declaration;

    /**
     * The settings of ph-css.
     */
    protected static CSSWriterSettings writerSettings;

    /**
     * Initializes a new object that encapsulate the ph-css declaration.
     * @param cssDeclaration The ph-css declaration.
     */
    public PHCSSDeclaration(final CSSDeclaration cssDeclaration) {
        this.declaration = cssDeclaration;
        if (writerSettings == null) {
            writerSettings = new CSSWriterSettings(ECSSVersion.CSS30);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getValue() {
        return declaration.getExpression().getAsCSSString(writerSettings);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getValues() {
        return Arrays.asList(declaration.getExpression()
                .getAsCSSString(writerSettings).split(","));
    }

    /**
     * {@inheritDoc}
     */
    public String getProperty() {
        return declaration.getProperty();
    }
}
