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
 * The AccessibleEvent interface fix problems of accessibility associated
 * with Javascript events in elements.
 */
public interface AccessibleEvent {
	
	/**
	 * Provide a solution for the element that has drop events.
	 * @param element The element that will be fixed.
	 */
	public void fixDrop(HTMLDOMElement element);
	
	/**
	 * Provide a solution for the element that has drag events.
	 * @param element The element that will be fixed.
	 */
	public void fixDrag(HTMLDOMElement element);
	
	/**
	 * Provide a solution for elements that has Drag-and-Drop events.
	 */
	public void fixDragsandDrops();
	
	/**
	 * Provide a solution for the element that has inaccessible hover events.
	 * @param element The element that will be fixed.
	 */
	public void fixHover(HTMLDOMElement element);
	
	/**
	 * Provide a solution for elements that has inaccessible hover events.
	 */
	public void fixHovers();
	
	/**
	 * Provide a solution for the element that has inaccessible active events.
	 * @param element The element that will be fixed.
	 */
	public void fixActive(HTMLDOMElement element);
	
	/**
	 * Provide a solution for elements that has inaccessible active events.
	 */
	public void fixActives();
}