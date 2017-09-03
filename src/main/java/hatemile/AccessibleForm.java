/*
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
 * The AccessibleForm interface fixes accessibility problems associated
 * with forms.
 */
public interface AccessibleForm {
	
	/**
	 * Display that the field is required.
	 * @param requiredField The required field.
	 */
	public void fixRequiredField(HTMLDOMElement requiredField);
	
	/**
	 * Display that the fields is required.
	 */
	public void fixRequiredFields();
	
	/**
	 * Display that the field have range.
	 * @param rangeField The range field.
	 */
	public void fixRangeField(HTMLDOMElement rangeField);
	
	/**
	 * Display that the fields have range.
	 */
	public void fixRangeFields();
	
	/**
	 * Display that the field have autocomplete.
	 * @param autoCompleteField The field with autocomplete.
	 */
	public void fixAutoCompleteField(HTMLDOMElement autoCompleteField);
	
	/**
	 * Display that the fields have autocomplete.
	 */
	public void fixAutoCompleteFields();
}