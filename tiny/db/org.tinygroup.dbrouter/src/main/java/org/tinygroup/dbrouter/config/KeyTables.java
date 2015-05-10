package org.tinygroup.dbrouter.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("key-tables")
public class KeyTables {

	@XStreamImplicit
	private List<KeyTable> keyTableList;
	
	//二级map，第一级key是language，第二级key是className
	private Map<String,Map<String,KeyTable>> keyMap;

	public List<KeyTable> getKeyTableList() {
		if(keyTableList==null){
			keyTableList = new ArrayList<KeyTable>();	
		}
		return keyTableList;
	}

	public void setKeyTableList(List<KeyTable> keyTableList) {
		this.keyTableList = keyTableList;
	}
	
	public void init(){
		keyMap = new HashMap<String,Map<String,KeyTable>>();
		for(KeyTable table:getKeyTableList()){
			Map<String,KeyTable> classMap = keyMap.get(table.getLanguage());
			if(classMap==null){
				classMap = new HashMap<String,KeyTable>();
				keyMap.put(table.getLanguage(), classMap);
			}
			classMap.put(table.getClassName(), table);
		}
	}
	
	public KeyTable getKeyTable(String language,String className){
		if(keyMap.containsKey(language)){
		   return keyMap.get(language).get(className);
		}
		return null;
	}
	
}
