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
 * The AccessibleFormImplementation class is official implementation of
 * AccessibleForm interface.
 */
public class AccessibleFormImplementation implements AccessibleForm {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The prefix of generated ids.
	 */
	protected final String prefixId;
	
	/**
	 * The description prefix of required fields.
	 */
	protected final String prefixRequiredField;
	
	/**
	 * The description suffix of required fields.
	 */
	protected final String suffixRequiredField;
	
	/**
	 * The description prefix of range fields for minimum value.
	 */
	protected final String prefixRangeMinField;
	
	/**
	 * The description suffix of range fields for minimum value.
	 */
	protected final String suffixRangeMinField;
	
	/**
	 * The description prefix of range fields for maximum value.
	 */
	protected final String prefixRangeMaxField;
	
	/**
	 * The description suffix of range fields for maximum value.
	 */
	protected final String suffixRangeMaxField;
	
	/**
	 * The description prefix of autocomplete fields.
	 */
	protected final String prefixAutoCompleteField;
	
	/**
	 * The description suffix of autocomplete fields.
	 */
	protected final String suffixAutoCompleteField;
	
	/**
	 * The value for description of field, when it has inline and list
	 * autocomplete.
	 */
	protected final String textAutoCompleteValueBoth;
	
	/**
	 * The value for description of field, when it has list autocomplete.
	 */
	protected final String textAutoCompleteValueList;
	
	/**
	 * The value for description of field, when it has inline autocomplete.
	 */
	protected final String textAutoCompleteValueInline;
	
	/**
	 * The value for description of field, when it not has autocomplete.
	 */
	protected final String textAutoCompleteValueNone;
	
	/**
	 * The name of attribute for not modify the elements.
	 */
	protected final String dataIgnore;
	
	/**
	 * The name of attribute that store the description prefix of required
	 * fields.
	 */
	protected final String dataLabelPrefixRequiredField;
	
	/**
	 * The name of attribute that store the description suffix of required
	 * fields.
	 */
	protected final String dataLabelSuffixRequiredField;
	
	/**
	 * The name of attribute that store the description prefix of range fields
	 * for minimum value.
	 */
	protected final String dataLabelPrefixRangeMinField;
	
	/**
	 * The name of attribute that store the description suffix of range fields
	 * for minimum value.
	 */
	protected final String dataLabelSuffixRangeMinField;
	
	/**
	 * The name of attribute that store the description prefix of range fields
	 * for maximum value.
	 */
	protected final String dataLabelPrefixRangeMaxField;
	
	/**
	 * The name of attribute that store the description suffix of range fields
	 * for maximum value.
	 */
	protected final String dataLabelSuffixRangeMaxField;
	
	/**
	 * The name of attribute that store the description prefix of autocomplete
	 * fields.
	 */
	protected final String dataLabelPrefixAutoCompleteField;
	
	/**
	 * The name of attribute that store the description suffix of autocomplete
	 * fields.
	 */
	protected final String dataLabelSuffixAutoCompleteField;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the forms
	 * of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleFormImplementation(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		dataLabelPrefixRequiredField = "data-prefixrequiredfield";
		dataLabelSuffixRequiredField = "data-suffixrequiredfield";
		dataLabelPrefixRangeMinField = "data-prefixvalueminfield";
		dataLabelSuffixRangeMinField = "data-suffixvalueminfield";
		dataLabelPrefixRangeMaxField = "data-prefixvaluemaxfield";
		dataLabelSuffixRangeMaxField = "data-suffixvaluemaxfield";
		dataLabelPrefixAutoCompleteField = "data-prefixautocompletefield";
		dataLabelSuffixAutoCompleteField = "data-suffixautocompletefield";
		dataIgnore = "data-ignoreaccessibilityfix";
		prefixId = configure.getParameter("prefix-generated-ids");
		prefixRequiredField = configure.getParameter("prefix-required-field");
		suffixRequiredField = configure.getParameter("suffix-required-field");
		prefixRangeMinField = configure.getParameter("prefix-range-min-field");
		suffixRangeMinField = configure.getParameter("suffix-range-min-field");
		prefixRangeMaxField = configure.getParameter("prefix-range-max-field");
		suffixRangeMaxField = configure.getParameter("suffix-range-max-field");
		prefixAutoCompleteField = configure.getParameter("prefix-autocomplete-field");
		suffixAutoCompleteField = configure.getParameter("suffix-autocomplete-field");
		textAutoCompleteValueBoth = configure.getParameter("text-autocomplete-value-both");
		textAutoCompleteValueList = configure.getParameter("text-autocomplete-value-list");
		textAutoCompleteValueInline = configure.getParameter("text-autocomplete-value-inline");
		textAutoCompleteValueNone = configure.getParameter("text-autocomplete-value-none");
	}
	
