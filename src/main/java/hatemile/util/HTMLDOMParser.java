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
package hatemile.util;

import java.util.Collection;

/**
 * The HTMLDOMParser interface contains the methods for
 * access the native parser.
 * @version 1.0
 */
public interface HTMLDOMParser {
	
	/**
	 * Find all elements in the parser by selector.
	 * @param selector The selector.
	 * @return The parser with the elements founded.
	 */
	public HTMLDOMParser find(String selector);
	
	/**
	 * Find if the element is contained in parser.
	 * @param element The element.
	 * @return The parser with the element, if the element is contained in parser.
	 */
	public HTMLDOMParser find(HTMLDOMElement element);
	
	/**
	 * Find all elements in the parser by selector, children of
	 * founded elements.
	 * @param selector The selector.
	 * @return The parser with the elements founded.
	 */
	public HTMLDOMParser findChildren(String selector);
	
	/**
	 * Find if the element is children of founded elements.
	 * @param child The element.
	 * @return The parser with the element, if the element is children
	 * of founded elements.
	 */
	public HTMLDOMParser findChildren(HTMLDOMElement child);
	
	/**
	 * Find all elements in the parser by selector, descendants of
	 * founded elements.
	 * @param selector The selector.
	 * @return The parser with the elements founded.
	 */
	public HTMLDOMParser findDescendants(String selector);
	
	/**
	 * Find if the element is descendant of founded elements.
	 * @param element The element.
	 * @return The parser with the element, if the element is descendant
	 * of founded elements.
	 */
	public HTMLDOMParser findDescendants(HTMLDOMElement element);
	
	/**
	 * Find all elements in the parser by selector, ancestors of
	 * founded elements.
	 * @param selector The selector.
	 * @return The parser with the elements founded.
	 */
	public HTMLDOMParser findAncestors(String selector);
	
	/**
	 * Find if the element is ancestor of founded elements.
	 * @param element The element.
	 * @return The parser with the element, if the element is ancestor
	 * of founded elements.
	 */
	public HTMLDOMParser findAncestors(HTMLDOMElement element);
	
	/**
	 * Returns the first element founded.
	 * @return The first element founded or null if not have elements founded.
	 */
	public HTMLDOMElement firstResult();
	
	/**
	 * Returns the last element founded.
	 * @return The last element founded or null if not have elements founded.
	 */
	public HTMLDOMElement lastResult();
	
	/**
	 * Returns a list with all elements founded.
	 * @return A list with all elements founded..
	 */
	public Collection<HTMLDOMElement> listResults();
	
	/**
	 * Create a element.
	 * @param tag The tag of element.
	 * @return The elemente created.
	 */
	public HTMLDOMElement createElement(String tag);
	
	/**
	 * Returns the HTML code of parser.
	 * @return The HTML code of parser.
	 */
	public String getHTML();
	
	/**
	 * Clear the memory of this object.
	 */
	public void clearParser();
}