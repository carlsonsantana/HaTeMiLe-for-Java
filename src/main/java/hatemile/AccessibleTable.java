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
 * The AccessibleTable interface fix the problems of accessibility associated
 * with the tables.
 * @version 2014-07-23
 */
public interface AccessibleTable {
	
	/**
	 * Fix the table.
	 * @param table The table.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/H43.html" target="_blank">H43: Using id and headers attributes to associate data cells with header cells in data tables</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/H63.html" target="_blank">H63: Using the scope attribute to associate header cells and data cells in data tables</a>
	 */
	public void fixTable(HTMLDOMElement table);
	
	/**
	 * Fix the tables.
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/H43.html" target="_blank">H43: Using id and headers attributes to associate data cells with header cells in data tables</a>
	 * @see <a href="http://www.w3.org/TR/WCAG20-TECHS/H63.html" target="_blank">H63: Using the scope attribute to associate header cells and data cells in data tables</a>
	 */
	public void fixTables();
}