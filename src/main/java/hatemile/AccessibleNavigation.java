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
 * The AccessibleNavigation interface improve the accessibility of navigation.
 */
public interface AccessibleNavigation {

    /**
     * Provide a content skipper for element.
     * @param element The element.
     */
    void provideNavigationBySkipper(HTMLDOMElement element);

    /**
     * Provide navigation by content skippers.
     */
    void provideNavigationByAllSkippers();

    /**
     * Provide navigation by heading.
     * @param heading The heading element.
     */
    void provideNavigationByHeading(HTMLDOMElement heading);

    /**
     * Provide navigation by headings of page.
     */
    void provideNavigationByAllHeadings();

    /**
     * Provide an alternative way to access the long description of element.
     * @param image The image with long description.
     */
    void provideNavigationToLongDescription(HTMLDOMElement image);

    /**
     * Provide an alternative way to access the longs descriptions of all
     * elements of page.
     */
    void provideNavigationToAllLongDescriptions();
}
