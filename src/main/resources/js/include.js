__exports.__aria_grabbed__elements__ = [];
__exports.__aria_dropeffect__elements__ = [];

enterPressed = function(keyCode) {
	var enter1, enter2;
	enter1 = '\n'.charCodeAt(0);
	enter2 = '\r'.charCodeAt(0);
	return (keyCode === enter1) || (keyCode === enter2);
};

keyboardAccess = function(element) {
	var tag;
	if (!element.hasAttribute('tabindex')) {
		tag = element.tagName.toUpperCase();
		if ((tag === 'A') && (!element.hasAttribute('href'))) {
			element.setAttribute('tabindex', '0');
		} else if ((tag !== 'INPUT') && (tag !== 'BUTTON') && (tag !== 'SELECT') && (tag !== 'TEXTAREA')) {
			element.setAttribute('tabindex', '0');
		}
	}
};

clearDropEffect = function() {
	var activeEvents, dragEvents, droppedElement, droppedElements, hoverEvents, _i, _len;
	droppedElements = __exports.__aria_dropeffect__elements__;
	for (_i = 0, _len = droppedElements.length; _i < _len; _i++) {
		droppedElement = droppedElements[_i];
		dragEvents = (!hasEvent(droppedElement, 'keydown', 'data-keydownadded', 'drag')) && (!hasEvent(droppedElement, 'keyup', 'data-keyupadded', 'drag'));
		activeEvents = (!droppedElement.hasAttribute('data-keypressadded')) && (!hasEvent(droppedElement, 'keydown', 'data-keydownadded', 'active')) && (!hasEvent(droppedElement, 'keyup', 'data-keyupadded', 'active'));
		hoverEvents = (!hasEvent(droppedElement, 'focus', 'data-focusadded', 'hover')) && (!hasEvent(droppedElement, 'blur', 'data-bluradded', 'hover'));
		droppedElement.setAttribute('aria-dropeffect', 'none');
		if (droppedElement.hasAttribute('tabindex') && dragEvents && activeEvents && hoverEvents) {
			droppedElement.removeAttribute('tabindex');
		}
	}
};

generateDropEffect = function() {
	var ariaDropEffect, dropEffect, droppedElement, droppedElements, effectAllowed, _i, _len;
	dropEffect = __exports.__dragEventDataTransfer__.dropEffect;
	effectAllowed = __exports.__dragEventDataTransfer__.effectAllowed;
	if ((dropEffect === 'none') || ((dropEffect !== 'copy') && (dropEffect !== 'link') && (dropEffect !== 'move'))) {
		if ((effectAllowed === 'copyLink') || (effectAllowed === 'copyMove') || (effectAllowed === 'linkMove') || (effectAllowed === 'all')) {
			ariaDropEffect = 'popup';
		} else if ((effectAllowed === 'copy') || (effectAllowed === 'move') || (effectAllowed === 'link')) {
			ariaDropEffect = effectAllowed;
		} else {
			ariaDropEffect = 'move';
		}
	} else {
		ariaDropEffect = dropEffect;
	}
	droppedElements = __exports.__aria_dropeffect__elements__;
	for (_i = 0, _len = droppedElements.length; _i < _len; _i++) {
		droppedElement = droppedElements[_i];
		if (hasEvent(droppedElement, 'drop')) {
			droppedElement.setAttribute('aria-dropeffect', ariaDropEffect);
		}
		keyboardAccess(droppedElement);
	}
};

executeMouseEvent = function(type, element, event) {
	executeEvent(element, createMouseEvent(type, element, event));
};

