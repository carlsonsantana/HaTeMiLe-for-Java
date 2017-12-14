# HaTeMiLe for Java

HaTeMiLe (HTML Accessible) is a library that can convert a HTML code in a HTML code more accessible.

## Accessibility solutions

* [Associate HTML elements](https://github.com/carlsonsantana/HaTeMiLe-for-Java/wiki/Associate-HTML-elements);
* [Provide a polyfill to CSS Speech and CSS Aural properties](https://github.com/carlsonsantana/HaTeMiLe-for-Java/wiki/Provide-a-polyfill-to-CSS-Speech-and-CSS-Aural-properties);
* [Display inacessible informations of page](https://github.com/carlsonsantana/HaTeMiLe-for-Java/wiki/Display-inacessible-informations-of-page);
* [Enable all functionality of page available from a keyboard](https://github.com/carlsonsantana/HaTeMiLe-for-Java/wiki/Enable-all-functionality-of-page-available-from-a-keyboard);
* [Improve the acessibility of forms](https://github.com/carlsonsantana/HaTeMiLe-for-Java/wiki/Improve-the-acessibility-of-forms);
* [Provide accessibility resources to navigate](https://github.com/carlsonsantana/HaTeMiLe-for-Java/wiki/Provide-accessibility-resources-to-navigate).

## Documentation

To generate the full API documentation of HaTeMiLe of Java:

1. [Install Maven](https://maven.apache.org/install.html);
2. Execute the command `mvn site` in HaTeMiLe of Java directory.

## Usage

Import all needed classes:

```java
import org.hatemile.AccessibleAssociation;
import org.hatemile.AccessibleCSS;
import org.hatemile.AccessibleDisplay;
import org.hatemile.AccessibleEvent;
import org.hatemile.AccessibleForm;
import org.hatemile.AccessibleNavigation;
import org.hatemile.implementation.AccessibleAssociationImplementation;
import org.hatemile.implementation.AccessibleCSSImplementation;
import org.hatemile.implementation.AccessibleDisplayScreenReaderImplementation;
import org.hatemile.implementation.AccessibleEventImplementation;
import org.hatemile.implementation.AccessibleFormImplementation;
import org.hatemile.implementation.AccessibleNavigationImplementation;
import org.hatemile.util.Configure;
import org.hatemile.util.css.StyleSheetParser;
import org.hatemile.util.css.phcss.PHCSSParser;
import org.hatemile.util.html.HTMLDOMParser;
import org.hatemile.util.html.jsoup.JsoupHTMLDOMParser;
```

Instanciate the configuration, the parsers and solution classes and execute them:

```java
//Configure
Configure configure = new Configure();
//Parsers
HTMLDOMParser htmlParser = new JsoupHTMLDOMParser(htmlCode);
StyleSheetParser cssParser = new PHCSSParser(htmlParser, currentURL);
//Execute
AccessibleAssociation accessibleAssociation =
        new AccessibleAssociationImplementation(htmlParser, configure);
accessibleAssociation.associateAllDataCellsWithHeaderCells();
accessibleAssociation.associateAllLabelsWithFields();

AccessibleCSS accessibleCSS =
        new AccessibleCSSImplementation(htmlParser, cssParser, configure);
accessibleCSS.provideAllSpeakProperties();

AccessibleDisplay accessibleDisplay =
        new AccessibleDisplayScreenReaderImplementation(htmlParser, configure);
accessibleDisplay.displayAllAlternativeTextImages();
accessibleDisplay.displayAllCellHeaders();
accessibleDisplay.displayAllDragsAndDrops();
accessibleDisplay.displayAllLanguages();
accessibleDisplay.displayAllLinksAttributes();
accessibleDisplay.displayAllRoles();
accessibleDisplay.displayAllShortcuts();
accessibleDisplay.displayAllTitles();
accessibleDisplay.displayAllWAIARIAStates();

AccessibleEvent accessibleEvent =
        new AccessibleEventImplementation(htmlParser, configure);
accessibleEvent.makeAccessibleAllClickEvents();
accessibleEvent.makeAccessibleAllDragandDropEvents();
accessibleEvent.makeAccessibleAllHoverEvents();

AccessibleForm accessibleForm =
        new AccessibleFormImplementation(htmlParser, configure);
accessibleForm.markAllAutoCompleteFields();
accessibleForm.markAllInvalidFields();
accessibleForm.markAllRangeFields();
accessibleForm.markAllRequiredFields();

AccessibleNavigation accessibleNavigation =
        new AccessibleNavigationImplementation(htmlParser, configure);
accessibleNavigation.provideNavigationByAllHeadings();
accessibleNavigation.provideNavigationByAllSkippers();
accessibleNavigation.provideNavigationToAllLongDescriptions();

accessibleDisplay.displayAllShortcuts();
```

```java
PrintWriter printWriter = new PrintWriter("example.html");
printWriter.println(htmlParser.getHTML());
printWriter.close();
```

## Contributing

If you want contribute with HaTeMiLe for Java, read [contributing guidelines](CONTRIBUTING.md).

## See also
* [HaTeMiLe for CSS](https://github.com/carlsonsantana/HaTeMiLe-for-CSS)
* [HaTeMiLe for JavaScript](https://github.com/carlsonsantana/HaTeMiLe-for-JavaScript)
* [HaTeMiLe for PHP](https://github.com/carlsonsantana/HaTeMiLe-for-PHP)
* [HaTeMiLe for Python](https://github.com/carlsonsantana/HaTeMiLe-for-Python)
* [HaTeMiLe for Ruby](https://github.com/carlsonsantana/HaTeMiLe-for-Ruby)
