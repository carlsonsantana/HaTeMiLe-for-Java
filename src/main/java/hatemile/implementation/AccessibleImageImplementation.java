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

import hatemile.AccessibleImage;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;

import java.util.Collection;

/**
 * The AccessibleImageImplementation class is official implementation of
 * AccessibleImage interface.
 */
public class AccessibleImageImplementation implements AccessibleImage {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The prefix of generated ids.
	 */
	protected final String prefixId;
	
	/**
	 * The HTML class of element for show the long description of image.
	 */
	protected final String classLongDescriptionLink;
	
	/**
	 * The prefix of content of long description.
	 */
	protected final String prefixLongDescriptionLink;
	
	/**
	 * The suffix of content of long description.
	 */
	protected final String suffixLongDescriptionLink;
	
	/**
	 * The name of attribute that link the anchor of long description with the
	 * image.
	 */
	protected final String dataLongDescriptionForImage;
	
	/**
	 * The name of attribute for not modify the elements.
	 */
	protected final String dataIgnore;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the images
	 * of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleImageImplementation(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		classLongDescriptionLink = "longdescription-link";
		prefixLongDescriptionLink = configure.getParameter("prefix-longdescription-link");
		suffixLongDescriptionLink = configure.getParameter("suffix-longdescription-link");
		dataLongDescriptionForImage = "data-longdescriptionfor";
		dataIgnore = "data-ignoreaccessibilityfix";
	}
	
	public void fixLongDescription(HTMLDOMElement element) {
		if (element.hasAttribute("longdesc")) {
			CommonFunctions.generateId(element, prefixId);
			String id = element.getAttribute("id");
			if (parser.find("[" + dataLongDescriptionForImage + "=" + id + "]").firstResult() == null) {
				String text;
				if (element.hasAttribute("alt")) {
					text = prefixLongDescriptionLink + " " + element.getAttribute("alt")
							+ " " + suffixLongDescriptionLink;
				} else {
					text = prefixLongDescriptionLink + " " + suffixLongDescriptionLink;
				}
				HTMLDOMElement anchor = parser.createElement("a");
				anchor.setAttribute("href", element.getAttribute("longdesc"));
				anchor.setAttribute("target", "_blank");
				anchor.setAttribute(dataLongDescriptionForImage, id);
				anchor.setAttribute("class", classLongDescriptionLink);
				anchor.appendText(text.trim());
				element.insertAfter(anchor);
			}
		}
	}
	
	public void fixLongDescriptions() {
		Collection<HTMLDOMElement> elements = parser.find("[longdesc]").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixLongDescription(element);
			}
		}
	}
}