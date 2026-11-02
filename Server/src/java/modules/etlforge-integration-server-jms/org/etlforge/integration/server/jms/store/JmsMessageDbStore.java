/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.store;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.data.DataElement;
import org.etlforge.integration.server.db.msgdb.MsgDb;
import org.etlforge.integration.server.db.msgdb.MsgDb.SendingProgressStatus;
import org.etlforge.integration.server.jms.JmsServerException;
import org.etlforge.integration.server.jms.messaging.LocationProperties;
import org.etlforge.integration.server.jms.messaging.MessageToSend;

import com.google.common.collect.Maps;

   
   
                          
                         
   
               
   
public final class JmsMessageDbStore implements JmsMessageStore {

    private final MsgDb msgDb;

    public JmsMessageDbStore(MsgDb msgDb) {
        checkNotNull(msgDb, "msgDb");
        this.msgDb = msgDb;
    }

    @Override
    public void setJmsMessageSentStatus(long jmsMsgId, SendingProgressStatus status) throws JmsServerException {
        Statement stmt = null;
        try {
            stmt = msgDb.createStatement();
            stmt.execute("UPDATE OutgoingJmsMsgTab SET SendingProgress = " + status.toInt()
                            + " WHERE JmsMsgInstanceId = " + Long.toString(jmsMsgId));
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to update message sent status on the message with ID " + jmsMsgId, ex);
        } finally {
            KongaDbUtils.close(stmt);
        }
    }

       
                                                                                                  
                                                   
       
                      
                                                    
                                 
                                                                 
       
    @Override
    public void archiveJmsMessage(long jmsMsgId) throws JmsServerException {
        Statement stmt = null;
        ResultSet rows = null;
        String sql = "SELECT JmsPropertiesId FROM OutgoingJmsMsgTab WHERE JmsMessage_ID = " + Long.toString(jmsMsgId);
        try {
            stmt = msgDb.createStatement();
            rows = stmt.executeQuery(sql);
            if (rows.next()) {
                int jmsPropertiesId = rows.getInt(1);
                if (jmsPropertiesId > 0) {
                    sql = "INSERT INTO ArchiveJmsPropertiesTab SELECT * FROM JmsPropertiesTab WHERE JmsProperiesId = "
                                    + Long.toString(jmsPropertiesId)
                                    + "; DELETE FROM JmsPropertiesTab WHERE JmsProperiesId = "
                                    + Long.toString(jmsPropertiesId);
                    stmt.execute(sql);
                }
            }

            sql = "INSERT INTO ArchiveOutgoingJmsMsgTab SELECT * FROM OutgoingJmsMsgTab WHERE JmsMessage_ID = "
                            + Long.toString(jmsMsgId) + "; DELETE FROM OutgoingJmsMsgTab WHERE JmsMessage_ID = "
                            + Long.toString(jmsMsgId);
            stmt.execute(sql);

            sql = "INSERT INTO ArchiveJmsSendingPropertiesTab SELECT * FROM JmsSendingPropertiesTab WHERE JmsMessage_ID = "
                            + Long.toString(jmsMsgId)
                            + "; DELETE FROM JmsSendingPropertiesTab WHERE JmsMessage_ID = "
                            + Long.toString(jmsMsgId);
            stmt.execute(sql);
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to archive the JMS message with id " + jmsMsgId, ex);
        } finally {
            KongaDbUtils.close(rows);
            KongaDbUtils.close(stmt);
        }
    }

    @Override
    public LocationProperties getLocationProperties(long msgId) throws JmsServerException {
        final String sql = "SELECT SendingPropertyKey, SendingPropertyValue " + "FROM JmsSendingPropertiesTab "
                        + "WHERE JmsMsgInstanceId = " + Long.toString(msgId);
        Statement stmt = null;
        ResultSet rows = null;
        LocationProperties locationProps = new LocationProperties();
        try {
            stmt = msgDb.createStatement();
            rows = stmt.executeQuery(sql);
            while (rows.next()) {
                locationProps.addProperty(rows.getString(1), rows.getString(2));
            }
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to get the location properties for the JMS message with id " + msgId,
                            ex);
        } finally {
            KongaDbUtils.close(rows);
            KongaDbUtils.close(stmt);
        }
        return locationProps;
    }

    @Override
    public Map<String, DataElement> getMessageProperties(long id) throws JmsServerException {
        final String sql = "SELECT JmsPropertyName, JmsPropertyType, JmsPropertyValue " + "FROM JmsPropertiesTab "
                        + "WHERE JmsProperiesId = " + Long.toString(id);
        Statement stmt = null;
        ResultSet rows = null;
        Map<String, DataElement> messageProps = Maps.newHashMap();
        try {
            stmt = msgDb.createStatement();
            rows = stmt.executeQuery(sql);
            while (rows.next()) {
                DataElement de = new DataElement(DataElement.Type.fromString(rows.getString(2)), rows.getString(3));
                messageProps.put(rows.getString(1), de);
            }
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to read the properties for the message with id " + id, ex);
        } finally {
            KongaDbUtils.close(rows);
            KongaDbUtils.close(stmt);
        }
        return messageProps;
    }

    @Override
    public MessageToSend getMessageToSend() throws JmsServerException {
        try {
            final String sql = "SELECT OutgoingJmsMsgTab.JmsMsgInstanceId, OutgoingJmsMsgTab.JmsPropertiesId, "
                            + "OutgoingJmsMsgTab.JmsPayloadPath " + "FROM OutgoingJmsMsgTab "
                            + "WHERE OutgoingJmsMsgTab.SendingProgress = "
                            + Long.toString(SendingProgressStatus.NOT_SENT.toInt());
            Statement stmt = null;
            ResultSet rows = null;
            MessageToSend msg = new MessageToSend();
            long propertiesId;
            String payloadPath;

            try {
                msgDb.setAutoCommit(false);
                stmt = msgDb.createStatement();
                rows = stmt.executeQuery(sql);
                if (!rows.next())
                    return null;
                msg.msgId = rows.getInt(1);
                propertiesId = rows.getInt(2);
                payloadPath = rows.getString(3);
            } finally {
                KongaDbUtils.close(rows);
                KongaDbUtils.close(stmt);
            }

            setJmsMessageSentStatus(msg.msgId, SendingProgressStatus.SENDING);
            msgDb.commit();
            msgDb.setAutoCommit(true);

            msg.locationProps = getLocationProperties(msg.msgId);
            msg.messageProps = getMessageProperties(propertiesId);
            msg.payloadPath = payloadPath;
            return msg;
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to retrieve the message to send", ex);
        }
    }
    
}
