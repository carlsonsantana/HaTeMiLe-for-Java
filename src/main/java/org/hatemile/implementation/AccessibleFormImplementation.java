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

import org.hatemile.AccessibleForm;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import org.hatemile.util.IDGenerator;

/**
 * The AccessibleFormImplementation class is official implementation of
 * {@link org.hatemile.AccessibleForm}.
 */
public class AccessibleFormImplementation implements AccessibleForm {

    /**
     * The ID of script element that contains the list of IDs of fields with
     * validation.
     */
    public static final String ID_SCRIPT_LIST_VALIDATION_FIELDS =
            "hatemile-scriptlist-validation-fields";

    /**
     * The ID of script element that execute validations on fields.
     */
    public static final String ID_SCRIPT_EXECUTE_VALIDATION =
            "hatemile-validation-script";

    /**
     * The client-site required fields list.
     */
    public static final String REQUIRED_FIELDS_LIST = "required_fields";

    /**
     * The client-site pattern fields list.
     */
    public static final String PATTERN_FIELDS_LIST = "pattern_fields";

    /**
     * The client-site fields with length list.
     */
    public static final String LIMITED_FIELDS_LIST = "fields_with_length";

    /**
     * The client-site range fields list.
     */
    public static final String RANGE_FIELDS_LIST = "range_fields";

    /**
     * The client-site week fields list.
     */
    public static final String WEEK_FIELDS_LIST = "week_fields";

    /**
     * The client-site month fields list.
     */
    public static final String MONTH_FIELDS_LIST = "month_fields";

    /**
     * The client-site datetime fields list.
     */
    public static final String DATETIME_FIELDS_LIST = "datetime_fields";

    /**
     * The client-site time fields list.
     */
    public static final String TIME_FIELDS_LIST = "time_fields";

    /**
     * The client-site date fields list.
     */
    public static final String DATE_FIELDS_LIST = "date_fields";

    /**
     * The client-site email fields list.
     */
    public static final String EMAIL_FIELDS_LIST = "email_fields";

    /**
     * The client-site URL fields list.
     */
    public static final String URL_FIELDS_LIST = "url_fields";

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser parser;

    /**
     * The id generator.
     */
    protected final IDGenerator idGenerator;

    /**
     * The state that indicates if the scripts used by solutions was added in
     * parser.
     */
    protected boolean scriptsAdded;

    /**
     * The script element that contains the list with IDs of fields with
     * validation.
     */
    protected HTMLDOMElement scriptListFieldsWithValidation;

    /**
     * Initializes a new object that manipulate the accessibility of the forms
     * of parser.
     * @param htmlParser The HTML parser.
     * @param configure The configuration of HaTeMiLe.
     */
    public AccessibleFormImplementation(final HTMLDOMParser htmlParser,
            final Configure configure) {
        parser = Objects.requireNonNull(htmlParser);
        idGenerator = new IDGenerator("form");
    }

    /**
     * Returns the appropriate value for attribute aria-autocomplete of field.
     * @param field The field.
     * @return The ARIA value of field.
     */
    protected String getARIAAutoComplete(final HTMLDOMElement field) {
        String tagName = field.getTagName();
        String type = null;
        if (field.hasAttribute("type")) {
            type = field.getAttribute("type").toLowerCase();
        }
        if ((tagName.equals("TEXTAREA")) || ((tagName.equals("INPUT"))
                && (!(("button".equals(type)) || ("submit".equals(type))
                    || ("reset".equals(type)) || ("image".equals(type))
                    || ("file".equals(type)) || ("checkbox".equals(type))
                    || ("radio".equals(type)) || ("hidden".equals(type)))))) {
            String value = null;
            if (field.hasAttribute("autocomplete")) {
                value = field.getAttribute("autocomplete").toLowerCase();
            } else {
                HTMLDOMElement form = parser.find(field).findAncestors("form")
                        .firstResult();
                if ((form == null) && (field.hasAttribute("form"))) {
                    form = parser.find("#" + field.getAttribute("form"))
                            .firstResult();
                }
                if ((form != null) && (form.hasAttribute("autocomplete"))) {
                    value = form.getAttribute("autocomplete").toLowerCase();
                }
            }
            if ("on".equals(value)) {
                return "both";
            } else if ((field.hasAttribute("list")) && (parser
                    .find("datalist[id=\"" + field.getAttribute("list") + "\"]")
                    .firstResult() != null)) {
                return "list";
            } else if ("off".equals(value)) {
                return "none";
            }
        }
        return null;
    }