	/**
	 * Display in label the information of field.
	 * @param label The label.
	 * @param field The field.
	 * @param prefix The prefix.
	 * @param suffix The suffix.
	 * @param dataPrefix The name of prefix attribute.
	 * @param dataSuffix The name of suffix attribute.
	 */
	protected void addPrefixSuffix(HTMLDOMElement label, HTMLDOMElement field
			, String prefix, String suffix, String dataPrefix, String dataSuffix) {
		String content = field.getAttribute("aria-label");
		if (!prefix.isEmpty()) {
			label.setAttribute(dataPrefix, prefix);
			if (!content.contains(prefix)) {
				content = prefix + " " + content;
			}
		}
		if (!suffix.isEmpty()) {
			label.setAttribute(dataSuffix, suffix);
			if (!content.contains(suffix)) {
				content += " " + suffix;
			}
		}
		field.setAttribute("aria-label", content);
	}
	
	/**
	 * Display in label the information if the field is required.
	 * @param label The label.
	 * @param requiredField The required field.
	 */
	protected void fixLabelRequiredField(HTMLDOMElement label, HTMLDOMElement requiredField) {
		if (((requiredField.hasAttribute("required"))
				|| ((requiredField.hasAttribute("aria-required"))
				&& (requiredField.getAttribute("aria-required").equalsIgnoreCase("true"))))
				&& (requiredField.hasAttribute("aria-label"))
				&& (!label.hasAttribute(dataLabelPrefixRequiredField))
				&& (!label.hasAttribute(dataLabelSuffixRequiredField))) {
			addPrefixSuffix(label, requiredField, prefixRequiredField, suffixRequiredField
					, dataLabelPrefixRequiredField, dataLabelSuffixRequiredField);
		}
	}
	
	/**
	 * Display in label the information of range of field.
	 * @param label The label.
	 * @param rangeField The range field.
	 */
	protected void fixLabelRangeField(HTMLDOMElement label, HTMLDOMElement rangeField) {
		if (rangeField.hasAttribute("aria-label")) {
			if ((rangeField.hasAttribute("min") || rangeField.hasAttribute("aria-valuemin"))
					&& (!label.hasAttribute(dataLabelPrefixRangeMinField))
					&& (!label.hasAttribute(dataLabelSuffixRangeMinField))) {
				String value;
				if (rangeField.hasAttribute("min")) {
					value = rangeField.getAttribute("min");
				} else {
					value = rangeField.getAttribute("aria-valuemin");
				}
				addPrefixSuffix(label, rangeField, prefixRangeMinField.replace("{{value}}", value)
						, suffixRangeMinField.replace("{{value}}", value)
						, dataLabelPrefixRangeMinField, dataLabelSuffixRangeMinField);
			}
			if ((rangeField.hasAttribute("max") || rangeField.hasAttribute("aria-valuemax"))
					&& (!label.hasAttribute(dataLabelPrefixRangeMaxField))
					&& (!label.hasAttribute(dataLabelSuffixRangeMaxField))) {
				String value;
				if (rangeField.hasAttribute("max")) {
					value = rangeField.getAttribute("max");
				} else {
					value = rangeField.getAttribute("aria-valuemax");
				}
				addPrefixSuffix(label, rangeField, prefixRangeMaxField.replace("{{value}}", value)
						, suffixRangeMaxField.replace("{{value}}", value)
						, dataLabelPrefixRangeMaxField, dataLabelSuffixRangeMaxField);
			}
		}
	}
	
	/**
	 * Display in label the information if the field has autocomplete.
	 * @param label The label.
	 * @param autoCompleteField The autocomplete field.
	 */
	protected void fixLabelAutoCompleteField(HTMLDOMElement label, HTMLDOMElement autoCompleteField) {
		String prefixAutoCompleteFieldModified = "";
		String suffixAutoCompleteFieldModified = "";
		if ((autoCompleteField.hasAttribute("aria-label"))
				&& (!label.hasAttribute(dataLabelPrefixAutoCompleteField))
				&& (!label.hasAttribute(dataLabelSuffixAutoCompleteField))) {
			String autocomplete = "";
			if (autoCompleteField.hasAttribute("autocomplete")) {
				autocomplete = autoCompleteField.getAttribute("autocomplete").toLowerCase();
			} else {
				HTMLDOMElement form = parser.find(autoCompleteField).findAncestors("form").firstResult();
				if ((form != null) && (form.hasAttribute("autocomplete"))) {
					autocomplete = form.getAttribute("autocomplete").toLowerCase();
				}
			}
			if (!autocomplete.isEmpty()) {
				if (autocomplete.equals("on")) {
					if (!prefixAutoCompleteField.isEmpty()) {
						prefixAutoCompleteFieldModified = prefixAutoCompleteField.replace("{{value}}", textAutoCompleteValueBoth);
					}
					if (!suffixAutoCompleteField.isEmpty()) {
						suffixAutoCompleteFieldModified = suffixAutoCompleteField.replace("{{value}}", textAutoCompleteValueBoth);
					}
				} else if (autocomplete.equals("off")) {
					if (!prefixAutoCompleteField.isEmpty()) {
						prefixAutoCompleteFieldModified = prefixAutoCompleteField.replace("{{value}}", textAutoCompleteValueNone);
					}
					if (!suffixAutoCompleteField.isEmpty()) {
						suffixAutoCompleteFieldModified = suffixAutoCompleteField.replace("{{value}}", textAutoCompleteValueNone);
					}
				} else if (autoCompleteField.hasAttribute("list")
						&& (parser.find("datalist[id=" + autoCompleteField.getAttribute("list") + "]").firstResult() != null)) {
					if (!prefixAutoCompleteField.isEmpty()) {
						prefixAutoCompleteFieldModified = prefixAutoCompleteField.replace("{{value}}", textAutoCompleteValueList);
					}
					if (!suffixAutoCompleteField.isEmpty()) {
						suffixAutoCompleteFieldModified = suffixAutoCompleteField.replace("{{value}}", textAutoCompleteValueList);
					}
				}
				addPrefixSuffix(label, autoCompleteField, prefixAutoCompleteFieldModified
						, suffixAutoCompleteFieldModified, dataLabelPrefixAutoCompleteField
						, dataLabelSuffixAutoCompleteField);
			}
		}
	}
	
