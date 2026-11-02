/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.properties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.etlforge.integration.server.data.DataElement;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.XmlStringBuilder;

import com.google.common.collect.Maps;

   
                                                               
   
                          
               
  
   
public final class JmsPropertyUtils {
	
	private static final String ROOT = "JmsMessage";
	private static final String HEADERS = "Headers";
	private static final String PROPERTIES = "Properties";
	private static final String HEADER = "Header";
	private static final String PROPERTY = "Property";
	private static final String TYPE = "Type";
	private static final String NAME = "Name";
	
	public static final Map<String, DataElement> mergeHeadersAndProperties(
			JmsHeaders headers, JmsProperties properties,
			String headerPrefix, String propertyPrefix) {
		Map<String, DataElement> deMap = Maps.newHashMap();
		Set<JmsHeader> headerSet = headers.getDefinedHeaders();
		for ( JmsHeader header : headerSet ) {
			DataElement de = headers.getHeader(header);
			deMap.put(headerPrefix+header.toString(), de);
		}
		
		Set<String> propertySet = properties.getPropertyNames();
		for ( String name : propertySet ) {
			DataElement de = properties.getProperty(name);
			deMap.put(propertyPrefix+name, de);
		}
		return deMap;
	}
	
	   
                                                                        
    
                             
                                    
                                                 
    
	public static final void writeAsXml(JmsHeaders jmsHeaders, JmsProperties jmsProperties, String fileName) throws IOException {
		File file = new File(fileName);
		                                                                                           
		                         
			                                                                                                                  

		XmlStringBuilder builder = new XmlStringBuilder();
		builder.addElement(ROOT);

		builder.addElement(HEADERS);
		Set<JmsHeader> headers = jmsHeaders.getDefinedHeaders();
		for ( JmsHeader header : headers ) {
			DataElement de = jmsHeaders.getHeader(header);
			builder.addElement(HEADER);
			builder.addAttribute(TYPE, de.getType().toString());
			builder.addAttribute(NAME, header.getName());
			builder.setElementValue(de.toString());
			builder.closeElement();
		}
		builder.closeElement();

		builder.addElement(PROPERTIES);
		Set<String> properties = jmsProperties.getPropertyNames();
		for (String name : properties) {
			DataElement de = jmsProperties.getProperty(name);
			builder.addElement(PROPERTY);
			builder.addAttribute(TYPE, de.getType().toString());
			builder.addAttribute(NAME, name);
			builder.setElementValue(de.toString());
			builder.closeElement();
		}
		builder.closeAllElements();
		
		FileWriter fileWriter = null; 
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(builder.getXml());
		}
		finally {
			KongaIoUtils.close(fileWriter);
		}
	}
}
