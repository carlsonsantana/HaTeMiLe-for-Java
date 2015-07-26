HaTeMiLe-for-Java
=================
HaTeMiLe is a libary that can convert a HTML code in a HTML code more accessible. Increasing more informations for screen readers, forcing the browsers(olds and newers) show more informations, forcing that mouse events too be accessed by keyboard and more. But, for use HaTeMiLe is need that a correctly semantic HTML code.

## How to Use
1.  Instanciate a new object with HTMLDOMParser interface, setting the HTML code;
2.  Instanciate a new Configuration object;
3.  Instanciate a new object with AccessibleForm, AccessibleImage, AccessibleNavigation, AccessibleTable, AccessibleEvent or AccessibleSelector interface and call yours methods;
4.  Get the HTML code of object with HTMLDOMParser interface.

## Example
	import hatemile.AccessibleEvent;
	import hatemile.AccessibleForm;
	import hatemile.AccessibleImage;
	import hatemile.AccessibleSelector;
	import hatemile.AccessibleNavigation;
	import hatemile.AccessibleTable;
	import hatemile.implementation.AccessibleEventImplementation;
	import hatemile.implementation.AccessibleFormImplementation;
	import hatemile.implementation.AccessibleImageImplementation;
	import hatemile.implementation.AccessibleSelectorImplementation;
	import hatemile.implementation.AccessibleNavigationImplementation;
	import hatemile.implementation.AccessibleTableImplementation;
	import hatemile.util.Configure;
	import hatemile.util.HTMLDOMParser;
	import hatemile.util.jsoup.JsoupHTMLDOMParser;
	import java.io.IOException;
	import javax.xml.parsers.ParserConfigurationException;
	import org.xml.sax.SAXException;

	public class Main {

		public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
			Configure configure = new Configure();
			HTMLDOMParser parser = new JsoupHTMLDOMParser("<!DOCTYPE html>\n" +
					"	<html>\n" +
					"		<head>\n" +
					"			<title>HaTeMiLe Tests</title>\n" +
					"			<meta charset=\"UTF-8\" />\n" +
					"		</head>\n" +
					"		<body>\n" +
					"			<h1>HaTeMiLe Tests</h1>\n" +
					"			<!-- Events -->\n" +
					"			<div>\n" +
					"				<h2>Test Events</h2>\n" +
					"				<a href=\"#\" onclick=\"alert('Alert A')\">Alert</a>\n" +
					"				<button onclick=\"alert('Alert Button')\">Alert</button>\n" +
					"				<input type=\"button\" onclick=\"alert('Alert Input')\" value=\"Alert\" />\n" +
					"				<span onclick=\"alert('Alert Span')\" style=\"background: red;\">Alert</span>\n" +
					"				<i onclick=\"alert('Alert I')\">Alert</i>\n" +
					"				<div style=\"height: 300px; width: 300px; border: 1px solid black\" onclick=\"alert('Alert Div')\">\n" +
					"					Alert\n" +
					"				</div>\n" +
					"				<span onclick=\"alert('Alert span')\" onkeypress=\"console.log('Console SPAN')\" style=\"background: blueviolet;\">Console</span>\n" +
					"				<hr />\n" +
					"				<a href=\"#\" onmouseover=\"console.log('Over A')\" onmouseout=\"console.log('Out A')\">Console</a>\n" +
					"				<button onmouseover=\"console.log('Over Button')\" onmouseout=\"console.log('Out Button')\">Console</button>\n" +
					"				<input type=\"button\" onmouseover=\"console.log('Over Input')\" value=\"Console\" onmouseout=\"console.log('Out Input')\" />\n" +
					"				<span onmouseover=\"console.log('Over Span')\" style=\"background: red;\" onmouseout=\"console.log('Out Span')\">Console</span>\n" +
					"				<i onmouseover=\"console.log('Over I')\" onmouseout=\"console.log('Out I')\">Console</i>\n" +
					"				<div style=\"height: 300px; width: 300px; border: 1px solid black\" ondrop=\"event.preventDefault();event.target.appendChild(document.getElementById(event.dataTransfer.getData('text')));\" ondragover=\"event.preventDefault();\">\n" +
					"					Console\n" +
					"				</div>\n" +
					"				<span id=\"draggable-item\" ondragstart=\"event.dataTransfer.setData('text', event.target.id);\" draggable=\"true\">Drag-and-Drop</span>\n" +
					"			</div>\n" +
					"			<!-- Forms -->\n" +
					"			<form autocomplete=\"off\" id=\"form1\">\n" +
					"				<h2>Test Forms</h2>\n" +
					"				<label for=\"field1\">Field1</label>\n" +
					"				<input type=\"text\" value=\"\" required=\"required\" id=\"field1\" autocomplete=\"on\" />\n" +
					"				<label>\n" +
					"					Field2\n" +
					"					<div>\n" +
					"						<input type=\"text\" value=\"\" required=\"required\" autocomplete=\"off\" />\n" +
					"					</div>\n" +
					"				</label>\n" +
					"				<label for=\"field3\">Field3</label>\n" +
					"				<textarea required=\"required\" id=\"field3\" autocomplete></textarea>\n" +
					"				<label>\n" +
					"					Field4\n" +
					"					<textarea required=\"required\" autocomplete=\"none\"></textarea>\n" +
					"				</label>\n" +
					"				<label for=\"field5\">Field5</label>\n" +
					"				<select required=\"required\" id=\"field5\">\n" +
					"					<option value=\"\">0</option>\n" +
					"					<option value=\"1\">1</option>\n" +
					"					<option value=\"2\">2</option>\n" +
					"				</select>\n" +
					"				<label>\n" +
					"					Field6\n" +
					"					<select required=\"required\">\n" +
					"						<option value=\"\">0</option>\n" +
					"						<option value=\"1\">1</option>\n" +
					"						<option value=\"2\">2</option>\n" +
					"					</select>\n" +
					"				</label>\n" +
					"				<label for=\"field7\">Field7</label>\n" +
					"				<input type=\"number\" min=\"0\" value=\"0\" max=\"10\" id=\"field7\" />\n" +
					"				<input type=\"submit\" value=\"Submit\" />\n" +
					"			</form>\n" +
					"			<input type=\"text\" value=\"\" required=\"\" form=\"form1\" />\n" +
					"			<!-- Images -->\n" +
					"			<div>\n" +
					"				<h2>Test Images</h2>\n" +
					"				<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Handicapped_Accessible_sign.svg/2000px-Handicapped_Accessible_sign.svg.png\" alt=\"Acessibility\" longdesc=\"https://www.wikimedia.org/\" />\n" +
					"				<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/HTML5_logo_and_wordmark.svg/2000px-HTML5_logo_and_wordmark.svg.png\" alt=\"HTML5\" longdesc=\"http://www.w3.org/html/\" />\n" +
					"			</div>\n" +
					"			<!-- Shortcuts -->\n" +
					"			<form action=\"http://www.webplatform.org/\">\n" +
					"				<h2>Test Shortcuts</h2>\n" +
					"				<a href=\"http://www.w3.org/html/\" title=\"Go to HTML5\" accesskey=\"q\">HTML5</a><br />\n" +
					"				<a href=\"https://www.wikimedia.org/\" accesskey=\"w\">Go to Wikimidia</a><br />\n" +
					"				<label id=\"label1\">Field1</label>\n" +
					"				<input type=\"text\" value=\"\" aria-labelledby=\"label1\" accesskey=\"e\" /><br />\n" +
					"				<input type=\"text\" value=\"\" aria-label=\"Field 2\" accesskey=\"r\" /><br />\n" +
					"				<input type=\"image\" src=\"https://octodex.github.com/images/octobiwan.jpg\" alt=\"Octobiwan\" accesskey=\"t\" /><br />\n" +
					"				<input type=\"reset\" value=\"Reset button\" accesskey=\"y\" /><br />\n" +
					"				<input type=\"button\" value=\"Show shortcuts\" accesskey=\"u\" onclick=\"alert('Only in client-side version.');\" /><br />\n" +
					"				<input type=\"submit\" value=\"Subit Button\" accesskey=\"i\" /><br />\n" +
					"			</form>\n" +
					"			<!-- Tables -->\n" +
					"			<div>\n" +
					"				<h2>Test Tables</h2>\n" +
					"				<table>\n" +
					"					<thead>\n" +
					"						<tr>\n" +
					"							<th rowspan=\"3\">1</th>\n" +
					"							<th rowspan=\"2\">2</th>\n" +
					"							<th>3</th>\n" +
					"							<td>4</td>\n" +
					"						</tr>\n" +
					"						<tr>\n" +
					"							<th colspan=\"2\">1</th>\n" +
					"						</tr>\n" +
					"						<tr>\n" +
					"							<th>1</th>\n" +
					"							<th>2</th>\n" +
					"							<td>3</td>\n" +
					"						</tr>\n" +
					"					</thead>\n" +
					"					<tbody>\n" +
					"						<tr>\n" +
					"							<td></td>\n" +
					"							<td></td>\n" +
					"							<td></td>\n" +
					"							<td></td>\n" +
					"						</tr>\n" +
					"						<tr>\n" +
					"							<td rowspan=\"2\"></td>\n" +
					"							<td></td>\n" +
					"							<th></th>\n" +
					"							<td></td>\n" +
					"						</tr>\n" +
					"						<tr>\n" +
					"							<td colspan=\"2\"></td>\n" +
					"							<td></td>\n" +
					"						</tr>\n" +
					"						<tr>\n" +
					"							<td></td>\n" +
					"							<td></td>\n" +
					"							<td></td>\n" +
					"						</tr>\n" +
					"					</tbody>\n" +
					"				</table>\n" +
					"				<table>\n" +
					"					<tr>\n" +
					"						<th>1</th>\n" +
					"						<td>2</td>\n" +
					"						<th colspan=\"2\">3</th>\n" +
					"					</tr>\n" +
					"					<tr>\n" +
					"						<td>1</td>\n" +
					"						<td colspan=\"2\">2</td>\n" +
					"						<td>3</td>\n" +
					"					</tr>\n" +
					"					<tr>\n" +
					"						<td>1</td>\n" +
					"						<th>2</th>\n" +
					"						<td>3</td>\n" +
					"						<td>4</td>\n" +
					"					</tr>\n" +
					"				</table>\n" +
					"			</div>\n" +
					"		</body>\n" +
					"	</html>");

			AccessibleEvent accessibleEvent = new AccessibleEventImplementation(parser, configure, true);
			AccessibleForm accessibleForm = new AccessibleFormImplementation(parser, configure);
			AccessibleImage accessibleImage = new AccessibleImageImplementation(parser, configure);
			AccessibleSelector accessibleSelector = new AccessibleSelectorImplementation(parser, configure);
			AccessibleNavigation accessibleNavigation = new AccessibleNavigationImplementation(parser, configure);
			AccessibleTable accessibleTable = new AccessibleTableImplementation(parser, configure);

			accessibleEvent.fixDragsandDrops();
			accessibleEvent.fixActives();
			accessibleEvent.fixHovers();

			accessibleForm.fixAutoCompleteFields();
			accessibleForm.fixRequiredFields();
			accessibleForm.fixLabels();
			accessibleForm.fixRangeFields();

			accessibleImage.fixLongDescriptions();

			accessibleSelector.fixSelectors();

			accessibleNavigation.fixShortcuts();
			accessibleNavigation.fixSkippers();
			accessibleNavigation.fixHeadings();

			accessibleTable.fixAssociationCellsTables();

			System.out.println(parser.getHTML());
		}
	}
