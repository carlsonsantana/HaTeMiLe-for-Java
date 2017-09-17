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

import hatemile.AccessibleAssociation;
import hatemile.util.CommonFunctions;
import hatemile.util.Configure;
import hatemile.util.html.HTMLDOMElement;
import hatemile.util.html.HTMLDOMParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The AccessibleAssociationImplementation class is official implementation of
 * AccessibleAssociation.
 */
public class AccessibleAssociationImplementation implements AccessibleAssociation {

	/**
	 * The HTML parser.
	 */
	protected final HTMLDOMParser parser;

	/**
	 * The prefix of generated ids.
	 */
	protected final String prefixId;

	/**
	 * Initializes a new object that improve the accessibility of associations
	 * of parser.
	 * @param parser The HTML parser.
	 * @param configure The configuration of HaTeMiLe.
	 */
	public AccessibleAssociationImplementation(final HTMLDOMParser parser, final Configure configure) {
		this.parser = parser;
		prefixId = configure.getParameter("prefix-generated-ids");
	}

	/**
	 * Returns a list that represents the table.
	 * @param part The table header, table footer or table body.
	 * @return The list that represents the table.
	 */
	protected Collection<Collection<HTMLDOMElement>> getModelTable(final HTMLDOMElement part) {
		Collection<HTMLDOMElement> rows = parser.find(part).findChildren("tr").listResults();
		Collection<Collection<HTMLDOMElement>> table = new ArrayList<Collection<HTMLDOMElement>>();
		for (HTMLDOMElement row : rows) {
			table.add(getModelRow(parser.find(row).findChildren("td,th").listResults()));
		}
		return getValidModelTable(table);
	}

	/**
	 * Returns a list that represents the table with the rowspans.
	 * @param originalTable The list that represents the table without the rowspans.
	 * @return The list that represents the table with the rowspans.
	 */
	protected Collection<Collection<HTMLDOMElement>> getValidModelTable(final Collection<Collection<HTMLDOMElement>> originalTable) {
		int cellsAdded;
		int newCellIndex;
		int rowspan;
		List<Collection<HTMLDOMElement>> originalTableList = new ArrayList<Collection<HTMLDOMElement>>(originalTable);
		List<Collection<HTMLDOMElement>> newTable = new ArrayList<Collection<HTMLDOMElement>>();
		if (!originalTableList.isEmpty()) {
			for (int rowIndex = 0, lengthTable = originalTableList.size(); rowIndex < lengthTable; rowIndex++) {
				cellsAdded = 0;
				List<HTMLDOMElement> originalRow = new ArrayList<HTMLDOMElement>(originalTableList.get(rowIndex));
				if (newTable.size() <= rowIndex) {
					newTable.add(new ArrayList<HTMLDOMElement>());
				}
				for (int cellIndex = 0, lengthRow = originalRow.size(); cellIndex < lengthRow; cellIndex++) {
					HTMLDOMElement cell = originalRow.get(cellIndex);
					newCellIndex = cellIndex + cellsAdded;
					List<HTMLDOMElement> newRow = (List<HTMLDOMElement>) newTable.get(rowIndex);
					while ((newRow.size() > newCellIndex) && (newRow.get(newCellIndex) != null)) {
						if (newRow.size() <= newCellIndex) {
							newRow.add(null);
							break;
						} else {
							cellsAdded++;
							newCellIndex = cellIndex + cellsAdded;
						}
					}
					newRow.set(newCellIndex, cell);
					if (cell.hasAttribute("rowspan")) {
						rowspan = Integer.parseInt(cell.getAttribute("rowspan"));
						for (int newRowIndex = rowIndex; rowspan > 1; rowspan--, newRowIndex++) {
							if (newTable.size() <= newRowIndex) {
								newTable.add(new ArrayList<HTMLDOMElement>());
							}
							while (newTable.get(newRowIndex).size() < newCellIndex) {
								newTable.get(newRowIndex).add(null);
							}
							newTable.get(newRowIndex).add(cell);
						}
					}
				}
			}
		}
		return newTable;
	}

	/**
	 * Returns a list that represents the line of table with the colspans.
	 * @param originalRow The list that represents the line of table without the
	 * colspans.
	 * @return The list that represents the line of table with the colspans.
	 */
	protected Collection<HTMLDOMElement> getModelRow(final Collection<HTMLDOMElement> originalRow) {
		List<HTMLDOMElement> newRow = new ArrayList<HTMLDOMElement>(originalRow);
		List<HTMLDOMElement> rowList = new ArrayList<HTMLDOMElement>(originalRow);
		if (!newRow.isEmpty()) {
			int size = newRow.size();
			int cellsAdded = 0;
			int colspan;
			for (int i = size - 1; 0 <= i; i--) {
				if (rowList.get(i).hasAttribute("colspan")) {
					colspan = Integer.parseInt(rowList.get(i).getAttribute("colspan"));
					while (colspan > 1) {
						colspan--;
						cellsAdded++;
						newRow.add(i + cellsAdded, rowList.get(i));
					}
				}
			}
		}
		return newRow;
	}

