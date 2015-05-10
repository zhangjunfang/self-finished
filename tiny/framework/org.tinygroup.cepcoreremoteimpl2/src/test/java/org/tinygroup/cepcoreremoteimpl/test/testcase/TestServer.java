package org.tinygroup.cepcoreremoteimpl.test.testcase;
import java.util.ArrayList;

import org.tinygroup.tinyrunner.Runner;

public class TestServer {
	public static void main(String[] args) throws Exception {
		Runner.init("applicationserver.xml", new ArrayList<String>());
		
	}
}
