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
package hatemile.implementation;

import hatemile.AccessibleSelector;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;
import hatemile.util.SelectorChange;

import java.util.Collection;

public class AccessibleSelectorImpl implements AccessibleSelector {
	protected final HTMLDOMParser parser;
	protected final Collection<SelectorChange> changes;
	protected final String dataIgnore;

	public AccessibleSelectorImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		changes = configure.getSelectorChanges();
		dataIgnore = configure.getParameter("data-ignore");
	}

	public void fixSelectors() {
		for (SelectorChange change : changes) {
			Collection<HTMLDOMElement> elements = parser.find(change.getSelector()).listResults();
			for (HTMLDOMElement element : elements) {
				if (!element.hasAttribute(dataIgnore)) {
					element.setAttribute(change.getAttribute(), change.getValueForAttribute());
				}
			}
		}
	}
}