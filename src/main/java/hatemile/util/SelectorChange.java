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

/**
 * The SelectorChange class store the selector that
 * be attribute change.
 * @version 1.0
 */
public class SelectorChange {
	
	/**
	 * The selector.
	 */
	protected String selector;
	
	/**
	 * The attribute that will change.
	 */
	protected String attribute;
	
	/**
	 * The value of the attribute.
	 */
	protected String valueForAttribute;
	
	/**
	 * Inicializes a new object.
	 */
	public SelectorChange() {

	}
	
	/**
	 * Inicializes a new object with the values pre-defineds.
	 * @param selector The selector.
	 * @param attribute The attribute.
	 * @param valueForAttribute The value of the attribute.
	 */
	public SelectorChange(String selector, String attribute, String valueForAttribute) {
		this.selector = selector;
		this.attribute = attribute;
		this.valueForAttribute = valueForAttribute;
	}

	/**
	 * Returns the selector.
	 * @return The selector.
	 */
	public String getSelector() {
		return selector;
	}

	/**
	 * Change the selector.
	 * @param selector The new selector.
	 */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * Returns the attribute.
	 * @return The attribute.
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Change the attribute.
	 * @param attribute The new attribute.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns the value of the attribute.
	 * @return The value of the attribute.
	 */
	public String getValueForAttribute() {
		return valueForAttribute;
	}

	/**
	 * Change the value of the attribute.
	 * @param valueForAttribute The new value of the attribute.
	 */
	public void setValueForAttribute(String valueForAttribute) {
		this.valueForAttribute = valueForAttribute;
	}
}
