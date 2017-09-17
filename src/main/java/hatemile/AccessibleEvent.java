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

import hatemile.util.html.HTMLDOMElement;

/**
 * The AccessibleEvent interface improve the accessibility, making elements
 * events available from a keyboard.
 */
public interface AccessibleEvent {

	/**
	 * Make the drop events of element available from a keyboard.
	 * @param element The element with drop event.
	 */
	void makeAccessibleDropEvents(HTMLDOMElement element);

	/**
	 * Make the drag events of element available from a keyboard.
	 * @param element The element with drag event.
	 */
	void makeAccessibleDragEvents(HTMLDOMElement element);

	/**
	 * Make all Drag-and-Drop events of page available from a keyboard.
	 */
	void makeAccessibleAllDragandDropEvents();

	/**
	 * Make the hover events of element available from a keyboard.
	 * @param element The element with hover event.
	 */
	void makeAccessibleHoverEvents(HTMLDOMElement element);

	/**
	 * Make all hover events of page available from a keyboard.
	 */
	void makeAccessibleAllHoverEvents();

	/**
	 * Make the click events of element available from a keyboard.
	 * @param element The element with click events.
	 */
	void makeAccessibleClickEvents(HTMLDOMElement element);

	/**
	 * Make all click events of page available from a keyboard.
	 */
	void makeAccessibleAllClickEvents();
}
