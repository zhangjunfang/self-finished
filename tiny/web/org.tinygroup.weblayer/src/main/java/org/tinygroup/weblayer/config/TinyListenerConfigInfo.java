package org.tinygroup.weblayer.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("tiny-listener")
public class TinyListenerConfigInfo {
    @XStreamImplicit
	private List<ServletContextListenerConfig> servletContextListenerConfigs;
    
    @XStreamImplicit
	private List<ServletContextAttributeListenerConfig> servletContextAttributeListenerConfigs;
    
    @XStreamImplicit
	private List<SessionListenerConfig> sessionListenerConfigs;

	@XStreamImplicit
	private List<SessionBindingListenerConfig> sessionBindingListenerConfigs;

	@XStreamImplicit
	private List<SessionAttributeListenerConfig> sessionAttributeListenerConfigs;

	@XStreamImplicit
	private List<SessionActivationListenerConfig> sessionActivationListenerConfigs;

	@XStreamImplicit
	private List<ServletRequestListenerConfig> servletRequestListenerConfigs;

	@XStreamImplicit
	private List<ServletRequestAttributeListenerConfig> servletRequestAttributeListenerConfigs;

	public List<ServletContextListenerConfig> getServletContextListenerConfigs() {
		if (servletContextListenerConfigs == null) {
			servletContextListenerConfigs = new ArrayList<ServletContextListenerConfig>();
		}
		return servletContextListenerConfigs;
	}

	public void setServletContextListenerConfigs(
			List<ServletContextListenerConfig> servletContextListenerConfigs) {
		this.servletContextListenerConfigs = servletContextListenerConfigs;
	}

	public List<ServletContextAttributeListenerConfig> getServletContextAttributeListenerConfigs() {
		if (servletContextAttributeListenerConfigs == null) {
			servletContextAttributeListenerConfigs = new ArrayList<ServletContextAttributeListenerConfig>();
		}
		return servletContextAttributeListenerConfigs;
	}

	public void setServletContextAttributeListenerConfigs(
			List<ServletContextAttributeListenerConfig> servletContextAttributeListenerConfigs) {
		this.servletContextAttributeListenerConfigs = servletContextAttributeListenerConfigs;
	}

	public List<SessionListenerConfig> getSessionListenerConfigs() {
		if (sessionListenerConfigs == null) {
			sessionListenerConfigs = new ArrayList<SessionListenerConfig>();
		}
		return sessionListenerConfigs;
	}

	public void setSessionListenerConfigs(
			List<SessionListenerConfig> sessionListenerConfigs) {
		this.sessionListenerConfigs = sessionListenerConfigs;
	}

	public List<SessionBindingListenerConfig> getSessionBindingListenerConfigs() {
		if (sessionBindingListenerConfigs == null) {
			sessionBindingListenerConfigs = new ArrayList<SessionBindingListenerConfig>();
		}
		return sessionBindingListenerConfigs;
	}

	public void setSessionBindingListenerConfigs(
			List<SessionBindingListenerConfig> sessionBindingListenerConfigs) {
		this.sessionBindingListenerConfigs = sessionBindingListenerConfigs;
	}

	public List<SessionAttributeListenerConfig> getSessionAttributeListenerConfigs() {
		if (sessionAttributeListenerConfigs == null) {
			sessionAttributeListenerConfigs = new ArrayList<SessionAttributeListenerConfig>();
		}
		return sessionAttributeListenerConfigs;
	}

	public void setSessionAttributeListenerConfigs(
			List<SessionAttributeListenerConfig> sessionAttributeListenerConfigs) {
		this.sessionAttributeListenerConfigs = sessionAttributeListenerConfigs;
	}

	public List<SessionActivationListenerConfig> getSessionActivationListenerConfigs() {
		if (sessionActivationListenerConfigs == null) {
			sessionActivationListenerConfigs = new ArrayList<SessionActivationListenerConfig>();
		}
		return sessionActivationListenerConfigs;
	}

	public void setSessionActivationListenerConfigs(
			List<SessionActivationListenerConfig> sessionActivationListenerConfigs) {
		this.sessionActivationListenerConfigs = sessionActivationListenerConfigs;
	}

	public List<ServletRequestListenerConfig> getServletRequestListenerConfigs() {
		if (servletRequestListenerConfigs == null) {
			servletRequestListenerConfigs = new ArrayList<ServletRequestListenerConfig>();
		}
		return servletRequestListenerConfigs;
	}

	public void setServletRequestListenerConfigs(
			List<ServletRequestListenerConfig> servletRequestListenerConfigs) {
		this.servletRequestListenerConfigs = servletRequestListenerConfigs;
	}

	public List<ServletRequestAttributeListenerConfig> getServletRequestAttributeListenerConfigs() {
		if (servletRequestAttributeListenerConfigs == null) {
			servletRequestAttributeListenerConfigs = new ArrayList<ServletRequestAttributeListenerConfig>();
		}
		return servletRequestAttributeListenerConfigs;
	}

	public void setServletRequestAttributeListenerConfigs(
			List<ServletRequestAttributeListenerConfig> servletRequestAttributeListenerConfigs) {
		this.servletRequestAttributeListenerConfigs = servletRequestAttributeListenerConfigs;
	}
    
}
