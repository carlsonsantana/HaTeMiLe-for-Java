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
package org.hatemile.implementation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.hatemile.AccessibleCSS;
import org.hatemile.AccessibleDisplay;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.css.StyleSheetDeclaration;
import org.hatemile.util.css.StyleSheetParser;
import org.hatemile.util.css.StyleSheetRule;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMNode;
import org.hatemile.util.html.HTMLDOMParser;
import org.hatemile.util.html.HTMLDOMTextNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The AccessibleCSSImplementation class is official implementation of
 * {@link org.hatemile.AccessibleCSS}.
 */
public class AccessibleCSSImplementation implements AccessibleCSS {

    /**
     * The operation of speakAs method.
     */
    protected interface Operation {

        /**
         * The operation method of speakAs method.
         * @param content The text content of element.
         * @param index The index of pattern in text content of element.
         * @param children The children of element.
         */
        void execute(String content, int index, List<HTMLDOMElement> children);
    }

    /**
     * The name of attribute for identify isolator elements.
     */
    public static final String DATA_ISOLATOR_ELEMENT = "data-auxiliarspan";

    /**
     * The name of attribute for identify the element created or modified to
     * support speak property.
     */
    public static final String DATA_SPEAK = "data-cssspeak";

    /**
     * The name of attribute for identify the element created or modified to
     * support speak-as property.
     */
    public static final String DATA_SPEAK_AS = "data-cssspeakas";

    /**
     * The valid element tags for inherit the speak and speak-as properties.
     */
    public static final List<String> VALID_INHERIT_TAGS = Collections
            .unmodifiableList(Arrays.asList("SPAN", "A", "RT", "DFN", "ABBR",
                "Q", "CITE", "EM", "TIME", "VAR", "SAMP", "I", "B", "SUB",
                "SUP", "SMALL", "STRONG", "MARK", "RUBY", "INS", "DEL", "KBD",
                "BDO", "CODE", "P", "FIGCAPTION", "FIGURE", "PRE", "DIV", "OL",
                "UL", "LI", "BLOCKQUOTE", "DL", "DT", "DD", "FIELDSET",
                "LEGEND", "LABEL", "FORM", "BODY", "ASIDE", "ADDRESS", "H1",
                "H2", "H3", "H4", "H5", "H6", "SECTION", "HEADER", "NAV",
                "ARTICLE", "FOOTER", "HGROUP", "CAPTION", "SUMMARY", "DETAILS",
                "TABLE", "TR", "TD", "TH", "TBODY", "THEAD", "TFOOT"));

    /**
     * The valid element tags for speak and speak-as properties.
     */
    public static final List<String> VALID_TAGS = Collections
            .unmodifiableList(Arrays.asList("SPAN", "A", "RT", "DFN", "ABBR",
                "Q", "CITE", "EM", "TIME", "VAR", "SAMP", "I", "B", "SUB",
                "SUP", "SMALL", "STRONG", "MARK", "RUBY", "INS", "DEL", "KBD",
                "BDO", "CODE", "P", "FIGCAPTION", "FIGURE", "PRE", "DIV", "LI",
                "BLOCKQUOTE", "DT", "DD", "FIELDSET", "LEGEND", "LABEL", "FORM",
                "BODY", "ASIDE", "ADDRESS", "H1", "H2", "H3", "H4", "H5", "H6",
                "SECTION", "HEADER", "NAV", "ARTICLE", "FOOTER", "CAPTION",
                "SUMMARY", "DETAILS", "TD", "TH"));

    /**
     * The regular expression to validate speak-as property.
     */
    public static final Pattern REGULAR_EXPRESSION_SPEAK_AS = Pattern
            .compile("^((normal)|(inherit)|(initial)|(digits)|"
                + "(literal\\-punctuation)|(no\\-punctuation)|(spell\\-out)|"
                + "((digits) ((literal\\-punctuation)|(no\\-punctuation)|"
                + "(spell\\-out)))|(((literal\\-punctuation)|(no\\-punctuation)"
                + "|(spell\\-out)) (digits))|(((literal\\-punctuation)|"
                + "(no\\-punctuation)) (spell\\-out))|((spell\\-out) "
                + "((literal\\-punctuation)|(no\\-punctuation)))|((digits) "
                + "((literal\\-punctuation)|(no\\-punctuation)) (spell\\-out))|"
                + "((digits) (spell\\-out) ((literal\\-punctuation)|"
                + "(no\\-punctuation)))|(((literal\\-punctuation)|"
                + "(no\\-punctuation)) (digits) (spell\\-out))|"
                + "(((literal\\-punctuation)|(no\\-punctuation)) (spell\\-out) "
                + "(digits))|((spell\\-out) (digits) ((literal\\-punctuation)|"
                + "(no\\-punctuation)))|((spell\\-out) ((literal\\-punctuation)"
                + "|(no\\-punctuation)) (digits)))$");

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser htmlParser;

