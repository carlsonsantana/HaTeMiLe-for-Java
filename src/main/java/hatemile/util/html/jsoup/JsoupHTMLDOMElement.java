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
package hatemile.util.html.jsoup;

import hatemile.util.html.HTMLDOMElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class JsoupHTMLDOMElement implements HTMLDOMElement {

	/**
	 * The Jsoup native element encapsulated.
	 */
	protected Element element;

	/**
	 * Initializes a new object that encapsulate the Jsoup Element.
	 * @param element The Jsoup Element.
	 */
	public JsoupHTMLDOMElement(final Element element) {
		this.element = element;
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
		element.attr(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAttribute(final String name) {
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
	public HTMLDOMElement cloneElement() {
		return new JsoupHTMLDOMElement(element.clone());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTextContent() {
		return element.text();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getData() {
		return element;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setData(final Object data) {
		element = (Element) data;
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement appendElement(final HTMLDOMElement element) {
		this.element.appendChild((Element) element.getData());
		return element;
	}

	/**
	 * {@inheritDoc}
	 */
	public void appendText(final String text) {
		element.appendText(text);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInnerHTML() {
		List<Node> childNodes = element.childNodes();
		String string = "";
		for (Node node : childNodes) {
			string += toString(node);
		}
		return string;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInnerHTML(final String html) {
		element.html(html);
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
	public HTMLDOMElement insertBefore(final HTMLDOMElement newElement) {
		Element parent = element.parent();
		int index = parent.childNodes().indexOf(element);
		Collection<Element> children = new ArrayList<Element>();
		children.add((Element) newElement.getData());
		parent.insertChildren(index, children);
		return newElement;
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement insertAfter(final HTMLDOMElement newElement) {
		Element parent = element.parent();
		int index = parent.childNodes().indexOf(element);
		if (index < parent.childNodes().size()) {
			Collection<Element> children = new ArrayList<Element>();
			children.add((Element) newElement.getData());
			parent.insertChildren(index + 1, children);
		} else {
			parent.appendChild((Element) newElement.getData());
		}
		return newElement;
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement removeElement() {
		element.remove();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement replaceElement(final HTMLDOMElement newElement) {
		element.replaceWith((Element) newElement.getData());
		return newElement;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<HTMLDOMElement> getChildren() {
		Collection<HTMLDOMElement> elements = new ArrayList<HTMLDOMElement>();
		Elements children = element.children();
		for (Element child : children) {
			elements.add(new JsoupHTMLDOMElement(child));
		}
		return elements;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasChildren() {
		return !element.children().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement getParentElement() {
		Element parent = element.parent();
		if ((parent == null) || (parent instanceof Document)) {
			return null;
		}
		return new JsoupHTMLDOMElement(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement getFirstElementChild() {
		if (!hasChildren()) {
			return null;
		}
		return new JsoupHTMLDOMElement(element.children().first());
	}

	/**
	 * {@inheritDoc}
	 */
	public HTMLDOMElement getLastElementChild() {
		if (!hasChildren()) {
			return null;
		}
		return new JsoupHTMLDOMElement(element.children().last());
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
		int hash = 3;
		hash = 13 * hash + this.element.hashCode();
		return hash;
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
			Element element = (Element) node;
			string += "<" + element.tagName();
			Attributes attributes = element.attributes();
			for (Attribute attribute : attributes) {
				string += " " + attribute.getKey() + "=\"" + attribute.getValue() + "\"";
			}
			if (childNodes.isEmpty() && element.tag().isSelfClosing()) {
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
			Element element = (Element) node;
			if (!childNodes.isEmpty() || !element.tag().isSelfClosing()) {
				string += "</" + element.tagName() + ">";
			}
		}
		return string;
	}
}
