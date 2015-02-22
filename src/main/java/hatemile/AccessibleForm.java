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
 * The AccessibleForm interface fix problems of accessibility associated
 * with forms.
 */
public interface AccessibleForm {
	
	/**
	 * If field is required, display this information.
	 * @param requiredField The element that will be fixed.
	 */
	public void fixRequiredField(HTMLDOMElement requiredField);
	
	/**
	 * In fields is required, display this information.
	 */
	public void fixRequiredFields();
	
	/**
	 * If field has range, display the range.
	 * @param rangeField The element that will be fixed.
	 */
	public void fixRangeField(HTMLDOMElement rangeField);
	
	/**
	 * In range fields, display the range.
	 */
	public void fixRangeFields();
	
	/**
	 * If field has autocomplete, display this information.
	 * @param autoCompleteField The element that will be fixed.
	 */
	public void fixAutoCompleteField(HTMLDOMElement autoCompleteField);
	
	/**
	 * In fields has autocomplete, display this information.
	 */
	public void fixAutoCompleteFields();
	
	/**
	 * Associate label with field.
	 * @param label The element that will be fixed.
	 */
	public void fixLabel(HTMLDOMElement label);
	
	/**
	 * Associate labels with fields.
	 */
	public void fixLabels();
}