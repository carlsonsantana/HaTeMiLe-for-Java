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

/**
 * The HTMLDOMNode interface contains the methods for access the Node.
 */
public interface HTMLDOMNode {

    /**
     * Returns the text content of node.
     * @return The text content of node.
     */
    String getTextContent();

    /**
     * Insert a node before this node.
     * @param newNode The node that be inserted.
     * @return This node.
     */
    HTMLDOMNode insertBefore(HTMLDOMNode newNode);

    /**
     * Insert a node after this node.
     * @param newNode The node that be inserted.
     * @return This node.
     */
    HTMLDOMNode insertAfter(HTMLDOMNode newNode);

    /**
     * Remove this node of the parser.
     * @return The removed node.
     */
    HTMLDOMNode removeNode();

    /**
     * Replace this node for other node.
     * @param newNode The node that replace this node.
     * @return This node.
     */
    HTMLDOMNode replaceNode(HTMLDOMNode newNode);

    /**
     * Append a text content in node.
     * @param text The text content.
     * @return This node.
     */
    HTMLDOMNode appendText(String text);

    /**
     * Prepend a text content in node.
     * @param text The text content.
     * @return This node.
     */
    HTMLDOMNode prependText(String text);

    /**
     * Returns the parent element of this node.
     * @return The parent element of this node.
     */
    HTMLDOMElement getParentElement();

    /**
     * Returns the native object of this node.
     * @return The native object of this node.
     */
    Object getData();

    /**
     * Modify the native object of this node.
     * @param data The native object of this node.
     */
    void setData(Object data);
}
