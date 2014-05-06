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

public class AccessibleEventImpl implements AccessibleEvent {

	protected final HTMLDOMParser parser;
	protected final String idScriptEvent;
	protected final String prefixId;
	protected final String idListIdsScriptOnClick;
	protected final String idFunctionScriptFixOnClick;
	protected final String dataFocused;
	protected final String dataPressed;
	protected final String dataIgnore;
	protected boolean mainScriptAdded;
	protected boolean otherScriptsAdded;
	protected HTMLDOMElement scriptList;

	public AccessibleEventImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		idScriptEvent = configure.getParameter("id-script-event");
		idListIdsScriptOnClick = configure.getParameter("id-list-ids-script-onclick");
		idFunctionScriptFixOnClick = configure.getParameter("id-function-script-fix-onclick");
		dataFocused = configure.getParameter("data-focused");
		dataPressed = configure.getParameter("data-pressed");
		dataIgnore = configure.getParameter("data-ignore");
		mainScriptAdded = false;
		otherScriptsAdded = false;
	}

	protected void generateMainScript() {
		if (parser.find("#" + idScriptEvent).firstResult() == null) {
			HTMLDOMElement script = parser.createElement("script");
			script.setAttribute("id", idScriptEvent);
			script.setAttribute("type", "text/javascript");

			String javascript = "\nfunction onFocusEvent(element) {\n"
					+ "	element.setAttribute('" + dataFocused + "', 'true');\n"
					+ "	if (element.onmouseover != undefined) {\n"
					+ "		element.onmouseover();\n"
					+ "	}\n"
					+ "}\n"
					+ "function onBlurEvent(element) {\n"
					+ "	if (element.hasAttribute('" + dataFocused + "')) {\n"
					+ "		if ((element.getAttribute('" + dataFocused + "').toLowerCase() == 'true') && (element.onmouseout != undefined)) {\n"
					+ "			element.onmouseout();\n"
					+ "		}\n"
					+ "		element.setAttribute('" + dataFocused + "', 'false');\n"
					+ "	}\n"
					+ "}\n"
					+ "function onKeyPressEvent(element, event) {\n"
					+ "	element.setAttribute('" + dataPressed + "', event.keyCode);\n"
					+ "}\n"
					+ "function onKeyPressUp(element, event) {\n"
					+ "	var key = event.keyCode;\n"
					+ "	var enter1 = \"\\n\".charCodeAt(0);\n"
					+ "	var enter2 = \"\\r\".charCodeAt(0);\n"
					+ "	if ((key == enter1) || (key == enter2)) {\n"
					+ "		if (element.hasAttribute('" + dataPressed + "')) {\n"
					+ "			if (key == parseInt(element.getAttribute('" + dataPressed + "'))) {\n"
					+ "				if (element.onclick != undefined) {\n"
					+ "					element.click();\n"
					+ "				}\n"
					+ "				element.removeAttribute('" + dataPressed + "');\n"
					+ "			}\n"
					+ "		}\n"
					+ "	}\n"
					+ "}\n";
			script.appendText(javascript);

			HTMLDOMElement local = parser.find("head").firstResult();
			if (local == null) {
				local = parser.find("body").firstResult();
			}
			local.appendElement(script);
		}
		mainScriptAdded = true;
	}

	protected void generateOtherScripts() {
		scriptList = parser.find("#" + idListIdsScriptOnClick).firstResult();
		if (scriptList == null) {
			scriptList = parser.createElement("script");
			scriptList.setAttribute("id", idListIdsScriptOnClick);
			scriptList.setAttribute("type", "text/javascript");
			scriptList.appendText("\nidsElementsWithOnClick = [];\n");
			parser.find("body").firstResult().appendElement(scriptList);
		}
		if (parser.find("#" + idFunctionScriptFixOnClick).firstResult() == null) {
			HTMLDOMElement scriptFunction = parser.createElement("script");
			scriptFunction.setAttribute("id", idFunctionScriptFixOnClick);
			scriptFunction.setAttribute("type", "text/javascript");

			String javascript = "\nfor (var i = 0, length = idsElementsWithOnClick.length; i < length; i++) {\n"
					+ "	var element = document.getElementById(idsElementsWithOnClick[i]);\n"
					+ "	element.onkeypress = function(event) {\n"
					+ "		onKeyPressEvent(element, event);\n"
					+ "	};\n"
					+ "	element.onkeyup = function(event) {\n"
					+ "		onKeyPressUp(element, event);\n"
					+ "	};\n"
					+ "}\n";
			scriptFunction.appendText(javascript);
			parser.find("body").firstResult().appendElement(scriptFunction);
		}
		otherScriptsAdded = true;
	}

	protected void addElementIdWithOnClick(String id) {
		scriptList.appendText("idsElementsWithOnClick.push('" + id + "');\n");
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
			if ((!element.hasAttribute("onkeypress")) && (!element.hasAttribute("onkeyup")) && (!element.hasAttribute("onkeydown"))) {
				addElementIdWithOnClick(element.getAttribute("id"));
			}
		}
	}

	public void fixOnClicks() {
		Collection<HTMLDOMElement> elements = parser.find("[onclick]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixOnClick(element);
			}
		}
	}
}
