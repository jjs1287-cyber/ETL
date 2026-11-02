/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.properties;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.etlforge.integration.server.data.DataElement;

import com.google.common.collect.Maps;

   
                                          
   
                          
   
               
   
public final class JmsHeaders {

	private Map<JmsHeader, DataElement> headers = Maps.newHashMap();
	
	public JmsHeaders() {
		                                          
	}
	
	public JmsHeaders(Message message) throws JMSException {
		setHeadersFromMessage(message);
	}
	
	public void setHeadersFromMessage(Message message) throws JMSException {
		String s = message.getJMSCorrelationID();
		if ( s != null )
			headers.put(JmsHeader.JmsCorrelationId, new DataElement(s));
		
		int i = message.getJMSDeliveryMode();
		headers.put(JmsHeader.JmsDeliveryMode, new DataElement(i));
		
		Destination dest = message.getJMSDestination();
		addDestination(JmsHeader.JmsDestination, dest);
		
		long l = message.getJMSExpiration();
		headers.put(JmsHeader.JmsExpiration, new DataElement(l));
		
		s = message.getJMSMessageID();
		headers.put(JmsHeader.JmsMessageID, new DataElement(s));
		
		i = message.getJMSPriority();
		headers.put(JmsHeader.JmsPriority, new DataElement(i));
		
		boolean b = message.getJMSRedelivered();
		headers.put(JmsHeader.JmsRedelivered, new DataElement(b));
		
		dest = message.getJMSReplyTo();
		addDestination(JmsHeader.JmsReplyTo, dest);
		
		l = message.getJMSTimestamp();
		headers.put(JmsHeader.JmsTimestamp, new DataElement(l));
		
		s = message.getJMSType();
		if ( s != null )
			headers.put(JmsHeader.JmsType, new DataElement(s));
	}
	
	public Set<JmsHeader> getDefinedHeaders() {
		return headers.keySet();
	}

	   
                                                            
                                                                        
    
                                           
                                                                                    
    
	public DataElement getHeader(JmsHeader header) {
		if ( header == null )
			return null;
		return headers.get(header);
	}

	private void addDestination(JmsHeader jmsHeader, Destination dest) {
		if ( jmsHeader.getType() != DataElement.Type.JAVA_OBJECT )
			throw new IllegalArgumentException("Only JMS headers of type serializable can be added!");
		if ( dest instanceof Serializable ) {
			Serializable serializableDest = (Serializable)dest;
			try {
				headers.put(JmsHeader.JmsDestination,
						DataElement.serializedJavaObject(serializableDest));
			} catch (IOException e) {
				                                                                     
				e.printStackTrace();
			}
		}
	}
	
}
