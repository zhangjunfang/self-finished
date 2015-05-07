package com.restbird.server.httpserver.netty;

import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Request {

	private QueryStringDecoder decoderQuery;

	// private boolean requestPostFl;
	/**
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		Map<String, List<String>> params = decoderQuery.parameters();
		for (Entry<String, List<String>> entry : params.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();
			for (String value : values) {
				if (key.equals(name)) {
					return value;
				}
			}
		}
		return null;
	}

	protected QueryStringDecoder getDecoderQuery() {
		return decoderQuery;
	}

	protected void setDecoderQuery(QueryStringDecoder decoderQuery) {
		this.decoderQuery = decoderQuery;
	}

}
