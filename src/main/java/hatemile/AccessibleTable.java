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
 * The AccessibleTable interface fix problems of accessibility associated
 * with tables.
 */
public interface AccessibleTable {
	
	/**
	 * Associate data cells with header cells of table.
	 * @param table The table.
	 */
	public void fixAssociationCellsTable(HTMLDOMElement table);
	
	/**
	 * Associate data cells with header cells of tables.
	 */
	public void fixAssociationCellsTables();
}