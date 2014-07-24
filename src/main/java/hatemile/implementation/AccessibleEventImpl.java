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
 * @version 2014-07-23
 */
public class AccessibleEventImpl implements AccessibleEvent {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The id of script element that has the functions that put events in the
	 * elements.
	 */
	protected final String idScriptEvent;
	
	/**
	 * The id of script element that has the list of elements that will have its
	 * events modified.
	 */
	protected final String idListIdsScriptOnActive;
	
	/**
	 * The id of script element that modify the events of elements.
	 */
	protected final String idFunctionScriptFixOnActive;
	
	/**
	 * The prefix of generated id.
	 */
	protected final String prefixId;
	
	/**
	 * The name of attribute for that the element not can be modified by
	 * HaTeMiLe.
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
	 * The script element that contains the list of elements that will have its
	 * events modified.
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
		idListIdsScriptOnActive = configure.getParameter("id-list-ids-script-onactive");
		idFunctionScriptFixOnActive = configure.getParameter("id-function-script-fix-onactive");
		dataIgnore = "data-" + configure.getParameter("data-ignore");
		mainScriptAdded = false;
		otherScriptsAdded = false;
	}
	
	/**
	 * Generate the main script in parser.
	 */
	protected void generateMainScript() {
		HTMLDOMElement local = parser.find("head").firstResult();
		if (local == null) {
			local = parser.find("body").firstResult();
		}
		if ((local != null) && (parser.find("#" + idScriptEvent).firstResult() == null)) {
			HTMLDOMElement script = parser.createElement("script");
			script.setAttribute("id", idScriptEvent);
			script.setAttribute("type", "text/javascript");
			String javascript = "function onFocusEvent(e){if(e.onmouseover!=undefined){try{e.onmouseover();}catch(x){}}}function onBlurEvent(e){if(e.onmouseout!=undefined){try{e.onmouseout();}catch(x){}}}function isEnter(k){var n=\"\\n\".charCodeAt(0);var r=\"\\r\".charCodeAt(0);return ((k==n)||(k==r));}function onKeyDownEvent(l,v){if(isEnter(v.keyCode)&&(l.onmousedown!=undefined)){try{l.onmousedown();}catch(x){}}}function onKeyPressEvent(l,v){if(isEnter(v.keyCode)){if(l.onclick!=undefined){try{l.click();}catch(x){}}else if(l.ondblclick!=undefined){try{l.ondblclick();}catch(x){}}}}function onKeyUpEvent(l,v){if(isEnter(v.keyCode)&&(l.onmouseup!=undefined)){try{l.onmouseup();}catch(x){}}}";
			script.appendText(javascript);
			local.appendElement(script);
		}
		mainScriptAdded = true;
	}
	
	/**
	 * Generate the other scripts in parser.
	 */
	protected void generateOtherScripts() {
		HTMLDOMElement local = parser.find("body").firstResult();
		if (local != null) {
			scriptList = parser.find("#" + idListIdsScriptOnActive).firstResult();
			if (scriptList == null) {
				scriptList = parser.createElement("script");
				scriptList.setAttribute("id", idListIdsScriptOnActive);
				scriptList.setAttribute("type", "text/javascript");
				scriptList.appendText("var s=[];");
				local.appendElement(scriptList);
			}
			if (parser.find("#" + idFunctionScriptFixOnActive).firstResult() == null) {
				HTMLDOMElement scriptFunction = parser.createElement("script");
				scriptFunction.setAttribute("id", idFunctionScriptFixOnActive);
				scriptFunction.setAttribute("type", "text/javascript");
				String javascript = "var e;for(var i=0,l=s.length;i<l;i++){e=document.getElementById(s[i]);if(e.onkeypress==undefined){e.onkeypress=function(v){onKeyPressEvent(e,v);};}if(e.onkeyup==undefined){e.onkeyup=function(v){onKeyUpEvent(e,v);};}if(e.onkeydown==undefined){e.onkeydown=function(v){onKeyDownEvent(e,v);};}}";
				scriptFunction.appendText(javascript);
				local.appendElement(scriptFunction);
			}
		}
		otherScriptsAdded = true;
	}
	
	/**
	 * Add the id of element in list of elements that will have its events
	 * modified.
	 * @param element The element with id.
	 */
	protected void addEventInElement(HTMLDOMElement element) {
		if (!otherScriptsAdded) {
			generateOtherScripts();
		}
		
		if (scriptList != null) {
			CommonFunctions.generateId(element, prefixId);
			scriptList.appendText("s.push('" + element.getAttribute("id") + "');");
		} else {
			if (!element.hasAttribute("onkeypress")) {
				element.setAttribute("onkeypress", "try{onKeyPressEvent(this,event);}catch(x){}");
			}
			if (!element.hasAttribute("onkeyup")) {
				element.setAttribute("onkeyup", "try{onKeyUpEvent(this,event);}catch(x){}");
			}
			if (!element.hasAttribute("onkeydown")) {
				element.setAttribute("onkeydown", "try{onKeyDownEvent(this,event);}catch(x){}");
			}
		}
	}
	
	public void fixOnHover(HTMLDOMElement element) {
		String tag = element.getTagName();
		if (!((tag.equals("INPUT")) || (tag.equals("BUTTON")) || (tag.equals("A"))
				|| (tag.equals("SELECT")) || (tag.equals("TEXTAREA"))
				|| (element.hasAttribute("tabindex")))) {
			element.setAttribute("tabindex", "0");
		}
		
		if (!mainScriptAdded) {
			generateMainScript();
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
	
	public void fixOnActive(HTMLDOMElement element) {
		String tag = element.getTagName();
		if (!((tag.equals("INPUT")) || (tag.equals("BUTTON")) || (tag.equals("A")))) {
			if (!((element.hasAttribute("tabindex")) || (tag.equals("SELECT"))
					|| (tag.equals("TEXTAREA")))) {
				element.setAttribute("tabindex", "0");
			}
			
			if (!mainScriptAdded) {
				generateMainScript();
			}
			
			addEventInElement(element);
		}
	}
	
	public void fixOnActives() {
		Collection<HTMLDOMElement> elements = parser
				.find("[onclick],[onmousedown],[onmouseup],[ondblclick]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixOnActive(element);
			}
		}
	}
}