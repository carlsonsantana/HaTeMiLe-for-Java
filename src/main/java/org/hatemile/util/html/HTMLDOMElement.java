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
 * The HTMLDOMElement interface contains the methods for access of the HTML
 * element.
 */
public interface HTMLDOMElement extends HTMLDOMNode, Cloneable {

    /**
     * Returns the tag name of element.
     * @return The tag name of element in uppercase letters.
     */
    String getTagName();

    /**
     * Returns the value of a attribute.
     * @param name The name of attribute.
     * @return The value of the attribute or null if the element not contains
     * the attribute.
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
     * Check that the element has an attribute.
     * @param name The name of attribute.
     * @return True if the element has the attribute or false if the element not
     * has the attribute.
     */
    boolean hasAttribute(String name);

    /**
     * Check that the element has attributes.
     * @return True if the element has attributes or false if the element not
     * has attributes.
     */
    boolean hasAttributes();

    /**
     * Append a element child.
     * @param element The element that be inserted.
     * @return This element.
     */
    HTMLDOMElement appendElement(HTMLDOMElement element);

    /**
     * Prepend a element child.
     * @param element The element that be inserted.
     * @return This element.
     */
    HTMLDOMElement prependElement(HTMLDOMElement element);

    /**
     * Returns the elements children of this element.
     * @return The elements children of this element.
     */
    List<HTMLDOMElement> getChildrenElements();

    /**
     * Returns the children of this element.
     * @return The children of this element.
     */
    List<HTMLDOMNode> getChildren();

    /**
     * Joins adjacent Text nodes.
     * @return This element.
     */
    HTMLDOMElement normalize();

    /**
     * Check that the element has elements children.
     * @return True if the element has elements children or false if the element
     * not has elements children.
     */
    boolean hasChildrenElements();

    /**
     * Check that the element has children.
     * @return True if the element has children or false if the element not has
     * children.
     */
    boolean hasChildren();

    /**
     * Returns the inner HTML code of this element.
     * @return The inner HTML code of this element.
     */
    String getInnerHTML();

    /**
     * Returns the HTML code of this element.
     * @return The HTML code of this element.
     */
    String getOuterHTML();

    /**
     * Clone this element.
     * @return The clone.
     */
    HTMLDOMElement cloneElement();

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
     * Returns the first node child of this element.
     * @return The first node child of this element.
     */
    HTMLDOMNode getFirstNodeChild();

    /**
     * Returns the last node child of this element.
     * @return The last node child of this element.
     */
    HTMLDOMNode getLastNodeChild();
}
