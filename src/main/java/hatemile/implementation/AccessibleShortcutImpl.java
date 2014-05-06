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

public class AccessibleShortcutImpl implements AccessibleShortcut {
	protected final HTMLDOMParser parser;
	protected final String idContainerShortcuts;
	protected final String idSkipLinkContainerShortcuts;
	protected final String idSkipContainerShortcuts;
	protected final String textSkipLinkContainerShortcuts;
	protected final String dataAccessKey;
	protected final String dataIgnore;
	protected final String prefix;
	protected HTMLDOMElement list;
	
	public AccessibleShortcutImpl(HTMLDOMParser parser, Configure configure) {
		this(parser, configure, null);
	}
	
	public AccessibleShortcutImpl(HTMLDOMParser parser, Configure configure, String userAgent) {
		this.parser = parser;
		idContainerShortcuts = configure.getParameter("id-container-shortcuts");
		idSkipLinkContainerShortcuts = configure.getParameter("id-skip-link-container-shortcuts");
		idSkipContainerShortcuts = configure.getParameter("id-skip-container-shortcuts");
		dataAccessKey = configure.getParameter("data-accesskey");
		textSkipLinkContainerShortcuts = configure.getParameter("text-skip-container-shortcuts");
		dataIgnore = configure.getParameter("data-ignore");
		
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			boolean mac = userAgent.contains("mac");
			boolean konqueror = userAgent.contains("konqueror");
			boolean spoofer = userAgent.contains("spoofer");
			boolean safari = userAgent.contains("applewebkit");
			boolean windows = userAgent.contains("windows");
			if (userAgent.contains("opera")) {
				prefix = "SHIFT + ESC";
			} else if ((userAgent.contains("chrome")) && (!spoofer) && (mac)) {
				prefix = "CTRL + OPTION";
			} else if ((safari) && (!windows) && (!spoofer)) {
				prefix = "CTRL + ALT";
			} else if ((!windows) && ((safari) || (mac) || (konqueror))) {
				prefix = "CTRL";
			} else if ((userAgent.matches("firefox/[2-9]|minefield/3"))) {
				prefix = "ALT + SHIFT";
			} else {
				prefix = "ALT";
			}
		} else {
			prefix = "ALT";
		}
	}
	
	protected String getDescription(HTMLDOMElement element) {
		String description = "";
		if (element.hasAttribute("title")) {
			description = element.getAttribute("title");
		} else if (element.hasAttribute("aria-labelledby")) {
			String[] labelsIds = element.getAttribute("aria-labelledby").split("[ \n\t\r]+");
			for (int i = 0, length = labelsIds.length; i < length; i++) {
				HTMLDOMElement label = parser.find("#" + labelsIds[i]).firstResult();
				if (label != null) {
					description = label.getTextContent();
					break;
				}
			}
		} else if (element.hasAttribute("aria-label")) {
			description = element.getAttribute("aria-label");
		} else if (element.hasAttribute("alt")) {
			description = element.getAttribute("alt");
		} else if (element.getTagName().equals("INPUT")) {
			String type = element.getAttribute("type").toLowerCase().trim();
			if (((type.equals("button")) || (type.equals("submit")) || (type.equals("reset"))) && (element.hasAttribute("value"))) {
				description = element.getAttribute("value");
			}
		} else {
			description = element.getTextContent();
		}
		description = description.replaceAll("[ \n\t\r]+", " ").trim();
		return description;
	}
	
	protected HTMLDOMElement generateList() {
		HTMLDOMElement container = parser.find("#" + idContainerShortcuts).firstResult();
		if (container == null) {
			container = parser.createElement("div");
			container.setAttribute("id", idContainerShortcuts);
			HTMLDOMElement firstChild = parser.find("body").firstResult().getFirstElementChild();
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
		}
		HTMLDOMElement htmlList = parser.find(container).findChildren("ul").firstResult();
		if (htmlList == null) {
			htmlList = parser.createElement("ul");
			container.appendElement(htmlList);
		}
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
			String[] keys = element.getAttribute("accesskey").split("[ \n\t\r]+");
			if (list == null) {
				list = generateList();
			}
			for (int i = 0, length = keys.length; i < length; i++) {
				String key = keys[i].toUpperCase();
				if (parser.find(list).findChildren("[" + dataAccessKey + "=" + key + "]").firstResult() == null) {
					HTMLDOMElement item = parser.createElement("li");
					item.setAttribute(dataAccessKey, key);
					item.appendText(prefix + " + " + key + ": " + description);
					list.appendElement(item);
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