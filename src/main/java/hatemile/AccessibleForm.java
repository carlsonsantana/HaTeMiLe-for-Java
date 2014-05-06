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

public interface AccessibleForm {

	public void fixRequiredField(HTMLDOMElement element);

	public void fixRequiredFields();

	public void fixDisabledField(HTMLDOMElement element);

	public void fixDisabledFields();

	public void fixReadOnlyField(HTMLDOMElement element);

	public void fixReadOnlyFields();

	public void fixRangeField(HTMLDOMElement element);

	public void fixRangeFields();

	public void fixTextField(HTMLDOMElement element);

	public void fixTextFields();

	public void fixSelectField(HTMLDOMElement element);

	public void fixSelectFields();

	public void fixLabel(HTMLDOMElement element);

	public void fixLabels();
}