    /**
     * Returns the content of file.
     * @param file The name of file.
     * @return The content of file.
     */
    protected String getContentFromFile(final String file) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(getClass().getResourceAsStream(file));
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine()).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Include the scripts used by solutions.
     */
    protected void generateValidationScripts() {
        HTMLDOMElement local = parser.find("head,body").firstResult();
        if (local != null) {
            if (parser.find("#" + AccessibleEventImplementation
                    .ID_SCRIPT_COMMON_FUNCTIONS).firstResult() == null) {
                HTMLDOMElement commonFunctionsScript =
                        parser.createElement("script");
                commonFunctionsScript.setAttribute("id",
                        AccessibleEventImplementation
                            .ID_SCRIPT_COMMON_FUNCTIONS);
                commonFunctionsScript.setAttribute("type", "text/javascript");
                commonFunctionsScript
                        .appendText(getContentFromFile("/js/common.js"));
                local.prependElement(commonFunctionsScript);
            }
            scriptListFieldsWithValidation = parser
                    .find("#" + ID_SCRIPT_LIST_VALIDATION_FIELDS).firstResult();
            if (scriptListFieldsWithValidation == null) {
                scriptListFieldsWithValidation = parser.createElement("script");
                scriptListFieldsWithValidation
                        .setAttribute("id", ID_SCRIPT_LIST_VALIDATION_FIELDS);
                scriptListFieldsWithValidation
                        .setAttribute("type", "text/javascript");
                scriptListFieldsWithValidation
                        .appendText("var hatemileValidationList = {")
                        .appendText("\"" + REQUIRED_FIELDS_LIST + "\": [],")
                        .appendText("\"" + PATTERN_FIELDS_LIST + "\": [],")
                        .appendText("\"" + LIMITED_FIELDS_LIST + "\": [],")
                        .appendText("\"" + RANGE_FIELDS_LIST + "\": [],")
                        .appendText("\"" + WEEK_FIELDS_LIST + "\": [],")
                        .appendText("\"" + MONTH_FIELDS_LIST + "\": [],")
                        .appendText("\"" + DATETIME_FIELDS_LIST + "\": [],")
                        .appendText("\"" + TIME_FIELDS_LIST + "\": [],")
                        .appendText("\"" + DATE_FIELDS_LIST + "\": [],")
                        .appendText("\"" + EMAIL_FIELDS_LIST + "\": [],")
                        .appendText("\"" + URL_FIELDS_LIST + "\": []")
                        .appendText("};");
                local.appendElement(scriptListFieldsWithValidation);
            }
            if (parser.find("#" + ID_SCRIPT_EXECUTE_VALIDATION).firstResult()
                    == null) {
                HTMLDOMElement scriptFunction = parser.createElement("script");
                scriptFunction.setAttribute("id", ID_SCRIPT_EXECUTE_VALIDATION);
                scriptFunction.setAttribute("type", "text/javascript");
                scriptFunction
                        .appendText(getContentFromFile("/js/validation.js"));

                parser.find("body").firstResult().appendElement(scriptFunction);
            }
        }
        scriptsAdded = true;
    }

    /**
     * Validate the field when its value change.
     * @param field The field.
     * @param listAttribute The list attribute of field with validation.
     */
    protected void validate(final HTMLDOMElement field,
            final String listAttribute) {
        if (!scriptsAdded) {
            generateValidationScripts();
        }
        idGenerator.generateId(field);
        scriptListFieldsWithValidation.appendText("hatemileValidationList."
                + listAttribute + ".push('" + field.getAttribute("id") + "');");
    }

    /**
     * {@inheritDoc}
     */
    public void markRequiredField(final HTMLDOMElement requiredField) {
        if (requiredField.hasAttribute("required")) {
            requiredField.setAttribute("aria-required", "true");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markAllRequiredFields() {
        Collection<HTMLDOMElement> requiredFields = parser.find("[required]")
                .listResults();
        for (HTMLDOMElement requiredField : requiredFields) {
            if (CommonFunctions.isValidElement(requiredField)) {
                markRequiredField(requiredField);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markRangeField(final HTMLDOMElement rangeField) {
        if (rangeField.hasAttribute("min")) {
            rangeField.setAttribute("aria-valuemin",
                    rangeField.getAttribute("min"));
        }
        if (rangeField.hasAttribute("max")) {
            rangeField.setAttribute("aria-valuemax",
                    rangeField.getAttribute("max"));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markAllRangeFields() {
        Collection<HTMLDOMElement> rangeFields = parser.find("[min],[max]")
                .listResults();
        for (HTMLDOMElement rangeField : rangeFields) {
            if (CommonFunctions.isValidElement(rangeField)) {
                markRangeField(rangeField);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markAutoCompleteField(final HTMLDOMElement autoCompleteField) {
        String ariaAutoComplete = getARIAAutoComplete(autoCompleteField);
        if (ariaAutoComplete != null) {
            autoCompleteField.setAttribute("aria-autocomplete",
                    ariaAutoComplete);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markAllAutoCompleteFields() {
        Collection<HTMLDOMElement> autoCompleteFields = parser.find(
                "input[autocomplete],textarea[autocomplete],"
                + "form[autocomplete] input,form[autocomplete] textarea,[list],"
                + "[form]").listResults();
        for (HTMLDOMElement autoCompleteField : autoCompleteFields) {
            if (CommonFunctions.isValidElement(autoCompleteField)) {
                markAutoCompleteField(autoCompleteField);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markInvalidField(final HTMLDOMElement field) {
        if ((field.hasAttribute("required"))
                || ((field.hasAttribute("aria-required"))
                    && (field.getAttribute("aria-required")
                        .equalsIgnoreCase("true")))) {
            validate(field, REQUIRED_FIELDS_LIST);
        }
        if (field.hasAttribute("pattern")) {
            validate(field, PATTERN_FIELDS_LIST);
        }
        if ((field.hasAttribute("minlength"))
                || (field.hasAttribute("maxlength"))) {
            validate(field, LIMITED_FIELDS_LIST);
        }
        if ((field.hasAttribute("aria-valuemin"))
                || (field.hasAttribute("aria-valuemax"))) {
            validate(field, RANGE_FIELDS_LIST);
        }
        if (field.hasAttribute("type")) {
            String type = field.getAttribute("type").toLowerCase();
            if (type.equals("week")) {
                validate(field, WEEK_FIELDS_LIST);
            } else if (type.equals("month")) {
                validate(field, MONTH_FIELDS_LIST);
            } else if ((type.equals("datetime-local"))
                    || (type.equals("datetime"))) {
                validate(field, DATETIME_FIELDS_LIST);
            } else if (type.equals("time")) {
                validate(field, TIME_FIELDS_LIST);
            } else if (type.equals("date")) {
                validate(field, DATE_FIELDS_LIST);
            } else if ((type.equals("number")) || (type.equals("range"))) {
                validate(field, RANGE_FIELDS_LIST);
            } else if (type.equals("email")) {
                validate(field, EMAIL_FIELDS_LIST);
            } else if (type.equals("url")) {
                validate(field, URL_FIELDS_LIST);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void markAllInvalidFields() {
        Collection<HTMLDOMElement> fields = parser.find("[required],"
                + "input[pattern],input[minlength],input[maxlength],"
                + "textarea[minlength],textarea[maxlength],input[type=week],"
                + "input[type=month],input[type=datetime-local],"
                + "input[type=datetime],input[type=time],input[type=date],"
                + "input[type=number],input[type=range],input[type=email],"
                + "input[type=url],[aria-required=true],input[aria-valuemin],"
                + "input[aria-valuemax]").listResults();
        for (HTMLDOMElement field : fields) {
            if (CommonFunctions.isValidElement(field)) {
                markInvalidField(field);
            }
        }
    }
}
