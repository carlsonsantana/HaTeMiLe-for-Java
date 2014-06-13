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
package hatemile;

import hatemile.util.HTMLDOMElement;

/**
 * The AccessibleForm interface fix the problems of accessibility
 * associated with the forms.
 * @version 1.0
 */
public interface AccessibleForm {
	
	/**
	 * Fix required field.
	 * @param requiredField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/2014/NOTE-WCAG20-TECHS-20140311/ARIA2">ARIA2: Identifying a required field with the aria-required property</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-required">aria-required (property) | Supported States and Properties</a>
	 */
	public void fixRequiredField(HTMLDOMElement requiredField);
	
	/**
	 * Fix required fields.
	 * @see <a href="http://www.w3.org/TR/2014/NOTE-WCAG20-TECHS-20140311/ARIA2">ARIA2: Identifying a required field with the aria-required property</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-required">aria-required (property) | Supported States and Properties</a>
	 */
	public void fixRequiredFields();
	
	/**
	 * Fix disabled field.
	 * @param disabledField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-disabled">aria-disabled (property) | Supported States and Properties</a>
	 */
	public void fixDisabledField(HTMLDOMElement disabledField);
	
	/**
	 * Fix disabled fields.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-disabled">aria-disabled (property) | Supported States and Properties</a>
	 */
	public void fixDisabledFields();
	
	/**
	 * Fix read-only field.
	 * @param readOnlyField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-readonly">aria-readonly (property) | Supported States and Properties</a>
	 */
	public void fixReadOnlyField(HTMLDOMElement readOnlyField);
	
	/**
	 * Fix read-only fields.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-readonly">aria-readonly (property) | Supported States and Properties</a>
	 */
	public void fixReadOnlyFields();
	
	/**
	 * Fix range field.
	 * @param rangeField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemin">aria-valuemin (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemax">aria-valuemax (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/WAI/GL/wiki/Using_WAI-ARIA_range_attributes_for_range_widgets_such_as_progressbar,_scrollbar,_slider,_and_spinbutton">Using WAI-ARIA range attributes for range widgets such as progressbar, scrollbar, slider and spinbutton</a>
	 * @see <a href="http://www.w3.org/WAI/GL/2013/WD-WCAG20-TECHS-20130711/ARIA3.html">ARIA3: Identifying valid range information with the aria-valuemin and aria-valuemax properties</a>
	 */
	public void fixRangeField(HTMLDOMElement rangeField);
	
	/**
	 * Fix range fields.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemin">aria-valuemin (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemax">aria-valuemax (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/WAI/GL/wiki/Using_WAI-ARIA_range_attributes_for_range_widgets_such_as_progressbar,_scrollbar,_slider,_and_spinbutton">Using WAI-ARIA range attributes for range widgets such as progressbar, scrollbar, slider and spinbutton</a>
	 * @see <a href="http://www.w3.org/WAI/GL/2013/WD-WCAG20-TECHS-20130711/ARIA3.html">ARIA3: Identifying valid range information with the aria-valuemin and aria-valuemax properties</a>
	 */
	public void fixRangeFields();
	
	/**
	 * Fix text field.
	 * @param textField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-multiline">aria-multiline (property) | Supported States and Properties</a>
	 */
	public void fixTextField(HTMLDOMElement textField);
	
	/**
	 * Fix text fields.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-multiline">aria-multiline (property) | Supported States and Properties</a>
	 */
	public void fixTextFields();
	
	/**
	 * Fix select field.
	 * @param selectField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-multiselectable">aria-multiselectable (property) | Supported States and Properties</a>
	 */
	public void fixSelectField(HTMLDOMElement selectField);
	
	/**
	 * Fix select fields.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-multiselectable">aria-multiselectable (property) | Supported States and Properties</a>
	 */
	public void fixSelectFields();
	
	/**
	 * Fix field with label.
	 * @param label The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-label">aria-label (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-labelledby">aria-labelledby (property) | Supported States and Properties</a>
	 */
	public void fixLabel(HTMLDOMElement label);
	
	/**
	 * Fix fields with labels.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-label">aria-label (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-labelledby">aria-labelledby (property) | Supported States and Properties</a>
	 */
	public void fixLabels();
}
