HaTeMiLe-for-Java
=================
HaTeMiLe is a libary that can convert a HTML code in a HTML code more accessible. Increasing more informations for screen readers, forcing the browsers(olds and newers) show more informations, forcing that mouse events too be accessed by keyboard and more. But, for use HaTeMiLe is need that a correctly semantic HTML code.

## How to Use
1.  Instanciate a new object with HTMLDOMParser interface, setting the HTML code;
2.  Instanciate a new Configuration object;
3.  Instanciate a new object with AccessibleForm, AccessibleImage, AccessibleShortcut, AccessibleTable, AccessibleEvent or AccessibleSelector interface and call yours methods;
4.  Get the HTML code of object with HTMLDOMParser interface.

## Example
	import hatemile.AccessibleEvent;
	import hatemile.AccessibleForm;
	import hatemile.AccessibleImage;
	import hatemile.AccessibleSelector;
	import hatemile.AccessibleShortcut;
	import hatemile.AccessibleTable;
	import hatemile.implementation.AccessibleEventImpl;
	import hatemile.implementation.AccessibleFormImpl;
	import hatemile.implementation.AccessibleImageImpl;
	import hatemile.implementation.AccessibleSelectorImpl;
	import hatemile.implementation.AccessibleShortcutImpl;
	import hatemile.implementation.AccessibleTableImpl;
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
	"<html>\n" +
	"	<head>\n" +
	"		<title>HaTeMiLe Tests</title>\n" +
	"		<meta charset=\"UTF-8\" />\n" +
	"	</head>\n" +
	"	<body>\n" +
	"		<h1>HaTeMiLe Tests</h1>\n" +
	"		<!-- Events -->\n" +
	"		<div>\n" +
	"			<h2>Test Events</h2>\n" +
	"			<a href=\"#\" onclick=\"alert('Alert A')\">Alert</a>\n" +
	"			<button onclick=\"alert('Alert Button')\">Alert</button>\n" +
	"			<input type=\"button\" onclick=\"alert('Alert Input')\" value=\"Alert\" />\n" +
	"			<span onclick=\"alert('Alert Span')\" style=\"background: red;\">Alert</span>\n" +
	"			<i onclick=\"alert('Alert I')\">Alert</i>\n" +
	"			<div style=\"height: 300px; width: 300px; border: 1px solid black\"  onclick=\"alert('Alert Div')\">\n" +
	"				Alert\n" +
	"			</div>\n" +
	"			<span onclick=\"alert('Alert span')\" onkeypress=\"console.log('Console SPAN')\" style=\"background: blueviolet;\">Console</span>\n" +
	"			<hr />\n" +
	"			<a href=\"#\" onmouseover=\"console.log('Over A')\" onmouseout=\"console.log('Out A')\">Console</a>\n" +
	"			<button onmouseover=\"console.log('Over Button')\" onmouseout=\"console.log('Out Button')\">Console</button>\n" +
	"			<input type=\"button\" onmouseover=\"console.log('Over Input')\" value=\"Console\" onmouseout=\"console.log('Out Input')\" />\n" +
	"			<span onmouseover=\"console.log('Over Span')\" style=\"background: red;\" onmouseout=\"console.log('Out Span')\">Console</span>\n" +
	"			<i onmouseover=\"console.log('Over I')\" onmouseout=\"console.log('Out I')\">Console</i>\n" +
	"			<div style=\"height: 300px; width: 300px; border: 1px solid black\"  onmouseout=\"console.log('Out Div')\" onmouseover=\"console.log('Console Div')\">\n" +
	"				Console\n" +
	"			</div>\n" +
	"			<span onmouseover=\"console.log('Over span')\" onmouseout=\"console.log('Out Span')\" onfocus=\"alert('Alert SPAN')\" style=\"background: blueviolet;\">Alert</span>\n" +
	"		</div>\n" +
	"		<!-- Forms -->\n" +
	"		<form autocomplete=\"off\" id=\"form1\">\n" +
	"			<h2>Test Forms</h2>\n" +
	"			<label for=\"field1\">Field1</label>\n" +
	"			<input type=\"text\" value=\"\" required=\"required\" id=\"field1\" autocomplete=\"on\" />\n" +
	"			<label>\n" +
	"				Field2\n" +
	"				<div>\n" +
	"					<input type=\"text\" value=\"\" required=\"required\" autocomplete=\"off\" />\n" +
	"				</div>\n" +
	"			</label>\n" +
	"			<label for=\"field3\">Field3</label>\n" +
	"			<textarea required=\"required\" id=\"field3\" autocomplete></textarea>\n" +
	"			<label>\n" +
	"				Field4\n" +
	"				<textarea required=\"required\" autocomplete=\"none\"></textarea>\n" +
	"			</label>\n" +
	"			<label for=\"field5\">Field5</label>\n" +
	"			<select required=\"required\" id=\"field5\">\n" +
	"				<option value=\"\">0</option>\n" +
	"				<option value=\"1\">1</option>\n" +
	"				<option value=\"2\">2</option>\n" +
	"			</select>\n" +
	"			<label>\n" +
	"				Field6\n" +
	"				<select required=\"required\">\n" +
	"					<option value=\"\">0</option>\n" +
	"					<option value=\"1\">1</option>\n" +
	"					<option value=\"2\">2</option>\n" +
	"				</select>\n" +
	"			</label>\n" +
	"			<label for=\"field7\">Field7</label>\n" +
	"			<input type=\"number\" min=\"0\" value=\"0\" max=\"10\" id=\"field7\" />\n" +
	"			<input type=\"submit\" value=\"Submit\" />\n" +
	"		</form>\n" +
	"		<input type=\"text\" value=\"\" required=\"\" form=\"form1\" />\n" +
	"		<!-- Images -->\n" +
	"		<div>\n" +
	"			<h2>Test Images</h2>\n" +
	"			<img src=\"http://4.bp.blogspot.com/-JOqxgp-ZWe0/U3BtyEQlEiI/AAAAAAAAOfg/Doq6Q2MwIKA/s1600/google-logo-874x288.png\" alt=\"Google Logo\" longdesc=\"http://www.google.com/\" usemap=\"#laram\" />\n" +
	"			<img src=\"https://fbcdn-dragon-a.akamaihd.net/hphotos-ak-xap1/t39.2178-6/851562_329175193877061_87544187_n.jpg\" alt=\"Facebook Logo\" usemap=\"#laram\" />\n" +
	"			<img src=\"http://4.bp.blogspot.com/-JOqxgp-ZWe0/U3BtyEQlEiI/AAAAAAAAOfg/Doq6Q2MwIKA/s1600/google-logo-874x288.png\" alt=\"Google Logo\" usemap=\"#laram2\" />\n" +
	"			<img src=\"https://fbcdn-dragon-a.akamaihd.net/hphotos-ak-xap1/t39.2178-6/851562_329175193877061_87544187_n.jpg\" alt=\"Facebook Logo\" usemap=\"#laram2\" />\n" +
	"			<img src=\"https://fbcdn-dragon-a.akamaihd.net/hphotos-ak-xap1/t39.2178-6/851562_329175193877061_87544187_n.jpg\" alt=\"Facebook Logo\" usemap=\"#laram3\" />\n" +
	"			<img src=\"http://4.bp.blogspot.com/-JOqxgp-ZWe0/U3BtyEQlEiI/AAAAAAAAOfg/Doq6Q2MwIKA/s1600/google-logo-874x288.png\" alt=\"Google Logo\" longdesc=\"http://www.google.com/\" usemap=\"#laram6\" />\n" +
	"			<img src=\"http://4.bp.blogspot.com/-JOqxgp-ZWe0/U3BtyEQlEiI/AAAAAAAAOfg/Doq6Q2MwIKA/s1600/google-logo-874x288.png\" alt=\"Google Logo\" longdesc=\"http://www.google.com/\" usemap=\"#laram7\" />\n" +
	"			<map id=\"laram\" name=\"laram\">\n" +
	"				<area shape=\"rect\" href=\"http://www.google.com/\" alt=\"Google\" target=\"_blank\" coords=\"260,280,395,360\" />\n" +
	"				<area shape=\"rect\" href=\"http://www.facebook.com/\" alt=\"Facebook\" coords=\"222,113,395,148\" />\n" +
	"			</map>\n" +
	"			<map id=\"laram2\" name=\"laram2\">\n" +
	"				<area shape=\"rect\" href=\"http://www.google.com/\" target=\"_blank\" coords=\"260,280,395,360\" />\n" +
	"				<area shape=\"rect\" href=\"http://www.facebook.com/\" coords=\"222,113,395,148\" />\n" +
	"			</map>\n" +
	"			<map id=\"laram4\" name=\"laram4\">\n" +
	"				<area shape=\"rect\" href=\"http://www.google.com/\" alt=\"Google\" target=\"_blank\" coords=\"260,280,395,360\" />\n" +
	"				<area shape=\"rect\" href=\"http://www.facebook.com/\" alt=\"Facebook\" coords=\"222,113,395,148\" />\n" +
	"			</map>\n" +
	"			<map id=\"laram5\" name=\"laram5\">\n" +
	"				<area shape=\"rect\" href=\"http://www.google.com/\" target=\"_blank\" coords=\"260,280,395,360\" />\n" +
	"				<area shape=\"rect\" href=\"http://www.facebook.com/\" coords=\"222,113,395,148\" />\n" +
	"			</map>\n" +
	"			<map id=\"laram6\">\n" +
	"				<area shape=\"rect\" href=\"http://www.google.com/\" alt=\"Google\" target=\"_blank\" coords=\"260,280,395,360\" />\n" +
	"				<area shape=\"rect\" href=\"http://www.facebook.com/\" alt=\"Facebook\" coords=\"222,113,395,148\" />\n" +
	"			</map>\n" +
	"			<map name=\"laram7\">\n" +
	"				<area shape=\"rect\" href=\"http://www.google.com/\" alt=\"Google\" target=\"_blank\" coords=\"260,280,395,360\" />\n" +
	"				<area shape=\"rect\" href=\"http://www.facebook.com/\" alt=\"Facebook\" coords=\"222,113,395,148\" />\n" +
	"			</map>\n" +
	"		</div>\n" +
	"		<!-- Shortcuts -->\n" +
	"		<form action=\"http://www.webplatform.org/\">\n" +
	"			<h2>Test Shortcuts</h2>\n" +
	"			<a href=\"http://www.google.com.br/\" title=\"Go to Google\" accesskey=\"q\">Google</a><br />\n" +
	"			<a href=\"http://www.facebook.com/\" accesskey=\"w\">Go to Facebook</a><br />\n" +
	"			<label id=\"label1\">Field1</label>\n" +
	"			<input type=\"text\" value=\"\" aria-labelledby=\"label1\" accesskey=\"e\" /><br />\n" +
	"			<input type=\"text\" value=\"\" aria-label=\"Field 2\" accesskey=\"r\" /><br />\n" +
	"			<input type=\"image\" src=\"https://octodex.github.com/images/octobiwan.jpg\" alt=\"Octobiwan\" accesskey=\"t\" /><br />\n" +
	"			<input type=\"reset\" value=\"Reset button\" accesskey=\"y\" /><br />\n" +
	"			<input type=\"button\" value=\"Show shortcuts\" accesskey=\"u\" onclick=\"alert('Only in client-side version.');\" /><br />\n" +
	"			<input type=\"submit\" value=\"Subit Button\" accesskey=\"i\" /><br />\n" +
	"		</form>\n" +
	"		<!-- Tables -->\n" +
	"		<div>\n" +
	"			<h2>Test Tables</h2>\n" +
	"			<table>\n" +
	"				<thead>\n" +
	"					<tr>\n" +
	"						<th rowspan=\"3\">1</th>\n" +
	"						<th rowspan=\"2\">2</th>\n" +
	"						<th>3</th>\n" +
	"						<td>4</td>\n" +
	"					</tr>\n" +
	"					<tr>\n" +
	"						<th colspan=\"2\">1</th>\n" +
	"					</tr>\n" +
	"					<tr>\n" +
	"						<th>1</th>\n" +
	"						<th>2</th>\n" +
	"						<td>3</td>\n" +
	"					</tr>\n" +
	"				</thead>\n" +
	"				<tbody>\n" +
	"					<tr>\n" +
	"						<td></td>\n" +
	"						<td></td>\n" +
	"						<td></td>\n" +
	"						<td></td>\n" +
	"					</tr>\n" +
	"					<tr>\n" +
	"						<td rowspan=\"2\"></td>\n" +
	"						<td></td>\n" +
	"						<th></th>\n" +
	"						<td></td>\n" +
	"					</tr>\n" +
	"					<tr>\n" +
	"						<td colspan=\"2\"></td>\n" +
	"						<td></td>\n" +
	"					</tr>\n" +
	"					<tr>\n" +
	"						<td></td>\n" +
	"						<td></td>\n" +
	"						<td></td>\n" +
	"					</tr>\n" +
	"				</tbody>\n" +
	"			</table>\n" +
	"			<table>\n" +
	"				<tr>\n" +
	"					<th>1</th>\n" +
	"					<td>2</td>\n" +
	"					<th colspan=\"2\">3</th>\n" +
	"				</tr>\n" +
	"				<tr>\n" +
	"					<td>1</td>\n" +
	"					<td colspan=\"2\">2</td>\n" +
	"					<td>3</td>\n" +
	"				</tr>\n" +
	"				<tr>\n" +
	"					<td>1</td>\n" +
	"					<th>2</th>\n" +
	"					<td>3</td>\n" +
	"					<td>4</td>\n" +
	"				</tr>\n" +
	"			</table>\n" +
	"		</div>\n" +
	"	</body>\n" +
	"</html>");
		
			AccessibleEvent accessibleEvent = new AccessibleEventImpl(parser, configure);
			AccessibleForm accessibleForm = new AccessibleFormImpl(parser, configure);
			AccessibleImage accessibleImage = new AccessibleImageImpl(parser, configure);
			AccessibleSelector accessibleSelector = new AccessibleSelectorImpl(parser, configure);
			AccessibleShortcut accessibleShortcut = new AccessibleShortcutImpl(parser, configure);
			AccessibleTable accessibleTable = new AccessibleTableImpl(parser, configure);
			
			accessibleEvent.fixOnActives();
			accessibleEvent.fixOnHovers();
			
			accessibleForm.fixAutoCompletes();
			accessibleForm.fixRequiredFields();
			accessibleForm.fixLabels();
			accessibleForm.fixRangeFields();
			
			accessibleImage.fixLongDescriptions();
			accessibleImage.fixMaps();
			
			accessibleSelector.fixSelectors();
			
			accessibleShortcut.fixShortcuts();
			
			accessibleTable.fixTables();
			
			System.out.println(parser.getHTML());
		}
	}
