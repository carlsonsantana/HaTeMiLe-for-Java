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

import java.util.Arrays;
import java.util.Objects;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * The JsoupHTMLDOMNode class is official implementation of HTMLDOMNode
 * interface for the Jsoup library.
 */
public abstract class JsoupHTMLDOMNode implements HTMLDOMNode {

    /**
     * The Jsoup native node encapsulated.
     */
    protected Node node;

    /**
     * Initializes a new object that encapsulate the Jsoup Node.
     * @param jsoupNode The Jsoup Node.
     */
    protected JsoupHTMLDOMNode(final Node jsoupNode) {
        this.node = Objects.requireNonNull(jsoupNode);
    }

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
