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
 * The AccessibleImageImpl class is official implementation of AccessibleImage
 * interface.
 * @see AccessibleImage
 * @version 1.0
 */
public class AccessibleImageImpl implements AccessibleImage {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The prefix of generated id.
	 */
	protected final String prefixId;
	
	/**
	 * The HTML class of the list of map of image.
	 */
	protected final String classListImageAreas;
	
	/**
	 * The HTML class of the element for show the long description of image.
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
	 * The name of attribute that link the list generated
	 * by map with a image.
	 */
	protected final String dataListForImage;
	
	/**
	 * The name of attribute that link the anchor of long
	 * description with a image.
	 */
	protected final String dataLongDescriptionForImage;
	
	/**
	 * The name of attribute for the element that not can be modified
	 * by HaTeMiLe.
	 */
	protected final String dataIgnore;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the
	 * images of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleImageImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		classListImageAreas = configure.getParameter("class-list-image-areas");
		classLongDescriptionLink = configure.getParameter("class-longdescription-link");
		prefixLongDescriptionLink = configure.getParameter("prefix-longdescription-link");
		suffixLongDescriptionLink = configure.getParameter("suffix-longdescription-link");
		dataListForImage = configure.getParameter("data-list-for-image");
		dataLongDescriptionForImage = configure.getParameter("data-longdescription-for-image");
		dataIgnore = configure.getParameter("data-ignore");
	}

	public void fixMap(HTMLDOMElement map) {
		if (map.getTagName().equals("MAP")) {
			String name = null;
			if (map.hasAttribute("name")) {
				name = map.getAttribute("name").trim();
			} else if (map.hasAttribute("id")) {
				name = map.getAttribute("id").trim();
			}
			if ((name != null) && (!name.isEmpty())) {
				HTMLDOMElement list = parser.createElement("ul");
				list.setAttribute("class", classListImageAreas);
				Collection<HTMLDOMElement> areas = parser.find(map).findChildren("area,a").listResults();
				for (HTMLDOMElement area : areas) {
					if (area.hasAttribute("alt")) {
						HTMLDOMElement item = parser.createElement("li");
						HTMLDOMElement anchor = parser.createElement("a");
						anchor.appendText(area.getAttribute("alt").trim());
						CommonFunctions.setListAttributes(area, anchor, new String[] {"href",
								"target", "download", "hreflang", "media",
								"rel", "type", "title"});
						item.appendElement(anchor);
						list.appendElement(item);
					}
				}
				if (list.hasChildren()) {
					Collection<HTMLDOMElement> images = parser.find("[usemap=#" + name + "]").listResults();
					String id;
					for (HTMLDOMElement image : images) {
						CommonFunctions.generateId(image, prefixId);
						id = image.getAttribute("id").trim();
						if (parser.find("[" + dataListForImage + "=" + id + "]").firstResult() == null) {
							HTMLDOMElement newList = list.cloneElement();
							newList.setAttribute(dataListForImage, id);
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

	public void fixLongDescription(HTMLDOMElement image) {
		if (image.hasAttribute("longdesc")) {
			CommonFunctions.generateId(image, prefixId);
			String id = image.getAttribute("id").trim();
			if (parser.find("[" + dataLongDescriptionForImage + "=" + id + "]").firstResult() == null) {
				String text;
				if (image.hasAttribute("alt")) {
					text = prefixLongDescriptionLink + " " + image.getAttribute("alt").trim() + " " + suffixLongDescriptionLink;
				} else {
					text = prefixLongDescriptionLink + " " + suffixLongDescriptionLink;
				}
				text = text.trim();
				String longDescription = image.getAttribute("longdesc").trim();
				HTMLDOMElement anchor = parser.createElement("a");
				anchor.setAttribute("href", longDescription);
				anchor.setAttribute("target", "_blank");
				anchor.setAttribute(dataLongDescriptionForImage, id);
				anchor.setAttribute("class", classLongDescriptionLink);
				anchor.appendText(text);
				image.insertAfter(anchor);
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
