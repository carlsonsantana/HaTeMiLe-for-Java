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

/**
 * The CommonFuncionts class contains the used methods by HaTeMiLe
 * classes.
 * @version 1.0
 */
public class CommonFunctions {
	
	/**
	 * Count the number of ids created.
	 */
	protected static int count = 0;
	
	/**
	 * The private constructor prevents that the class not can be
	 * initialized.
	 */
	private CommonFunctions() {
		
	}

	/**
	 * Generate a id for a element.
	 * @param element The element.
	 * @param prefix The prefix of id.
	 */
	public static void generateId(HTMLDOMElement element, String prefix) {
		if (!element.hasAttribute("id")) {
			element.setAttribute("id", prefix + Integer.toString(count));
			count++;
		}
	}

	/**
	 * Copy a list of attributes of a element for other element.
	 * @param element1 The element that have attributes copied.
	 * @param element2 The element that copy the attributes.
	 * @param attributes The list of attributes that will be copied.
	 */
	public static void setListAttributes(HTMLDOMElement element1, HTMLDOMElement element2, String[] attributes) {
		for (int i = 0, length = attributes.length; i < length; i++) {
			if (element1.hasAttribute(attributes[i])) {
				element2.setAttribute(attributes[i], element1.getAttribute(attributes[i]));
			}
		}
	}

	/**
	 * Increase a item in a HTML list.
	 * @param list The HTML list.
	 * @param stringToIncrease The value of item.
	 * @return The HTML list with the item added, if the item
	 * not was contained in list.
	 */
	public static String increaseInList(String list, String stringToIncrease) {
		if (((list != null) && (!list.isEmpty())) && ((stringToIncrease != null) && (!stringToIncrease.isEmpty()))) {
			String[] elements = list.split("[ \n\t\r]+");
			for (int i = 0, length = elements.length; i < length; i++) {
				String string = elements[i];
				if (string.equals(stringToIncrease)) {
					return list;
				}
			}
			return list + " " + stringToIncrease;
		} else if ((list != null) && (!list.isEmpty())) {
			return list;
		} else {
			return stringToIncrease;
		}
	}
}
