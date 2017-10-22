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
package org.hatemile.util.html;

import java.util.List;

/**
 * The HTMLDOMParser interface contains the methods for access a native parser.
 */
public interface HTMLDOMParser {

    /**
     * Find all elements in the parser by selector.
     * @param selector The selector.
     * @return The parser with the elements found.
     */
    HTMLDOMParser find(String selector);

    /**
     * Find if a element is contained in parser.
     * @param element The element.
     * @return The parser with the element, if the element is contained in
     * parser.
     */
    HTMLDOMParser find(HTMLDOMElement element);

    /**
     * Find all elements in the parser by selector, children of found elements.
     * @param selector The selector.
     * @return The parser with the elements found.
     */
    HTMLDOMParser findChildren(String selector);

    /**
     * Find if a element is a child of found elements.
     * @param child The element.
     * @return The parser with the element, if the element is child of found
     * elements.
     */
    HTMLDOMParser findChildren(HTMLDOMElement child);

    /**
     * Find all elements in the parser by selector, descendants of found
     * elements.
     * @param selector The selector.
     * @return The parser with the elements found.
     */
    HTMLDOMParser findDescendants(String selector);

    /**
     * Find if a element is descendant of found elements.
     * @param element The element.
     * @return The parser with the element, if the element is descendant of
     * found elements.
     */
    HTMLDOMParser findDescendants(HTMLDOMElement element);

    /**
     * Find all elements in the parser by selector, ancestors of found elements.
     * @param selector The selector.
     * @return The parser with the elements found.
     */
    HTMLDOMParser findAncestors(String selector);

    /**
     * Find if a element is ancestor of found elements.
     * @param element The element.
     * @return The parser with the element, if the element is ancestor of found
     * elements.
     */
    HTMLDOMParser findAncestors(HTMLDOMElement element);

    /**
     * Returns the first element found.
     * @return The first element found or null if not have elements found.
     */
    HTMLDOMElement firstResult();

    /**
     * Returns the last element found.
     * @return The last element found or null if not have elements found.
     */
    HTMLDOMElement lastResult();

    /**
     * Returns a list with all elements found.
     * @return The list with all elements found.
     */
    List<HTMLDOMElement> listResults();

    /**
     * Create a element.
     * @param tag The tag of element.
     * @return The element created.
     */
    HTMLDOMElement createElement(String tag);

    /**
     * Returns the HTML code of parser.
     * @return The HTML code of parser.
     */
    String getHTML();

    /**
     * Returns the parser.
     * @return The parser or root element of the parser.
     */
    Object getParser();

    /**
     * Clear the memory of this object.
     */
    void clearParser();
}
