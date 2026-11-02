/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.etlforge.integration.server.data.DataElement;
import org.etlforge.integration.server.jms.properties.JmsHeader;
import org.etlforge.io.KongaIoUtils;

   
                      
   
                          
   
               
   
public class Sender {
	public void sendMessage(MessageToSend msgToSend) {
		Session session = null;
		Connection connection = null;
        MessageProducer producer = null;
		try {
			InitialDirContext ctx = LocationFactory.getInitialContextFactory(msgToSend.locationProps);
			connection = LocationFactory.getConnection(msgToSend.locationProps, ctx);
			session = LocationFactory.getSession(connection);
			
			Destination dest = (Destination) ctx.lookup(msgToSend.locationProps.getDestinationName());
		    producer = session.createProducer(dest);
	
			TextMessage msg = session.createTextMessage();
			String s = KongaIoUtils.read(new File(msgToSend.payloadPath));
			msg.setText(s);
			
			                                                        
			int deliveryMode = producer.getDeliveryMode();
			int priority = producer.getPriority();
			long timeToLive = producer.getTimeToLive();
			SpecialJmsHeaders specialHeaders =
				addPropertiesToMessage(msg, msgToSend.messageProps, deliveryMode, priority, timeToLive);
			
			deliveryMode = specialHeaders.getDeliveryMode();
			priority = specialHeaders.getPriority();
			timeToLive = specialHeaders.getTimeToLiveMs();
			
			producer.send(msg, deliveryMode, priority, timeToLive);
			session.commit();

            System.out.println();
			System.out.println("Sent Message:");
			System.out.println(s);
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
			Exception le = e.getLinkedException();
			if (le != null) {
				System.out.println("Linked exception: " + le);
				le.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
            Closer.close(producer);
            Closer.close(session);
            Closer.close(connection);
		}
	}
	
	private static class SpecialJmsHeaders {
		private int deliveryMode;
		private int priority;
		private long timeToLive;
		
		SpecialJmsHeaders(int deliveryMode, int priority, long timeToLive) {
			this.deliveryMode = deliveryMode;
			this.priority = priority;
			this.timeToLive = timeToLive;
		}
		
		int getDeliveryMode() {
			return deliveryMode;
		}
		
		int getPriority() {
			return priority;
		}
		
		long getTimeToLiveMs() {
			return timeToLive;
		}
	}
	
	   
                                                                                                      
                                                                                                   
                                                                                             
                                                                      
       
                                                                                              
                           
       
	public SpecialJmsHeaders addPropertiesToMessage(TextMessage msg, Map<String, DataElement> props,
			int defaultDeliveryMode, int defaultPriority, long defaultTimeToLive) throws JMSException {
		int deliveryMode = defaultDeliveryMode;
		int priority = defaultPriority;
		long timeToLive = defaultTimeToLive;
		
		for(Map.Entry<String, DataElement> entry : props.entrySet() ) {
			String propName = entry.getKey();
			DataElement value = entry.getValue();
			final String headerPrefix = "jms.header.out.";
			final String propertyPrefix = "jms.property.out.";
			if( propName.startsWith(headerPrefix) ) {
				String headerName = propName.substring(headerPrefix.length());
				if ( headerName.equalsIgnoreCase(JmsHeader.JmsDeliveryMode.getName()) ) {
					deliveryMode = value.toInt();
					if ( deliveryMode != DeliveryMode.NON_PERSISTENT && deliveryMode != DeliveryMode.PERSISTENT)
						deliveryMode = defaultDeliveryMode;
				}
				else if ( headerName.equalsIgnoreCase(JmsHeader.JmsPriority.getName())) {
					priority = value.toInt();
					if ( priority < 0 || priority > 9 )
						priority = defaultPriority;
				}
				else if ( headerName.equalsIgnoreCase("JMSTimeToLive") ) {
					timeToLive = value.toLong();
					if ( timeToLive < 0 )
						timeToLive = defaultTimeToLive;
				}
			}
			else if( propName.startsWith(propertyPrefix) ) {
				propName = propName.substring(propertyPrefix.length());
				switch( value.getType() ) {
				case DATE: msg.setLongProperty(propName, ((Date)value.getValue()).getTime()); break;
				default: msg.setObjectProperty(propName, value.getValue());
				}	
			}
		}
		
		return new SpecialJmsHeaders(deliveryMode, priority, timeToLive);
	}
}