executeDragEvent = function(type, element, event) {
	if (isEmpty(__exports.__dragEventDataTransfer__)) {
		__exports.__dragEventDataTransfer__ = {
			'files': null,
			'types': null,
			'effectAllowed': 'uninitialized',
			'dropEffect': 'none'
		};
		__exports.__dragEventDataTransfer__.setDragImage = function() {
		};
		__exports.__dragEventDataTransfer__.addElement = function() {
		};
		__exports.__dragEventDataTransfer__._data = {};
		__exports.__dragEventDataTransfer__.setData = function(format, data) {
			__exports.__dragEventDataTransfer__._data[format] = data;
		};
		__exports.__dragEventDataTransfer__.getData = function(format) {
			return __exports.__dragEventDataTransfer__._data[format];
		};
		__exports.__dragEventDataTransfer__.clearData = function(format) {
			if (isEmpty(format)) {
				__exports.__dragEventDataTransfer__._data = {};
			} else {
				__exports.__dragEventDataTransfer__._data[format] = void 0;
			}
		};
	}
	executeEvent(element, createDragEvent(type, element, event));
};

executeEvent = function(element, event) {
	var error, handlerEvent, listenerEvent, _i, _len, _ref;
	if (hasEvent(element, event.type)) {
		try {
			if (!isEmpty(element.dispatchEvent)) {
				element.dispatchEvent(event);
			} else {
				handlerEvent = element['on' + event.type];
				if (!isEmpty(handlerEvent)) {
					handlerEvent(event);
				}
				if ((!isEmpty(element.eventListenerList)) && (!isEmpty(element.eventListenerList[event.type]))) {
					_ref = element.eventListenerList[event.type];
					for (_i = 0, _len = _ref.length; _i < _len; _i++) {
						listenerEvent = _ref[_i];
						listenerEvent(event);
					}
				}
			}
		} catch (_error) {
			error = _error;
		}
	}
};

createMouseEvent = function(type, element, event) {
	var data, mouseEvent;
	data = {
		'view': event.view,
		'bubbles': true,
		'cancelable': true,
		'target': element,
		'altKey': event.altKey,
		'ctrlKey': event.ctrlKey,
		'cancelBubble': false,
		'isTrusted': true,
		'metaKey': false,
		'shiftKey': event.shiftKey,
		'clientX': 0,
		'clientY': 0,
		'pageX': 0,
		'pageY': 0,
		'screenX': 0,
		'screenY': 0
	};
	if (isEmpty(Event)) {
		mouseEvent = data;
		mouseEvent.type = type;
	} else {
		mouseEvent = new Event(type, data);
	}
	mouseEvent.preventDefault = function() {
		return event.preventDefault();
	};
	mouseEvent.stopImmediatePropagation = function() {
		return event.stopImmediatePropagation();
	};
	mouseEvent.stopPropagation = function() {
		return event.stopPropagation();
	};
	return mouseEvent;
};

createDragEvent = function(type, element, event) {
	var dragEvent;
	dragEvent = createMouseEvent(type, element, event);
	dragEvent.dataTransfer = __exports.__dragEventDataTransfer__;
	return dragEvent;
};

fixActiveInElement = function(element) {
	if (element.tagName.toUpperCase() !== 'A') {
		addEventHandler(element, 'keypress', 'data-keypressadded', 'active', function(event) {
			if (enterPressed(event.keyCode)) {
				if (hasEvent(element, 'click')) {
					executeMouseEvent('click', element, event);
				} else if (hasEvent(element, 'dblclick')) {
					executeMouseEvent('dblclick', element, event);
				}
			}
		});
	}
	addEventHandler(element, 'keyup', 'data-keyupadded', 'active', function(event) {
		if (enterPressed(event.keyCode)) {
			executeMouseEvent('mouseup', element, event);
		}
	});
	addEventHandler(element, 'keydown', 'data-keydownadded', 'active', function(event) {
		if (enterPressed(event.keyCode)) {
			executeMouseEvent('mousedown', element, event);
		}
	});
};

fixHoverInElement = function(element) {
	addEventHandler(element, 'focus', 'data-focusadded', 'hover', function(event) {
		executeMouseEvent('mouseover', element, event);
	});
	addEventHandler(element, 'blur', 'data-bluradded', 'hover', function(event) {
		executeMouseEvent('mouseout', element, event);
	});
};

