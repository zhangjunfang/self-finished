/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.tinypc.test.foreman;

import java.io.IOException;

import org.tinygroup.tinypc.Foreman;
import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.TestUtil;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.ForemanSelectAllWorker;
import org.tinygroup.tinypc.impl.JobCenterRemote;

public class TestSelectAll {
	public static void main(String[] args) {
		try {
			JobCenter jobCenter = new JobCenterRemote(TestUtil.CIP,TestUtil.CP,TestUtil.SIP,TestUtil.SP);
			Work work  = new WorkTask("a","aaa","");
			System.out.println(jobCenter.getRmiServer().getObject("Worker|a|8d4c1d5a4d2a49659631d8c07e0eb191"));
			Foreman f = new ForemanSelectAllWorker("a");
			jobCenter.registerForeman(f);
			jobCenter.doWork(work);
			jobCenter.unregisterForeMan(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
