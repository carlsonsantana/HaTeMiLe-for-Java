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
package hatemile.util;

import java.util.Collection;

public interface HTMLDOMParser {
	public HTMLDOMParser find(String selector);

	public HTMLDOMParser find(HTMLDOMElement element);

	public HTMLDOMParser findChildren(String selector);

	public HTMLDOMParser findChildren(HTMLDOMElement child);

	public HTMLDOMParser findDescendants(String selector);

	public HTMLDOMParser findDescendants(HTMLDOMElement element);

	public HTMLDOMParser findAncestors(String selector);

	public HTMLDOMParser findAncestors(HTMLDOMElement element);

	public HTMLDOMElement firstResult();

	public HTMLDOMElement lastResult();

	public Collection<HTMLDOMElement> listResults();

	public HTMLDOMElement createElement(String tag);

	public String getHTML();

	public void clearParser();
}