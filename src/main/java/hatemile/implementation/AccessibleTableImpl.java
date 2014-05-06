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

import hatemile.AccessibleTable;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccessibleTableImpl implements AccessibleTable {

	protected final HTMLDOMParser parser;
	protected final String prefixId;
	protected final String dataIgnore;

	public AccessibleTableImpl(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		dataIgnore = configure.getParameter("data-ignore");
	}

	protected Collection<Collection<HTMLDOMElement>> generatePart(HTMLDOMElement part) {
		Collection<HTMLDOMElement> rows = parser.find(part).findChildren("tr").listResults();
		Collection<Collection<HTMLDOMElement>> table = new ArrayList<Collection<HTMLDOMElement>>();
		for (HTMLDOMElement row : rows) {
			table.add(generateColspan(parser.find(row).findChildren("td,th").listResults()));
		}
		return generateRowspan(table);
	}

	protected Collection<Collection<HTMLDOMElement>> generateRowspan(Collection<Collection<HTMLDOMElement>> rows) {
		List<Collection<HTMLDOMElement>> copy = new ArrayList<Collection<HTMLDOMElement>>(rows);
		List<Collection<HTMLDOMElement>> table = new ArrayList<Collection<HTMLDOMElement>>();
		if (!rows.isEmpty()) {
			for (int i = 0, lengthRows = rows.size(); i < lengthRows; i++) {
				int columnIndex = 0;
				if (table.size() <= i) {
					table.add(new ArrayList<HTMLDOMElement>());
				}
				List<HTMLDOMElement> cells = new ArrayList<HTMLDOMElement>(copy.get(i));
				for (int j = 0, lengthCells = cells.size(); j < lengthCells; j++) {
					HTMLDOMElement cell = cells.get(j);
					int m = j + columnIndex;
					List<HTMLDOMElement> row = (List<HTMLDOMElement>) table.get(i);
					while (true) {
						if (row.size() <= m) {
							row.add(null);
							break;
						} else if (row.get(m) == null) {
							break;
						} else {
							columnIndex++;
							m = j + columnIndex;
						}
					}
					row.set(m, cell);
					if (cell.hasAttribute("rowspan")) {
						int rowspan = Integer.parseInt(cell.getAttribute("rowspan"));
						for (int k = 1; k < rowspan; k++) {
							int n = i + k;
							if (table.size() <= n) {
								table.add(new ArrayList<HTMLDOMElement>());
							}
							while (table.get(n).size() < m) {
								table.get(n).add(null);
							}
							table.get(n).add(cell);
						}
					}
				}
			}
		}
		return table;
	}

	protected Collection<HTMLDOMElement> generateColspan(Collection<HTMLDOMElement> row) {
		List<HTMLDOMElement> copy = new ArrayList<HTMLDOMElement>(row);
		List<HTMLDOMElement> cells = new ArrayList<HTMLDOMElement>(row);
		for (int i = 0, size = row.size(); i < size; i++) {
			HTMLDOMElement cell = cells.get(i);
			if (cell.hasAttribute("colspan")) {
				int colspan = Integer.parseInt(cell.getAttribute("colspan"));
				for (int j = 1; j < colspan; j++) {
					copy.add(i + j, cell);
				}
			}
		}
		return copy;
	}

	protected boolean validateHeader(Collection<Collection<HTMLDOMElement>> header) {
		if (header.isEmpty()) {
			return false;
		}
		int length = -1;
		for (Collection<HTMLDOMElement> elements : header) {
			if (elements.isEmpty()) {
				return false;
			} else if (length == -1) {
				length = elements.size();
			} else if (elements.size() != length) {
				return false;
			}
		}
		return true;
	}

	protected Collection<String> returnListIdsColumns(Collection<Collection<HTMLDOMElement>> header, int index) {
		Collection<String> ids = new ArrayList<String>();
		for (Collection<HTMLDOMElement> row : header) {
			List<HTMLDOMElement> cells = new ArrayList<HTMLDOMElement>(row);
			if (cells.get(index).getTagName().equals("TH")) {
				ids.add(cells.get(index).getAttribute("id"));
			}
		}
		return ids;
	}
	
	protected void fixBodyOrFooter(HTMLDOMElement element) {
		Collection<Collection<HTMLDOMElement>> table = generatePart(element);
		Collection<String> headersIds = new ArrayList<String>();
		for (Collection<HTMLDOMElement> cells : table) {
			headersIds.clear();
			for (HTMLDOMElement cell : cells) {
				if (cell.getTagName().equals("TH")) {
					CommonFunctions.generateId(cell, prefixId);
					cell.setAttribute("scope", "row");
					headersIds.add(cell.getAttribute("id"));
				}
			}
			if (!headersIds.isEmpty()) {
				for (HTMLDOMElement cell : cells) {
					if (cell.getTagName().equals("TD")) {
						String headers = null;
						if (cell.hasAttribute("headers")) {
							headers = cell.getAttribute("headers");
						}
						for (String headerId : headersIds) {
							headers = CommonFunctions.increaseInList(headers, headerId);
						}
						cell.setAttribute("headers", headers);
					}
				}
			}
		}
	}

	public void fixHeader(HTMLDOMElement element) {
		if (element.getTagName().equals("THEAD")) {
			Collection<HTMLDOMElement> cells = parser.find(element).findChildren("tr").findChildren("th").listResults();
			for (HTMLDOMElement cell : cells) {
				CommonFunctions.generateId(cell, prefixId);
				cell.setAttribute("scope", "col");
			}
		}
	}

	public void fixFooter(HTMLDOMElement element) {
		if (element.getTagName().equals("TFOOT")) {
			fixBodyOrFooter(element);
		}
	}

	public void fixBody(HTMLDOMElement element) {
		if (element.getTagName().equals("TBODY")) {
			fixBodyOrFooter(element);
		}
	}

	public void fixTable(HTMLDOMElement element) {
		HTMLDOMElement header = parser.find(element).findChildren("thead").firstResult();
		HTMLDOMElement body = parser.find(element).findChildren("tbody").firstResult();
		HTMLDOMElement footer = parser.find(element).findChildren("tfoot").firstResult();
		if (header != null) {
			fixHeader(header);

			Collection<Collection<HTMLDOMElement>> headerCells = generatePart(header);
			if ((validateHeader(headerCells)) && (body != null)) {
				int lengthHeader = headerCells.iterator().next().size();
				Collection<Collection<HTMLDOMElement>> table = generatePart(body);
				if (footer != null) {
					table.addAll(generatePart(footer));
				}
				int i;
				for (Collection<HTMLDOMElement> cells : table) {
					i = 0;
					if (cells.size() == lengthHeader) {
						for (HTMLDOMElement cell : cells) {
							Collection<String> ids = returnListIdsColumns(headerCells, i);
							String headers = null;
							if (cell.hasAttribute("headers")) {
								headers = cell.getAttribute("headers");
							}
							for (String id : ids) {
								headers = CommonFunctions.increaseInList(headers, id);
							}
							cell.setAttribute("headers", headers);
							i++;
						}
					}
				}
			}
		}
		if (body != null) {
			fixBody(body);
		}
		if (footer != null) {
			fixFooter(footer);
		}
	}

	public void fixTables() {
		Collection<HTMLDOMElement> elements = parser.find("table").listResults();
		for (HTMLDOMElement element : elements) {
			if (!element.hasAttribute(dataIgnore)) {
				fixTable(element);
			}
		}
	}
}