	/**
	 * Validate the list that represents the table header.
	 * @param header The list that represents the table header.
	 * @return True if the table header is valid or false if the table header is
	 * not valid.
	 */
	protected boolean validateHeader(final Collection<Collection<HTMLDOMElement>> header) {
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
	protected Collection<String> getCellsHeadersIds(final Collection<Collection<HTMLDOMElement>> header, final int index) {
		Collection<String> ids = new ArrayList<String>();
		for (Collection<HTMLDOMElement> row : header) {
			List<HTMLDOMElement> rowList = new ArrayList<HTMLDOMElement>(row);
			HTMLDOMElement cell = rowList.get(index);
			if ((cell.getTagName().equals("TH")) && (cell.getAttribute("scope").equals("col"))) {
				ids.add(cell.getAttribute("id"));
			}
		}
		return ids;
	}

	/**
	 * Associate the data cell with header cell of row.
	 * @param element The table body or table footer.
	 */
	protected void associateDataCellsWithHeaderCellsOfRow(final HTMLDOMElement element) {
		Collection<Collection<HTMLDOMElement>> table = getModelTable(element);
		Collection<String> headersIds = new ArrayList<String>();
		for (Collection<HTMLDOMElement> row : table) {
			headersIds.clear();
			for (HTMLDOMElement cell : row) {
				if (cell.getTagName().equals("TH")) {
					CommonFunctions.generateId(cell, prefixId);
					headersIds.add(cell.getAttribute("id"));

					cell.setAttribute("scope", "row");
				}
			}
			if (!headersIds.isEmpty()) {
				for (HTMLDOMElement cell : row) {
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
	 * Set the scope of header cells of table header.
	 * @param tableHeader The table header.
	 */
	protected void prepareHeaderCells(final HTMLDOMElement tableHeader) {
		Collection<HTMLDOMElement> cells = parser.find(tableHeader)
				.findChildren("tr").findChildren("th").listResults();
		for (HTMLDOMElement cell : cells) {
			CommonFunctions.generateId(cell, prefixId);

			cell.setAttribute("scope", "col");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void associateDataCellsWithHeaderCells(final HTMLDOMElement table) {
		HTMLDOMElement header = parser.find(table).findChildren("thead").firstResult();
		HTMLDOMElement body = parser.find(table).findChildren("tbody").firstResult();
		HTMLDOMElement footer = parser.find(table).findChildren("tfoot").firstResult();
		if (header != null) {
			prepareHeaderCells(header);

			Collection<Collection<HTMLDOMElement>> headerRows = getModelTable(header);
			if ((body != null) && (validateHeader(headerRows))) {
				int lengthHeader = headerRows.iterator().next().size();
				Collection<Collection<HTMLDOMElement>> fakeTable = getModelTable(body);
				if (footer != null) {
					fakeTable.addAll(getModelTable(footer));
				}
				int i;
				for (Collection<HTMLDOMElement> row : fakeTable) {
					if (row.size() == lengthHeader) {
						i = 0;
						for (HTMLDOMElement cell : row) {
							Collection<String> headersIds = getCellsHeadersIds(headerRows, i);
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
			associateDataCellsWithHeaderCellsOfRow(body);
		}
		if (footer != null) {
			associateDataCellsWithHeaderCellsOfRow(footer);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void associateAllDataCellsWithHeaderCells() {
		Collection<HTMLDOMElement> tables = parser.find("table").listResults();
		for (HTMLDOMElement table : tables) {
			if (CommonFunctions.isValidElement(table)) {
				associateDataCellsWithHeaderCells(table);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void associateLabelWithField(final HTMLDOMElement label) {
		if (label.getTagName().equals("LABEL")) {
			HTMLDOMElement field;
			if (label.hasAttribute("for")) {
				field = parser.find("#" + label.getAttribute("for")).firstResult();
			} else {
				field = parser.find(label).findDescendants("input,select,textarea").firstResult();

				if (field != null) {
					CommonFunctions.generateId(field, prefixId);
					label.setAttribute("for", field.getAttribute("id"));
				}
			}
			if (field != null) {
				if (!field.hasAttribute("aria-label")) {
					field.setAttribute("aria-label", label.getTextContent().replaceAll("[ \n\t\r]+", " ").trim());
				}

				CommonFunctions.generateId(label, prefixId);
				field.setAttribute("aria-labelledby", CommonFunctions.increaseInList(field.getAttribute("aria-labelledby"), label.getAttribute("id")));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void associateAllLabelsWithFields() {
		Collection<HTMLDOMElement> labels = parser.find("label").listResults();
		for (HTMLDOMElement label : labels) {
			if (CommonFunctions.isValidElement(label)) {
				associateLabelWithField(label);
			}
		}
	}
}
