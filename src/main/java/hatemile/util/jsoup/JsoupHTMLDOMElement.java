/*
Copyright 2014 Carlson Santana Cruz

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
package hatemile.util.jsoup;

import hatemile.util.HTMLDOMElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class JsoupHTMLDOMElement implements HTMLDOMElement {
	
	protected Element element;
	
	public JsoupHTMLDOMElement(Element element) {
		this.element = element;
	}

	public String getTagName() {
		return element.tagName().toUpperCase();
	}

	public String getAttribute(String name) {
		return element.attr(name);
	}

	public void setAttribute(String name, String value) {
		element.attr(name, value);
	}

	public void removeAttribute(String name) {
		if (element.hasAttr(name)) {
			element.removeAttr(name);
		}
	}

	public boolean hasAttribute(String name) {
		return element.hasAttr(name);
	}

	public HTMLDOMElement cloneElement() {
		return new JsoupHTMLDOMElement(element.clone());
	}

	public String getTextContent() {
		return element.text();
	}

	public Object getData() {
		return element;
	}

	public void setData(Object data) {
		element = (Element) data;
	}

	public HTMLDOMElement appendElement(HTMLDOMElement element) {
		this.element.appendChild((Element) element.getData());
		return element;
	}

	public void appendText(String text) {
		element.appendText(text);
	}

	public String getInnerHTML() {
		List<Node> childNodes = element.childNodes();
		String string = "";
		for (Node node : childNodes) {
			string += JsoupAuxiliarToString.toString(node);
		}
		return string;
	}

	public void setInnerHTML(String html) {
		element.html(html);
	}

	public String getOuterHTML() {
		return JsoupAuxiliarToString.toString(element);
	}

	public boolean hasAttributes() {
		return element.attributes().size() > 0;
	}

	public HTMLDOMElement insertBefore(HTMLDOMElement newElement) {
		Element parent = element.parent();
		int index = parent.childNodes().indexOf(element);
		Collection<Element> children = new ArrayList<Element>();
		children.add((Element) newElement.getData());
		parent.insertChildren(index, children);
		return newElement;
	}

	public HTMLDOMElement insertAfter(HTMLDOMElement newElement) {
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

	public HTMLDOMElement removeElement() {
		element.remove();
		return this;
	}

	public HTMLDOMElement replaceElement(HTMLDOMElement newElement) {
		element.replaceWith((Element) newElement.getData());
		return newElement;
	}

	public Collection<HTMLDOMElement> getChildren() {
		Collection<HTMLDOMElement> elements = new ArrayList<HTMLDOMElement>();
		Elements children = element.children();
		for (Element child : children) {
			elements.add(new JsoupHTMLDOMElement(child));
		}
		return elements;
	}

	public boolean hasChildren() {
		return !element.children().isEmpty();
	}

	public HTMLDOMElement getParentElement() {
		if ((element.parent() == null) || (element.parent() instanceof Document)) {
			return null;
		}
		return new JsoupHTMLDOMElement(element.parent());
	}

	public HTMLDOMElement getFirstElementChild() {
		if (!hasChildren()) {
			return null;
		}
		return new JsoupHTMLDOMElement(element.children().first());
	}

	public HTMLDOMElement getLastElementChild() {
		if (!hasChildren()) {
			return null;
		}
		return new JsoupHTMLDOMElement(element.children().last());
	}
	
	public boolean equals(Object object) {
		if (this != object) {
			if (!(object instanceof HTMLDOMElement)) {
				return false;
			}
			HTMLDOMElement htmlDOMElement = (HTMLDOMElement) object;
			return this.getData().equals(htmlDOMElement.getData());
		}
		return true;
	}
}