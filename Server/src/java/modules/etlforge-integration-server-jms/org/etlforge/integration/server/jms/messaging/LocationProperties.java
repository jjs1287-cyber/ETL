/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.util.Map;
import java.util.Set;

import javax.naming.Context;

import com.google.common.collect.Maps;

   
                          
   
               
  
   
public final class LocationProperties {
    
	public enum LocationPropertyKeys {
		CONNECTION_FACTORY("Jms.ConnectionFactoryJndiName"),
		INITIAL_CONTEXT_FACTORY("Jndi."+Context.INITIAL_CONTEXT_FACTORY), 
		JNDI_URL("Jndi."+Context.PROVIDER_URL), 
		CONNECTION_USER_NAME("Jms.Connection.UserName"),
		CONNECTION_PASSWORD("Jms.Connection.Password"),
		DESTINATION_NAME("Jms.DestinationPath"),
		MESSAGE_SELECTOR("Jms.MessageSelector");
		private final String name;
		
		private LocationPropertyKeys(String name) { 
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	} 
	private Map<String, String> propMap;
	
	public LocationProperties() {
		propMap = Maps.newHashMap();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String,String> keyValue : propMap.entrySet()) {
			sb.append( "\t'" + keyValue.getKey() + "' = '" + keyValue.getValue() + "'\n" );
		}
		return sb.toString();
	}
	
	public Set<Map.Entry<String, String>> entrySet() {
		return propMap.entrySet();
	}
	
	public LocationProperties(Map<String, String> locationPropertyMap) {
		propMap = locationPropertyMap;
	}
	
	public void addProperty(String key, String value) {
		propMap.put(key, value);
	}
	
	public String getProperty(String key) {
		return propMap.get(key);
	}
	
	public String getConnectionFactory() {
		return propMap.get(LocationPropertyKeys.CONNECTION_FACTORY.toString());
	}
	
	public String getInitialContextFactory() {
		return propMap.get(LocationPropertyKeys.INITIAL_CONTEXT_FACTORY.toString());
	}
	
	public String getJndiUrl() {
		return propMap.get(LocationPropertyKeys.JNDI_URL.toString());
	}
	
	public String getConnectionUserName() {
		return propMap.get(LocationPropertyKeys.CONNECTION_USER_NAME.toString());
	}	
	
	public String getConnectionPassword() {
		return propMap.get(LocationPropertyKeys.CONNECTION_PASSWORD.toString());
	}	
	
	public String getDestinationName() {
		return propMap.get(LocationPropertyKeys.DESTINATION_NAME.toString());
	}

	public String getMessageSelector() {
		return propMap.get(LocationPropertyKeys.MESSAGE_SELECTOR.toString());
	}
	
}
