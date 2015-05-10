package org.tinygroup.weblayer.configmanager;


/**
 * TinyListenerConfigManager全局单实例
 * 
 * @author renhui
 * 
 */
public class TinyListenerConfigManagerHolder {

	private static TinyListenerConfigManager configManager;

	private TinyListenerConfigManagerHolder() {

	}

	public static TinyListenerConfigManager getInstance() {
		if (configManager == null) {
			configManager = new TinyListenerConfigManager();
		}
		return configManager;
	}
}