fixDragInElement = function(element) {
	if ((!hasEvent(element, 'keydown', 'data-keydownadded', 'drag')) && (!hasEvent(element, 'keyup', 'data-keyupadded', 'drag'))) {
		addEventHandler(element, 'keydown', 'data-keydownadded', 'drag', function(event) {
			var grabbedElement, grabbedElements, _i, _len;
			if ((event.keyCode === ' '.charCodeAt(0)) && (!element.hasAttribute('data-keypressed'))) {
				grabbedElements = __exports.__aria_grabbed__elements__;
				for (_i = 0, _len = grabbedElements.length; _i < _len; _i++) {
					grabbedElement = grabbedElements[_i];
					grabbedElement.setAttribute('aria-grabbed', 'false');
					executeDragEvent('dragend', grabbedElement, event);
				}
				element.setAttribute('aria-grabbed', 'true');
				element.setAttribute('data-keypressed', 'true');
				__exports.__aria_grabbed__elements__ = [element];
				executeDragEvent('dragstart', element, event);
				executeDragEvent('drag', element, event);
				generateDropEffect();
			}
		});
		addEventHandler(element, 'keyup', 'data-keyupadded', 'drag', function(event) {
			element.removeAttribute('data-keypressed');
		});
	}
};

fixDropInElement = function(element) {
	__exports.__aria_dropeffect__elements__.push(element);
	addEventHandler(element, 'focus', 'data-focusadded', 'drop', function(event) {
		if (!isEmpty(__exports.__aria_grabbed__elements__)) {
			executeDragEvent('dragenter', element, event);
			executeDragEvent('dragover', element, event);
			generateDropEffect();
		}
	});
	addEventHandler(element, 'blur', 'data-bluradded', 'drop', function(event) {
		if (!isEmpty(__exports.__aria_grabbed__elements__)) {
			executeDragEvent('dragleave', element, event);
			generateDropEffect();
		}
	});
	if ((!hasEvent(element, 'keydown', 'data-keydownadded', 'drop')) && (!hasEvent(element, 'keyup', 'data-keyupadded', 'drop'))) {
		addEventHandler(element, 'keydown', 'data-keydownadded', 'drop', function(event) {
			var grabbedElement, grabbedElements, _i, _len;
			if ((enterPressed(event.keyCode)) && (!element.hasAttribute('data-keypressed')) && (!isEmpty(__exports.__aria_grabbed__elements__))) {
				element.setAttribute('data-keypressed', 'true');
				if (hasEvent(element, 'drop')) {
					grabbedElements = __exports.__aria_grabbed__elements__;
					for (_i = 0, _len = grabbedElements.length; _i < _len; _i++) {
						grabbedElement = grabbedElements[_i];
						grabbedElement.setAttribute('aria-grabbed', 'false');
						executeDragEvent('dragend', grabbedElement, event);
					}
					__exports.__aria_grabbed__elements__ = [];
					clearDropEffect();
				}
				executeDragEvent('drop', element, event);
			}
		});
		addEventHandler(element, 'keyup', 'data-keyupadded', 'drop', function(event) {
			element.removeAttribute('data-keypressed');
		});
	}
};

addEventHandler(document.documentElement, 'keypress', 'data-keypressadded', 'active', function(event) {
	var grabbedElement, grabbedElements, _i, _len;
	if (event.keyCode === 27) {
		grabbedElements = __exports.__aria_grabbed__elements__;
		for (_i = 0, _len = grabbedElements.length; _i < _len; _i++) {
			grabbedElement = grabbedElements[_i];
			grabbedElement.setAttribute('aria-grabbed', 'false');
			executeDragEvent('dragend', grabbedElement, event);
		}
		__exports.__aria_grabbed__elements__ = [];
		clearDropEffect();
	}
});

for (var i = 0, length = activeElements.length; i < length; i++) {
	fixActiveInElement(document.getElementById(activeElements[i]));
}

for (var i = 0, length = hoverElements.length; i < length; i++) {
	fixHoverInElement(document.getElementById(hoverElements[i]));
}

for (var i = 0, length = dragElements.length; i < length; i++) {
	fixDragInElement(document.getElementById(dragElements[i]));
}

for (var i = 0, length = dropElements.length; i < length; i++) {
	fixDropInElement(document.getElementById(dropElements[i]));
}