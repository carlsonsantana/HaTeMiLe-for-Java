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
import org.hatemile.util.html.HTMLDOMParser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The class JsoupHTMLDOMParser is official implementation of HTMLDOMParser
 * interface for the Jsoup library.
 */
public class JsoupHTMLDOMParser implements HTMLDOMParser {

    /**
     * The root element of the parser.
     */
    protected Document document;

    /**
     * The found elements.
     */
    protected Elements results;

    /**
     * Initializes a new object that encapsulate the parser of Jsoup.
     * @param jsoupDocument The root element of the parser.
     */
    public JsoupHTMLDOMParser(final Document jsoupDocument) {
        this.document = jsoupDocument;
    }

    /**
     * Initializes a new object that encapsulate the parser of Jsoup.
     * @param code The HTML code.
     */
    public JsoupHTMLDOMParser(final String code) {
        document = Jsoup.parse(code);
    }

    /**
     * Search if the element is descendant of another element.
     * @param reference The reference element.
     * @param searched The element searched.
     * @return The element searched if it is descendant of reference element or
     * null if not is.
     */
    protected Element getDescendantOf(final Element reference,
            final Element searched) {
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

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser find(final String selector) {
        results = document.select(selector.replaceAll("\"", "")
                .replaceAll("'", ""));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser find(final HTMLDOMElement element) {
        results = new Elements((Element) element.getData());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser findChildren(final String selector) {
        Elements elements = new Elements();
        Elements descendants = results.select(selector.replaceAll("\"", "")
                .replaceAll("'", ""));
        for (Element element : descendants) {
            if ((results.contains(element.parent()))
                    && (!elements.contains(element))) {
                elements.add(element);
            }
        }
        results = elements;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser findChildren(final HTMLDOMElement child) {
        Element element = (Element) child.getData();
        for (Element result : results) {
            if (result.children().contains(element)) {
                results = new Elements(element);
                break;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser findDescendants(final String selector) {
        results = results.select(selector.replaceAll("\"", "")
                .replaceAll("'", ""));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser findDescendants(final HTMLDOMElement descendant) {
        Elements elements = new Elements();
        for (Element resultElement : results) {
            Element element = getDescendantOf(resultElement,
                    (Element) descendant.getData());
            if ((element != null) && (!elements.contains(element))) {
                elements.add(element);
            }
        }
        results = elements;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser findAncestors(final String selector) {
        Elements findedElements = document.select(selector.replaceAll("\"", "")
                .replaceAll("'", ""));
        Elements elements = new Elements();
        for (Element element : findedElements) {
            if ((results.parents().contains(element))
                    && (!elements.contains(element))) {
                elements.add(element);
            }
        }
        results = elements;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMParser findAncestors(final HTMLDOMElement element) {
        if (results.parents().contains((Element) element.getData())) {
            results = new Elements((Element) element.getData());
        } else {
            results = new Elements();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void clearParser() {
        results.clear();
        results = null;
        document = null;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement firstResult() {
        if (results.isEmpty()) {
            return null;
        }
        return new JsoupHTMLDOMElement(results.first());
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement lastResult() {
        if (results.isEmpty()) {
            return null;
        }
        return new JsoupHTMLDOMElement(results.last());
    }

    /**
     * {@inheritDoc}
     */
    public List<HTMLDOMElement> listResults() {
        List<HTMLDOMElement> elements = new ArrayList<HTMLDOMElement>();
        for (Element element : results) {
            elements.add(new JsoupHTMLDOMElement(element));
        }
        return elements;
    }

    /**
     * {@inheritDoc}
     */
    public String getHTML() {
        return (new JsoupHTMLDOMElement(document)).getOuterHTML();
    }

    /**
     * {@inheritDoc}
     */
    public Object getParser() {
        return document;
    }

    /**
     * {@inheritDoc}
     */
    public HTMLDOMElement createElement(final String tag) {
        return new JsoupHTMLDOMElement(document.createElement(tag));
    }

    @Override
    public boolean equals(final Object object) {
        if (this != object) {
            if (object == null) {
                return false;
            }
            if (!(object instanceof JsoupHTMLDOMParser)) {
                return false;
            }
            HTMLDOMParser parser = (HTMLDOMParser) object;
            if (!document.equals(parser.getParser())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.document.hashCode();
    }
}