    /**
     * The CSS parser.
     */
    protected final StyleSheetParser cssParser;

    /**
     * The configuration of HaTeMiLe.
     */
    protected final Configure configure;

    /**
     * The symbols with descriptions.
     */
    protected final Map<String, String> symbols;

    /**
     * The operation to speak one letter at a time for each word.
     */
    protected Operation operationSpeakAsSpellOut;

    /**
     * The operation to speak the punctuation.
     */
    protected Operation operationSpeakAsLiteralPunctuation;

    /**
     * The operation to no speak the punctuation for element.
     */
    protected Operation operationSpeakAsNoPunctuation;

    /**
     * The operation to speak the digit at a time for each number.
     */
    protected Operation operationSpeakAsDigits;

    /**
     * Initializes a new object that manipulate the accessibility of the CSS of
     * parser.
     * @param htmlDOMParser The HTML parser.
     * @param styleSheetParser The CSS parser.
     * @param hatemileConfiguration The configuration of HaTeMiLe.
     */
    public AccessibleCSSImplementation(final HTMLDOMParser htmlDOMParser,
            final StyleSheetParser styleSheetParser,
            final Configure hatemileConfiguration) {
        this(Objects.requireNonNull(htmlDOMParser),
                Objects.requireNonNull(styleSheetParser),
                Objects.requireNonNull(hatemileConfiguration),
                AccessibleCSSImplementation.class
                    .getResource("/hatemile-symbols.xml").getFile());
    }

    /**
     * Initializes a new object that manipulate the accessibility of the CSS of
     * parser.
     * @param htmlDOMParser The HTML parser.
     * @param styleSheetParser The CSS parser.
     * @param hatemileConfiguration The configuration of HaTeMiLe.
     * @param symbolFileName The file path of symbol configuration.
     */
    public AccessibleCSSImplementation(final HTMLDOMParser htmlDOMParser,
            final StyleSheetParser styleSheetParser,
            final Configure hatemileConfiguration,
            final String symbolFileName) {
        htmlParser = Objects.requireNonNull(htmlDOMParser);
        cssParser = Objects.requireNonNull(styleSheetParser);
        this.configure = hatemileConfiguration;
        symbols = getSymbols(symbolFileName, configure);
    }

