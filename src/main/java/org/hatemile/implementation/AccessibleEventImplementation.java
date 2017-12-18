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

import org.hatemile.AccessibleEvent;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import org.hatemile.util.IDGenerator;

/**
 * The AccessibleEventImplementation class is official implementation of
 * AccessibleEvent interface.
 */
public class AccessibleEventImplementation implements AccessibleEvent {

    /**
     * The id of script element that replace the event listener methods.
     */
    public static final String ID_SCRIPT_EVENT_LISTENER =
            "script-eventlistener";

    /**
     * The id of script element that contains the list of elements that has
     * inaccessible events.
     */
    public static final String ID_LIST_IDS_SCRIPT = "list-ids-script";

    /**
     * The id of script element that modify the events of elements.
     */
    public static final String ID_FUNCTION_SCRIPT_FIX =
            "id-function-script-fix";

    /**
     * The ID of script element that contains the common functions of scripts.
     */
    public static final String ID_SCRIPT_COMMON_FUNCTIONS =
            "hatemile-common-functions";

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser parser;

    /**
     * The id generator.
     */
    protected final IDGenerator idGenerator;

    /**
     * The state that indicates if the scripts used by solutions was added in
     * parser.
     */
    protected boolean mainScriptAdded;

    /**
     * The script element that contains the list of elements that has
     * inaccessible events.
     */
    protected HTMLDOMElement scriptList;

    /**
     * Initializes a new object that manipulate the accessibility of the
     * Javascript events of elements of parser.
     * @param htmlParser The HTML parser.
     * @param configure The configuration of HaTeMiLe.
     */
    public AccessibleEventImplementation(final HTMLDOMParser htmlParser,
            final Configure configure) {
        parser = Objects.requireNonNull(htmlParser);
        idGenerator = new IDGenerator("event");
        mainScriptAdded = false;
        scriptList = null;
    }

    /**
     * Returns the content of file.
     * @param file The name of file.
     * @return The content of file.
     */
    protected String getContentFromFile(final String file) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(getClass().getResourceAsStream(file));
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine()).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Provide keyboard access for element, if it not has.
     * @param element The element.
     */
    protected void keyboardAccess(final HTMLDOMElement element) {
        if (!element.hasAttribute("tabindex")) {
            String tag = element.getTagName();
            if ((tag.equals("A")) && (!element.hasAttribute("href"))) {
                element.setAttribute("tabindex", "0");
            } else if ((!tag.equals("A")) && (!tag.equals("INPUT"))
                    && (!tag.equals("BUTTON")) && (!tag.equals("SELECT"))
                    && (!tag.equals("TEXTAREA"))) {
                element.setAttribute("tabindex", "0");
            }
        }
    }

    /**
     * Include the scripts used by solutions.
     */
    protected void generateMainScripts() {
        HTMLDOMElement head = parser.find("head").firstResult();
        if (head != null) {
            HTMLDOMElement commonFunctionsScript = parser
                    .find("#" + ID_SCRIPT_COMMON_FUNCTIONS).firstResult();
            if (commonFunctionsScript == null) {
                commonFunctionsScript = parser.createElement("script");
                commonFunctionsScript
                        .setAttribute("id", ID_SCRIPT_COMMON_FUNCTIONS);
                commonFunctionsScript.setAttribute("type", "text/javascript");
                commonFunctionsScript
                        .appendText(getContentFromFile("/js/common.js"));
                head.prependElement(commonFunctionsScript);
            }
            if (parser.find("#" + ID_SCRIPT_EVENT_LISTENER).firstResult()
                    == null) {
                HTMLDOMElement script = parser.createElement("script");
                script.setAttribute("id", ID_SCRIPT_EVENT_LISTENER);
                script.setAttribute("type", "text/javascript");
                script.appendText(getContentFromFile("/js/eventlistener.js"));
                commonFunctionsScript.insertAfter(script);
            }
        }
        HTMLDOMElement local = parser.find("body").firstResult();
        if (local != null) {
            scriptList = parser.find("#" + ID_LIST_IDS_SCRIPT).firstResult();
            if (scriptList == null) {
                scriptList = parser.createElement("script");
                scriptList.setAttribute("id", ID_LIST_IDS_SCRIPT);
                scriptList.setAttribute("type", "text/javascript");
                scriptList.appendText("var activeElements = [];");
                scriptList.appendText("var hoverElements = [];");
                scriptList.appendText("var dragElements = [];");
                scriptList.appendText("var dropElements = [];");
                local.appendElement(scriptList);
            }
            if (parser.find("#" + ID_FUNCTION_SCRIPT_FIX).firstResult()
                    == null) {
                HTMLDOMElement scriptFunction = parser.createElement("script");
                scriptFunction.setAttribute("id", ID_FUNCTION_SCRIPT_FIX);
                scriptFunction.setAttribute("type", "text/javascript");
                scriptFunction.appendText(getContentFromFile("/js/include.js"));

                local.appendElement(scriptFunction);
            }
        }
        mainScriptAdded = true;
    }

    /**
     * Add a type of event in element.
     * @param element The element.
     * @param event The type of event.
     */
    protected void addEventInElement(final HTMLDOMElement element,
            final String event) {
        if (!mainScriptAdded) {
            generateMainScripts();
        }

        if (scriptList != null) {
            idGenerator.generateId(element);
            scriptList.appendText(event + "Elements.push('"
                    + element.getAttribute("id") + "');");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleDropEvents(final HTMLDOMElement element) {
        element.setAttribute("aria-dropeffect", "none");

        addEventInElement(element, "drop");
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleDragEvents(final HTMLDOMElement element) {
        keyboardAccess(element);

        element.setAttribute("aria-grabbed", "false");

        addEventInElement(element, "drag");
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleAllDragandDropEvents() {
        Collection<HTMLDOMElement> draggableElements = parser
                .find("[ondrag],[ondragstart],[ondragend]").listResults();
        for (HTMLDOMElement draggableElement : draggableElements) {
            if (CommonFunctions.isValidElement(draggableElement)) {
                makeAccessibleDragEvents(draggableElement);
            }
        }
        Collection<HTMLDOMElement> droppableElements = parser
                .find("[ondrop],[ondragenter],[ondragleave],[ondragover]")
                .listResults();
        for (HTMLDOMElement droppableElement : droppableElements) {
            if (CommonFunctions.isValidElement(droppableElement)) {
                makeAccessibleDropEvents(droppableElement);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleHoverEvents(final HTMLDOMElement element) {
        keyboardAccess(element);

        addEventInElement(element, "hover");
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleAllHoverEvents() {
        Collection<HTMLDOMElement> elements = parser
                .find("[onmouseover],[onmouseout]").listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                makeAccessibleHoverEvents(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleClickEvents(final HTMLDOMElement element) {
        keyboardAccess(element);

        addEventInElement(element, "active");
    }

    /**
     * {@inheritDoc}
     */
    public void makeAccessibleAllClickEvents() {
        Collection<HTMLDOMElement> elements = parser
                .find("[onclick],[onmousedown],[onmouseup],[ondblclick]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                makeAccessibleClickEvents(element);
            }
        }
    }
}
