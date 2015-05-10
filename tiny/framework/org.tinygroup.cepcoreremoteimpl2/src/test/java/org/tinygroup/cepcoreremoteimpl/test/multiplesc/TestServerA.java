package org.tinygroup.cepcoreremoteimpl.test.multiplesc;
import java.util.ArrayList;

import org.tinygroup.tinyrunner.Runner;

public class TestServerA {
	public static void main(String[] args) throws Exception {
		Runner.init("applicationserver.xml", new ArrayList<String>());
		
	}
}
