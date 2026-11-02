/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.integration.server.jms.properties.JmsHeaders;
import org.etlforge.integration.server.jms.properties.JmsProperties;
import org.etlforge.integration.server.jms.properties.JmsPropertyUtils;
import org.etlforge.io.KongaIoUtils;

   
                                            
   
                          
                         
   
               
   
public class MessageFilePersistor {

    private final boolean isDebugEnabled;
    
    public MessageFilePersistor(boolean isDebugEnabled) {
        this.isDebugEnabled = isDebugEnabled;
    }
    
    public String persist(TextMessage message, JmsHeaders headers, JmsProperties properties) 
                                                throws JMSException, ServerConfigurationException, IOException {
        String baseFileName = createBaseFileName();
        if (isDebugEnabled) {
            writeDebugInfo(message, headers, properties, baseFileName);
        }
        writeMessagePayload(message, baseFileName);
        return baseFileName;
    }
    
    private void writeDebugInfo(Message msg, JmsHeaders headers, JmsProperties properties, String baseFileName) {
        serializeMessage(msg, baseFileName);
        writeMessageProperties(headers, properties, baseFileName);
    }
    
    private void serializeMessage(Message msg, String baseFileName) {
        if (msg instanceof Serializable) {
                                                                 
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(new File(baseFileName + ".jms")));
                oos.writeObject(msg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                KongaIoUtils.close(oos);
            }
        }
    }
    
    private void writeMessageProperties(JmsHeaders headers, JmsProperties properties, String baseFileName) {
        try {
            JmsPropertyUtils.writeAsXml(headers, properties, baseFileName + ".properties");
        } catch (IOException e) {
                                                                 
            e.printStackTrace();
        }
    }

       
                                       
       
                         
                                                                
                          
                                                    
                           
                                                                   
       
    private void writeMessagePayload(TextMessage textMessage, String baseFileName) throws IOException, JMSException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(baseFileName + ".payload"));
            writer.write(textMessage.getText());
            writer.close();
        } finally {
            KongaIoUtils.close(writer);
        }
    }

       
                                                                                              
       
                                                                                                  
                                                                           
       
    private static String createBaseFileName() throws ServerConfigurationException {
        String baseName = ServerConfig.getServerHome() + File.separator;
        baseName += "DataInterchange" + File.separator;
        baseName += "incoming" + File.separator;
        baseName += "JMS" + File.separator;
        
        GregorianCalendar now = new GregorianCalendar();
        baseName += Integer.toString(now.get(Calendar.YEAR)) + File.separator;
        baseName += Integer.toString(1+now.get(Calendar.MONTH)) + File.separator;
        baseName += Integer.toString(now.get(Calendar.DAY_OF_MONTH));
        File baseDir = new File(baseName);
        if (baseDir.isFile()) {
            throw new ServerConfigurationException("This file should not exist: " + baseDir.getAbsolutePath());
        }
        if (!baseDir.isDirectory() && !baseDir.mkdirs()) {
            throw new ServerConfigurationException("Failed to create directory \"" + baseDir.getAbsolutePath()
                            + "\". Check permissions on all parent directories!");
        }
        baseName += File.separator + UUID.randomUUID().toString();
        return baseName;
    }
    
}
