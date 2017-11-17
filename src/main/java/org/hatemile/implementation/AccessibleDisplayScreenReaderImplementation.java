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
package org.hatemile.implementation;

import org.hatemile.AccessibleDisplay;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;
import java.util.Collection;
import java.util.Objects;

/**
 * The AccessibleDisplayScreenReaderImplementation class is official
 * implementation of AccessibleDisplay interface for screen readers.
 */
public class AccessibleDisplayScreenReaderImplementation
        implements AccessibleDisplay {

    /**
     * The id of list element that contains the description of shortcuts.
     */
    protected static final String ID_CONTAINER_SHORTCUTS =
            "container-shortcuts";

    /**
     * The id of text of description of container of shortcuts descriptions.
     */
    protected static final String ID_TEXT_SHORTCUTS = "text-shortcuts";

    /**
     * The name of attribute that links the description of shortcut, before the
     * element, with the element.
     */
    protected static final String DATA_ATTRIBUTE_ACCESSKEY_BEFORE_OF =
            "data-attributeaccesskeybeforeof";

    /**
     * The name of attribute that links the description of shortcut, after the
     * element, with the element.
     */
    protected static final String DATA_ATTRIBUTE_ACCESSKEY_AFTER_OF =
            "data-attributeaccesskeyafterof";

    /**
     * The browser shortcut prefix.
     */
    protected final String shortcutPrefix;

    /**
     * The prefix text of description of container of shortcut list, before all
     * elements.
     */
    protected final String attributeAccesskeyPrefixBefore;

    /**
     * The suffix text of description of container of shortcut list, before all
     * elements.
     */
    protected final String attributeAccesskeySuffixBefore;

    /**
     * The prefix text of description of container of shortcut list, after all
     * elements.
     */
    protected final String attributeAccesskeyPrefixAfter;

    /**
     * The suffix text of description of container of shortcut list, after all
     * elements.
     */
    protected final String attributeAccesskeySuffixAfter;

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser parser;

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
    public AccessibleDisplayScreenReaderImplementation(
            final HTMLDOMParser htmlParser, final Configure configure,
            final String userAgent) {
        this.parser = Objects.requireNonNull(htmlParser);
        shortcutPrefix = getShortcutPrefix(userAgent,
                configure.getParameter("attribute-accesskey-default"));
        attributeAccesskeyPrefixBefore = configure
                .getParameter("attribute-accesskey-prefix-before");
        attributeAccesskeySuffixBefore = configure
                .getParameter("attribute-accesskey-suffix-before");
        attributeAccesskeyPrefixAfter = configure
                .getParameter("attribute-accesskey-prefix-after");
        attributeAccesskeySuffixAfter = configure
                .getParameter("attribute-accesskey-suffix-after");
        listShortcutsAdded = false;
        listShortcuts = null;
    }

    /**
     * Returns the shortcut prefix of browser.
     * @param userAgent The user agent of browser.
     * @param standartPrefix The default prefix.
     * @return The shortcut prefix of browser.
     */
    protected final String getShortcutPrefix(final String userAgent,
            final String standartPrefix) {
        if (userAgent != null) {
            String lowerUserAgent = userAgent.toLowerCase();
            boolean opera = lowerUserAgent.contains("opera");
            boolean mac = lowerUserAgent.contains("mac");
            boolean konqueror = lowerUserAgent.contains("konqueror");
            boolean spoofer = lowerUserAgent.contains("spoofer");
            boolean safari = lowerUserAgent.contains("applewebkit");
            boolean windows = lowerUserAgent.contains("windows");
            boolean chrome = lowerUserAgent.contains("chrome");
            boolean firefox = lowerUserAgent
                    .matches("firefox/[2-9]|minefield/3");
            boolean ie = lowerUserAgent.contains("msie")
                    || lowerUserAgent.contains("trident");

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
    protected String getDescription(final HTMLDOMElement element) {
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
                descriptionIds = element.getAttribute("aria-labelledby")
                        .split("[ \n\t\r]+");
            } else {
                descriptionIds = element.getAttribute("aria-describedby")
                        .split("[ \n\t\r]+");
            }
            for (int i = 0, length = descriptionIds.length; i < length; i++) {
                HTMLDOMElement elementDescription = parser
                        .find("#" + descriptionIds[i]).firstResult();
                if (elementDescription != null) {
                    description = elementDescription.getTextContent();
                    break;
                }
            }
        } else if ((element.getTagName().equals("INPUT"))
                && (element.hasAttribute("type"))) {
            String type = element.getAttribute("type").toLowerCase();
            if (((type.equals("button")) || (type.equals("submit"))
                    || (type.equals("reset")))
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
        HTMLDOMElement container = parser.find("#" + ID_CONTAINER_SHORTCUTS)
                .firstResult();
        if (container == null) {
            HTMLDOMElement local = parser.find("body").firstResult();
            if (local != null) {
                container = parser.createElement("div");
                container.setAttribute("id", ID_CONTAINER_SHORTCUTS);

                HTMLDOMElement textContainer = parser.createElement("span");
                textContainer.setAttribute("id", ID_TEXT_SHORTCUTS);

                container.appendElement(textContainer);

                String beforeText = attributeAccesskeyPrefixBefore
                        + attributeAccesskeySuffixBefore;
                if (!beforeText.isEmpty()) {
                    textContainer.appendText(beforeText);
                    local.prependElement(container);
                } else {
                    textContainer.appendText(attributeAccesskeyPrefixAfter
                            + attributeAccesskeySuffixAfter);
                    local.appendElement(container);
                }
            }
        }

        HTMLDOMElement htmlList = null;
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
     * {@inheritDoc}
     */
    public void displayShortcut(final HTMLDOMElement element) {
        if (element.hasAttribute("accesskey")) {
            String description = getDescription(element);
            if (!element.hasAttribute("title")) {
                element.setAttribute("title", description);
            }

            if (!listShortcutsAdded) {
                listShortcuts = generateListShortcuts();
            }

            if (listShortcuts != null) {
                String[] keys = element.getAttribute("accesskey")
                        .split("[ \n\t\r]+");
                for (int i = 0, length = keys.length; i < length; i++) {
                    String key = keys[i].toUpperCase();
                    String attribute = "[" + DATA_ATTRIBUTE_ACCESSKEY_BEFORE_OF
                            + "=\"" + key + "\"]";
                    if (parser.find(listShortcuts).findChildren(attribute)
                            .firstResult() == null) {
                        HTMLDOMElement item = parser.createElement("li");
                        item.setAttribute(DATA_ATTRIBUTE_ACCESSKEY_BEFORE_OF,
                                key);
                        item.setAttribute(DATA_ATTRIBUTE_ACCESSKEY_AFTER_OF,
                                key);
                        item.appendText(shortcutPrefix + " + " + key + ": "
                                + description);
                        listShortcuts.appendElement(item);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllShortcuts() {
        Collection<HTMLDOMElement> elements = parser.find("[accesskey]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayShortcut(element);
            }
        }
    }
}
