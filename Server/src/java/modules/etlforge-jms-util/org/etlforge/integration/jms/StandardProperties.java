/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import java.util.Collection;

import org.etlforge.collections.KongaListUtils;

   
                                                                                  
      
                                     
   
                         
   
               
   
   
public final class StandardProperties {

    public static final MessageProperty USER_ID = new MessageProperty(
            "JMSXUserID", MessagePropertyType.STRING, false);

    public static final MessageProperty APP_ID = new MessageProperty(
            "JMSXAppID", MessagePropertyType.STRING, false);

    public static final MessageProperty DELIVERY_COUNT = new MessageProperty(
            "JMSXDeliveryCount", MessagePropertyType.INT, false);

    public static final MessageProperty GROUP_ID = new MessageProperty(
            "JMSXGroupID", MessagePropertyType.STRING, true);

    public static final MessageProperty GROUP_SEQ = new MessageProperty(
            "JMSXGroupSeq", MessagePropertyType.INT, true);

    public static final MessageProperty PRODUCER_TXID = new MessageProperty(
            "JMSXProducerTXID", MessagePropertyType.STRING, false);

    public static final MessageProperty CONSUMER_TXID = new MessageProperty(
            "JMSXConsumerTXID", MessagePropertyType.STRING, false);

    public static final MessageProperty RCV_TIMESTAMP = new MessageProperty(
            "JMSXRcvTimestamp", MessagePropertyType.LONG, false);

    public static final MessageProperty STATE = new MessageProperty(
            "JMSXState", MessagePropertyType.INT, false);


       
                                                     
       
       
    public static Collection<MessageProperty> getProperties() {
        return KongaListUtils.asList(
                USER_ID,
                APP_ID,
                DELIVERY_COUNT,
                GROUP_ID,
                GROUP_SEQ,
                PRODUCER_TXID,
                CONSUMER_TXID,
                RCV_TIMESTAMP,
                STATE);
    }

    
    private StandardProperties() {    }
}
