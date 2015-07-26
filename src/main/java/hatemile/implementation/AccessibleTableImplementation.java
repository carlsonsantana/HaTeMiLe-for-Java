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
package hatemile.implementation;

import hatemile.AccessibleTable;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.HTMLDOMElement;
import hatemile.util.HTMLDOMParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The AccessibleTableImplementation class is official implementation of
 * AccessibleTable interface.
 */
public class AccessibleTableImplementation implements AccessibleTable {
	
	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;
	
	/**
	 * The prefix of generated ids.
	 */
	protected final String prefixId;
	
	/**
	 * The name of attribute for not modify the elements.
	 */
	protected final String dataIgnore;
	
	/**
	 * Initializes a new object that manipulate the accessibility of the tables
	 * of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleTableImplementation(HTMLDOMParser parser, Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
		dataIgnore = "data-ignoreaccessibilityfix";
	}
	
	/**
	 * Returns a list that represents the table.
	 * @param part The table header, table footer or table body.
	 * @return The list that represents the table.
	 */
	protected Collection<Collection<HTMLDOMElement>> generatePart(HTMLDOMElement part) {
		Collection<HTMLDOMElement> rows = parser.find(part).findChildren("tr").listResults();
		Collection<Collection<HTMLDOMElement>> table = new ArrayList<Collection<HTMLDOMElement>>();
		for (HTMLDOMElement row : rows) {
			table.add(generateColspan(parser.find(row).findChildren("td,th").listResults()));
		}
		return generateRowspan(table);
	}
	
	/**
	 * Returns a list that represents the table with the rowspans.
	 * @param rows The list that represents the table without the rowspans.
	 * @return The list that represents the table with the rowspans.
	 */
	protected Collection<Collection<HTMLDOMElement>> generateRowspan
			(Collection<Collection<HTMLDOMElement>> rows) {
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
	
	/**
	 * Returns a list that represents the line of table with the colspans.
	 * @param row The list that represents the line of table without the
	 * colspans.
	 * @return The list that represents the line of table with the colspans.
	 */
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
	
	/**
	 * Validate the list that represents the table header.
	 * @param header The list that represents the table header.
	 * @return True if the table header is valid or false if the table header is
	 * not valid.
	 */
	protected boolean validateHeader(Collection<Collection<HTMLDOMElement>> header) {
		if (header.isEmpty()) {
			return false;
		}
		int length = -1;
		for (Collection<HTMLDOMElement> row : header) {
			if (row.isEmpty()) {
				return false;
			} else if (length == -1) {
				length = row.size();
			} else if (row.size() != length) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns a list with ids of rows of same column.
	 * @param header The list that represents the table header.
	 * @param index The index of columns.
	 * @return The list with ids of rows of same column.
	 */
	protected Collection<String> returnListIdsColumns(Collection<Collection<HTMLDOMElement>> header
			, int index) {
		Collection<String> ids = new ArrayList<String>();
		for (Collection<HTMLDOMElement> row : header) {
			List<HTMLDOMElement> cells = new ArrayList<HTMLDOMElement>(row);
			if (cells.get(index).getTagName().equals("TH")) {
				ids.add(cells.get(index).getAttribute("id"));
			}
		}
		return ids;
	}
	
	/**
	 * Fix the table body or table footer.
	 * @param element The table body or table footer.
	 */
	protected void fixBodyOrFooter(HTMLDOMElement element) {
		Collection<Collection<HTMLDOMElement>> table = generatePart(element);
		Collection<String> headersIds = new ArrayList<String>();
		for (Collection<HTMLDOMElement> cells : table) {
			headersIds.clear();
			for (HTMLDOMElement cell : cells) {
				if (cell.getTagName().equals("TH")) {
					CommonFunctions.generateId(cell, prefixId);
					headersIds.add(cell.getAttribute("id"));
					
					cell.setAttribute("scope", "row");
				}
			}
			if (!headersIds.isEmpty()) {
				for (HTMLDOMElement cell : cells) {
					if (cell.getTagName().equals("TD")) {
						String headers = cell.getAttribute("headers");
						for (String headerId : headersIds) {
							headers = CommonFunctions.increaseInList(headers, headerId);
						}
						cell.setAttribute("headers", headers);
					}
				}
			}
		}
	}
	
	/**
	 * Fix the table header.
	 * @param tableHeader The table header.
	 */
	protected void fixHeader(HTMLDOMElement tableHeader) {
		Collection<HTMLDOMElement> cells;
		cells = parser.find(tableHeader).findChildren("tr").findChildren("th").listResults();
		for (HTMLDOMElement cell : cells) {
			CommonFunctions.generateId(cell, prefixId);
			
			cell.setAttribute("scope", "col");
		}
	}
	
	public void fixAssociationCellsTable(HTMLDOMElement table) {
		HTMLDOMElement header = parser.find(table).findChildren("thead").firstResult();
		HTMLDOMElement body = parser.find(table).findChildren("tbody").firstResult();
		HTMLDOMElement footer = parser.find(table).findChildren("tfoot").firstResult();
		if (header != null) {
			fixHeader(header);
			
			Collection<Collection<HTMLDOMElement>> headerCells = generatePart(header);
			if ((body != null) && (validateHeader(headerCells))) {
				int lengthHeader = headerCells.iterator().next().size();
				Collection<Collection<HTMLDOMElement>> fakeTable = generatePart(body);
				if (footer != null) {
					fakeTable.addAll(generatePart(footer));
				}
				int i;
				for (Collection<HTMLDOMElement> cells : fakeTable) {
					if (cells.size() == lengthHeader) {
						i = 0;
						for (HTMLDOMElement cell : cells) {
							Collection<String> headersIds = returnListIdsColumns(headerCells, i);
							String headers = cell.getAttribute("headers");
							for (String headersId : headersIds) {
								headers = CommonFunctions.increaseInList(headers, headersId);
							}
							cell.setAttribute("headers", headers);
							i++;
						}
					}
				}
			}
		}
		if (body != null) {
			fixBodyOrFooter(body);
		}
		if (footer != null) {
			fixBodyOrFooter(footer);
		}
	}
	
	public void fixAssociationCellsTables() {
		Collection<HTMLDOMElement> tables = parser.find("table").listResults();
		for (HTMLDOMElement table : tables) {
			if (!table.hasAttribute(dataIgnore)) {
				fixAssociationCellsTable(table);
			}
		}
	}
}