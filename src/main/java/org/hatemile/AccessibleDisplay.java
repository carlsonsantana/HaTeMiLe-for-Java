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
package org.hatemile;

import org.hatemile.util.html.HTMLDOMElement;

/**
 * The AccessibleDisplay interface improve accessibility, showing informations.
 */
public interface AccessibleDisplay {

    /**
     * Display the shortcuts of element.
     * @param element The element with shortcuts.
     */
    void displayShortcut(HTMLDOMElement element);

    /**
     * Display all shortcuts of page.
     */
    void displayAllShortcuts();

    /**
     * Display the WAI-ARIA role of element.
     * @param element The element.
     */
    void displayRole(HTMLDOMElement element);

    /**
     * Display the WAI-ARIA roles of all elements of page.
     */
    void displayAllRoles();

    /**
     * Display the headers of each data cell of table.
     * @param tableCell The table cell.
     */
    void displayCellHeader(HTMLDOMElement tableCell);

    /**
     * Display the headers of each data cell of all tables of page.
     */
    void displayAllCellHeaders();

    /**
     * Display the WAI-ARIA attributes of element.
     * @param element The element with WAI-ARIA attributes.
     */
    void displayWAIARIAStates(HTMLDOMElement element);

    /**
     * Display the WAI-ARIA attributes of all elements of page.
     */
    void displayAllWAIARIAStates();
}
