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
package hatemile.implementation;

import hatemile.AccessibleDisplay;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.html.HTMLDOMElement;
import hatemile.util.html.HTMLDOMParser;
import java.util.Collection;

/**
 * The AccessibleDisplayScreenReaderImplementation class is official
 * implementation of AccessibleDisplay interface for screen readers.
 */
public class AccessibleDisplayScreenReaderImplementation implements AccessibleDisplay {

	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser htmlParser;

	/**
	 * The id of list element that contains the description of shortcuts.
	 */
	protected final String idContainerShortcuts;

	/**
	 * The id of text of description of container of shortcuts descriptions.
	 */
	protected final String idTextShortcuts;

	/**
	 * The name of attribute that link the list item element with the shortcut.
	 */
	protected final String dataAccessKey;

	/**
	 * The browser shortcut prefix.
	 */
	protected final String prefix;

	/**
	 * The text of description of container of shortcuts descriptions.
	 */
	protected final String textShortcuts;

	/**
	 * The list element of shortcuts.
	 */
	protected HTMLDOMElement listShortcuts;

	/**
	 * The state that indicates if the list of shortcuts of page was added.
	 */
	protected boolean listShortcutsAdded;

	/**
	 * Initializes a new object that manipulate the display for screen readers
	 * of parser.
	 * @param htmlParser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 * @param userAgent The user agent of browser.
	 */
	public AccessibleDisplayScreenReaderImplementation(HTMLDOMParser htmlParser
		, Configure configure, String userAgent) {
		this.htmlParser = htmlParser;
		idContainerShortcuts = "container-shortcuts";
		idTextShortcuts = "text-shortcuts";
		dataAccessKey = "data-shortcutdescriptionfor";
		prefix = getShortcutPrefix(userAgent, configure.getParameter("text-standart-shortcut-prefix"));
		textShortcuts = configure.getParameter("text-shortcuts");
		listShortcutsAdded = false;
		listShortcuts = null;
	}

	/**
	 * Returns the shortcut prefix of browser.
	 * @param userAgent The user agent of browser.
	 * @param standartPrefix The default prefix.
	 * @return The shortcut prefix of browser.
	 */
	protected final String getShortcutPrefix(String userAgent, String standartPrefix) {
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			boolean opera = userAgent.contains("opera");
			boolean mac = userAgent.contains("mac");
			boolean konqueror = userAgent.contains("konqueror");
			boolean spoofer = userAgent.contains("spoofer");
			boolean safari = userAgent.contains("applewebkit");
			boolean windows = userAgent.contains("windows");
			boolean chrome = userAgent.contains("chrome");
			boolean firefox = userAgent.matches("firefox/[2-9]|minefield/3");
			boolean ie = userAgent.contains("msie") || userAgent.contains("trident");

			if (opera) {
				return "SHIFT + ESC";
			} else if (chrome && mac && !spoofer) {
				return "CTRL + OPTION";
			} else if (safari && !windows && !spoofer) {
				return "CTRL + ALT";
			} else if (!windows && (safari || mac || konqueror)) {
				return "CTRL";
			} else if (firefox) {
				return "ALT + SHIFT";
			} else if (chrome || ie) {
				return "ALT";
			} else {
				return standartPrefix;
			}
		} else {
			return standartPrefix;
		}
	}

	/**
	 * Returns the description of element.
	 * @param element The element.
	 * @return The description of element.
	 */
	protected String getDescription(HTMLDOMElement element) {
		String description = null;
		if (element.hasAttribute("title")) {
			description = element.getAttribute("title");
		} else if (element.hasAttribute("aria-label")) {
			description = element.getAttribute("aria-label");
		} else if (element.hasAttribute("alt")) {
			description = element.getAttribute("alt");
		} else if (element.hasAttribute("label")) {
			description = element.getAttribute("label");
		} else if ((element.hasAttribute("aria-labelledby"))
				|| (element.hasAttribute("aria-describedby"))) {
			String[] descriptionIds;
			if (element.hasAttribute("aria-labelledby")) {
				descriptionIds = element.getAttribute("aria-labelledby").split("[ \n\t\r]+");
			} else {
				descriptionIds = element.getAttribute("aria-describedby").split("[ \n\t\r]+");
			}
			for (int i = 0, length = descriptionIds.length; i < length; i++) {
				HTMLDOMElement elementDescription = htmlParser.find("#" + descriptionIds[i]).firstResult();
				if (elementDescription != null) {
					description = elementDescription.getTextContent();
					break;
				}
			}
		} else if ((element.getTagName().equals("INPUT")) && (element.hasAttribute("type"))) {
			String type = element.getAttribute("type").toLowerCase();
			if (((type.equals("button")) || (type.equals("submit")) || (type.equals("reset")))
					&& (element.hasAttribute("value"))) {
				description = element.getAttribute("value");
			}
		}
		if (description == null) {
			description = element.getTextContent();
		}
		return description.replaceAll("[ \n\t\r]+", " ").trim();
	}

	/**
	 * Generate the list of shortcuts of page.
	 * @return The list of shortcuts of page.
	 */
	protected HTMLDOMElement generateListShortcuts() {
		HTMLDOMElement container = htmlParser.find("#" + idContainerShortcuts).firstResult();

		HTMLDOMElement htmlList = null;
		if (container == null) {
			HTMLDOMElement local = htmlParser.find("body").firstResult();
			if (local != null) {
				container = htmlParser.createElement("div");
				container.setAttribute("id", idContainerShortcuts);

				HTMLDOMElement textContainer = htmlParser.createElement("span");
				textContainer.setAttribute("id", idTextShortcuts);
				textContainer.appendText(textShortcuts);

				container.appendElement(textContainer);
				local.appendElement(container);
			}
		}
		if (container != null) {
			htmlList = htmlParser.find(container).findChildren("ul").firstResult();
			if (htmlList == null) {
				htmlList = htmlParser.createElement("ul");
				container.appendElement(htmlList);
			}
		}
		listShortcutsAdded = true;

		return htmlList;
	}

	public void displayShortcut(HTMLDOMElement element) {
		if (element.hasAttribute("accesskey")) {
			String description = getDescription(element);
			if (!element.hasAttribute("title")) {
				element.setAttribute("title", description);
			}

			if (!listShortcutsAdded) {
				listShortcuts = generateListShortcuts();
			}

			if (listShortcuts != null) {
				String[] keys = element.getAttribute("accesskey").split("[ \n\t\r]+");
				for (int i = 0, length = keys.length; i < length; i++) {
					String key = keys[i].toUpperCase();
					if (htmlParser.find(listShortcuts).findChildren("[" + dataAccessKey + "=\"" + key + "\"]")
							.firstResult() == null) {
						HTMLDOMElement item = htmlParser.createElement("li");
						item.setAttribute(dataAccessKey, key);
						item.appendText(prefix + " + " + key + ": " + description);
						listShortcuts.appendElement(item);
					}
				}
			}
		}
	}

	public void displayAllShortcuts() {
		Collection<HTMLDOMElement> elements = htmlParser.find("[accesskey]").listResults();
		for (HTMLDOMElement element : elements) {
			if (CommonFunctions.isValidElement(element)) {
				displayShortcut(element);
			}
		}
	}
}