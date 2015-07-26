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
package hatemile.util;

/**
 * The SelectorChange class store the selector that be attribute change.
 */
public class SelectorChange {
	
	/**
	 * The selector.
	 */
	protected final String selector;
	
	/**
	 * The attribute that will change.
	 */
	protected final String attribute;
	
	/**
	 * The value of the attribute.
	 */
	protected final String valueForAttribute;
	
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
	 * Returns the attribute.
	 * @return The attribute.
	 */
	public String getAttribute() {
		return attribute;
	}
	
	/**
	 * Returns the value of the attribute.
	 * @return The value of the attribute.
	 */
	public String getValueForAttribute() {
		return valueForAttribute;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this != object) {
			if (object == null) {
				return false;
			}
			if (!(object instanceof SelectorChange)) {
				return false;
			}
			SelectorChange selectorChange = (SelectorChange) object;
			if ((!selector.equals(selectorChange.getSelector()))
					|| (!attribute.equals(selectorChange.getAttribute()))
					|| (!valueForAttribute.equals(selectorChange.getValueForAttribute()))) {
				return false;
			}
		}
		return true;
	}
}