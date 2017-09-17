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

import hatemile.AccessibleEvent;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.html.HTMLDOMElement;
import hatemile.util.html.HTMLDOMParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AccessibleEventImplementation class is official implementation of
 * AccessibleEvent interface.
 */
public class AccessibleEventImplementation implements AccessibleEvent {

	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;

	/**
	 * The id of script element that replace the event listener methods.
	 */
	protected final String idScriptEventListener;

	/**
	 * The id of script element that contains the list of elements that has
	 * inaccessible events.
	 */
	protected final String idListIdsScript;

	/**
	 * The id of script element that modify the events of elements.
	 */
	protected final String idFunctionScriptFix;

	/**
	 * The prefix of generated ids.
	 */
	protected final String prefixId;

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
	 * The content of eventlistener.js.
	 */
	protected static String eventListenerScriptContent = null;

	/**
	 * The content of include.js.
	 */
	protected static String includeScriptContent = null;

	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * Javascript events of elements of parser.
	 * @param htmlParser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleEventImplementation(final HTMLDOMParser htmlParser, final Configure configure) {
		this.parser = htmlParser;
		prefixId = configure.getParameter("prefix-generated-ids");
		idScriptEventListener = "script-eventlistener";
		idListIdsScript = "list-ids-script";
		idFunctionScriptFix = "id-function-script-fix";
		mainScriptAdded = false;
		scriptList = null;
	}

	/**
	 * Returns the content of file.
	 * @param file The name of file.
	 * @return The content of file.
	 */
	protected String getContentFromFile(final String file) {
		InputStreamReader inputStreamReader = new InputStreamReader(File.class
						.getResourceAsStream(file));
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException ex) {
			Logger.getLogger(AccessibleEventImplementation.class.getName())
					.log(Level.SEVERE, null, ex);
		}
		try {
			bufferedReader.close();
			inputStreamReader.close();
		} catch (IOException ex) {
			Logger.getLogger(AccessibleEventImplementation.class.getName())
					.log(Level.SEVERE, null, ex);
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
		if ((head != null)
				&& (parser.find("#" + idScriptEventListener)
						.firstResult() == null)) {
			HTMLDOMElement script = parser.createElement("script");

			if (eventListenerScriptContent == null) {
				eventListenerScriptContent = getContentFromFile("/js/eventlistener.js");
			}

			script.setAttribute("id", idScriptEventListener);
			script.setAttribute("type", "text/javascript");
			script.appendText(eventListenerScriptContent);

			if (head.hasChildren()) {
				head.getFirstElementChild().insertBefore(script);
			} else {
				head.appendElement(script);
			}
		}
		HTMLDOMElement local = parser.find("body").firstResult();
		if (local != null) {
			scriptList = parser.find("#" + idListIdsScript).firstResult();
			if (scriptList == null) {
				scriptList = parser.createElement("script");
				scriptList.setAttribute("id", idListIdsScript);
				scriptList.setAttribute("type", "text/javascript");
				scriptList.appendText("var activeElements = [];");
				scriptList.appendText("var hoverElements = [];");
				scriptList.appendText("var dragElements = [];");
				scriptList.appendText("var dropElements = [];");
				local.appendElement(scriptList);
			}
			if (parser.find("#" + idFunctionScriptFix).firstResult() == null) {
				HTMLDOMElement scriptFunction = parser.createElement("script");

				if (includeScriptContent == null) {
					includeScriptContent = getContentFromFile("/js/include.js");
				}

				scriptFunction.setAttribute("id", idFunctionScriptFix);
				scriptFunction.setAttribute("type", "text/javascript");
				scriptFunction.appendText(includeScriptContent);

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
	protected void addEventInElement(final HTMLDOMElement element, final String event) {
		if (!mainScriptAdded) {
			generateMainScripts();
		}

		if (scriptList != null) {
			CommonFunctions.generateId(element, prefixId);
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
