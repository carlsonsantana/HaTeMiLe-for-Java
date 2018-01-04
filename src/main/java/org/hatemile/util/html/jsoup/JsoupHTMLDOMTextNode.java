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
package org.hatemile.util.html.jsoup;

import java.util.Objects;
import org.hatemile.util.html.HTMLDOMTextNode;
import org.jsoup.nodes.TextNode;

/**
 * The JsoupHTMLDOMTextNode class is official implementation of
 * {@link org.hatemile.util.html.HTMLDOMTextNode} for the Jsoup library.
 */
public class JsoupHTMLDOMTextNode extends JsoupHTMLDOMNode
        implements HTMLDOMTextNode {

    /**
     * The Jsoup native TextNode encapsulated.
     */
    protected TextNode textNode;

    /**
     * Initializes a new object that encapsulate the Jsoup TextNode.
     * @param jsoupTextNode The Jsoup TextNode.
     */
    public JsoupHTMLDOMTextNode(final TextNode jsoupTextNode) {
        super(Objects.requireNonNull(jsoupTextNode));
        this.textNode = jsoupTextNode;
    }

    /**
     * {@inheritDoc}
     */
    public String getTextContent() {
        return textNode.text();
    }

    /**
     * {@inheritDoc}
     */
    public void setTextContent(final String text) {
        textNode.text(Objects.requireNonNull(text));
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMTextNode appendText(final String text) {
        textNode.text(textNode.text() + Objects.requireNonNull(text));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMTextNode prependText(final String text) {
        textNode.text(Objects.requireNonNull(text) + textNode.text());
        return this;
    }
}
