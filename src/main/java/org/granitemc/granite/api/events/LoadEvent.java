package org.granitemc.granite.api.events;

import org.granitemc.granite.events.Event;

public class LoadEvent extends Event {

	public LoadEvent(Object oCause) {
		super(oCause, "plugin_load");
	}

}
