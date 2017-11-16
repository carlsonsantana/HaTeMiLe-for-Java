isEmpty = function(value) {
	if ((value === undefined) ||
			(value === false) ||
			(value === null)) {
		return true;
	} else if ((typeof value === typeof '') ||
			(typeof value === typeof [])) {
		if (value.length === 0) {
			return true;
		}
	}
	return false;
};

var __exports, __base;
__exports = this;
__exports.hatemile || (__exports.hatemile = {});
(__base = __exports.hatemile).util || (__base.util = {});
__exports.hatemile.util.CommonFunctions = {
	count: 0,
	generateId: function(element, prefix) {
		if (!element.hasAttribute('id')) {
			element.setAttribute('id', prefix + this.count.toString());
			this.count++;
		}
	},
	setListAttributes: function(element1, element2, attributes) {
		var attribute, _i, _len;
		for (_i = 0, _len = attributes.length; _i < _len; _i++) {
			attribute = attributes[_i];
			if (element1.hasAttribute(attribute)) {
				element2.setAttribute(attribute, element1.getAttribute(attribute));
			}
		}
	},
	increaseInList: function(list, stringToIncrease) {
		if (!(isEmpty(list) || isEmpty(stringToIncrease))) {
			if (this.inList(list, stringToIncrease)) {
				return list;
			} else {
				return list + ' ' + stringToIncrease;
			}
		} else if (isEmpty(list)) {
			return stringToIncrease;
		} else {
			return list;
		}
	},
	inList: function(list, stringToSearch) {
		var array, item, _i, _len;
		if (!(isEmpty(list) || isEmpty(stringToSearch))) {
			array = list.split(new RegExp('[ \n\t\r]+'));
			for (_i = 0, _len = array.length; _i < _len; _i++) {
				item = array[_i];
				if (item === stringToSearch) {
					return true;
				}
			}
		}
		return false;
	}
};

var addEventHandler = function(element, typeEvent, typeDataEvent, typeFix, eventHandler) {
	var attribute, found;
	if (!hasEvent(element, typeEvent, typeDataEvent, typeFix)) {
		found = false;
		attribute = element.getAttribute(typeDataEvent);
		if (!hasEvent(element, typeEvent)) {
			element['liston' + typeEvent] = [];
			element['on' + typeEvent] = function(event) {
				var addedEvent, _i, _len, _ref;
				_ref = element['liston' + typeEvent];
				for (_i = 0, _len = _ref.length; _i < _len; _i++) {
					addedEvent = _ref[_i];
					addedEvent(event);
				}
			};
		} else {
			found = __exports.hatemile.util.CommonFunctions.inList(attribute, typeFix);
		}
		if (!found) {
			element['liston' + typeEvent].push(eventHandler);
			attribute = __exports.hatemile.util.CommonFunctions.increaseInList(attribute, typeFix);
			element.setAttribute(typeDataEvent, attribute);
		}
	}
};

var hasEvent = function(element, typeEvent, typeDataEvent, typeFix) {
	var attribute;
	if (isEmpty(typeDataEvent) || isEmpty(typeFix)) {
		return (!isEmpty(element['on' + typeEvent])) || ((!isEmpty(element.eventListenerList)) && (!isEmpty(element.eventListenerList[typeEvent])));
	} else {
		attribute = element.getAttribute(typeDataEvent);
		return (hasEvent(element, typeEvent) && (!element.hasAttribute(typeDataEvent))) || __exports.hatemile.util.CommonFunctions.inList(attribute, typeFix);
	}
};