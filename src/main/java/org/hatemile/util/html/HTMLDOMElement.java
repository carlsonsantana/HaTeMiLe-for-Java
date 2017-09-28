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

import java.util.Collection;

/**
 * The HTMLDOMElement interface contains the methods for access of the HTML
 * element.
 */
public interface HTMLDOMElement extends Cloneable {

    /**
     * Returns the tag name of element.
     * @return The tag name of element in uppercase letters.
     */
    String getTagName();

    /**
     * Returns the value of a attribute.
     * @param name The name of attribute.
     * @return The value of the attribute, if the element not contains the
     * attribute returns null.
     */
    String getAttribute(String name);

    /**
     * Create or modify a attribute.
     * @param name The name of attribute.
     * @param value The value of attribute.
     */
    void setAttribute(String name, String value);

    /**
     * Remove a attribute of element.
     * @param name The name of attribute.
     */
    void removeAttribute(String name);

    /**
     * Returns if the element has an attribute.
     * @param name The name of attribute.
     * @return True if the element has the attribute or false if the element not
     * has the attribute.
     */
    boolean hasAttribute(String name);

    /**
     * Returns if the element has attributes.
     * @return True if the element has attributes or false if the element not
     * has attributes.
     */
    boolean hasAttributes();

    /**
     * Returns the text of element.
     * @return The text of element.
     */
    String getTextContent();

    /**
     * Insert a element before this element.
     * @param newElement The element that be inserted.
     * @return The element inserted.
     */
    HTMLDOMElement insertBefore(HTMLDOMElement newElement);

    /**
     * Insert a element after this element.
     * @param newElement The element that be inserted.
     * @return The element inserted.
     */
    HTMLDOMElement insertAfter(HTMLDOMElement newElement);

    /**
     * Remove this element of the parser.
     * @return The removed element.
     */
    HTMLDOMElement removeElement();

    /**
     * Replace this element for other element.
     * @param newElement The element that replace this element.
     * @return The element replaced.
     */
    HTMLDOMElement replaceElement(HTMLDOMElement newElement);

    /**
     * Append a element child.
     * @param element The element that be inserted.
     * @return The element inserted.
     */
    HTMLDOMElement appendElement(HTMLDOMElement element);

    /**
     * Returns the children of this element.
     * @return The children of this element.
     */
    Collection<HTMLDOMElement> getChildren();

    /**
     * Append a text child.
     * @param text The text.
     */
    void appendText(String text);

    /**
     * Returns if the element has children.
     * @return True if the element has children or false if the element not has
     * children.
     */
    boolean hasChildren();

    /**
     * Returns the parent element of this element.
     * @return The parent element of this element.
     */
    HTMLDOMElement getParentElement();

    /**
     * Returns the inner HTML code of this element.
     * @return The inner HTML code of this element.
     */
    String getInnerHTML();

    /**
     * Modify the inner HTML code of this element.
     * @param html The HTML code.
     */
    void setInnerHTML(String html);

    /**
     * Returns the HTML code of this element.
     * @return The HTML code of this element.
     */
    String getOuterHTML();

    /**
     * Returns the native object of this element.
     * @return The native object of this element.
     */
    Object getData();

    /**
     * Modify the native object of this element.
     * @param data The native object of this element.
     */
    void setData(Object data);

    /**
     * Returns the first element child of this element.
     * @return The first element child of this element.
     */
    HTMLDOMElement getFirstElementChild();

    /**
     * Returns the last element child of this element.
     * @return The last element child of this element.
     */
    HTMLDOMElement getLastElementChild();

    /**
     * Clone this element.
     * @return The clone.
     */
    HTMLDOMElement cloneElement();
}
