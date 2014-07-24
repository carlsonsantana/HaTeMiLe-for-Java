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
package hatemile.implementation;

import hatemile.AccessibleShortcut;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;

import java.util.Collection;

/**
 * The AccessibleShortcutImpl class is official implementation of
 * AccessibleShortcut interface.
 * @version 2014-07-23
 */
public class AccessibleShortcutImpl implements AccessibleShortcut {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The id of list element that contains the description of shortcuts.
	 */
	protected final String idContainerShortcuts;
	
	/**
	 * The id of link element that skip the list of shortcuts.
	 */
	protected final String idSkipLinkContainerShortcuts;
	
	/**
	 * The id of anchor element after the list of shortcuts.
	 */
	protected final String idSkipContainerShortcuts;
	
	/**
	 * The id of text that precede the shortcuts descriptions.
	 */
	protected final String idTextShortcuts;
	
	/**
	 * The text of link element that skip the list of shortcuts.
	 */
	protected final String textSkipLinkContainerShortcuts;
	
	/**
	 * The text that precede the shortcuts descriptions.
	 */
	protected final String textShortcuts;
	
	/**
	 * The name of attribute that link the list item element with the shortcut.
	 */
	protected final String dataAccessKey;
	
	/**
	 * The name of attribute for that the element not can be modified by
	 * HaTeMiLe.
	 */
	protected final String dataIgnore;
	
	/**
	 * The browser shortcut prefix.
	 */
	protected final String prefix;
	
	/**
	 * Standart browser prefix.
	 */
	protected final String standartPrefix;
	
	/**
	 * The list element of shortcuts of page.
	 */
	protected HTMLDOMElement list;
	
	/**
	 * The state that indicates if the list of shortcuts of page was added.
	 */
	protected boolean listAdded;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * shortcuts of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleShortcutImpl(HTMLDOMParser parser, Configure configure) {
		this(parser, configure, null);
	}
	
	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * shortcuts of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 * @param userAgent The user agent of the user.
	 */
	public AccessibleShortcutImpl(HTMLDOMParser parser, Configure configure, String userAgent) {
		this.parser = parser;
		idContainerShortcuts = configure.getParameter("id-container-shortcuts");
		idSkipLinkContainerShortcuts = configure.getParameter("id-skip-link-container-shortcuts");
		idSkipContainerShortcuts = configure.getParameter("id-skip-container-shortcuts");
		idTextShortcuts = configure.getParameter("id-text-shortcuts");
		textSkipLinkContainerShortcuts = configure.getParameter("text-skip-container-shortcuts");
		textShortcuts = configure.getParameter("text-shortcuts");
		standartPrefix = configure.getParameter("text-standart-shortcut-prefix");
		dataAccessKey = "data-" + configure.getParameter("data-accesskey");
		dataIgnore = "data-" + configure.getParameter("data-ignore");
		listAdded = false;
		
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
				prefix = "SHIFT + ESC";
			} else if (chrome && mac && !spoofer) {
				prefix = "CTRL + OPTION";
			} else if (safari && !windows && !spoofer) {
				prefix = "CTRL + ALT";
			} else if (!windows && (safari || mac || konqueror)) {
				prefix = "CTRL";
			} else if (firefox) {
				prefix = "ALT + SHIFT";
			} else if (chrome || ie) {
				prefix = "ALT";
			} else {
				prefix = standartPrefix;
			}
		} else {
			prefix = standartPrefix;
		}
	}
	
	/**
	 * Returns the description of element.
	 * @param element The element with description.
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
				HTMLDOMElement elementDescription = parser.find("#" + descriptionIds[i]).firstResult();
				if (elementDescription != null) {
					description = elementDescription.getTextContent();
					break;
				}
			}
		} else if (element.getTagName().equals("INPUT")) {
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
	protected HTMLDOMElement generateList() {
		HTMLDOMElement local = parser.find("body").firstResult();
		HTMLDOMElement htmlList = null;
		if (local != null) {
			HTMLDOMElement container = parser.find("#" + idContainerShortcuts).firstResult();
			if (container == null) {
				container = parser.createElement("div");
				container.setAttribute("id", idContainerShortcuts);
				HTMLDOMElement firstChild = local.getFirstElementChild();
				firstChild.insertBefore(container);
				
				HTMLDOMElement anchorJump = parser.createElement("a");
				anchorJump.setAttribute("id", idSkipLinkContainerShortcuts);
				anchorJump.setAttribute("href", "#" + idSkipContainerShortcuts);
				anchorJump.appendText(textSkipLinkContainerShortcuts);
				container.insertBefore(anchorJump);
				
				HTMLDOMElement anchor = parser.createElement("a");
				anchor.setAttribute("name", idSkipContainerShortcuts);
				anchor.setAttribute("id", idSkipContainerShortcuts);
				firstChild.insertBefore(anchor);
				
				HTMLDOMElement textContainer = parser.createElement("span");
				textContainer.setAttribute("id", idTextShortcuts);
				textContainer.appendText(textShortcuts);
				container.appendElement(textContainer);
			}
			htmlList = parser.find(container).findChildren("ul").firstResult();
			if (htmlList == null) {
				htmlList = parser.createElement("ul");
				container.appendElement(htmlList);
			}
		}
		listAdded = true;
		
		return htmlList;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void fixShortcut(HTMLDOMElement element) {
		if (element.hasAttribute("accesskey")) {
			String description = getDescription(element);
			if (!element.hasAttribute("title")) {
				element.setAttribute("title", description);
			}
			
			if (!listAdded) {
				list = generateList();
			}
			
			if (list != null) {
				String[] keys = element.getAttribute("accesskey").split("[ \n\t\r]+");
				for (int i = 0, length = keys.length; i < length; i++) {
					String key = keys[i].toUpperCase();
					if (parser.find(list).findChildren("[" + dataAccessKey + "=" + key + "]")
							.firstResult() == null) {
						HTMLDOMElement item = parser.createElement("li");
						item.setAttribute(dataAccessKey, key);
						item.appendText(prefix + " + " + key + ": " + description);
						list.appendElement(item);
					}
				}
			}
		}
	}
	
	public void fixShortcuts() {
		Collection<HTMLDOMElement> elements = parser.find("[accesskey]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixShortcut(element);
			}
		}
	}
}