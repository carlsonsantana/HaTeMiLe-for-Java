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

public class CommonFunctions {

	protected static int count = 0;
	
	private CommonFunctions() {
		
	}

	public static void generateId(HTMLDOMElement element, String prefix) {
		if (!element.hasAttribute("id")) {
			element.setAttribute("id", prefix + Integer.toString(count));
			count++;
		}
	}

	public static void setListAttributes(HTMLDOMElement element1, HTMLDOMElement element2, String[] attributes) {
		for (int i = 0, length = attributes.length; i < length; i++) {
			if (element1.hasAttribute(attributes[i])) {
				element2.setAttribute(attributes[i], element1.getAttribute(attributes[i]));
			}
		}
	}

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
