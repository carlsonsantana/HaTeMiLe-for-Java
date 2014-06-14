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
package hatemile;

import hatemile.util.HTMLDOMElement;

/**
 * The AccessibleTable interface fix the problems of accessibility
 * associated with the tables.
 * @version 1.0
 */
public interface AccessibleTable {
	
	/**
	 * Fix the table header.
	 * @param tableHeader The table header.
	 */
	public void fixHeader(HTMLDOMElement tableHeader);
	
	/**
	 * Fix the table footer.
	 * @param tableFooter The table footer.
	 */
	public void fixFooter(HTMLDOMElement tableFooter);
	
	/**
	 * Fix the table body.
	 * @param tableBody The table body.
	 */
	public void fixBody(HTMLDOMElement tableBody);
	
	/**
	 * Fix the table.
	 * @param table The table.
	 */
	public void fixTable(HTMLDOMElement table);
	
	/**
	 * Fix the tables.
	 */
	public void fixTables();
}
