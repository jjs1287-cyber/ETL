/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.util.Map;

import org.etlforge.integration.server.data.DataElement;

   
                                                                                              
                                          
   
                          
                 
   
   
public class MessageToSend {

    public long msgId;

    public LocationProperties locationProps;

    public Map<String, DataElement> messageProps;

    public String payloadPath;
}
