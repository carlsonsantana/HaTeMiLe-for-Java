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
package hatemile.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Configure class contains the configuration of HaTeMiLe.
 */
public class Configure {
	
	/**
	 * The parameters of configuration of HaTeMiLe.
	 */
	protected final Map<String, String> parameters;
	
	/**
	 * Initializes a new object that contains the configuration of HaTeMiLe.
	 */
	public Configure() {
		this("hatemile-configure.xml");
	}
	
	/**
	 * Initializes a new object that contains the configuration of HaTeMiLe.
	 * @param fileName The full path of file.
	 */
	public Configure(String fileName) {
		parameters = new HashMap<String, String>();
		InputStream inputStream = File.class.getResourceAsStream("/" + fileName);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			Element rootElement = document.getDocumentElement();

			if (rootElement.getTagName().equalsIgnoreCase("PARAMETERS")) {
				NodeList nodeListParameters = rootElement.getElementsByTagName("parameter");

				for (int i = 0, length = nodeListParameters.getLength(); i < length; i++) {
					Element parameterElement = (Element) nodeListParameters.item(i);
					if (parameterElement.hasAttribute("name")) {
						parameters.put(parameterElement.getAttribute("name"), parameterElement.getTextContent());
					}
				}
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					Logger.getLogger(Configure.class.getName()).log(Level.SEVERE, null, ex);
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
			if (!parameters.equals(configure.getParameters())) {
				return false;
			}
		}
		return true;
	}
}