	/**
	 * Returns the labels of field.
	 * @param field The field.
	 * @return The labels of field.
	 */
	protected Collection<HTMLDOMElement> getLabels(HTMLDOMElement field) {
		Collection<HTMLDOMElement> labels = null;
		if (field.hasAttribute("id")) {
			labels = parser.find("label[for=" + field.getAttribute("id") + "]").listResults();
		}
		if ((labels == null) || (labels.isEmpty())) {
			labels = parser.find(field).findAncestors("label").listResults();
		}
		return labels;
	}
	
	/**
	 * Fix the control to inform if it has autocomplete and the type.
	 * @param field The field.
	 * @param active If the element has autocomplete.
	 */
	protected void fixControlAutoComplete(HTMLDOMElement field, Boolean active) {
		if (Boolean.TRUE.equals(active)) {
			field.setAttribute("aria-autocomplete", "both");
		} else if (!((active == null) && (field.hasAttribute("aria-autocomplete")))) {
			if (field.hasAttribute("list")) {
				HTMLDOMElement list = parser.find("datalist[id=" + field.getAttribute("list") + "]")
						.firstResult();
				if (list != null) {
					field.setAttribute("aria-autocomplete", "list");
				}
			}
			if ((Boolean.FALSE.equals(active)) && ((!field.hasAttribute("aria-autocomplete"))
					|| (!field.getAttribute("aria-autocomplete").toLowerCase().equals("list")))) {
				field.setAttribute("aria-autocomplete", "none");
			}
		}
		Collection<HTMLDOMElement> labels = getLabels(field);
		for (HTMLDOMElement label : labels) {
			fixLabelAutoCompleteField(label, field);
		}
	}
	
	public void fixRequiredField(HTMLDOMElement requiredField) {
		if (requiredField.hasAttribute("required")) {
			requiredField.setAttribute("aria-required", "true");
			
			Collection<HTMLDOMElement> labels = getLabels(requiredField);
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
		Collection<HTMLDOMElement> labels = getLabels(rangeField);
		for (HTMLDOMElement label : labels) {
			fixLabelRangeField(label, rangeField);
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

	public void fixAutoCompleteField(HTMLDOMElement element) {
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
					Collection<HTMLDOMElement> fields;
					fields = parser.find(element).findDescendants("input,textarea").listResults();
					if (element.hasAttribute("id")) {
						String id = element.getAttribute("id");
						fields.addAll(parser
								.find("input[form=" + id + "],textarea[form=" + id + "]").listResults());
					}
					boolean fix;
					String type;
					for (HTMLDOMElement field : fields) {
						fix = true;
						if ((field.getTagName().equals("INPUT")) && (field.hasAttribute("type"))) {
							type = field.getAttribute("type").toLowerCase();
							if ((type.equals("button")) || (type.equals("submit"))
									|| (type.equals("reset")) || (type.equals("image"))
									|| (type.equals("file")) || (type.equals("checkbox"))
									|| (type.equals("radio")) || (type.equals("password"))
									|| (type.equals("hidden"))) {
								fix = false;
							}
						}
						if (fix) {
							String autoCompleteControlFormValue = field.getAttribute("autocomplete");
							if ("on".equals(autoCompleteControlFormValue)) {
								fixControlAutoComplete(field, true);
							} else if ("off".equals(autoCompleteControlFormValue)) {
								fixControlAutoComplete(field, false);
							} else {
								fixControlAutoComplete(field, active);
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

	public void fixAutoCompleteFields() {
		Collection<HTMLDOMElement> elements = parser.find("[autocomplete],[list]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixAutoCompleteField(element);
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
				fixLabelRangeField(label, field);
				fixLabelAutoCompleteField(label, field);
				
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
}