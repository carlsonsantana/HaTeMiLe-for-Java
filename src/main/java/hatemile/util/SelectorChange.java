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
package hatemile.util;

public class SelectorChange {

	protected String selector;
	protected String attribute;
	protected String valueForAttribute;

	public SelectorChange() {

	}

	public SelectorChange(String selector, String attribute, String valueForAttribute) {
		this.selector = selector;
		this.attribute = attribute;
		this.valueForAttribute = valueForAttribute;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValueForAttribute() {
		return valueForAttribute;
	}

	public void setValueForAttribute(String valueForAttribute) {
		this.valueForAttribute = valueForAttribute;
	}
}
