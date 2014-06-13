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
 * The HTMLDOMElement interface contains the methods for
 * access HTML element.
 */
public interface HTMLDOMElement extends Cloneable {
	
	/**
	 * Returns the tag name of element.
	 * @return The tag name of element in upper case letters.
	 */
	public String getTagName();
	
	/**
	 * Returns the value of the attribute.
	 * @param name The name of attribute.
	 * @return The value of the attribute, if the element not
	 * contains the attribute return null.
	 */
	public String getAttribute(String name);
	
	/**
	 * Create or modify the value of attribute.
	 * @param name The name of attribute.
	 * @param value The value of attribute.
	 */
	public void setAttribute(String name, String value);
	
	/**
	 * Remove a attribute of element.
	 * @param name The name of attribute.
	 */
	public void removeAttribute(String name);
	
	/**
	 * Returns if the element contains the attribute.
	 * @param name The name of attribute.
	 * @return Return true if the element contains the attribute or
	 * false if the element not contains the attribute.
	 */
	public boolean hasAttribute(String name);
	
	/**
	 * Returns if the element contains any attribute.
	 * @return Return true if the element contains any attribute or
	 * false if the element not contains any attribute.
	 */
	public boolean hasAttributes();
	
	/**
	 * Returns the text of element.
	 * @return The text of element.
	 */
	public String getTextContent();
	
	/**
	 * Insert a element before this element.
	 * @param newElement The element that be inserted.
	 * @return The element inserted.
	 */
	public HTMLDOMElement insertBefore(HTMLDOMElement newElement);
	
	/**
	 * Insert a element after this element.
	 * @param newElement The element that be inserted.
	 * @return The element inserted.
	 */
	public HTMLDOMElement insertAfter(HTMLDOMElement newElement);
	
	/**
	 * Remove the element of the parser.
	 * @return The removed element.
	 */
	public HTMLDOMElement removeElement();
	
	/**
	 * Replace this element for other element.
	 * @param newElement The element that replace this element.
	 * @return The element replaced.
	 */
	public HTMLDOMElement replaceElement(HTMLDOMElement newElement);
	
	/**
	 * Append a element child.
	 * @param element The element will be inserted.
	 * @return The element inserted.
	 */
	public HTMLDOMElement appendElement(HTMLDOMElement element);
	
	/**
	 * Returns the children of this element.
	 * @return The children of this element.
	 */
	public Collection<HTMLDOMElement> getChildren();
	
	/**
	 * Append a text child.
	 * @param text The text.
	 */
	public void appendText(String text);
	
	/**
	 * Returns if the element contains children.
	 * @return Return true if the element contains children or
	 * false if the element not contains children.
	 */
	public boolean hasChildren();
	
	/**
	 * Returns the parent element of this element.
	 * @return The parente element of this element.
	 */
	public HTMLDOMElement getParentElement();
	
	/**
	 * Returns the inner HTML code of this element.
	 * @return The inner HTML code of this element.
	 */
	public String getInnerHTML();
	
	/**
	 * Modify the HTML code of this element.
	 * @param html The HTML code.
	 */
	public void setInnerHTML(String html);
	
	/**
	 * Returns the HTML code of this element.
	 * @return The HTML code of this element.
	 */
	public String getOuterHTML();
	
	/**
	 * Returns the native object of this element.
	 * @return The native object of this element.
	 */
	public Object getData();
	
	/**
	 * Modify the native object of this element.
	 * @param data The native object of this element.
	 */
	public void setData(Object data);
	
	/**
	 * Clone this element.
	 * @return The clone.
	 */
	public HTMLDOMElement cloneElement();
	
	/**
	 * Returns the first element child of this element.
	 * @return The first element child of this element.
	 */
	public HTMLDOMElement getFirstElementChild();
	
	/**
	 * Returns the last element child of this element.
	 * @return The last element child of this element.
	 */
	public HTMLDOMElement getLastElementChild();
}
