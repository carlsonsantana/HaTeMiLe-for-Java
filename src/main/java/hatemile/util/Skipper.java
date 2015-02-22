/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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