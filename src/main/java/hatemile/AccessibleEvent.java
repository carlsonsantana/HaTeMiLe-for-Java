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
 * The AccessibleEvent interface fix the problems of accessibility associated
 * with Javascript events in the elements.
 * @version 2014-07-23
 */
public interface AccessibleEvent {
	
	/**
	 * Fix some problem of accessibility in the events that are called when an
	 * element is hovered.
	 * @param element The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G90.html" target="_blank">G90: Providing keyboard-triggered event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G202.html" target="_blank">G202: Ensuring keyboard control for all functionality</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR2.html" target="_blank">SCR2: Using redundant keyboard and mouse event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR20.html" target="_blank">SCR20: Using both keyboard and other device-specific functions</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR29.html" target="_blank">SCR29: Adding keyboard-accessible actions to static HTML elements</a>
	 */
	public void fixOnHover(HTMLDOMElement element);
	
	/**
	 * Fix some problem of accessibility in the events that are called when any
	 * element of page is hovered.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G90.html" target="_blank">G90: Providing keyboard-triggered event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G202.html" target="_blank">G202: Ensuring keyboard control for all functionality</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR2.html" target="_blank">SCR2: Using redundant keyboard and mouse event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR20.html" target="_blank">SCR20: Using both keyboard and other device-specific functions</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR29.html" target="_blank">SCR29: Adding keyboard-accessible actions to static HTML elements</a>
	 */
	public void fixOnHovers();
	
	/**
	 * Fix some problem of accessibility in the events that are called when an
	 * element is actived.
	 * @param element The element that will be fixed.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G90.html" target="_blank">G90: Providing keyboard-triggered event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G202.html" target="_blank">G202: Ensuring keyboard control for all functionality</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR2.html" target="_blank">SCR2: Using redundant keyboard and mouse event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR20.html" target="_blank">SCR20: Using both keyboard and other device-specific functions</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR29.html" target="_blank">SCR29: Adding keyboard-accessible actions to static HTML elements</a>
	 */
	public void fixOnActive(HTMLDOMElement element);
	
	/**
	 * Fix some problem of accessibility in the events that are called when any
	 * element of page is actived.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G90.html" target="_blank">G90: Providing keyboard-triggered event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/G202.html" target="_blank">G202: Ensuring keyboard control for all functionality</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR2.html" target="_blank">SCR2: Using redundant keyboard and mouse event handlers</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR20.html" target="_blank">SCR20: Using both keyboard and other device-specific functions</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/SCR29.html" target="_blank">SCR29: Adding keyboard-accessible actions to static HTML elements</a>
	 */
	public void fixOnActives();
}