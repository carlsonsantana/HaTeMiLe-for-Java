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

public class AccessibleImageImpl implements AccessibleImage {
	protected final HTMLDOMParser parser;
	protected final String prefixId;
	protected final String classListImageAreas;
	protected final String classLongDescriptionLink;
	protected final String textLongDescriptionLink;
	protected final String dataListForImage;
	protected final String dataLongDescriptionForImage;
	protected final String dataIgnore;

	public AccessibleImageImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		classListImageAreas = configure.getParameter("class-list-image-areas");
		classLongDescriptionLink = configure.getParameter("class-longdescription-link");
		textLongDescriptionLink = configure.getParameter("text-longdescription-link");
		dataListForImage = configure.getParameter("data-list-for-image");
		dataLongDescriptionForImage = configure.getParameter("data-longdescription-for-image");
		dataIgnore = configure.getParameter("data-ignore");
	}

	public void fixMap(HTMLDOMElement element) {
		if (element.getTagName().equals("MAP")) {
			String name = null;
			if (element.hasAttribute("name")) {
				name = element.getAttribute("name");
			} else if (element.hasAttribute("id")) {
				name = element.getAttribute("id");
			}
			if ((name != null) && (!name.isEmpty())) {
				HTMLDOMElement list = parser.createElement("ul");
				list.setAttribute("class", classListImageAreas);
				Collection<HTMLDOMElement> areas = parser.find(element).findChildren("area, a").listResults();
				for (HTMLDOMElement area : areas) {
					if (area.hasAttribute("alt")) {
						HTMLDOMElement item = parser.createElement("li");
						HTMLDOMElement anchor = parser.createElement("a");
						anchor.appendText(area.getAttribute("alt"));
						CommonFunctions.setListAttributes(area, anchor, new String[] {"href",
								"target", "download", "hreflang", "media",
								"rel", "type", "title"});
						item.appendElement(anchor);
						list.appendElement(item);
					}
				}
				if (list.hasChildren()) {
					Collection<HTMLDOMElement> images = parser.find("[usemap=#" + name + "]").listResults();
					for (HTMLDOMElement image : images) {
						CommonFunctions.generateId(image, prefixId);
						if (parser.find("[" + dataListForImage + "=" + image.getAttribute("id") + "]").firstResult() == null) {
							HTMLDOMElement newList = list.cloneElement();
							newList.setAttribute(dataListForImage, image.getAttribute("id"));
							image.insertAfter(newList);
						}
					}
				}
			}
		}
	}

	public void fixMaps() {
		Collection<HTMLDOMElement> elements = parser.find("map").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixMap(element);
			}
		}
	}

	public void fixLongDescription(HTMLDOMElement element) {
		if (element.hasAttribute("longdesc")) {
			CommonFunctions.generateId(element, prefixId);
			if (parser.find("[" + dataLongDescriptionForImage + "=" + element.getAttribute("id") + "]").firstResult() == null) {
				String text;
				if (element.hasAttribute("alt")) {
					text = element.getAttribute("alt") + " " + textLongDescriptionLink;
				} else {
					text = textLongDescriptionLink;
				}
				String longDescription = element.getAttribute("longdesc");
				HTMLDOMElement anchor = parser.createElement("a");
				anchor.setAttribute("href", longDescription);
				anchor.setAttribute("target", "_blank");
				anchor.setAttribute(dataLongDescriptionForImage, element.getAttribute("id"));
				anchor.setAttribute("class", classLongDescriptionLink);
				anchor.appendText(text);
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