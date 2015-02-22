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

import hatemile.AccessibleNavigation;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;
import hatemile.util.Skipper;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

/**
 * The AccessibleNavigationImplementation class is official implementation of
 * AccessibleNavigation interface.
 */
public class AccessibleNavigationImplementation implements AccessibleNavigation {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The id of list element that contains the description of shortcuts.
	 */
	protected final String idContainerShortcuts;
	
	/**
	 * The id of text of description of container of shortcuts descriptions.
	 */
	protected final String idTextShortcuts;
	
	/**
	 * The text of description of container of shortcuts descriptions.
	 */
	protected final String textShortcuts;
	
	/**
	 * The name of attribute that link the list item element with the shortcut.
	 */
	protected final String dataAccessKey;
	
	/**
	 * The name of attribute for not modify the elements.
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
	 * The id of list element that contains the skip links.
	 */
	protected final String idContainerSkippers;
	
	/**
	 * The id of list element that contains the links for the headings.
	 */
	protected final String idContainerHeading;
	
	/**
	 * The id of text of description of container of heading links.
	 */
	protected final String idTextHeading;
	
	/**
	 * The text of description of container of heading links.
	 */
	protected final String textHeading;
	
	/**
	 * The prefix of generated ids.
	 */
	protected final String prefixId;
	
	/**
	 * The skippers configured.
	 */
	protected final Collection<Skipper> skippers;
	
	/**
	 * The state that indicates if the container of skippers has added.
	 */
	protected boolean listSkippersAdded;
	
	/**
	 * The list element of skippers.
	 */
	protected HTMLDOMElement listSkipLinks;
	
	/**
	 * The name of attribute that links the anchor of skip link with the element.
	 */
	protected final String dataAnchorFor;
	
	/**
	 * The HTML class of anchor of skip link.
	 */
	protected final String classSkipLinkAnchor;
	
	/**
	 * The HTML class of anchor of heading link.
	 */
	protected final String classHeadingAnchor;
	
	/**
	 * The name of attribute that links the anchor of heading link with heading.
	 */
	protected final String dataHeadingAnchorFor;
	
	/**
	 * The state that indicates if the sintatic heading of parser be validated.
	 */
	protected boolean validateHeading;
	
	/**
	 * The state that indicates if the sintatic heading of parser is correct.
	 */
	protected boolean validHeading;
	
	/**
	 * The name of attribute that indicates the level of heading of link.
	 */
	protected final String dataHeadingLevel;
	
	/**
	 * The list element of shortcuts.
	 */
	protected HTMLDOMElement listShortcuts;
	
