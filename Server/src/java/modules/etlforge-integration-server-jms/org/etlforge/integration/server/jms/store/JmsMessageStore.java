/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.store;

import java.util.Map;

import org.etlforge.integration.server.data.DataElement;
import org.etlforge.integration.server.db.msgdb.MsgDb.SendingProgressStatus;
import org.etlforge.integration.server.jms.JmsServerException;
import org.etlforge.integration.server.jms.messaging.LocationProperties;
import org.etlforge.integration.server.jms.messaging.MessageToSend;

   
   
                          
                         
   
             
   
public interface JmsMessageStore {

    public void setJmsMessageSentStatus(long jmsMsgId, SendingProgressStatus status) throws JmsServerException;

       
                                                         
       
                      
                                                      
                                 
                                                                 
       
    public void archiveJmsMessage(long jmsMsgId) throws JmsServerException;

    public LocationProperties getLocationProperties(long msgId) throws JmsServerException;
    
    public Map<String, DataElement> getMessageProperties(long id) throws JmsServerException;

    public MessageToSend getMessageToSend() throws JmsServerException;
    
}
