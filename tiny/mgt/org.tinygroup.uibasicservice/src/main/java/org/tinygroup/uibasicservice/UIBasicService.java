package org.tinygroup.uibasicservice;

import java.util.List;

import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;

public class UIBasicService {
	private UIComponentManager uiManager;

	public UIComponentManager getUiManager() {
		return uiManager;
	}

	public void setUiManager(UIComponentManager uiManager) {
		this.uiManager = uiManager;
	}

	public List<UIComponent> getUiComponents() {
		return uiManager.getUiComponents();
	}

	public UIComponent getUIComponent(String name) {
		return uiManager.getUIComponent(name);
	}

	public List<UIComponent> getHealthUiComponents() {
		return uiManager.getHealthUiComponents();
	}

	public String[] getComponentJsArray(String name) {
		return uiManager.getComponentJsArray(getUIComponent(name));
	}

	public String[] getComponentCssArray(String name) {
		return uiManager.getComponentCssArray(getUIComponent(name));
	}
}
