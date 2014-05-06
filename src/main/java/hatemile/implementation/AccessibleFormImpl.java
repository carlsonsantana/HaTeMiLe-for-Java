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

import hatemile.AccessibleForm;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;

import java.util.Collection;

public class AccessibleFormImpl implements AccessibleForm {
	protected final HTMLDOMParser parser;
	protected final String prefixId;
	protected final String classRequiredField;
	protected final String sufixRequiredField;
	protected final String dataIgnore;
	
	public AccessibleFormImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		classRequiredField = configure.getParameter("class-required-field");
		sufixRequiredField = configure.getParameter("sufix-required-field");
		dataIgnore = configure.getParameter("data-ignore");
	}

	public void fixRequiredField(HTMLDOMElement element) {
		if (element.hasAttribute("required")) {
			element.setAttribute("aria-required", "true");
			Collection<HTMLDOMElement> labels = null;
			if (element.hasAttribute("id")) {
				labels = parser.find("label[for=" + element.getAttribute("id") + "]").listResults();
			}
			if ((labels == null) || (labels.isEmpty())) {
				labels = parser.find(element).findAncestors("label").listResults();
			}
			for (HTMLDOMElement label : labels) {
				label.setAttribute("class", CommonFunctions.increaseInList(label.getAttribute("class"), classRequiredField));
			}
		}
	}

	public void fixRequiredFields() {
		Collection<HTMLDOMElement> elements = parser.find("[required]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixRequiredField(element);
			}
		}
	}

	public void fixDisabledField(HTMLDOMElement element) {
		if (element.hasAttribute("disabled")) {
			element.setAttribute("aria-disabled", "true");
		}
	}

	public void fixDisabledFields() {
		Collection<HTMLDOMElement> elements = parser.find("[disabled]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixDisabledField(element);
			}
		}
	}

	public void fixReadOnlyField(HTMLDOMElement element) {
		if (element.hasAttribute("readonly")) {
			element.setAttribute("aria-readonly", "true");
		}
	}

	public void fixReadOnlyFields() {
		Collection<HTMLDOMElement> elements = parser.find("[readonly]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixReadOnlyField(element);
			}
		}
	}

	public void fixRangeField(HTMLDOMElement element) {
		if (element.hasAttribute("min")) {
			element.setAttribute("aria-valuemin", element.getAttribute("min").trim());
		}
		if (element.hasAttribute("max")) {
			element.setAttribute("aria-valuemax", element.getAttribute("max").trim());
		}
	}

	public void fixRangeFields() {
		Collection<HTMLDOMElement> elements = parser.find("[min],[max]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixRangeField(element);
			}
		}
	}

	public void fixTextField(HTMLDOMElement element) {
		if ((element.getTagName().equals("INPUT")) && (element.hasAttribute("type"))) {
			String type = element.getAttribute("type").toLowerCase().trim();
			if ((type.equals("text")) || (type.equals("search")) || (type.equals("email"))
					|| (type.equals("url")) || (type.equals("tel")) || (type.equals("number"))) {
				element.setAttribute("aria-multiline", "false");
			}
		} else if (element.getTagName().equals("TEXTAREA")) {
			element.setAttribute("aria-multiline", "true");
		}
	}

	public void fixTextFields() {
		Collection<HTMLDOMElement> elements = parser.find("input[type=text],input[type=search],input[type=email],input[type=url],input[type=tel],input[type=number],textarea").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixTextField(element);
			}
		}
	}

	public void fixSelectField(HTMLDOMElement element) {
		if (element.getTagName().equals("SELECT")) {
			if (element.hasAttribute("multiple")) {
				element.setAttribute("aria-multiselectable", "true");
			} else {
				element.setAttribute("aria-multiselectable", "false");
			}
		}
	}

	public void fixSelectFields() {
		Collection<HTMLDOMElement> elements = parser.find("select").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixSelectField(element);
			}
		}
	}

	public void fixLabel(HTMLDOMElement element) {
		if (element.getTagName().equals("LABEL")) {
			HTMLDOMElement input;
			if (element.hasAttribute("for")) {
				input = parser.find("#" + element.getAttribute("for")).firstResult();
			} else {
				input = parser.find(element).findDescendants("input,select,textarea").firstResult();
				if (input != null) {
					CommonFunctions.generateId(input, prefixId);
					element.setAttribute("for", input.getAttribute("id"));
				}
			}
			if (input != null) {
				if (!input.hasAttribute("aria-label")) {
					String label = element.getTextContent().replaceAll("[ \n\r\t]+", " ").trim();
					if (input.hasAttribute("aria-required")) {
						if ((input.getAttribute("aria-required").toLowerCase().trim().equals("true")) && (!label.contains(sufixRequiredField))) {
							label += " " + sufixRequiredField;
						}
					}
					input.setAttribute("aria-label", label);
				}
				CommonFunctions.generateId(element, prefixId);
				input.setAttribute("aria-labelledby", CommonFunctions.increaseInList(input.getAttribute("aria-labelledby"), element.getAttribute("id")));
			}
		}
	}

	public void fixLabels() {
		Collection<HTMLDOMElement> elements = parser.find("label").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixLabel(element);
			}
		}
	}
}