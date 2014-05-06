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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Configure {

	protected Map<String, String> parameters;
	protected Collection<SelectorChange> selectorChanges;

	public Configure() throws ParserConfigurationException, SAXException, IOException {
		this("/hatemile-configure.xml");
	}

	public Configure(String fileName) throws ParserConfigurationException, SAXException, IOException {
		parameters = new HashMap<String, String>();
		selectorChanges = new ArrayList<SelectorChange>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(File.class.getResourceAsStream(fileName));
		Element rootElement = document.getDocumentElement();
		//Read parameters
		NodeList nodeList = rootElement.getChildNodes();
		NodeList nodeParameters = null;
		NodeList nodeSelectorChanges = null;
		for (int i = 0, length = nodeList.getLength(); i < length; i++) {
			if (nodeList.item(i) instanceof Element) {
				Element element = (Element) nodeList.item(i);
				if (element.getTagName().toUpperCase().equals("PARAMETERS")) {
					nodeParameters = element.getChildNodes();
				} else if (element.getTagName().toUpperCase().equals("SELECTOR-CHANGES")) {
					nodeSelectorChanges = element.getChildNodes();
				}
			}
		}

		if (nodeParameters != null) {
			for (int i = 0; i < nodeParameters.getLength(); i++) {
				if (nodeParameters.item(i) instanceof Element) {
					Element parameter = (Element) nodeParameters.item(i);
					if ((parameter.getTagName().toUpperCase().equals("PARAMETER")) && (parameter.hasAttribute("name"))) {
						parameters.put(parameter.getAttribute("name"), parameter.getTextContent());
					}
				}
			}
		}

		if (nodeSelectorChanges != null) {
			for (int i = 0; i < nodeSelectorChanges.getLength(); i++) {
				if (nodeSelectorChanges.item(i) instanceof Element) {
					Element selector = (Element) nodeSelectorChanges.item(i);
					if ((selector.getTagName().toUpperCase().equals("SELECTOR-CHANGE"))
							&& (selector.hasAttribute("selector"))
							&& (selector.hasAttribute("attribute"))
							&& (selector.hasAttribute("value-attribute"))) {
						selectorChanges.add(new SelectorChange(selector.getAttribute("selector"), selector.getAttribute("attribute"), selector.getAttribute("value-attribute")));
					}
				}
			}
		}
	}

	public String getParameter(String parameter) {
		return parameters.get(parameter);
	}

	public Collection<SelectorChange> getSelectorChanges() {
		return selectorChanges;
	}
}
