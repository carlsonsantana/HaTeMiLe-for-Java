/*
 * Copyright 2017 carlson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hatemile.util.html.jsoup;

import java.util.Arrays;
import java.util.Objects;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 *
 * @author carlson
 */
public abstract class JsoupHTMLDOMNode implements HTMLDOMNode {

    /**
     * The Jsoup native node encapsulated.
     */
    protected Node node;

    /**
     * {@inheritDoc}
     */
    public String getTextContent() {
        return ((Element) node).text();
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode insertBefore(final HTMLDOMNode newNode) {
        Element parent = (Element) node.parent();
        int index = parent.childNodes().indexOf(node);
        parent.insertChildren(index, Arrays.asList((Node) newNode.getData()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode insertAfter(final HTMLDOMNode newNode) {
        Element parent = (Element) node.parent();
        int index = parent.childNodes().indexOf(node);
        if (index < parent.childNodes().size()) {
            parent.insertChildren(index + 1,
                    Arrays.asList((Node) newNode.getData()));
        } else {
            parent.appendChild((Node) newNode.getData());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode removeNode() {
        node.remove();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode replaceNode(final HTMLDOMNode newNode) {
        node.replaceWith((Node) newNode.getData());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode appendText(final String text) {
        Objects.requireNonNull(text);

        if (node instanceof Element) {
            ((Element) node).appendText(text);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode prependText(final String text) {
        Objects.requireNonNull(text);

        if (node instanceof Element) {
            ((Element) node).prependText(text);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement getParentElement() {
        Node parent = node.parent();
        if ((parent == null) || (parent instanceof Document)) {
            return null;
        }
        return new JsoupHTMLDOMElement((Element) parent);
    }

    /**
     * {@inheritDoc}
     */
    public Object getData() {
        return node;
    }

    /**
     * {@inheritDoc}
     */
    public void setData(final Object data) {
        node = (Node) Objects.requireNonNull(data);
    }
}
