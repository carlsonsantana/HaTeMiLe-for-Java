if (!Element.prototype.eventListenerList) {
	Element.prototype.eventListenerList = {};
	Element.prototype.__eventListenerListAdded = false;

	Element.prototype.__addEventListener = Element.prototype.addEventListener;
	Element.prototype.addEventListener = function() {
		if (!this.__eventListenerListAdded) {
			this.eventListenerList = {};
			this.__eventListenerListAdded = true;
		}
		if (!this.eventListenerList[arguments[0]]) {
			this.eventListenerList[arguments[0]] = [];
		}
		this.eventListenerList[arguments[0]].push(arguments[1]);
		return (this.__addEventListener.apply(this, arguments));
	};

	Element.prototype.__removeEventListener = Element.prototype.removeEventListener;
	Element.prototype.removeEventListener = function() {
		var found = false;
		if (!this.eventListenerList[arguments[0]]) {
			this.eventListenerList[arguments[0]] = [];
		}
		var key;
		for (key in this.eventListenerList[arguments[0]]) {
			found = this.eventListenerList[arguments[0]][key] === arguments[1];
			if (found) {
				break;
			}
		}
		if (found) {
			this.eventListenerList[arguments[0]].splice(key, 1);
		}
		return (this.__removeEventListener.apply(this, arguments));
	};
}