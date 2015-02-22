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

/**
 * The Configure class contains the configuration of HaTeMiLe.
 */
public class Configure {
	
	/**
	 * The parameters of configuration of HaTeMiLe.
	 */
	protected final Map<String, String> parameters;
	
	/**
	 * The changes that will be done in selectors.
	 */
	protected final Collection<SelectorChange> selectorChanges;
	
	/**
	 * The skippers.
	 */
	protected final Collection<Skipper> skippers;
	
	/**
	 * Initializes a new object that contains the configuration of HaTeMiLe.
	 * @throws ParserConfigurationException The exception thrown when the XML
	 * file contains a syntax error.
	 * @throws SAXException The exception thrown when the XML file contains a
	 * syntax error.
	 * @throws IOException The exception thrown when the file has problems of
	 * read.
	 */
	public Configure() throws ParserConfigurationException, SAXException, IOException {
		this("hatemile-configure.xml");
	}
	
	/**
	 * Initializes a new object that contains the configuration of HaTeMiLe.
	 * @param fileName The full path of file.
	 * @throws ParserConfigurationException The exception thrown when the XML
	 * file contains a syntax error.
	 * @throws SAXException The exception thrown when the XML file contains a
	 * syntax error.
	 * @throws IOException The exception thrown when the file has problems of
	 * read.
	 */
	public Configure(String fileName) throws ParserConfigurationException, SAXException, IOException {
		parameters = new HashMap<String, String>();
		selectorChanges = new ArrayList<SelectorChange>();
		skippers = new ArrayList<Skipper>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(File.class.getResourceAsStream("/" + fileName));
		Element rootElement = document.getDocumentElement();
		//Read parameters
		NodeList nodeList = rootElement.getChildNodes();
		NodeList nodeParameters = null;
		NodeList nodeSelectorChanges = null;
		NodeList nodeSkipLinks = null;
		for (int i = 0, length = nodeList.getLength(); i < length; i++) {
			if (nodeList.item(i) instanceof Element) {
				Element element = (Element) nodeList.item(i);
				if (element.getTagName().toUpperCase().equals("PARAMETERS")) {
					nodeParameters = element.getChildNodes();
				} else if (element.getTagName().toUpperCase().equals("SELECTOR-CHANGES")) {
					nodeSelectorChanges = element.getChildNodes();
				} else if (element.getTagName().toUpperCase().equals("SKIP-LINKS")) {
					nodeSkipLinks = element.getChildNodes();
				}
			}
		}
		
		if (nodeParameters != null) {
			for (int i = 0; i < nodeParameters.getLength(); i++) {
				if (nodeParameters.item(i) instanceof Element) {
					Element parameter = (Element) nodeParameters.item(i);
					if ((parameter.getTagName().toUpperCase().equals("PARAMETER"))
							&& (parameter.hasAttribute("name"))) {
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
						selectorChanges.add(new SelectorChange(selector.getAttribute("selector")
								, selector.getAttribute("attribute")
								, selector.getAttribute("value-attribute")));
					}
				}
			}
		}
		
		if (nodeSkipLinks != null) {
			for (int i = 0; i < nodeSkipLinks.getLength(); i++) {
				if (nodeSkipLinks.item(i) instanceof Element) {
					Element skipLink = (Element) nodeSkipLinks.item(i);
					if ((skipLink.getTagName().toUpperCase().equals("SKIP-LINK"))
							&& (skipLink.hasAttribute("selector"))
							&& (skipLink.hasAttribute("default-text"))
							&& (skipLink.hasAttribute("shortcut"))) {
						skippers.add(new Skipper(skipLink.getAttribute("selector")
								, skipLink.getAttribute("default-text")
								, skipLink.getAttribute("shortcut")));
					}
				}
			}
		}
	}
	
	/**
	 * Returns the parameters of configuration.
	 * @return The parameters of configuration.
	 */
	public Map<String, String> getParameters() {
		return new HashMap<String, String>(parameters);
	}
	
	/**
	 * Returns the value of a parameter of configuration.
	 * @param parameter The parameter.
	 * @return The value of the parameter.
	 */
	public String getParameter(String parameter) {
		return parameters.get(parameter);
	}
	
	/**
	 * Returns the changes that will be done in selectors.
	 * @return The changes that will be done in selectors.
	 */
	public Collection<SelectorChange> getSelectorChanges() {
		return new ArrayList<SelectorChange>(selectorChanges);
	}
	
	/**
	 * Returns the skippers.
	 * @return The skippers.
	 */
	public Collection<Skipper> getSkippers() {
		return new ArrayList<Skipper>(skippers);
	}
	
	@Override
	public boolean equals(Object object) {
		if (this != object) {
			if (object == null) {
				return false;
			}
			if (!(object instanceof Configure)) {
				return false;
			}
			Configure configure = (Configure) object;
			if ((!parameters.equals(configure.getParameters()))
					|| (!selectorChanges.equals(configure.getSelectorChanges()))) {
				return false;
			}
		}
		return true;
	}
}