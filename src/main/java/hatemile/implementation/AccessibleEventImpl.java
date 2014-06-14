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

import java.util.Collection;

/**
 * The AccessibleEventImpl class is official implementation of AccessibleEvent
 * interface.
 * @see AccessibleEvent
 * @version 1.0
 */
public class AccessibleEventImpl implements AccessibleEvent {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The id of script element that contains the functions that put events
	 * in the elements.
	 */
	protected final String idScriptEvent;
	
	/**
	 * The id of script element that contains the list of elements that will
	 * have its events modified.
	 */
	protected final String idListIdsScriptOnClick;
	
	/**
	 * The id of script element that modify the events of elements.
	 */
	protected final String idFunctionScriptFixOnClick;
	
	/**
	 * The prefix of generated id.
	 */
	protected final String prefixId;
	
	/**
	 * The name of attribute for that the element not can be modified
	 * by HaTeMiLe.
	 */
	protected final String dataIgnore;
	
	/**
	 * The state that indicates if the main script was added in parser.
	 */
	protected boolean mainScriptAdded;
	
	/**
	 * The state that indicates if the other scripts was added in parser.
	 */
	protected boolean otherScriptsAdded;
	
	/**
	 * The script element that contains the list of elements that will 
	 * have its events modified.
	 */
	protected HTMLDOMElement scriptList;

	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * Javascript events of elements of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleEventImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		idScriptEvent = configure.getParameter("id-script-event");
		dataIgnore = configure.getParameter("data-ignore");
		idListIdsScriptOnClick = configure.getParameter("id-list-ids-script-active");
		idFunctionScriptFixOnClick = configure.getParameter("id-function-script-fix-active");
		mainScriptAdded = false;
		otherScriptsAdded = false;
	}

	/**
	 * Generate the main script in parser.
	 */
	protected void generateMainScript() {
		if (parser.find("#" + idScriptEvent).firstResult() == null) {
			HTMLDOMElement script = parser.createElement("script");
			script.setAttribute("id", idScriptEvent);
			script.setAttribute("type", "text/javascript");
			String javascript = "function onFocusEvent(element) {"
					+ "if (element.onmouseover != undefined) {"
					+ "element.onmouseover();"
					+ "}"
					+ "}"
					+ "function onBlurEvent(element) {"
					+ "if (element.onmouseout != undefined) {"
					+ "element.onmouseout();"
					+ "}"
					+ "}"
					+ "function isEnter(keyCode) {"
					+ "var enter1 = \"\\n\".charCodeAt(0);"
					+ "var enter2 = \"\\r\".charCodeAt(0);"
					+ "return ((keyCode == enter1) || (keyCode == enter2));"
					+ "}"
					+ "function onKeyDownEvent(element, event) {"
					+ "if (isEnter(event.keyCode) && (element.onmousedown != undefined)) {"
					+ "element.onmousedown();"
					+ "}"
					+ "}"
					+ "function onKeyPressEvent(element, event) {"
					+ "if (isEnter(event.keyCode)) {"
					+ "if (element.onclick != undefined) {"
					+ "element.click();"
					+ "} else if (element.ondblclick != undefined) {"
					+ "element.ondblclick();"
					+ "}"
					+ "}"
					+ "}"
					+ "function onKeyUpEvent(element, event) {"
					+ "if (isEnter(event.keyCode) && (element.onmouseup != undefined)) {"
					+ "element.onmouseup();"
					+ "}"
					+ "}";
			script.appendText(javascript);

			HTMLDOMElement local = parser.find("head").firstResult();
			if (local == null) {
				local = parser.find("body").firstResult();
			}
			local.appendElement(script);
		}
		mainScriptAdded = true;
	}

	/**
	 * Generate the other scripts in parser.
	 */
	protected void generateOtherScripts() {
		scriptList = parser.find("#" + idListIdsScriptOnClick).firstResult();
		if (scriptList == null) {
			scriptList = parser.createElement("script");
			scriptList.setAttribute("id", idListIdsScriptOnClick);
			scriptList.setAttribute("type", "text/javascript");
			scriptList.appendText("idsElementsWithOnClick = [];");
			parser.find("body").firstResult().appendElement(scriptList);
		}
		if (parser.find("#" + idFunctionScriptFixOnClick).firstResult() == null) {
			HTMLDOMElement scriptFunction = parser.createElement("script");
			scriptFunction.setAttribute("id", idFunctionScriptFixOnClick);
			scriptFunction.setAttribute("type", "text/javascript");

			String javascript = "var element;"
					+ "for (var i = 0, length = idsElementsWithOnClick.length; i < length; i++) {"
					+ "element = document.getElementById(idsElementsWithOnClick[i]);"
					+ "if (element.onkeypress == undefined) {"
					+ "element.onkeypress = function(event) {"
					+ "onKeyPressEvent(element, event);"
					+ "};"
					+ "}"
					+ "if (element.onkeyup == undefined) {"
					+ "element.onkeyup = function(event) {"
					+ "onKeyUpEvent(element, event);"
					+ "};"
					+ "}"
					+ "if (element.onkeydown == undefined) {"
					+ "element.onkeydown = function(event) {"
					+ "onKeyDownEvent(element, event);"
					+ "};"
					+ "}"
					+ "}";
			scriptFunction.appendText(javascript);
			parser.find("body").firstResult().appendElement(scriptFunction);
		}
		otherScriptsAdded = true;
	}

	/**
	 * Add the id of element in list of elements that will
	 * have its events modified.
	 * @param element The element with id.
	 */
	protected void addElementIdWithOnClick(HTMLDOMElement element) {
		scriptList.appendText("idsElementsWithOnClick.push('" + element.getAttribute("id") + "');");
	}

	public void fixOnHover(HTMLDOMElement element) {
		if (!mainScriptAdded) {
			generateMainScript();
		}
		String tag = element.getTagName();
		if (!((tag.equals("INPUT")) || (tag.equals("BUTTON")) || (tag.equals("A")) || (tag.equals("SELECT")) || (tag.equals("TEXTAREA")) || (element.hasAttribute("tabindex")))) {
			element.setAttribute("tabindex", "0");
		}
		if (!element.hasAttribute("onfocus")) {
			element.setAttribute("onfocus", "onFocusEvent(this);");
		}
		if (!element.hasAttribute("onblur")) {
			element.setAttribute("onblur", "onBlurEvent(this);");
		}
	}

	public void fixOnHovers() {
		Collection<HTMLDOMElement> elements = parser.find("[onmouseover],[onmouseout]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixOnHover(element);
			}
		}
	}

	public void fixOnClick(HTMLDOMElement element) {
		String tag = element.getTagName();
		if (!((tag.equals("INPUT")) || (tag.equals("BUTTON")) || (tag.equals("A")))) {
			if (!mainScriptAdded) {
				generateMainScript();
			}
			if (!otherScriptsAdded) {
				generateOtherScripts();
			}
			if (!((element.hasAttribute("tabindex")) || (tag.equals("SELECT")) || (tag.equals("TEXTAREA")))) {
				element.setAttribute("tabindex", "0");
			}
			CommonFunctions.generateId(element, prefixId);
			addElementIdWithOnClick(element);
		}
	}

	public void fixOnClicks() {
		Collection<HTMLDOMElement> elements = parser.find("[onclick],[onmousedown],[onmouseup],[ondblclick]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixOnClick(element);
			}
		}
	}
}
