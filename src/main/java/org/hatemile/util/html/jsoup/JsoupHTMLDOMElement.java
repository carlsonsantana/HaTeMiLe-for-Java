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

import org.hatemile.util.html.HTMLDOMElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.hatemile.util.html.HTMLDOMNode;
import org.hatemile.util.html.HTMLDOMTextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * The JsoupHTMLDOMElement class is official implementation of HTMLDOMElement
 * interface for the Jsoup library.
 */
public class JsoupHTMLDOMElement extends JsoupHTMLDOMNode
        implements HTMLDOMElement {

    /**
     * The Jsoup native element encapsulated.
     */
    protected Element element;

    /**
     * Initializes a new object that encapsulate the Jsoup Element.
     * @param jsoupElement The Jsoup Element.
     */
    public JsoupHTMLDOMElement(final Element jsoupElement) {
        super(Objects.requireNonNull(jsoupElement));
        this.element = jsoupElement;
    }

    /**
     * {@inheritDoc}
     */
    public String getTagName() {
        return element.tagName().toUpperCase();
    }

    /**
     * {@inheritDoc}
     */
    public String getAttribute(final String name) {
        return element.attr(name);
    }

    /**
     * {@inheritDoc}
     */
    public void setAttribute(final String name, final String value) {
        element.attr(Objects.requireNonNull(name),
                Objects.requireNonNull(value));
    }

    /**
     * {@inheritDoc}
     */
    public void removeAttribute(final String name) {
        Objects.requireNonNull(name);

        if (hasAttribute(name)) {
            element.removeAttr(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAttribute(final String name) {
        return element.hasAttr(name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAttributes() {
        return element.attributes().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    public String getTextContent() {
        if (getTagName().equals("STYLE")) {
            return element.data();
        } else {
            return element.text();
        }
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement appendElement(final HTMLDOMElement newElement) {
        this.element.appendChild((Element) newElement.getData());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement prependElement(final HTMLDOMElement newElement) {
        this.element.prependChild((Element) newElement.getData());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public List<HTMLDOMElement> getChildrenElements() {
        List<HTMLDOMElement> elements = new ArrayList<HTMLDOMElement>();
        Elements children = element.children();
        for (Element child : children) {
            elements.add(new JsoupHTMLDOMElement(child));
        }
        return Collections.unmodifiableList(elements);
    }

    /**
     * {@inheritDoc}
     */
    public List<HTMLDOMNode> getChildren() {
        List<HTMLDOMNode> children = new ArrayList<HTMLDOMNode>();
        List<Node> childNodes = element.childNodes();
        for (Node child : childNodes) {
            if (child instanceof Element) {
                children.add(new JsoupHTMLDOMElement((Element) child));
            } else if (child instanceof TextNode) {
                children.add(new JsoupHTMLDOMTextNode((TextNode) child));
            }
        }
        return Collections.unmodifiableList(children);
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement appendText(final String text) {
        element.appendText(Objects.requireNonNull(text));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement prependText(final String text) {
        element.prependText(Objects.requireNonNull(text));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement normalize() {
        HTMLDOMNode lastNode = null;
        List<HTMLDOMNode> children = this.getChildren();
        for (HTMLDOMNode child : children) {
            if (lastNode != null) {
                if ((lastNode instanceof HTMLDOMTextNode)
                        && (child instanceof HTMLDOMTextNode)) {
                    child.prependText(lastNode.getTextContent());
                    lastNode.removeNode();
                }
            }
            if (child instanceof HTMLDOMElement) {
                ((HTMLDOMElement) child).normalize();
            }
            lastNode = child;
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChildrenElements() {
        return !element.children().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChildren() {
        List<Node> childNodes = element.childNodes();
        for (Node child : childNodes) {
            if (child instanceof Element) {
                return true;
            } else if (child instanceof TextNode) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getInnerHTML() {
        List<Node> childNodes = element.childNodes();
        String string = "";
        for (Node child : childNodes) {
            string += toString(child);
        }
        return string;
    }

    /**
     * {@inheritDoc}
     */
    public void setInnerHTML(final String html) {
        element.html(Objects.requireNonNull(html));
    }

    /**
     * {@inheritDoc}
     */
    public String getOuterHTML() {
        return toString(element);
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement cloneElement() {
        return new JsoupHTMLDOMElement(element.clone());
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement getFirstElementChild() {
        if (!hasChildrenElements()) {
            return null;
        }
        return new JsoupHTMLDOMElement(element.children().first());
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement getLastElementChild() {
        if (!hasChildrenElements()) {
            return null;
        }
        return new JsoupHTMLDOMElement(element.children().last());
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode getFirstNodeChild() {
        List<Node> childNodes = element.childNodes();
        for (Node child : childNodes) {
            if (child instanceof Element) {
                return new JsoupHTMLDOMElement((Element) child);
            } else if (child instanceof TextNode) {
                return new JsoupHTMLDOMTextNode((TextNode) child);
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMNode getLastNodeChild() {
        Node lastNode = null;
        List<Node> childNodes = element.childNodes();
        for (Node child : childNodes) {
            if (child instanceof Element) {
                lastNode = child;
            } else if (child instanceof TextNode) {
                lastNode = child;
            }
        }

        if (lastNode != null) {
            if (lastNode instanceof Element) {
                return new JsoupHTMLDOMElement((Element) lastNode);
            } else if (lastNode instanceof TextNode) {
                return new JsoupHTMLDOMTextNode((TextNode) lastNode);
            }
        }
        return null;
    }

    @Override
    public boolean equals(final Object object) {
        if (this != object) {
            if (object == null) {
                return false;
            }
            if (!(object instanceof HTMLDOMElement)) {
                return false;
            }
            HTMLDOMElement htmlDOMElement = (HTMLDOMElement) object;
            return this.getData().equals(htmlDOMElement.getData());
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.element.hashCode();
    }

    @Override
    public HTMLDOMElement clone() {
        return cloneElement();
    }

    /**
     * Convert a Jsoup Node to a HTML code.
     * @param node The Jsoup Node.
     * @return The HTML code of the Jsoup Node.
     */
    protected String toString(final Node node) {
        String string = "";
        List<Node> childNodes = node.childNodes();
        if (node instanceof Comment) {
            string += ((Comment) node).toString();
        } else if (node instanceof DataNode) {
            string += ((DataNode) node).toString();
        } else if (node instanceof DocumentType) {
            string += ((DocumentType) node).toString();
        } else if (node instanceof TextNode) {
            string += ((TextNode) node).getWholeText();
        } else if ((node instanceof Element) && (!(node instanceof Document))) {
            Element jsoupElement = (Element) node;
            string += "<" + jsoupElement.tagName();
            Attributes attributes = jsoupElement.attributes();
            for (Attribute attribute : attributes) {
                string += " " + attribute.getKey() + "=\""
                        + attribute.getValue() + "\"";
            }
            if (childNodes.isEmpty() && jsoupElement.tag().isSelfClosing()) {
                string += " />";
            } else {
                string += ">";
            }
        }
        if (!childNodes.isEmpty()) {
            for (Node childNode : childNodes) {
                string += toString(childNode);
            }
        }
        if ((node instanceof Element) && (!(node instanceof Document))) {
            Element jsoupElement = (Element) node;
            if (!childNodes.isEmpty() || !jsoupElement.tag().isSelfClosing()) {
                string += "</" + jsoupElement.tagName() + ">";
            }
        }
        return string;
    }
}
