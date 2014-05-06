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
import hatemile.util.HTMLDOMParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupHTMLDOMParser implements HTMLDOMParser {
	
	protected Document document;
	protected Elements results;
	
	public JsoupHTMLDOMParser(File file) throws IOException {
		this(file, "UTF-8");
	}
	
	public JsoupHTMLDOMParser(File file, String charset) throws IOException {
		document = Jsoup.parse(file, charset);
	}
	
	public JsoupHTMLDOMParser(String code) {
		document = Jsoup.parse(code);
	}
	
	public HTMLDOMParser find(String selector) {
		results = document.select(selector);
		return this;
	}
	
	public HTMLDOMParser find(HTMLDOMElement element) {
		Collection<Element> elements = new ArrayList<Element>();
		elements.add((Element) element.getData());
		results = new Elements(elements);
		return this;
	}

	public HTMLDOMParser findChildren(String selector) {
		Collection<Element> elements = new ArrayList<Element>();
		Elements descendants = results.select(selector);
		for (Element element : descendants) {
			if ((results.contains(element.parent())) && (!elements.contains(element))) {
				elements.add(element);
			}
		}
		results = new Elements(elements);
		return this;
	}
	
	public HTMLDOMParser findChildren(HTMLDOMElement child) {
		Collection<Element> elements = new ArrayList<Element>();
		Element element = (Element) child.getData();
		for (Element result : results) {
			if (result.children().contains(element)) {
				elements.add(element);
				break;
			}
		}
		results = new Elements(elements);
		return this;
	}
	
	public HTMLDOMParser findDescendants(String selector) {
		results = results.select(selector);
		return this;
	}
	
	public HTMLDOMParser findDescendants(HTMLDOMElement descendant) {
		Collection<Element> elements = new ArrayList<Element>();
		for (Element resultElement : results) {
			Element element = getDescendantOf(resultElement, (Element) descendant.getData());
			if ((element != null) && (!elements.contains(element))) {
				elements.add(element);
			}
		}
		results = new Elements(elements);
		return this;
	}

	public HTMLDOMParser findAncestors(String selector) {
		Elements findedElements = document.select(selector);
		Collection<Element> elements = new ArrayList<Element>();
		for (Element element : findedElements) {
			if ((results.parents().contains(element)) && (!elements.contains(element))) {
				elements.add(element);
			}
		}
		results = new Elements(elements);
		return this;
	}
	
	public HTMLDOMParser findAncestors(HTMLDOMElement element) {
		if (results.parents().contains((Element) element.getData())) {
			Collection<Element> elements = new ArrayList<Element>();
			elements.add((Element) element.getData());
			results = new Elements(elements);
		} else {
			results = new Elements();
		}
		return this;
	}
	
	public void clearParser() {
		results.clear();
		results = null;
		document = null;
	}

	public HTMLDOMElement firstResult() {
		if (results.isEmpty()) {
			return null;
		}
		return new JsoupHTMLDOMElement(results.first());
	}
	
	public HTMLDOMElement lastResult() {
		if (results.isEmpty()) {
			return null;
		}
		return new JsoupHTMLDOMElement(results.last());
	}

	public Collection<HTMLDOMElement> listResults() {
		Collection<HTMLDOMElement> elements = new ArrayList<HTMLDOMElement>();
		for (Element element : results) {
			elements.add(new JsoupHTMLDOMElement(element));
		}
		return elements;
	}

	public String getHTML() {
		return JsoupAuxiliarToString.toString(document);
	}

	public HTMLDOMElement createElement(String tag) {
		return new JsoupHTMLDOMElement(document.createElement(tag));
	}

	protected Element getDescendantOf(Element reference, Element searched) {
		if (reference.children().contains(searched)) {
			return searched;
		} else if (!reference.children().isEmpty()) {
			Elements children = reference.children();
			for (Element child : children) {
				Element element = getDescendantOf(child, searched);
				if (element != null) {
					return element;
				}
			}
		}
		return null;
	}
}