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
import hatemile.util.Skipper;

/**
 * The AccessibleNavigation interface fixes accessibility problems associated
 * with navigation.
 */
public interface AccessibleNavigation {
	
	/**
	 * Display the shortcuts of element.
	 * @param element The element with shortcuts.
	 */
	public void fixShortcut(HTMLDOMElement element);
	
	/**
	 * Display the shortcuts of elements.
	 */
	public void fixShortcuts();
	
	/**
	 * Provide content skipper for element.
	 * @param element The element.
	 * @param skipper The skipper.
	 */
	public void fixSkipper(HTMLDOMElement element, Skipper skipper);
	
	/**
	 * Provide content skippers.
	 */
	public void fixSkippers();
	
	/**
	 * Provide a navigation by heading.
	 * @param element The heading element.
	 */
	public void fixHeading(HTMLDOMElement element);
	
	/**
	 * Provide a navigation by headings.
	 */
	public void fixHeadings();
}