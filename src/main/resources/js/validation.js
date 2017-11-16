var DATA_IGNORE = 'data-ignoreaccessibilityfix';
var DATA_EVENT_CHANGE_ADDED = 'data-changeadded';
var DATA_INVALID_URL = 'data-invalidurl';
var DATA_INVALID_EMAIL = 'data-invalidemail';
var DATA_INVALID_RANGE = 'data-invalidrange';
var DATA_INVALID_LENGTH = 'data-invalidlength';
var DATA_INVALID_PATTERN = 'data-invalidpattern';
var DATA_INVALID_REQUIRED = 'data-invalidrequired';
var DATA_INVALID_DATE = 'data-invaliddate';
var DATA_INVALID_TIME = 'data-invalidtime';
var DATA_INVALID_DATETIME = 'data-invaliddatetime';
var DATA_INVALID_MONTH = 'data-invalidmonth';
var DATA_INVALID_WEEK = 'data-invalidweek';
var VALIDATION_TYPE = 'type';
var VALIDATION_REQUIRED = 'required';
var VALIDATION_PATTERN = 'pattern';
var VALIDATION_LENGTH = 'length';

var isValid = function (field) {
	if (field.hasAttribute(DATA_INVALID_URL)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_EMAIL)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_RANGE)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_LENGTH)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_PATTERN)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_REQUIRED)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_DATE)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_TIME)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_DATETIME)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_MONTH)) {
		return false;
	} else if (field.hasAttribute(DATA_INVALID_WEEK)) {
		return false;
	} else {
		return true;
	}
};

var validateNow = function (field, dataInvalid, validateFunction) {
	if (validateFunction(field)) {
		if (field.hasAttribute(dataInvalid)) {
			field.removeAttribute(dataInvalid);
			if ((field.hasAttribute('aria-invalid')) && (isValid(field))) {
				field.removeAttribute('aria-invalid');
			}
		}
	} else {
		field.setAttribute(dataInvalid, 'true');
		field.setAttribute('aria-invalid', 'true');
	}
};

var validate = function (idField, dataInvalid, typeFix, validateFunction) {
	var field = document.getElementById(idField);
	validateNow(field, dataInvalid, validateFunction);
	addEventHandler(field, 'change', DATA_EVENT_CHANGE_ADDED, typeFix, function (event) {
		return validateNow(field, dataInvalid, validateFunction);
	});
};

var isValidRegularExpression = function (value, pattern) {
	var regularExpression;
	regularExpression = new RegExp(pattern);
	return regularExpression.test(value);
};

var isValidURL = function (field) {
	return isEmpty(field.value) || isValidRegularExpression(field.value, '([a-zA-Z][a-zA-Z0-9\\+\\.\\-]*):(\\/\\/)?(?:(?:(?:[a-zA-Z0-9_\\.' + '\\-\\+!$&\'\\(\\)*\\+,;=]|%[0-9a-f]{2})+:)*(?:[a-zA-Z0-9_\\.\\-\\+' + '%!$&\'\\(\\)*\\+,;=]|%[0-9a-f]{2})+@)?(?:(?:[a-z0-9\\-\\.]|%' + '[0-9a-f]{2})+|(?:\\[(?:[0-9a-f]{0,4}:)*(?:[0-9a-f]{0,4})\\]))' + '(?::[0-9]+)?(?:[\\/|\\?](?:[a-zA-Z0-9_#!:\\.\\?\\+=&@!$\'~*,;\\/' + '\\(\\)\\[\\]\\-]|%[0-9a-f]{2})*)?');
};

var isValidEmail = function (field) {
	var regularExpression;
	regularExpression = '(?:[a-z0-9!#$%&\'*+\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&' + '\'*+\/=?^_`{|}~-]+)*|"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21' + '\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*")' + '@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*' + '[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}' + '(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:' + '[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|' + '\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])';
	if (field.hasAttribute('multiple')) {
		regularExpression = regularExpression + "( *, *" + regularExpression + ")*";
	}
	return isEmpty(field.value) || isValidRegularExpression(field.value, "^(" + regularExpression + ")?$");
};

var isValidDate = function (field) {
	return isEmpty(field.value) || isValidRegularExpression(field.value, '^([0-9]{2}((((' + '[02468][048])|([13579][26]))-(02)-((0[1-9])|([12][0-9])))|' + '(([0-9]{2})-((02-((0[1-9])|(1[0-9])|(2[0-8])))|(((0[469])|(11))-' + '((0[1-9])|([12][0-9])|(30)))|(((0[13578])|(10)|(12))-((0[1-9])|' + '([12][0-9])|(3[01])))))))?$');
};

var isValidTime = function (field) {
	return isEmpty(field.value) || isValidRegularExpression(field.value, '^((([01][0-9])|' + '(2[0-3])):[0-5][0-9])?$');
};

