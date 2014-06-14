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
package hatemile.util.jsoup;

import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * The JsoupAuxiliarToString class is auxiliary class to convert
 * a Jsoup Node to a HTML code.
 */
public class JsoupAuxiliarToString {
	
	/**
	 * Convert a Jsoup Node to a HTML code.
	 * @param node The Jsoup Node.
	 * @return The HTML code of the Jsoup Node.
	 */
	public static String toString(Node node) {
		String string = "";
		List<Node> childNodes = node.childNodes();
		if (node instanceof Comment) {
			string += ((Comment) node).toString();
		} else if (node instanceof DataNode) {
			string += ((DataNode) node).toString();
		} else if (node instanceof DocumentType) {
			string += ((DocumentType) node).toString();
		} else if (node instanceof TextNode) {
			string += ((TextNode) node).getWholeText();
		} else if ((node instanceof Element) && (!(node instanceof Document))) {
			Element element = (Element) node;
			string += "<" + element.tagName();
			Attributes attributes = element.attributes();
			for (Attribute attribute : attributes) {
				string += " " + attribute.getKey() + "=\"" + attribute.getValue() + "\"";
			}
			if (childNodes.isEmpty() && element.tag().isSelfClosing()) {
				string += " />";
			} else {
				string += ">";
			}
		}
		if (!childNodes.isEmpty()) {
			for (Node childNode : childNodes) {
				string += toString(childNode);
			}
		}
		if ((node instanceof Element) && (!(node instanceof Document))) {
			Element element = (Element) node;
			if (!childNodes.isEmpty() || !element.tag().isSelfClosing()) {
				string += "</" + element.tagName() + ">";
			}
		}
		return string;
	}
}