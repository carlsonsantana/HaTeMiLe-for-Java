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
 * The AccessibleForm interface fix the problems of accessibility associated
 * with the forms.
 * @version 2014-07-23
 */
public interface AccessibleForm {
	
	/**
	 * Fix required field.
	 * @param requiredField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/H90.html" target="_blank">H90: Indicating required form controls using label or legend</a>
	 * @see <a href="http://www.w3.org/TR/2014/NOTE-WCAG20-TECHS-20140311/ARIA2" target="_blank">ARIA2: Identifying a required field with the aria-required property</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/F81.html" target="_blank">F81: Failure of Success Criterion 1.4.1 due to identifying required or error fields using color differences only</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-required" target="_blank">aria-required (property) | Supported States and Properties</a>
	 */
	public void fixRequiredField(HTMLDOMElement requiredField);
	
	/**
	 * Fix required fields.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/H90.html" target="_blank">H90: Indicating required form controls using label or legend</a>
	 * @see <a href="http://www.w3.org/TR/2014/NOTE-WCAG20-TECHS-20140311/ARIA2" target="_blank">ARIA2: Identifying a required field with the aria-required property</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/F81.html" target="_blank">F81: Failure of Success Criterion 1.4.1 due to identifying required or error fields using color differences only</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-required" target="_blank">aria-required (property) | Supported States and Properties</a>
	 */
	public void fixRequiredFields();
	
	/**
	 * Fix range field.
	 * @param rangeField The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemin" target="_blank">aria-valuemin (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemax" target="_blank">aria-valuemax (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/WAI/GL/wiki/Using_WAI-ARIA_range_attributes_for_range_widgets_such_as_progressbar,_scrollbar,_slider,_and_spinbutton" target="_blank">Using WAI-ARIA range attributes for range widgets such as progressbar, scrollbar, slider and spinbutton</a>
	 * @see <a href="http://www.w3.org/WAI/GL/2013/WD-WCAG20-TECHS-20130711/ARIA3.html" target="_blank">ARIA3: Identifying valid range information with the aria-valuemin and aria-valuemax properties</a>
	 */
	public void fixRangeField(HTMLDOMElement rangeField);
	
	/**
	 * Fix range fields.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemin" target="_blank">aria-valuemin (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-valuemax" target="_blank">aria-valuemax (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/WAI/GL/wiki/Using_WAI-ARIA_range_attributes_for_range_widgets_such_as_progressbar,_scrollbar,_slider,_and_spinbutton" target="_blank">Using WAI-ARIA range attributes for range widgets such as progressbar, scrollbar, slider and spinbutton</a>
	 * @see <a href="http://www.w3.org/WAI/GL/2013/WD-WCAG20-TECHS-20130711/ARIA3.html" target="_blank">ARIA3: Identifying valid range information with the aria-valuemin and aria-valuemax properties</a>
	 */
	public void fixRangeFields();
	
	/**
	 * Fix field associated with the label.
	 * @param label The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-label" target="_blank">aria-label (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-labelledby" target="_blank">aria-labelledby (property) | Supported States and Properties</a>
	 */
	public void fixLabel(HTMLDOMElement label);
	
	/**
	 * Fix fields associated with the labels.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-label" target="_blank">aria-label (property) | Supported States and Properties</a>
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-labelledby" target="_blank">aria-labelledby (property) | Supported States and Properties</a>
	 */
	public void fixLabels();
	
	/**
	 * Fix element to inform if has autocomplete and the type.
	 * @param element The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-autocomplete" target="_blank">aria-autocomplete (property) | Supported States and Properties</a>
	 */
	public void fixAutoComplete(HTMLDOMElement element);
	
	/**
	 * Fix elements to inform if has autocomplete and the type.
	 * @see <a href="http://www.w3.org/TR/wai-aria/states_and_properties#aria-autocomplete" target="_blank">aria-autocomplete (property) | Supported States and Properties</a>
	 */
	public void fixAutoCompletes();
}