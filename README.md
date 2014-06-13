HaTeMiLe-for-Java
=================
HaTeMiLe is a libary that can convert a HTML code in a HTML code more accessible. Increasing more informations for screen readers, forcing the browsers(olds and newers) show more informations, forcing that mouse events too be accessed by keyboard and more. But, for use HaTeMiLe is need that a correctly semantic HTML code.

## How to Use

1.  Instanciate a new object with HTMLDOMParser interface, setting the HTML code;
2.  Instanciate a new Configuration object;
3.  Instanciate a new object with AccessibleForm, AccessibleImage, AccessibleShortcut, AccessibleTable, AccessibleEvent or AccessibleSelector interface and call yours methods;
4.  Get the HTML code of object with HTMLDOMParser interface.

## Example

HTMLDOMParser selector = new JsoupHTMLDOMParser("&lt;!DOCTYPE html&gt;\n" +
"&lt;html&gt;\n" +
"&nbsp;&nbsp;&nbsp; &lt;head&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;title&gt;Title&lt;/title&gt;\n" +
"&nbsp;&nbsp;&nbsp; &lt;/head&gt;\n" +
"&nbsp;&nbsp;&nbsp; &lt;body&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;label for=\"field1\" onclick=\"alert('Show me!');\"&gt;Field1\. &lt;/label&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;input type=\"text\" id=\"field1\" required=\"required\" /&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;label&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; Field2:\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;select disabled multiple&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;option value=\"1\"&gt;One&lt;/option&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;option value=\"2\"&gt;Two&lt;/option&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;option value=\"3\"&gt;Tree&lt;/option&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;option value=\"4\"&gt;Four&lt;/option&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;/select&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;/label&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;img src=\"http://static3.thedrum.com/uploads/drum_basic_article/119547/main_images/social_media_0.jpg\" alt=\"Google+\" longdesc=\"https://plus.google.com/\" usemap=\"#laram\" /&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;map id=\"laram\" name=\"laram\"&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;area shape=\"rect\" href=\"https://twitter.com/\" alt=\"Twitter\" target=\"_blank\" coords=\"260,280,395,360\"&gt;&lt;/area&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;area shape=\"rect\" href=\"http://www.facebook.com/\" alt=\"Facebook\" coords=\"222,113,395,148\"&gt;&lt;/area&gt;\n" +
"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;/map&gt;\n" +
"&nbsp;&nbsp;&nbsp; &lt;/body&gt;\n" +
"&lt;/html&gt;");
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; Configure configure = new Configure();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; AccessibleForm accessibleForm = new AccessibleFormImpl(selector, configure);
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixRequiredFields();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixDisabledFields();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixLabels();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixRangeFields();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixReadOnlyFields();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixSelectFields();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleForm.fixTextFields();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; AccessibleImage accessibleImage = new AccessibleImageImpl(selector, configure);
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleImage.fixMaps();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleImage.fixLongDescriptions();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; AccessibleShortcut accessibleShortcut = new AccessibleShortcutImpl(selector, configure, "mozilla/5.0 (x11\. ubuntu; linux x86_64\. rv:27.0\. gecko/2010010\. firefox/27.0");
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleShortcut.fixShortcuts();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; AccessibleTable accessibleTable = new AccessibleTableImpl(selector, configure);
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleTable.fixTables();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; AccessibleEvent accessibleEvent = new AccessibleEventImpl(selector, configure);
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleEvent.fixOnHovers();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; accessibleEvent.fixOnClicks();
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; System.out.println(selector.getHTML());
