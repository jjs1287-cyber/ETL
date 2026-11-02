/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.data.DataElement;
import org.etlforge.integration.server.data.OperationDataElementManager;
import org.etlforge.integration.server.db.ServerDbInfo;
import org.etlforge.integration.server.db.msgdb.MsgDb;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.integration.server.jms.properties.JmsHeaders;
import org.etlforge.integration.server.jms.properties.JmsProperties;
import org.etlforge.integration.server.jms.properties.JmsPropertyUtils;

   
                                                                               
   
                          
                         
   
               
   
   
public class JmsOperationQueuer {

    private boolean isDebugEnabled = false;

    public JmsOperationQueuer(boolean debug) {
        isDebugEnabled = debug;
    }

       
                                                                                                 
                                                               
       
                         
                                                   
                     
                                                    
       
                           
                                 
       
    public void addTextMessageToIncomingMsgTab(long operationId, TextMessage message) throws JMSException,
                    MessagingException {
        checkMessage(message);
        JmsHeaders headers = new JmsHeaders(message);
        JmsProperties properties = new JmsProperties(message);
        String baseFileName = persistMessageToDisk(message, headers, properties);
        addToIncomingMsgTabImpl(headers, properties, operationId, baseFileName);
    }

    private String persistMessageToDisk(TextMessage message, JmsHeaders headers, JmsProperties properties)
                    throws JMSException, MessagingException {
        MessageFilePersistor persistor = new MessageFilePersistor(isDebugEnabled);
        try {
            return persistor.persist(message, headers, properties);
        } catch (ServerConfigurationException e) {
            throw new MessagingException("Server Configuration Error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingException("Failed to write payload to disk: " + e.getMessage(), e);
        }
    }

    private void addToIncomingMsgTabImpl(JmsHeaders headers, JmsProperties properties, long operationId,
                    String baseFileName) throws MessagingException {
        Connection idbConnection = null, msgDbConnection = null;
        try {
                                                                                             
            Map<String, DataElement> deMap = JmsPropertyUtils.mergeHeadersAndProperties(headers, properties,
                            "jms.header.in.", "jms.property.in.");

                                                                     
            deMap.put("__JmsBaseFileName", new DataElement(baseFileName));

            idbConnection = ServerDbInfo.getIDbConnectionInfo().getConnection();
            idbConnection.setAutoCommit(false);
            OperationDataElementManager deMgr = new OperationDataElementManager(idbConnection);
            int sessionId = deMgr.addDataElements(deMap);

                                 
            msgDbConnection = ServerDbInfo.getMsgDbConnectionInfo().getConnection();
            msgDbConnection.setAutoCommit(false);
            MsgDb msgDb = new MsgDb(msgDbConnection);
            long msgId = msgDb.insertIncomingProcessMsg(this.getClass().getName(), "Process Engine",
                            "Incoming JMS Message", "Interchange", "1", Priority.MSG_PRIORITY_NORMAL.toInt(), 1);

                                            
            msgDb.insertProcessDataElement(msgId, "__SessionID", new DataElement(sessionId));

                                              
            msgDb.insertProcessDataElement(msgId, "OperationID", new DataElement((int) operationId));

                                                     
            msgDb.flagMessageForExecution(msgId);
            idbConnection.commit();
            msgDbConnection.commit();
        } catch (SQLException e) {
            throw new MessagingException("SQL Exception occurred when inserting message into process queue: "
                            + e.getMessage(), e);
        } finally {
            KongaDbUtils.close(idbConnection);
            KongaDbUtils.close(msgDbConnection);
        }
    }

    private void checkMessage(Message message) {
        if (!(message instanceof TextMessage)) {
            throw new IllegalArgumentException("Currently, only TextMessages are supported.");
        }
    }
}
