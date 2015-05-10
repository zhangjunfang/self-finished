package org.tinygroup.cepcoreremoteimpl.test.testcase.counter;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.threadgroup.AbstractProcessor;

public class CounterDealProcessor extends AbstractProcessor {
	int count;
	CEPCore cep;
	public CounterDealProcessor(CEPCore cep, String name) {
		super(name);
		this.cep = cep;
	}

	protected void action() throws Exception {
		while(true){
			Event t = getEvent();
			cep.process(t);
		}
	}
	
	public static Event getEvent() {
		Context c = new ContextImpl();
		c.put("name", "name");
		Event e = Event.createEvent("add", c);
		return e;
	}

}
