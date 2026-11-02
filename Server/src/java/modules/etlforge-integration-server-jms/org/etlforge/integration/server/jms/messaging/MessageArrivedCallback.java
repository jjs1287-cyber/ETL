/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import javax.jms.JMSException;
import javax.jms.TextMessage;

   
                 
  
   
public interface MessageArrivedCallback {
	public void messageArrived(TextMessage m) throws MessagingException, JMSException;
}
