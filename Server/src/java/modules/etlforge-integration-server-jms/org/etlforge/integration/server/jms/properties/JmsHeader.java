/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.properties;

import org.etlforge.integration.server.data.DataElement;

   
                                     
   
                   
             
  
   
public enum JmsHeader {
	JmsCorrelationId("JMSCorrelationID", DataElement.Type.INT),
	JmsDeliveryMode("JMSDeliveryMode", DataElement.Type.INT),
	JmsDestination("JMSDestination", DataElement.Type.JAVA_OBJECT),
	JmsExpiration("JMSExpiration", DataElement.Type.LONG),
	JmsMessageID("JMSMessageID", DataElement.Type.STRING),
	JmsPriority("JMSPriority", DataElement.Type.INT),
	JmsRedelivered("JMSRedelivered", DataElement.Type.BOOL),
	JmsReplyTo("JMSReplyTo", DataElement.Type.JAVA_OBJECT),
	JmsTimestamp("JMSTimestamp", DataElement.Type.LONG),
	JmsType("JMSType", DataElement.Type.STRING);
	
	private String name;
	private DataElement.Type type;
	
	JmsHeader(String name, DataElement.Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public DataElement.Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return getName();
	}
}