    /**
     * Returns the symbols of configuration.
     * @param fileName The file path of symbol configuration.
     * @param configure The configuration of HaTeMiLe.
     * @return The symbols of configuration.
     */
    protected static Map<String, String> getSymbols(final String fileName,
            final Configure configure) {
        Map<String, String> symbols = new HashMap<String, String>();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();

            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElement = document.getDocumentElement();

            if (rootElement.getTagName().equalsIgnoreCase("SYMBOLS")) {
                NodeList nodeListSkippers = rootElement
                        .getElementsByTagName("symbol");

                for (int i = 0, length = nodeListSkippers.getLength();
                        i < length; i++) {
                    Element symbolElement = (Element) nodeListSkippers.item(i);

                    if ((symbolElement.hasAttribute("symbol"))
                            && (symbolElement.hasAttribute("description"))) {
                        symbols.put(symbolElement.getAttribute("symbol"),
                                configure.getParameter(symbolElement
                                        .getAttribute("description")));
                    }
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(AccessibleNavigationImplementation.class
                        .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return symbols;
    }

    /**
     * Returns the symbol formated to be searched by regular expression.
     * @param symbol The symbol.
     * @return The symbol formated.
     */
    protected String getFormatedSymbol(final String symbol) {
        return symbol.replace("\\", "\\\\").replace(".", "\\.")
                .replace("+", "\\+").replace("*", "\\*").replace("?", "\\?")
                .replace("^", "\\^").replace("$", "\\$").replace("[", "\\[")
                .replace("]", "\\[").replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)").replace("|", "\\|")
                .replace("/", "\\/").replace(",", "\\,").replace("!", "\\!")
                .replace("=", "\\=").replace(":", "\\:").replace("-", "\\-");
    }

    /**
     * Returns the description of symbol.
     * @param symbol The symbol.
     * @return The description of symbol.
     */
    protected String getDescriptionOfSymbol(final String symbol) {
        for (Map.Entry<String, String> entry : symbols.entrySet()) {
            if (entry.getKey().equals(symbol)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Returns the regular expression to search all symbols.
     * @return The regular expression to search all symbols.
     */
    protected String getRegularExpressionOfSymbols() {
        String formatedSymbol;
        StringBuilder regularExpression = null;
        for (String key : symbols.keySet()) {
            formatedSymbol = getFormatedSymbol(key);
            if (regularExpression == null) {
                regularExpression =
                        new StringBuilder("(" + formatedSymbol + ")");
            } else {
                regularExpression.append("|(")
                        .append(formatedSymbol)
                        .append(")");
            }
        }
        if (regularExpression == null) {
            return null;
        } else {
            return regularExpression.toString();
        }
    }

    /**
     * Check that the children of element can be manipulated to apply the CSS
     * properties.
     * @param element The element.
     * @return True if the children of element can be manipulated to apply the
     * CSS properties or false if the children of element cannot be manipulated
     * to apply the CSS properties.
     */
    protected boolean isValidInheritElement(final HTMLDOMElement element) {
        return (VALID_INHERIT_TAGS.contains(element.getTagName()))
                && (!element.hasAttribute(CommonFunctions.DATA_IGNORE));
    }

    /**
     * Check that the element can be manipulated to apply the CSS properties.
     * @param element The element.
     * @return True if the element can be manipulated to apply the CSS
     * properties or false if the element cannot be manipulated to apply the CSS
     * properties.
     */
    protected boolean isValidElement(final HTMLDOMElement element) {
        return VALID_TAGS.contains(element.getTagName());
    }

    /**
     * Isolate text nodes of element nodes.
     * @param element The element.
     */
    protected void isolateTextNode(final HTMLDOMElement element) {
        if ((element.hasChildrenElements()) && (isValidElement(element))) {
            if (isValidElement(element)) {
                HTMLDOMElement span;
                List<HTMLDOMNode> childNodes = element.getChildren();
                for (HTMLDOMNode childNode : childNodes) {
                    if (childNode instanceof HTMLDOMTextNode) {
                        span = htmlParser.createElement("span");
                        span.setAttribute(DATA_ISOLATOR_ELEMENT, "true");
                        span.appendText(childNode.getTextContent());

                        childNode.replaceNode(span);
                    }
                }
            }
            List<HTMLDOMElement> children = element.getChildrenElements();
            for (HTMLDOMElement elementChild : children) {
                isolateTextNode(elementChild);
            }
        }
    }

    /**
     * Replace the element by own text content.
     * @param element The element.
     */
    protected void replaceElementByOwnContent(final HTMLDOMElement element) {
        if (element.hasChildrenElements()) {
            List<HTMLDOMElement> children = element.getChildrenElements();
            for (HTMLDOMElement child : children) {
                element.insertBefore(child);
            }
            element.removeNode();
        } else if (element.hasChildren()) {
            element.replaceNode(element.getFirstNodeChild());
        }
    }

    /**
     * Visit and execute a operation in element and descendants.
     * @param element The element.
     * @param list The list of valid visited elements.
     */
    protected void visitValidElements(final HTMLDOMElement element,
            final List<HTMLDOMElement> list) {
        if (isValidInheritElement(element)) {
            if (element.hasChildrenElements()) {
                List<HTMLDOMElement> children = element.getChildrenElements();
                for (HTMLDOMElement child : children) {
                    visitValidElements(child, list);
                }
            } else if (isValidElement(element)) {
                list.add(element);
            }
        }
    }

    /**
     * Returns all valid descendants of element.
     * @param element The element;
     * @return All valid descendants of element.
     */
    protected List<HTMLDOMElement> getAllValidDescendants(
            final HTMLDOMElement element) {
        List<HTMLDOMElement> list = new ArrayList<HTMLDOMElement>();
        visitValidElements(element, list);
        return list;
    }

    /**
     * Create a element to show the content.
     * @param content The text content of element.
     * @param dataPropertyValue The value of custom attribute used to identify
     * the fix.
     * @return The element to show the content.
     */
    protected HTMLDOMElement createContentElement(final String content,
            final String dataPropertyValue) {
        HTMLDOMElement contentElement = htmlParser.createElement("span");
        contentElement.setAttribute(DATA_ISOLATOR_ELEMENT, "true");
        contentElement.setAttribute(DATA_SPEAK_AS, dataPropertyValue);
        contentElement.appendText(content);
        return contentElement;
    }

    /**
     * Create a element to show the content, only to aural displays.
     * @param content The text content of element.
     * @param dataPropertyValue The value of custom attribute used to identify
     * the fix.
     * @return The element to show the content.
     */
    protected HTMLDOMElement createAuralContentElement(final String content,
            final String dataPropertyValue) {
        HTMLDOMElement contentElement = createContentElement(content,
                dataPropertyValue);
        contentElement.setAttribute("unselectable", "on");
        contentElement.setAttribute("class", "screen-reader-only");
        return contentElement;
    }

    /**
     * Create a element to show the content, only to visual displays.
     * @param content The text content of element.
     * @param dataPropertyValue The value of custom attribute used to identify
     * the fix.
     * @return The element to show the content.
     */
    protected HTMLDOMElement createVisualContentElement(final String content,
            final String dataPropertyValue) {
        HTMLDOMElement contentElement = createContentElement(content,
                dataPropertyValue);
        contentElement.setAttribute("aria-hidden", "true");
        contentElement.setAttribute("role", "presentation");
        return contentElement;
    }

    /**
     * Speak the content of element only.
     * @param element The element.
     */
    protected void speakNormal(final HTMLDOMElement element) {
        if (element.hasAttribute(DATA_SPEAK)) {
            if ((element.getAttribute(DATA_SPEAK).equals("none"))
                    && (!element.hasAttribute(DATA_ISOLATOR_ELEMENT))) {
                element.removeAttribute("role");
                element.removeAttribute("aria-hidden");
                element.removeAttribute(DATA_SPEAK);
            } else {
                replaceElementByOwnContent(element);
            }
        }
    }

    /**
     * Speak the content of element and descendants.
     * @param element The element.
     */
    protected void speakNormalInherit(final HTMLDOMElement element) {
        for (HTMLDOMElement validDescendant : getAllValidDescendants(element)) {
            speakNormal(validDescendant);
        }

        element.normalize();
    }

    /**
     * No speak any content of element only.
     * @param element The element.
     */
    protected void speakNone(final HTMLDOMElement element) {
        element.setAttribute("role", "presentation");
        element.setAttribute("aria-hidden", "true");
        element.setAttribute(DATA_SPEAK, "none");
    }

    /**
     * No speak any content of element and descendants.
     * @param element The element.
     */
    protected void speakNoneInherit(final HTMLDOMElement element) {
        isolateTextNode(element);

        for (HTMLDOMElement validDescendant : getAllValidDescendants(element)) {
            speakNone(validDescendant);
        }
    }

    /**
     * Execute a operation by regular expression for element only.
     * @param element The element.
     * @param regularExpression The regular expression.
     * @param dataPropertyValue The value of custom attribute used to identify
     * the fix.
     * @param operation The operation to be executed.
     */
    protected void speakAs(final HTMLDOMElement element,
            final String regularExpression, final String dataPropertyValue,
            final Operation operation) {
        int index;
        Matcher matcher;
        List<HTMLDOMElement> children = new ArrayList<HTMLDOMElement>();
        Pattern pattern = Pattern.compile(regularExpression);
        String content = element.getTextContent();
        while (!content.isEmpty()) {
            matcher = pattern.matcher(content);
            if (matcher.find()) {
                index = matcher.start();
                operation.execute(content, index, children);

                index = index + 1;
                content = content.substring(index);
            } else {
                break;
            }
        }
        if (!children.isEmpty()) {
            if (!content.isEmpty()) {
                children.add(createContentElement(content, dataPropertyValue));
            }
            while (element.hasChildren()) {
                element.getFirstNodeChild().removeNode();
            }
            for (HTMLDOMElement child : children) {
                element.appendElement(child);
            }
        }
    }

    /**
     * Revert changes of a speakAs method for element and descendants.
     * @param element The element.
     * @param dataPropertyValue The value of custom attribute used to identify
     * the fix.
     */
    protected void reverseSpeakAs(final HTMLDOMElement element,
            final String dataPropertyValue) {
        String dataProperty = "[" + DATA_SPEAK_AS + "=\"" + dataPropertyValue
                + "\"]";

        List<HTMLDOMElement> auxiliarElements = htmlParser.find(element)
                .findDescendants(dataProperty + "[unselectable=\"on\"]")
                .listResults();
        for (HTMLDOMElement auxiliarElement : auxiliarElements) {
            auxiliarElement.removeNode();
        }

        List<HTMLDOMElement> contentElements = htmlParser.find(element)
                .findDescendants(dataProperty + "[" + DATA_ISOLATOR_ELEMENT
                    + "=\"true\"]").listResults();
        for (HTMLDOMElement contentElement : contentElements) {
            replaceElementByOwnContent(contentElement);
        }

        element.normalize();
    }

    /**
     * Use the default speak configuration of user agent for element and
     * descendants.
     * @param element The element.
     */
    protected void speakAsNormal(final HTMLDOMElement element) {
        reverseSpeakAs(element, "spell-out");
        reverseSpeakAs(element, "literal-punctuation");
        reverseSpeakAs(element, "no-punctuation");
        reverseSpeakAs(element, "digits");
    }

    /**
     * Speak one letter at a time for each word for element only.
     * @param element The element.
     */
    protected void speakAsSpellOut(final HTMLDOMElement element) {
        final String dataPropertyValue = "spell-out";
        if (operationSpeakAsSpellOut == null) {
            operationSpeakAsSpellOut = new Operation() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void execute(final String content, final int index,
                        final List<HTMLDOMElement> children) {
                    children.add(createContentElement(content
                            .substring(0, index + 1), dataPropertyValue));

                    children.add(createAuralContentElement(" ",
                            dataPropertyValue));
                }
            };
        }

        speakAs(element, "[a-zA-Z]", dataPropertyValue,
                operationSpeakAsSpellOut);
    }

    /**
     * Speak one letter at a time for each word for elements and descendants.
     * @param element The element.
     */
    protected void speakAsSpellOutInherit(final HTMLDOMElement element) {
        reverseSpeakAs(element, "spell-out");

        isolateTextNode(element);

        for (HTMLDOMElement validDescendant : getAllValidDescendants(element)) {
            speakAsSpellOut(validDescendant);
        }
    }

    /**
     * Speak the punctuation for elements only.
     * @param element The element.
     */
    protected void speakAsLiteralPunctuation(final HTMLDOMElement element) {
        final String dataPropertyValue = "literal-punctuation";
        if (operationSpeakAsLiteralPunctuation == null) {
            operationSpeakAsLiteralPunctuation = new Operation() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void execute(final String content, final int index,
                        final List<HTMLDOMElement> children) {
                    if (index != 0) {
                        children.add(createContentElement(content.substring(0,
                                index), dataPropertyValue));
                    }
                    children.add(createAuralContentElement(" "
                            + getDescriptionOfSymbol(Character.toString(content
                                .charAt(index)))
                            + " ", dataPropertyValue));

                    children.add(createVisualContentElement(Character
                            .toString(content.charAt(index)),
                                dataPropertyValue));
                }
            };
        }
        speakAs(element, getRegularExpressionOfSymbols(), dataPropertyValue,
                operationSpeakAsLiteralPunctuation);
    }

    /**
     * Speak the punctuation for elements and descendants.
     * @param element The element.
     */
    protected void speakAsLiteralPunctuationInherit(
            final HTMLDOMElement element) {
        reverseSpeakAs(element, "literal-punctuation");
        reverseSpeakAs(element, "no-punctuation");

        isolateTextNode(element);

        for (HTMLDOMElement validDescendant : getAllValidDescendants(element)) {
            speakAsLiteralPunctuation(validDescendant);
        }
    }

    /**
     * No speak the punctuation for element only.
     * @param element The element.
     */
    protected void speakAsNoPunctuation(final HTMLDOMElement element) {
        final String dataPropertyValue = "no-punctuation";
        if (operationSpeakAsNoPunctuation == null) {
            operationSpeakAsNoPunctuation = new Operation() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void execute(final String content, final int index,
                        final List<HTMLDOMElement> children) {
                    if (index != 0) {
                        children.add(createContentElement(content
                                .substring(0, index), dataPropertyValue));
                    }
                    children.add(createVisualContentElement(Character
                            .toString(content.charAt(index)),
                                dataPropertyValue));
                }
            };
        }
        speakAs(element, "[!\"#$%&'\\(\\)\\*\\+,-\\./:;<=>?@\\[\\\\\\]\\^_`\\{"
                + "\\|\\}\\~]", dataPropertyValue,
                operationSpeakAsNoPunctuation);
    }

    /**
     * No speak the punctuation for element and descendants.
     * @param element The element.
     */
    protected void speakAsNoPunctuationInherit(final HTMLDOMElement element) {
        reverseSpeakAs(element, "literal-punctuation");
        reverseSpeakAs(element, "no-punctuation");

        isolateTextNode(element);

        for (HTMLDOMElement validDescendant : getAllValidDescendants(element)) {
            speakAsNoPunctuation(validDescendant);
        }
    }

    /**
     * Speak the digit at a time for each number for element only.
     * @param element The element.
     */
    protected void speakAsDigits(final HTMLDOMElement element) {
        final String dataPropertyValue = "digits";
        if (operationSpeakAsDigits == null) {
            operationSpeakAsDigits = new Operation() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void execute(final String content, final int index,
                        final List<HTMLDOMElement> children) {
                    if (index != 0) {
                        children.add(createContentElement(content
                                .substring(0, index), dataPropertyValue));
                    }
                    children.add(createAuralContentElement(" ",
                            dataPropertyValue));

                    children.add(createContentElement(Character.toString(content
                            .charAt(index)), dataPropertyValue));
                }
            };
        }
        speakAs(element, "[0-9]", dataPropertyValue, operationSpeakAsDigits);
    }

    /**
     * Speak the digit at a time for each number for element and descendants.
     * @param element The element.
     */
    protected void speakAsDigitsInherit(final HTMLDOMElement element) {
        reverseSpeakAs(element, "digits");

        isolateTextNode(element);

        for (HTMLDOMElement validDescendant : getAllValidDescendants(element)) {
            speakAsDigits(validDescendant);
        }
    }

    /**
     * Speaks the numbers for element and descendants as a word number.
     * @param element The element.
     */
    protected void speakAsContinuousInherit(final HTMLDOMElement element) {
        reverseSpeakAs(element, "digits");
    }

    /**
     * The cells headers will be spoken for every data cell for element and
     * descendants.
     * @param element The element.
     */
    protected void speakHeaderAlwaysInherit(final HTMLDOMElement element) {
        speakHeaderOnceInherit(element);

        List<HTMLDOMElement> cellElements = htmlParser.find(element)
                .findDescendants("td[headers],th[headers]").listResults();
        AccessibleDisplay accessibleDisplay =
                new AccessibleDisplayScreenReaderImplementation(htmlParser,
                    configure);
        for (HTMLDOMElement cellElement : cellElements) {
            accessibleDisplay.displayCellHeader(cellElement);
        }
    }

    /**
     * The cells headers will be spoken one time for element and descendants.
     * @param element The element.
     */
    protected void speakHeaderOnceInherit(final HTMLDOMElement element) {
        List<HTMLDOMElement> headerElements = htmlParser.find(element)
                .findDescendants("["
                    + AccessibleDisplayScreenReaderImplementation
                        .DATA_ATTRIBUTE_HEADERS_OF
                    + "]").listResults();
        for (HTMLDOMElement headerElement : headerElements) {
            headerElement.removeNode();
        }
    }

    /**
     * Provide the CSS features of speaking and speech properties in element.
     * @param element The element.
     * @param rule The stylesheet rule.
     */
    protected void provideSpeakProperties(final HTMLDOMElement element,
            final StyleSheetRule rule) {
        String propertyValue;
        List<StyleSheetDeclaration> declarations;
        if (rule.hasProperty("speak")) {
            declarations = rule.getDeclarations("speak");
            for (StyleSheetDeclaration declaration : declarations) {
                propertyValue = declaration.getValue();
                if (propertyValue.equals("none")) {
                    speakNoneInherit(element);
                } else if (propertyValue.equals("normal")) {
                    speakNormalInherit(element);
                } else if (propertyValue.equals("spell-out")) {
                    speakAsSpellOutInherit(element);
                }
            }
        }
        if (rule.hasProperty("speak-as")) {
            List<String> speakAsValues;
            declarations = rule.getDeclarations("speak-as");
            for (StyleSheetDeclaration declaration : declarations) {
                propertyValue = declaration.getValue();
                if (REGULAR_EXPRESSION_SPEAK_AS.matcher(propertyValue)
                        .matches()) {
                    speakAsValues = declaration.getValues();
                    speakAsNormal(element);
                    for (String speakAsValue : speakAsValues) {
                        if (speakAsValue.equals("spell-out")) {
                            speakAsSpellOutInherit(element);
                        } else if (speakAsValue.equals("literal-punctuation")) {
                            speakAsLiteralPunctuationInherit(element);
                        } else if (speakAsValue.equals("no-punctuation")) {
                            speakAsNoPunctuationInherit(element);
                        } else if (speakAsValue.equals("digits")) {
                            speakAsDigitsInherit(element);
                        }
                    }
                }
            }
        }
        if (rule.hasProperty("speak-punctuation")) {
            declarations = rule.getDeclarations("speak-punctuation");
            for (StyleSheetDeclaration declaration : declarations) {
                propertyValue = declaration.getValue();
                if (propertyValue.equals("code")) {
                    speakAsLiteralPunctuationInherit(element);
                } else if (propertyValue.equals("none")) {
                    speakAsNoPunctuationInherit(element);
                }
            }
        }
        if (rule.hasProperty("speak-numeral")) {
            declarations = rule.getDeclarations("speak-numeral");
            for (StyleSheetDeclaration declaration : declarations) {
                propertyValue = declaration.getValue();
                if (propertyValue.equals("digits")) {
                    speakAsDigitsInherit(element);
                } else if (propertyValue.equals("continuous")) {
                    speakAsContinuousInherit(element);
                }
            }
        }
        if (rule.hasProperty("speak-header")) {
            declarations = rule.getDeclarations("speak-header");
            for (StyleSheetDeclaration declaration : declarations) {
                propertyValue = declaration.getValue();
                if (propertyValue.equals("always")) {
                    speakHeaderAlwaysInherit(element);
                } else if (propertyValue.equals("once")) {
                    speakHeaderOnceInherit(element);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideSpeakProperties(final HTMLDOMElement element) {
        List<HTMLDOMElement> speakElements;
        List<StyleSheetRule> rules = cssParser.getRules(Arrays.asList("speak",
                "speak-punctuation", "speak-numeral", "speak-header",
                "speak-as"));
        for (StyleSheetRule rule : rules) {
            speakElements = htmlParser.find(rule.getSelector()).listResults();
            for (HTMLDOMElement speakElement : speakElements) {
                if (speakElement.equals(element)) {
                    provideSpeakProperties(element, rule);
                    break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideAllSpeakProperties() {
        String selector = null;
        List<StyleSheetRule> rules = cssParser.getRules(Arrays.asList("speak",
                "speak-punctuation", "speak-numeral", "speak-header",
                "speak-as"));
        for (StyleSheetRule rule : rules) {
            if (selector == null) {
                selector = rule.getSelector();
            } else {
                selector += "," + rule.getSelector();
            }
        }
        if (selector != null) {
            List<HTMLDOMElement> elements = htmlParser.find(selector)
                    .listResults();
            for (HTMLDOMElement element : elements) {
                if (CommonFunctions.isValidElement(element)) {
                    provideSpeakProperties(element);
                }
            }
        }
    }
}