	/**
	 * The state that indicates if the list of shortcuts of page was added.
	 */
	protected boolean listShortcutsAdded;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * navigation of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleNavigationImplementation(HTMLDOMParser parser, Configure configure) {
		this(parser, configure, null);
	}
	
	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * navigation of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 * @param userAgent The user agent of the user.
	 */
	public AccessibleNavigationImplementation(HTMLDOMParser parser, Configure configure, String userAgent) {
		this.parser = parser;
		idContainerShortcuts = "container-shortcuts";
		idContainerSkippers = "container-skiplinks";
		idContainerHeading = "container-heading";
		idTextShortcuts = "text-shortcuts";
		idTextHeading = "text-heading";
		classSkipLinkAnchor = "skiplink-anchor";
		classHeadingAnchor = "heading-anchor";
		dataAccessKey = "data-shortcutdescriptionfor";
		dataIgnore = "data-ignoreaccessibilityfix";
		dataAnchorFor = "data-anchorfor";
		dataHeadingAnchorFor = "data-headinganchorfor";
		dataHeadingLevel = "data-headinglevel";
		prefixId = configure.getParameter("prefix-generated-ids");
		textShortcuts = configure.getParameter("text-shortcuts");
		textHeading = configure.getParameter("text-heading");
		standartPrefix = configure.getParameter("text-standart-shortcut-prefix");
		skippers = configure.getSkippers();
		listShortcutsAdded = false;
		listSkippersAdded = false;
		validateHeading = false;
		validHeading = false;
		listSkipLinks = null;
		listShortcuts = null;
		
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
	protected HTMLDOMElement generateListShortcuts() {
		HTMLDOMElement container = parser.find("#" + idContainerShortcuts).firstResult();
		
		HTMLDOMElement htmlList = null;
		if (container == null) {
			HTMLDOMElement local = parser.find("body").firstResult();
			if (local != null) {
				container = parser.createElement("div");
				container.setAttribute("id", idContainerShortcuts);
				
				HTMLDOMElement textContainer = parser.createElement("span");
				textContainer.setAttribute("id", idTextShortcuts);
				textContainer.appendText(textShortcuts);
				
				container.appendElement(textContainer);
				local.appendElement(container);
			}
		}
		if (container != null) {
			htmlList = parser.find(container).findChildren("ul").firstResult();
			if (htmlList == null) {
				htmlList = parser.createElement("ul");
				container.appendElement(htmlList);
			}
		}
		listShortcutsAdded = true;
		
		return htmlList;
	}
	
	/**
	 * Generate the list of skip links of page.
	 * @return The list of skip links of page.
	 */
	protected HTMLDOMElement generateListSkipLinks() {
		HTMLDOMElement container = parser.find("#" + idContainerSkippers).firstResult();
		HTMLDOMElement htmlList = null;
		if (container == null) {
			HTMLDOMElement local = parser.find("body").firstResult();
			if (local != null) {
				container = parser.createElement("div");
				container.setAttribute("id", idContainerSkippers);
				local.getFirstElementChild().insertBefore(container);
			}
		}
		if (container != null) {
			htmlList = parser.find(container).findChildren("ul").firstResult();
			if (htmlList == null) {
				htmlList = parser.createElement("ul");
				container.appendElement(htmlList);
			}
		}
		listSkippersAdded = true;
		
		return htmlList;
	}
	
	/**
	 * Generate the list of heading links of page.
	 * @return The list of heading links of page.
	 */
	protected HTMLDOMElement generateListHeading() {
		HTMLDOMElement container = parser.find("#" + idContainerHeading).firstResult();
		HTMLDOMElement htmlList = null;
		if (container == null) {
			HTMLDOMElement local = parser.find("body").firstResult();
			if (local != null) {
				container = parser.createElement("div");
				container.setAttribute("id", idContainerHeading);
				
				HTMLDOMElement textContainer = parser.createElement("span");
				textContainer.setAttribute("id", idTextHeading);
				textContainer.appendText(textHeading);
				
				container.appendElement(textContainer);
				local.appendElement(container);
			}
		}
		if (container != null) {
			htmlList = parser.find(container).findChildren("ol").firstResult();
			if (htmlList == null) {
				htmlList = parser.createElement("ol");
				container.appendElement(htmlList);
			}
		}
		return htmlList;
	}
	
	/**
	 * Returns the level of heading.
	 * @param element The heading.
	 * @return The level of heading.
	 */
	protected int getHeadingLevel(HTMLDOMElement element) {
		String tag = element.getTagName();
		if (tag.equals("H1")) {
			return 1;
		} else if (tag.equals("H2")) {
			return 2;
		} else if (tag.equals("H3")) {
			return 3;
		} else if (tag.equals("H4")) {
			return 4;
		} else if (tag.equals("H5")) {
			return 5;
		} else if (tag.equals("H6")) {
			return 6;
		} else {
			return -1;
		}
	}
	
	/**
	 * Inform if the headings of page are sintatic correct.
	 * @return True if the headings of page are sintatic correct or false if not.
	 */
	protected boolean isValidHeading() {
		Collection<HTMLDOMElement> elements = parser.find("h1,h2,h3,h4,h5,h6").listResults();
		int lastLevel = 0;
		int countMainHeading = 0;
		int level;
		validateHeading = true;
		for (HTMLDOMElement element : elements) {
			level = getHeadingLevel(element);
			if (level == 1) {
				if (countMainHeading == 1) {
					return false;
				} else {
					countMainHeading = 1;
				}
			}
			if ((level - lastLevel) > 1) {
				return false;
			}
			lastLevel = level;
		}
		return true;
	}
	
	/**
	 * Generate an anchor for the element.
	 * @param element The element.
	 * @param dataAttribute The name of attribute that links the element with
	 * the anchor.
	 * @param anchorClass The HTML class of anchor.
	 * @return The anchor.
	 */
	protected HTMLDOMElement generateAnchorFor(HTMLDOMElement element
			, String dataAttribute, String anchorClass) {
		CommonFunctions.generateId(element, prefixId);
		HTMLDOMElement anchor = null;
		if (parser.find("[" + dataAttribute + "=" + element.getAttribute("id") + "]").firstResult() == null) {
			if (element.getTagName().equals("A")) {
				anchor = element;
			} else {
				anchor = parser.createElement("a");
				CommonFunctions.generateId(anchor, prefixId);
				anchor.setAttribute("class", anchorClass);
				element.insertBefore(anchor);
			}
			if (!anchor.hasAttribute("name")) {
				anchor.setAttribute("name", anchor.getAttribute("id"));
			}
			anchor.setAttribute(dataAttribute, element.getAttribute("id"));
		}
		return anchor;
	}
	
	/**
	 * Replace the shortcut of elements, that has the shortcut passed.
	 * @param shortcut The shortcut.
	 */
	protected void freeShortcut(String shortcut) {
		String shortcuts;
		String key;
		boolean found = false;
		String alphaNumbers = "1234567890abcdefghijklmnopqrstuvwxyz";
		Collection<HTMLDOMElement> elements = parser.find("[accesskey]").listResults();
		for (HTMLDOMElement element : elements) {
			shortcuts = element.getAttribute("accesskey").toLowerCase();
			if (CommonFunctions.inList(shortcuts, shortcut)) {
				for (int i = 0, length = alphaNumbers.length(); i < length; i++) {
					key = Character.toString(alphaNumbers.charAt(i));
					found = true;
					for (HTMLDOMElement elementWithShortcuts : elements) {
						shortcuts = elementWithShortcuts.getAttribute("accesskey").toLowerCase();
						if (CommonFunctions.inList(shortcuts, key)) {
							found = false;
							break;
						}
					}
					if (found) {
						element.setAttribute("accesskey", key);
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
	}
	
	public void fixShortcut(HTMLDOMElement element) {
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
					if (parser.find(listShortcuts).findChildren("[" + dataAccessKey + "=" + key + "]")
							.firstResult() == null) {
						HTMLDOMElement item = parser.createElement("li");
						item.setAttribute(dataAccessKey, key);
						item.appendText(prefix + " + " + key + ": " + description);
						listShortcuts.appendElement(item);
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

	public void fixSkipper(HTMLDOMElement element, Skipper skipLink) {
		if (!listSkippersAdded) {
			listSkipLinks = generateListSkipLinks();
		}
		if (listSkipLinks != null) {
			HTMLDOMElement anchor = generateAnchorFor(element, dataAnchorFor, classSkipLinkAnchor);
			if (anchor != null) {
				HTMLDOMElement itemLink = parser.createElement("li");
				HTMLDOMElement link = parser.createElement("a");
				link.setAttribute("href", "#" + anchor.getAttribute("name"));
				link.appendText(skipLink.getDefaultText());
				
				List<String> shortcuts = new ArrayList<String>(skipLink.getShortcuts());
				if (!shortcuts.isEmpty()) {
					String shortcut = shortcuts.get(0);
					if (!shortcut.isEmpty()) {
						freeShortcut(shortcut);
						link.setAttribute("accesskey", shortcut);
					}
				}
				CommonFunctions.generateId(link, prefixId);
				
				itemLink.appendElement(link);
				listSkipLinks.appendElement(itemLink);
			}
		}
	}

	public void fixSkippers() {
		Collection<HTMLDOMElement> elements;
		boolean count;
		int index = 0;
		List<String> shortcuts;
		String defaultText;
		for (Skipper skipLink : skippers) {
			elements = parser.find(skipLink.getSelector()).listResults();
			count = elements.size() > 1;
			if (count) {
				index = 1;
			}
			shortcuts = new ArrayList<String>(skipLink.getShortcuts());
			for (HTMLDOMElement element : elements) {
				if (!element.hasAttribute(dataIgnore)) {
					if (count) {
						defaultText = skipLink.getDefaultText() + " " + Integer.toString(index++);
					} else {
						defaultText = skipLink.getDefaultText();
					}
					if (shortcuts.size() > 0) {
						fixSkipper(element, new Skipper(skipLink.getSelector(), defaultText, shortcuts.get(shortcuts.size() - 1)));
						shortcuts.remove(shortcuts.size() - 1);
					} else {
						fixSkipper(element, new Skipper(skipLink.getSelector(), defaultText, ""));
					}
				}
			}
		}
	}

	public void fixHeading(HTMLDOMElement element) {
		if (!validateHeading) {
			validHeading = isValidHeading();
		}
		if (validHeading) {
			HTMLDOMElement anchor = generateAnchorFor(element, dataHeadingAnchorFor, classHeadingAnchor);
			if (anchor != null) {
				HTMLDOMElement list = null;
				int level = getHeadingLevel(element);
				if (level == 1) {
					list = generateListHeading();
				} else {
					HTMLDOMElement superItem = parser.find("#" + idContainerHeading)
							.findDescendants("[" + dataHeadingLevel + "=" + Integer.toString(level - 1) + "]").lastResult();
					if (superItem != null) {
						list = parser.find(superItem).findChildren("ol").firstResult();
						if (list == null) {
							list = parser.createElement("ol");
							superItem.appendElement(list);
						}
					}
				}
				if (list != null) {
					HTMLDOMElement item = parser.createElement("li");
					item.setAttribute(dataHeadingLevel, Integer.toString(level));
					
					HTMLDOMElement link = parser.createElement("a");
					link.setAttribute("href", "#" + anchor.getAttribute("name"));
					link.appendText(element.getTextContent());
					
					item.appendElement(link);
					list.appendElement(item);
				}
			}
		}
	}

	public void fixHeadings() {
		Collection<HTMLDOMElement> elements = parser.find("h1,h2,h3,h4,h5,h6").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixHeading(element);
			}
		}
	}
}