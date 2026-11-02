/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.store;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.jms.JmsServerException;
import org.etlforge.integration.server.jms.messaging.LocationProperties;

import com.google.common.collect.Maps;

   
  
                          
                         
  
               
   
public final class JmsLocationInfoDbStore implements JmsLocationInfoStore {

    private final Connection tranDbConn;

    public JmsLocationInfoDbStore(Connection tranDbConn) {
        checkNotNull(tranDbConn, "tranDbConn");
        this.tranDbConn = tranDbConn;
    }

    @Override
    public Map<Long, LocationProperties> getJmsLocationInfo() throws JmsServerException {
        Map<Long, LocationProperties> opToPropsMap = Maps.newHashMap();

                                                                                     
        String sql = "SELECT OperationsTab.Operation_ID, SourcesTab.jms_message_selector, "
                        + "JmsLocationPropertiesTab.KeyName, JmsLocationPropertiesTab.Value "
                        + "FROM OperationsTab "
                        + "JOIN SourcesTab ON SourcesTab.Source_ID = OperationsTab.Source_ID "
                        + "JOIN JmsLocationPropertiesTab ON SourcesTab.Source_ID = JmsLocationPropertiesTab.JmsLocationId "
                        + "WHERE SourcesTab.Source_Type_ID = 13 " + "ORDER BY Operation_ID";
        Statement stmt = null;
        ResultSet rows = null;
        try {
            stmt = tranDbConn.createStatement();
            rows = stmt.executeQuery(sql);
            long opId = -1;
            while (rows.next()) {
                opId = rows.getInt(1);
                LocationProperties locationProperties = opToPropsMap.get(opId);
                if (locationProperties == null) {
                    Map<String, String> props = Maps.newHashMap();
                    String messageSelector = rows.getString(2);
                    if (messageSelector != null) {
                        props.put("Jms.MessageSelector", messageSelector);
                    }
                    locationProperties = new LocationProperties(props);
                    opToPropsMap.put(opId, locationProperties);
                }
                locationProperties.addProperty(rows.getString(3), rows.getString(4));
            }
        } catch (SQLException ex) {
            throw new JmsServerException("Failed to retrieve JMS location info", ex);
        } finally {
            KongaDbUtils.close(rows);
            KongaDbUtils.close(stmt);
        }
        return opToPropsMap;
    }

}
