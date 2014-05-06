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

public interface HTMLDOMElement extends Cloneable {

	public String getTagName();

	public String getAttribute(String name);

	public void setAttribute(String name, String value);

	public void removeAttribute(String name);

	public boolean hasAttribute(String name);

	public boolean hasAttributes();

	public String getTextContent();

	public HTMLDOMElement insertBefore(HTMLDOMElement newElement);

	public HTMLDOMElement insertAfter(HTMLDOMElement newElement);

	public HTMLDOMElement removeElement();

	public HTMLDOMElement replaceElement(HTMLDOMElement newElement);

	public HTMLDOMElement appendElement(HTMLDOMElement element);

	public Collection<HTMLDOMElement> getChildren();

	public void appendText(String text);

	public boolean hasChildren();

	public HTMLDOMElement getParentElement();

	public String getInnerHTML();

	public void setInnerHTML(String html);

	public String getOuterHTML();

	public Object getData();

	public void setData(Object data);

	public HTMLDOMElement cloneElement();

	public HTMLDOMElement getFirstElementChild();

	public HTMLDOMElement getLastElementChild();
}