var isValidDateTime = function (field) {
	return isEmpty(field.value) || isValidRegularExpression(field.value, '^([0-9]{2}((((' + '[02468][048])|([13579][26]))-(02)-((0[1-9])|([12][0-9])))|' + '(([0-9]{2})-((02-((0[1-9])|(1[0-9])|(2[0-8])))|(((0[469])|(11))-' + '((0[1-9])|([12][0-9])|(30)))|(((0[13578])|(10)|(12))-((0[1-9])|' + '([12][0-9])|(3[01]))))))T(([01][0-9])|(2[0-3])):[0-5][0-9]((:[0-5]' + '[0-9].[0-9])|(Z))?)?$');
};

var isValidMonth = function (field) {
	return isEmpty(field.value) || isValidRegularExpression(field.value, '^([0-9]{4}-' + '((0[1-9])|(1[0-2])))?$');
};

var isValidWeek = function (field) {
	return isEmpty(field.value) || isValidRegularExpression(field.value, '^([0-9]{4}-W' + '((0[1-9])|([1-4][0-9])|(5[0-3])))?$');
};

var isValidRange = function (field) {
	var maxValue, minValue, value;
	if (!isEmpty(field.value)) {
		if (!isValidRegularExpression(field.value, '^[-+]?[0-9]+([.,][0-9]+)?$')) {
			return false;
		}
		value = parseFloat(field.value);
		if (field.hasAttribute('min') || field.hasAttribute('aria-valuemin')) {
			if (field.hasAttribute('min')) {
				minValue = parseFloat(field.getAttribute('min'));
			} else if (field.hasAttribute('aria-valuemin')) {
				minValue = parseFloat(field.getAttribute('aria-valuemin'));
			}
			if (value < minValue) {
				return false;
			}
		}
		if (field.hasAttribute('max') || field.hasAttribute('aria-valuemax')) {
			if (field.hasAttribute('max')) {
				maxValue = parseFloat(field.getAttribute('max'));
			} else if (field.hasAttribute('aria-valuemax')) {
				maxValue = parseFloat(field.getAttribute('aria-valuemax'));
			}
			if (value > maxValue) {
				return false;
			}
		}
	}
	return true;
};

var isValidLength = function (field) {
	if (field.hasAttribute('minlength')) {
		if (field.value.length < parseInt(field.getAttribute('minlength'))) {
			return false;
		}
	}
	if (field.hasAttribute('maxlength')) {
		if (field.value.length > parseInt(field.getAttribute('maxlength'))) {
			return false;
		}
	}
	return true;
};

var isValidPattern = function (field) {
	return isValidRegularExpression(field.value, field.getAttribute('pattern'));
};

var isValidRequired = function (field) {
	return !isEmpty(field.value);
};

window.addEventListener('load', function() {
	for (var i = 0, length = hatemileValidationList.required_fields.length; i < length; i++) {
		validate(hatemileValidationList.required_fields[i], DATA_INVALID_REQUIRED, VALIDATION_REQUIRED, isValidRequired);
	}

	for (var i = 0, length = hatemileValidationList.pattern_fields.length; i < length; i++) {
		validate(hatemileValidationList.pattern_fields[i], DATA_INVALID_PATTERN, VALIDATION_PATTERN, isValidPattern);
	}

	for (var i = 0, length = hatemileValidationList.fields_with_length.length; i < length; i++) {
		validate(hatemileValidationList.fields_with_length[i], DATA_INVALID_LENGTH, VALIDATION_LENGTH, isValidLength);
	}

	for (var i = 0, length = hatemileValidationList.range_fields.length; i < length; i++) {
		validate(hatemileValidationList.range_fields[i], DATA_INVALID_RANGE, VALIDATION_TYPE, isValidRange);
	}

	for (var i = 0, length = hatemileValidationList.week_fields.length; i < length; i++) {
		validate(hatemileValidationList.week_fields[i], DATA_INVALID_WEEK, VALIDATION_TYPE, isValidWeek);
	}

	for (var i = 0, length = hatemileValidationList.month_fields.length; i < length; i++) {
		validate(hatemileValidationList.month_fields[i], DATA_INVALID_MONTH, VALIDATION_TYPE, isValidMonth);
	}

	for (var i = 0, length = hatemileValidationList.datetime_fields.length; i < length; i++) {
		validate(hatemileValidationList.datetime_fields[i], DATA_INVALID_DATETIME, VALIDATION_TYPE, isValidDateTime);
	}

	for (var i = 0, length = hatemileValidationList.time_fields.length; i < length; i++) {
		validate(hatemileValidationList.time_fields[i], DATA_INVALID_TIME, VALIDATION_TYPE, isValidTime);
	}

	for (var i = 0, length = hatemileValidationList.date_fields.length; i < length; i++) {
		validate(hatemileValidationList.date_fields[i], DATA_INVALID_DATE, VALIDATION_TYPE, isValidDate);
	}

	for (var i = 0, length = hatemileValidationList.email_fields.length; i < length; i++) {
		validate(hatemileValidationList.email_fields[i], DATA_INVALID_EMAIL, VALIDATION_TYPE, isValidEmail);
	}

	for (var i = 0, length = hatemileValidationList.url_fields.length; i < length; i++) {
		validate(hatemileValidationList.url_fields[i], DATA_INVALID_URL, VALIDATION_TYPE, isValidURL);
	}
});