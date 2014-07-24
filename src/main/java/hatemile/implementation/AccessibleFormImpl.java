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
 * @version 2014-07-23
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
	 * The name of attribute for the label of a required field.
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
	 * The name of attribute for that the element not can be modified by
	 * HaTeMiLe.
	 */
	protected final String dataIgnore;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the forms
	 * of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleFormImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		dataLabelRequiredField = "data-" + configure.getParameter("data-label-required-field");
		dataIgnore = "data-" + configure.getParameter("data-ignore");
		prefixRequiredField = configure.getParameter("prefix-required-field");
		suffixRequiredField = configure.getParameter("suffix-required-field");
	}
	
	/**
	 * Do the label or the aria-label to inform in label that the field is
	 * required.
	 * @param label The label.
	 * @param requiredField The required field.
	 */
	protected void fixLabelRequiredField(HTMLDOMElement label, HTMLDOMElement requiredField) {
		if ((requiredField.hasAttribute("required"))
				|| ((requiredField.hasAttribute("aria-required"))
				&& (requiredField.getAttribute("aria-required").toLowerCase().equals("true")))) {
			if (!label.hasAttribute(dataLabelRequiredField)) {
				label.setAttribute(dataLabelRequiredField, "true");
			}
			
			if (requiredField.hasAttribute("aria-label")) {
				String contentLabel = requiredField.getAttribute("aria-label");
				if ((!prefixRequiredField.isEmpty()) && (!contentLabel.contains(prefixRequiredField))) {
					contentLabel = prefixRequiredField + " " + contentLabel;
				}
				if ((!suffixRequiredField.isEmpty()) && (!contentLabel.contains(suffixRequiredField))) {
					contentLabel += " " + suffixRequiredField;
				}
				requiredField.setAttribute("aria-label", contentLabel);
			}
		}
	}
	
	/**
	 * Fix the control to inform if it has autocomplete and the type.
	 * @param control The form control.
	 * @param active If the element has autocomplete.
	 */
	protected void fixControlAutoComplete(HTMLDOMElement control, Boolean active) {
		if (Boolean.TRUE.equals(active)) {
			control.setAttribute("aria-autocomplete", "both");
		} else if (!((active == null) && (control.hasAttribute("aria-autocomplete")))) {
			if (control.hasAttribute("list")) {
				HTMLDOMElement list = parser.find("datalist[id=" + control.getAttribute("list") + "]")
						.firstResult();
				if (list != null) {
					control.setAttribute("aria-autocomplete", "list");
				}
			}
			if ((Boolean.FALSE.equals(active)) && ((!control.hasAttribute("aria-autocomplete"))
					|| (!control.getAttribute("aria-autocomplete").toLowerCase().equals("list")))) {
				control.setAttribute("aria-autocomplete", "none");
			}
		}
	}
	
	public void fixRequiredField(HTMLDOMElement requiredField) {
		if (requiredField.hasAttribute("required")) {
			requiredField.setAttribute("aria-required", "true");
			
			Collection<HTMLDOMElement> labels = null;
			if (requiredField.hasAttribute("id")) {
				labels = parser.find("label[for=" + requiredField.getAttribute("id") + "]")
						.listResults();
			}
			if ((labels == null) || (labels.isEmpty())) {
				labels = parser.find(requiredField).findAncestors("label").listResults();
			}
			for (HTMLDOMElement label : labels) {
				fixLabelRequiredField(label, requiredField);
			}
		}
	}
	
	public void fixRequiredFields() {
		Collection<HTMLDOMElement> requiredFields = parser.find("[required]").listResults();
		for (HTMLDOMElement requiredField : requiredFields) {
			if (!requiredField.hasAttribute(dataIgnore)) {
				fixRequiredField(requiredField);
			}
		}
	}
	
	public void fixRangeField(HTMLDOMElement rangeField) {
		if (rangeField.hasAttribute("min")) {
			rangeField.setAttribute("aria-valuemin", rangeField.getAttribute("min"));
		}
		if (rangeField.hasAttribute("max")) {
			rangeField.setAttribute("aria-valuemax", rangeField.getAttribute("max"));
		}
	}
	
	public void fixRangeFields() {
		Collection<HTMLDOMElement> rangeFields = parser.find("[min],[max]").listResults();
		for (HTMLDOMElement rangeField : rangeFields) {
			if (!rangeField.hasAttribute(dataIgnore)) {
				fixRangeField(rangeField);
			}
		}
	}
	
	public void fixLabel(HTMLDOMElement label) {
		if (label.getTagName().equals("LABEL")) {
			HTMLDOMElement field;
			if (label.hasAttribute("for")) {
				field = parser.find("#" + label.getAttribute("for")).firstResult();
			} else {
				field = parser.find(label).findDescendants("input,select,textarea").firstResult();
				
				if (field != null) {
					CommonFunctions.generateId(field, prefixId);
					label.setAttribute("for", field.getAttribute("id"));
				}
			}
			if (field != null) {
				if (!field.hasAttribute("aria-label")) {
					field.setAttribute("aria-label"
							, label.getTextContent().replaceAll("[ \n\t\r]+", " ").trim());
				}
				
				fixLabelRequiredField(label, field);
				
				CommonFunctions.generateId(label, prefixId);
				field.setAttribute("aria-labelledby", CommonFunctions.increaseInList
						(field.getAttribute("aria-labelledby"), label.getAttribute("id")));
			}
		}
	}
	
	public void fixLabels() {
		Collection<HTMLDOMElement> labels = parser.find("label").listResults();
		for (HTMLDOMElement label : labels) {
			if (!label.hasAttribute(dataIgnore)) {
				fixLabel(label);
			}
		}
	}

	public void fixAutoComplete(HTMLDOMElement element) {
		if (element.hasAttribute("autocomplete")) {
			Boolean active;
			String value = element.getAttribute("autocomplete");
			if (value.equals("on")) {
				active = Boolean.TRUE;
			} else if (value.equals("off")) {
				active = Boolean.FALSE;
			} else {
				active = null;
			}
			if (active != null) {
				if (element.getTagName().equals("FORM")) {
					Collection<HTMLDOMElement> controls;
					controls = parser.find(element).findDescendants("input,textarea").listResults();
					if (element.hasAttribute("id")) {
						String id = element.getAttribute("id");
						controls.addAll(parser
								.find("input[form=" + id + "],textarea[form=" + id + "]").listResults());
					}
					boolean fix;
					String type;
					for (HTMLDOMElement control : controls) {
						fix = true;
						if ((control.getTagName().equals("INPUT")) && (control.hasAttribute("type"))) {
							type = control.getAttribute("type").toLowerCase();
							if ((type.equals("button")) || (type.equals("submit"))
									|| (type.equals("reset")) || (type.equals("image"))
									|| (type.equals("file")) || (type.equals("checkbox"))
									|| (type.equals("radio")) || (type.equals("password"))
									|| (type.equals("hidden"))) {
								fix = false;
							}
						}
						if (fix) {
							String autoCompleteControlFormValue = control.getAttribute("autocomplete");
							if ("on".equals(autoCompleteControlFormValue)) {
								fixControlAutoComplete(control, true);
							} else if ("off".equals(autoCompleteControlFormValue)) {
								fixControlAutoComplete(control, false);
							} else {
								fixControlAutoComplete(control, active);
							}
						}
					}
				} else {
					fixControlAutoComplete(element, active);
				}
			}
		}
		if ((!element.hasAttribute("aria-autocomplete")) && (element.hasAttribute("list"))) {
			fixControlAutoComplete(element, null);
		}
	}

	public void fixAutoCompletes() {
		Collection<HTMLDOMElement> elements = parser.find("[autocomplete],[list]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixAutoComplete(element);
			}
		}
	}
}