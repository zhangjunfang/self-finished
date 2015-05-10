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
package org.tinygroup.bundle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tinygroup.bundle.BundleException;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.config.BundleDefine;

public class BundleService {
	private BundleManager bundleManager;

	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public void addBundle(BundleDefine bundleDefine) {
		bundleManager.addBundleDefine(bundleDefine);
	}

	public void startBundle(BundleDefine bundleDefine) {
		bundleManager.start(bundleDefine);
	}

	public void stopBundle(BundleDefine bundleDefine) {
		bundleManager.stop(bundleDefine.getName());
	}

	public void removeBundle(BundleDefine bundleDefine) throws BundleException {
		bundleManager.removeBundle(bundleDefine);

	}

	public List<BundleDefine> getBundleDefines() {
		Map<String, BundleDefine> map = bundleManager.getBundleDefines();
		List<BundleDefine> list = new ArrayList<BundleDefine>();
		list.addAll(map.values());
		return list;
	}

	public boolean checkBundleStop(String bundleName) {
		return bundleManager.checkBundleStop(bundleName);
	}

}
