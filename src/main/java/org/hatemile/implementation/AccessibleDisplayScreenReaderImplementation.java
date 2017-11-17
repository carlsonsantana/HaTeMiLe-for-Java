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

import java.util.ArrayList;
import java.util.Arrays;
import org.hatemile.AccessibleDisplay;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
     * The HTML class of content to force the screen reader show the current
     * state of element, before it.
     */
    protected static final String CLASS_FORCE_READ_BEFORE = "force-read-before";

    /**
     * The HTML class of content to force the screen reader show the current
     * state of element, after it.
     */
    protected static final String CLASS_FORCE_READ_AFTER = "force-read-after";

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
     * The name of attribute that links the content of role of element with the
     * element, before it.
     */
    protected static final String DATA_ROLE_BEFORE_OF = "data-rolebeforeof";

    /**
     * The name of attribute that links the content of role of element with the
     * element, after it.
     */
    protected static final String DATA_ROLE_AFTER_OF = "data-roleafterof";

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
     * The prefix text of roles, before the element.
     */
    protected final String attributeRolePrefixBefore;

    /**
     * The suffix text of roles, before the element.
     */
    protected final String attributeRoleSuffixBefore;

    /**
     * The prefix text of roles, after the element.
     */
    protected final String attributeRolePrefixAfter;

    /**
     * The suffix text of roles, after the element.
     */
    protected final String attributeRoleSuffixAfter;

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser parser;

    /**
     * The prefix of generated ids.
     */
    protected final String prefixId;

    /**
     * The roles and it descriptions.
     */
    protected final Map<String, String> roles;

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
        prefixId = configure.getParameter("prefix-generated-ids");
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
        attributeRolePrefixBefore = configure
                .getParameter("attribute-role-prefix-before");
        attributeRoleSuffixBefore = configure
                .getParameter("attribute-role-suffix-before");
        attributeRolePrefixAfter = configure
                .getParameter("attribute-role-prefix-after");
        attributeRoleSuffixAfter = configure
                .getParameter("attribute-role-suffix-after");
        listShortcutsAdded = false;
        listShortcuts = null;

        roles = new HashMap<String, String>();
        roles.put("alert", configure.getParameter("role-alert"));
        roles.put("alertdialog", configure.getParameter("role-alertdialog"));
        roles.put("application", configure.getParameter("role-application"));
        roles.put("article", configure.getParameter("role-article"));
        roles.put("banner", configure.getParameter("role-banner"));
        roles.put("button", configure.getParameter("role-button"));
        roles.put("checkbox", configure.getParameter("role-checkbox"));
        roles.put("columnheader", configure.getParameter("role-columnheader"));
        roles.put("combobox", configure.getParameter("role-combobox"));
        roles.put("complementary",
                configure.getParameter("role-complementary"));
        roles.put("contentinfo", configure.getParameter("role-contentinfo"));
        roles.put("definition", configure.getParameter("role-definition"));
        roles.put("dialog", configure.getParameter("role-dialog"));
        roles.put("directory", configure.getParameter("role-directory"));
        roles.put("document", configure.getParameter("role-document"));
        roles.put("form", configure.getParameter("role-form"));
        roles.put("grid", configure.getParameter("role-grid"));
        roles.put("gridcell", configure.getParameter("role-gridcell"));
        roles.put("group", configure.getParameter("role-group"));
        roles.put("heading", configure.getParameter("role-heading"));
        roles.put("img", configure.getParameter("role-img"));
        roles.put("link", configure.getParameter("role-link"));
        roles.put("list", configure.getParameter("role-list"));
        roles.put("listbox", configure.getParameter("role-listbox"));
        roles.put("listitem", configure.getParameter("role-listitem"));
        roles.put("log", configure.getParameter("role-log"));
        roles.put("main", configure.getParameter("role-main"));
        roles.put("marquee", configure.getParameter("role-marquee"));
        roles.put("math", configure.getParameter("role-math"));
        roles.put("menu", configure.getParameter("role-menu"));
        roles.put("menubar", configure.getParameter("role-menubar"));
        roles.put("menuitem", configure.getParameter("role-menuitem"));
        roles.put("menuitemcheckbox",
                configure.getParameter("role-menuitemcheckbox"));
        roles.put("menuitemradio",
                configure.getParameter("role-menuitemradio"));
        roles.put("navigation", configure.getParameter("role-navigation"));
        roles.put("note", configure.getParameter("role-note"));
        roles.put("option", configure.getParameter("role-option"));
        roles.put("presentation", configure.getParameter("role-presentation"));
        roles.put("progressbar", configure.getParameter("role-progressbar"));
        roles.put("radio", configure.getParameter("role-radio"));
        roles.put("radiogroup", configure.getParameter("role-radiogroup"));
        roles.put("region", configure.getParameter("role-region"));
        roles.put("row", configure.getParameter("role-row"));
        roles.put("rowgroup", configure.getParameter("role-rowgroup"));
        roles.put("rowheader", configure.getParameter("role-rowheader"));
        roles.put("scrollbar", configure.getParameter("role-scrollbar"));
        roles.put("search", configure.getParameter("role-search"));
        roles.put("separator", configure.getParameter("role-separator"));
        roles.put("slider", configure.getParameter("role-slider"));
        roles.put("spinbutton", configure.getParameter("role-spinbutton"));
        roles.put("status", configure.getParameter("role-status"));
        roles.put("tab", configure.getParameter("role-tab"));
        roles.put("tablist", configure.getParameter("role-tablist"));
        roles.put("tabpanel", configure.getParameter("role-tabpanel"));
        roles.put("textbox", configure.getParameter("role-textbox"));
        roles.put("timer", configure.getParameter("role-timer"));
        roles.put("toolbar", configure.getParameter("role-toolbar"));
        roles.put("tooltip", configure.getParameter("role-tooltip"));
        roles.put("tree", configure.getParameter("role-tree"));
        roles.put("treegrid", configure.getParameter("role-treegrid"));
        roles.put("treeitem", configure.getParameter("role-treeitem"));
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
     * Insert a element before or after other element.
     * @param element The reference element.
     * @param insertedElement The element that be inserted.
     * @param before To insert the element before the other element.
     */
    protected void insert(final HTMLDOMElement element,
            final HTMLDOMElement insertedElement, final boolean before) {
        String tagName = element.getTagName();
        Collection<String> appendTags = Arrays.asList("BODY", "A", "FIGCAPTION",
                "LI", "DT", "DD", "LABEL", "OPTION", "TD", "TH");
        Collection<String> controls = Arrays.asList("INPUT", "SELECT",
                "TEXTAREA");
        if (tagName.equals("HTML")) {
            HTMLDOMElement body = parser.find("body").firstResult();
            if (body != null) {
                insert(body, insertedElement, before);
            }
        } else if (appendTags.contains(tagName)) {
            element.prependElement(insertedElement);
        } else if (controls.contains(tagName)) {
            Collection<HTMLDOMElement> labels = new ArrayList<HTMLDOMElement>();
            if (element.hasAttribute("id")) {
                labels = parser.find("label[for=\"" + element.getAttribute("id")
                        + "\"]").listResults();
            }
            if (labels.isEmpty()) {
                labels = parser.find(element).findAncestors("label")
                        .listResults();
            }
            for (HTMLDOMElement label : labels) {
                insert(label, insertedElement, before);
            }
        } else if (before) {
            element.insertBefore(insertedElement);
        } else {
            element.insertAfter(insertedElement);
        }
    }

    /**
     * Force the screen reader display an information of element.
     * @param element The reference element.
     * @param textBefore The text content to show before the element.
     * @param textAfter The text content to show after the element.
     * @param dataBeforeOf The name of attribute that links the before content
     * with element.
     * @param dataAfterOf The name of attribute that links the after content
     * with element.
     */
    protected void forceReadSimple(final HTMLDOMElement element,
            final String textBefore, final String textAfter,
            final String dataBeforeOf, final String dataAfterOf) {
        CommonFunctions.generateId(element, prefixId);
        String identifier = element.getAttribute("id");

        if (!textBefore.isEmpty()) {
            HTMLDOMElement referenceBefore = parser.find("[" + dataBeforeOf
                    + "=\"" + identifier + "\"]").firstResult();

            if (referenceBefore != null) {
                referenceBefore.removeNode();
            }

            HTMLDOMElement span = parser.createElement("span");
            span.setAttribute("class", CLASS_FORCE_READ_BEFORE);
            span.setAttribute(dataBeforeOf, identifier);
            span.appendText(textBefore);
            insert(element, span, true);
        }
        if (!textAfter.isEmpty()) {
            HTMLDOMElement referenceAfter = parser.find("[" + dataAfterOf
                    + "=\"" + identifier + "\"]").firstResult();

            if (referenceAfter != null) {
                referenceAfter.removeNode();
            }

            HTMLDOMElement span = parser.createElement("span");
            span.setAttribute("class", CLASS_FORCE_READ_AFTER);
            span.setAttribute(dataAfterOf, identifier);
            span.appendText(textAfter);
            insert(element, span, false);
        }
    }

    /**
     * Force the screen reader display an information of element with prefixes
     * or suffixes.
     * @param element The reference element.
     * @param value The value to be show.
     * @param textPrefixBefore The prefix of value to show before the element.
     * @param textSuffixBefore The suffix of value to show before the element.
     * @param textPrefixAfter The prefix of value to show after the element.
     * @param textSuffixAfter The suffix of value to show after the element.
     * @param dataBeforeOf The name of attribute that links the before content
     * with element.
     * @param dataAfterOf The name of attribute that links the after content
     * with element.
     */
    protected void forceRead(final HTMLDOMElement element, final String value,
            final String textPrefixBefore, final String textSuffixBefore,
            final String textPrefixAfter, final String textSuffixAfter,
            final String dataBeforeOf, final String dataAfterOf) {
        String textBefore = "";
        String textAfter = "";
        if ((!textPrefixBefore.isEmpty()) || (!textSuffixBefore.isEmpty())) {
            textBefore = textPrefixBefore + value + textSuffixBefore;
        }
        if ((!textPrefixAfter.isEmpty()) || (!textSuffixAfter.isEmpty())) {
            textAfter = textPrefixAfter + value + textSuffixAfter;
        }
        forceReadSimple(element, textBefore, textAfter, dataBeforeOf,
                dataAfterOf);
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

    /**
     * {@inheritDoc}
     */
    public void displayRole(final HTMLDOMElement element) {
        if (element.hasAttribute("role")) {
            String role = element.getAttribute("role");
            if (roles.containsKey(role)) {
                forceRead(element, roles.get(role), attributeRolePrefixBefore,
                        attributeRoleSuffixBefore, attributeRolePrefixAfter,
                        attributeRoleSuffixAfter, DATA_ROLE_BEFORE_OF,
                        DATA_ROLE_AFTER_OF);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllRoles() {
        Collection<HTMLDOMElement> elements = parser.find("[role]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayRole(element);
            }
        }
    }
}
