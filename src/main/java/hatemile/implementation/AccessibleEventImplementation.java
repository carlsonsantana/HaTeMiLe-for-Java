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

import hatemile.AccessibleEvent;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;
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
	 * The name of attribute for not modify the elements.
	 */
	protected final String dataIgnore;
	
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
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleEventImplementation(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		idScriptEventListener = "script-eventlistener";
		idListIdsScript = "list-ids-script";
		idFunctionScriptFix = "id-function-script-fix";
		dataIgnore = "data-ignoreaccessibilityfix";
		mainScriptAdded = false;
		scriptList = null;
	}
	
	/**
	 * Returns the content of file.
	 * @param file The name of file.
	 * @return The content of file.
	 */
	protected String getContentFromFile(String file) {
		BufferedReader bufferedReader
				= new BufferedReader(new InputStreamReader(File.class.getResourceAsStream(file)));
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException ex) {
			Logger.getLogger(AccessibleEventImplementation.class.getName()).log(Level.SEVERE, null, ex);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Provide keyboard access for element, if it not has.
	 * @param element The element.
	 */
	protected void keyboardAccess(HTMLDOMElement element) {
		if (!element.hasAttribute("tabindex")) {
			String tag = element.getTagName();
			if ((tag.equals("A")) && (!element.hasAttribute("href"))) {
				element.setAttribute("tabindex", "0");
			} else if ((!tag.equals("A")) && (!tag.equals("INPUT")) && (!tag.equals("BUTTON"))
					&& (!tag.equals("SELECT")) && (!tag.equals("TEXTAREA"))) {
				element.setAttribute("tabindex", "0");
			}
		}
	}
	
	/**
	 * Include the scripts used by solutions.
	 */
	protected void generateMainScripts() {
		HTMLDOMElement head = parser.find("head").firstResult();
		if ((head != null) && (parser.find("#" + idScriptEventListener).firstResult() == null)) {
			HTMLDOMElement script = parser.createElement("script");
			script.setAttribute("id", idScriptEventListener);
			script.setAttribute("type", "text/javascript");
			script.appendText(getContentFromFile("/js/eventlistener.js"));
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
				scriptFunction.setAttribute("id", idFunctionScriptFix);
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
	protected void addEventInElement(HTMLDOMElement element, String event) {
		if (!mainScriptAdded) {
			generateMainScripts();
		}
		
		if (scriptList != null) {
			CommonFunctions.generateId(element, prefixId);
			scriptList.appendText(event + "Elements.push('" + element.getAttribute("id") + "');");
		}
	}
	
	public void fixDrop(HTMLDOMElement element) {
		element.setAttribute("aria-dropeffect", "none");
		
		addEventInElement(element, "drop");
	}

	public void fixDrag(HTMLDOMElement element) {
		keyboardAccess(element);
		
		addEventInElement(element, "drag");
	}

	public void fixDragsandDrops() {
		Collection<HTMLDOMElement> draggableElements = parser.find("[ondrag],[ondragstart],[ondragend]").listResults();
		for (HTMLDOMElement element : draggableElements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixDrag(element);
			}
		}
		Collection<HTMLDOMElement> droppableElements = parser.find("[ondrop],[ondragenter],[ondragleave],[ondragover]").listResults();
		for (HTMLDOMElement element : droppableElements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixDrop(element);
			}
		}
	}
	
	public void fixHover(HTMLDOMElement element) {
		keyboardAccess(element);
		
		addEventInElement(element, "hover");
	}
	
	public void fixHovers() {
		Collection<HTMLDOMElement> elements = parser.find("[onmouseover],[onmouseout]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixHover(element);
			}
		}
	}
	
	public void fixActive(HTMLDOMElement element) {
		keyboardAccess(element);
		
		addEventInElement(element, "active");
	}
	
	public void fixActives() {
		Collection<HTMLDOMElement> elements = parser
				.find("[onclick],[onmousedown],[onmouseup],[ondblclick]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixActive(element);
			}
		}
	}
}