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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * The Skipper class store the selector that will be add a skipper.
 */
public class Skipper {
	
	/**
	 * The selector.
	 */
	protected final String selector;
	
	/**
	 * The default text of skipper.
	 */
	protected final String defaultText;
	
	/**
	 * The shortcuts of skipper.
	 */
	protected final Collection<String> shortcuts;
	
	/**
	 * Inicializes a new object with the values pre-defineds.
	 * @param selector The selector.
	 * @param defaultText The default text of skipper.
	 * @param shortcuts The shortcuts of skipper.
	 */
	public Skipper(String selector, String defaultText, String shortcuts) {
		this.selector = selector;
		this.defaultText = defaultText;
		if (shortcuts.isEmpty()) {
			this.shortcuts = new ArrayList<String>();
		} else {
			this.shortcuts = Arrays.asList(shortcuts.split("[ \n\t\r]+"));
		}
	}
	
	/**
	 * Returns the selector.
	 * @return The selector.
	 */
	public String getSelector() {
		return selector;
	}
	
	/**
	 * Returns the default text of skipper.
	 * @return The default text of skipper.
	 */
	public String getDefaultText() {
		return defaultText;
	}
	
	/**
	 * Returns the shortcuts of skipper.
	 * @return The shortcuts of skipper.
	 */
	public Collection<String> getShortcuts() {
		return new ArrayList<String>(shortcuts);
	}
}