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

/**
 * The AccessibleFormImpl class is official implementation of AccessibleForm
 * interface.
 * @see AccessibleForm
 * @version 1.0
 */
public class AccessibleFormImpl implements AccessibleForm {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The prefix of generated id.
	 */
	protected final String prefixId;
	
	/**
	 * The name of attribute for the label of required field.
	 */
	protected final String dataLabelRequiredField;
	
	/**
	 * The prefix of required field.
	 */
	protected final String prefixRequiredField;
	
	/**
	 * The suffix of required field.
	 */
	protected final String suffixRequiredField;
	
	/**
	 * The name of attribute for the element that not can be modified
	 * by HaTeMiLe.
	 */
	protected final String dataIgnore;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * forms of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleFormImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		dataLabelRequiredField = configure.getParameter("data-label-required-field");
		prefixRequiredField = configure.getParameter("prefix-required-field");
		suffixRequiredField = configure.getParameter("suffix-required-field");
		dataIgnore = configure.getParameter("data-ignore");
	}

	public void fixRequiredField(HTMLDOMElement requiredField) {
		if (requiredField.hasAttribute("required")) {
			requiredField.setAttribute("aria-required", "true");
			Collection<HTMLDOMElement> labels = null;
			if (requiredField.hasAttribute("id")) {
				labels = parser.find("label[for=" + requiredField.getAttribute("id").trim() + "]").listResults();
			}
			if ((labels == null) || (labels.isEmpty())) {
				labels = parser.find(requiredField).findAncestors("label").listResults();
			}
			for (HTMLDOMElement label : labels) {
				label.setAttribute(dataLabelRequiredField, "true");
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

	public void fixDisabledField(HTMLDOMElement disabledField) {
		if (disabledField.hasAttribute("disabled")) {
			disabledField.setAttribute("aria-disabled", "true");
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

	public void fixReadOnlyField(HTMLDOMElement readOnlyField) {
		if (readOnlyField.hasAttribute("readonly")) {
			readOnlyField.setAttribute("aria-readonly", "true");
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

	public void fixRangeField(HTMLDOMElement rangeField) {
		if (rangeField.hasAttribute("min")) {
			rangeField.setAttribute("aria-valuemin", rangeField.getAttribute("min").trim());
		}
		if (rangeField.hasAttribute("max")) {
			rangeField.setAttribute("aria-valuemax", rangeField.getAttribute("max").trim());
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

	public void fixTextField(HTMLDOMElement textField) {
		if ((textField.getTagName().equals("INPUT")) && (textField.hasAttribute("type"))) {
			String type = textField.getAttribute("type").trim().toLowerCase();
			if ((type.equals("text")) || (type.equals("password")) || (type.equals("search")) || (type.equals("email"))
					|| (type.equals("url")) || (type.equals("tel")) || (type.equals("number"))) {
				textField.setAttribute("aria-multiline", "false");
			}
		} else if (textField.getTagName().equals("TEXTAREA")) {
			textField.setAttribute("aria-multiline", "true");
		}
	}

	public void fixTextFields() {
		Collection<HTMLDOMElement> elements = parser.find("input[type=text],input[type=password],input[type=search],input[type=email],input[type=url],input[type=tel],input[type=number],textarea").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixTextField(element);
			}
		}
	}

	public void fixSelectField(HTMLDOMElement selectField) {
		if (selectField.getTagName().equals("SELECT")) {
			if (selectField.hasAttribute("multiple")) {
				selectField.setAttribute("aria-multiselectable", "true");
			} else {
				selectField.setAttribute("aria-multiselectable", "false");
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

	public void fixLabel(HTMLDOMElement label) {
		if (label.getTagName().equals("LABEL")) {
			HTMLDOMElement input;
			if (label.hasAttribute("for")) {
				input = parser.find("#" + label.getAttribute("for").trim()).firstResult();
			} else {
				input = parser.find(label).findDescendants("input,select,textarea").firstResult();
				if (input != null) {
					CommonFunctions.generateId(input, prefixId);
					label.setAttribute("for", input.getAttribute("id").trim());
				}
			}
			if (input != null) {
				if (!input.hasAttribute("aria-label")) {
					String contentLabel = label.getTextContent().trim();
					if (input.hasAttribute("aria-required")) {
						if ((input.getAttribute("aria-required").trim().toLowerCase().equals("true")) && (!contentLabel.contains(prefixRequiredField))) {
							contentLabel = prefixRequiredField + " " + contentLabel;
						}
						if ((input.getAttribute("aria-required").trim().toLowerCase().equals("true")) && (!contentLabel.contains(suffixRequiredField))) {
							contentLabel += " " + suffixRequiredField;
						}
					}
					input.setAttribute("aria-label", contentLabel);
				}
				CommonFunctions.generateId(label, prefixId);
				input.setAttribute("aria-labelledby", CommonFunctions.increaseInList(input.getAttribute("aria-labelledby"), label.getAttribute("id")));
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