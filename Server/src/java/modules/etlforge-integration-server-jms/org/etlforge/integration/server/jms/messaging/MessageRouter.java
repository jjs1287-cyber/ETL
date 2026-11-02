/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.etlforge.db.DBConnectionInfo;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.db.ServerDbInfo;
import org.etlforge.integration.server.db.msgdb.MsgDb;
import org.etlforge.integration.server.jms.JmsServerException;
import org.etlforge.integration.server.jms.store.JmsLocationInfoDbStore;
import org.etlforge.integration.server.jms.store.JmsLocationInfoStore;
import org.etlforge.integration.server.jms.store.JmsMessageDbStore;
import org.etlforge.integration.server.jms.store.JmsMessageStore;
import org.etlforge.util.kongaprotocol.KongaModuleStatusBitFields;

   
                                                                                              
                                                                                                  
                              
   
                          
                          
   
               
   
public class MessageRouter {

    private static final ExecutorService cachedThreadExecutor = Executors.newCachedThreadPool();

    private final JmsOperationQueuer opJobHandler;

    private final List<Listener> listeners = new ArrayList<Listener>();

    private Timer sendingTimer;

    private TaskCheckForOutgoing outgoingTimerTask;

    private volatile int currentStatus = KongaModuleStatusBitFields.rsSTOPPED;

    public MessageRouter(JmsOperationQueuer opJobHandler) {
        this.opJobHandler = opJobHandler;
    }

       
                                                                 
       
       
    private void createListeners() throws JmsServerException {
        Connection conn = null;
        try {
            conn = ServerDbInfo.getTranDbConnectionInfo().getConnection(
                            DBConnectionInfo.SUGGESTED_TIMEOUT_FOR_GET_CONNECTION);
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to get a connection to the backend database", ex);
        }
        JmsLocationInfoStore infoStore = new JmsLocationInfoDbStore(conn);
        Map<Long, LocationProperties> opToLocationPropsMap = infoStore.getJmsLocationInfo();
        for (Entry<Long, LocationProperties> entry : opToLocationPropsMap.entrySet()) {
            Listener listener = new Listener(entry.getValue());
            listeners.add(listener);
            OperationMessageHandler opMsgHandler = new OperationMessageHandler(opJobHandler, entry.getKey());
            cachedThreadExecutor.submit(new RunnableListener(listener, opMsgHandler));
        }
    }

       
                                                                                                    
                                      
       
                              
       
    public long getStatus() {
        return currentStatus;
    }

    public synchronized void start() throws JmsServerException {
        currentStatus = KongaModuleStatusBitFields.rsSTARTING;
        createListeners();
        startOutgoingTimer();
        currentStatus = KongaModuleStatusBitFields.rsRUNNING;
    }

    public synchronized void stop() {
        currentStatus = KongaModuleStatusBitFields.rsSTOPPING;
        stopListeners();
        stopOutgoingTimer();
        currentStatus = KongaModuleStatusBitFields.rsSTOPPED;
    }

    public synchronized void update() throws JmsServerException {
        stopListeners();
        createListeners();
    }

    public synchronized void checkOutgoing() {
        if (outgoingTimerTask != null)
            outgoingTimerTask.run();
    }

    private void stopListeners() {
        for (Listener listener : listeners) {
            listener.stopListening();
        }
    }

    private void startOutgoingTimer() {
        final long delayMilliseconds = 0;                    
        final long periodMilliseconds = 5000;                        

                                                                 
        if (outgoingTimerTask != null) {
                                                                          
                           
            outgoingTimerTask.shouldStop();
            outgoingTimerTask = null;
        }

        if (sendingTimer != null) {
                                                                  
            sendingTimer.cancel();
            sendingTimer = null;
        }

        outgoingTimerTask = new TaskCheckForOutgoing();
        sendingTimer = new Timer();
        sendingTimer.schedule(outgoingTimerTask, delayMilliseconds, periodMilliseconds);
    }

    private void stopOutgoingTimer() {
        if (outgoingTimerTask == null || sendingTimer == null) return;
        outgoingTimerTask.shouldStop();                                                           
        
        sendingTimer.cancel();
        sendingTimer = null;
        outgoingTimerTask = null;
    }


    static class OperationMessageHandler implements MessageArrivedCallback {

        long opId;                                                             

        JmsOperationQueuer jobHandler = null;

        OperationMessageHandler(JmsOperationQueuer opJobHandler, long operationId) {
            opId = operationId;
            jobHandler = opJobHandler;
        }

        @Override
        public void messageArrived(TextMessage m) throws MessagingException, JMSException {
            jobHandler.addTextMessageToIncomingMsgTab(opId, m);
        }
    }


    private static class TaskCheckForOutgoing extends TimerTask {

        private Connection msgDbConn;

        private MsgDb msgDb;

        private JmsMessageStore messageStore;

        private Sender sender;

        private volatile boolean shouldStop;

        public TaskCheckForOutgoing() {
            try {
                msgDbConn = ServerDbInfo.getMsgDbConnectionInfo().getConnection();
                msgDb = new MsgDb(msgDbConn);
                messageStore = new JmsMessageDbStore(msgDb);
                sender = new Sender();
            } catch (SQLException e) {
                                                                                       
                e.printStackTrace();
            }
        }

        public void shouldStop() {
            shouldStop = true;
        }

                                                                          
                                                     
        @Override
        public synchronized void run() {
            try {
                if (shouldStop)
                    return;
                if (msgDb == null) {
                    throw new IllegalStateException("The database connection has been closed!");
                }

                                                                              
                                                
                MessageToSend msgToSend;
                do {
                    msgToSend = messageStore.getMessageToSend();
                    if (msgToSend != null) {
                        try {
                            sender.sendMessage(msgToSend);
                            messageStore.setJmsMessageSentStatus(msgToSend.msgId, MsgDb.SendingProgressStatus.SENT);
                        } catch (JmsServerException e) {
                            messageStore.setJmsMessageSentStatus(msgToSend.msgId, MsgDb.SendingProgressStatus.ERROR);
                            throw e;
                        }
                    }
                } while (msgToSend != null && !shouldStop);

            } catch (JmsServerException e) {
                                                                                       
                e.printStackTrace();
            } finally {
                if (shouldStop) {
                                                                          
                    KongaDbUtils.close(msgDbConn);
                    this.msgDbConn = null;
                    this.msgDb = null;
                    this.sender = null;
                }
            }
        }
    }
    
}
