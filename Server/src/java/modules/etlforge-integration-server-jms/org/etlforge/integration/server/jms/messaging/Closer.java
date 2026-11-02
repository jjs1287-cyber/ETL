/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.etlforge.integration.jms.JmsCloseable;
import org.etlforge.integration.jms.JmsCloseableFactory;

   
                                                           
      
                                     
   
                         
   
               
   
final class Closer {

    private Closer() {    }

       
                                                                                     
                                                                                                 
                                  
       
       
    static void close(Connection c) {
        close(JmsCloseableFactory.create(c), "connection");
    }

       
                                                                                  
                                                                                                 
                               
       
       
    static void close(Session s) {
        close(JmsCloseableFactory.create(s), "session");
    }

       
                                                                                           
                                                                                               
                                  
       
       
    static void close(MessageProducer mp) {
        close(JmsCloseableFactory.create(mp), "message producer");
    }

       
                                                                                        
                                                                                               
                                  
       
       
    static void close(QueueBrowser qb) {
        close(JmsCloseableFactory.create(qb), "queue browser");
    }

       
                                                                                           
                                                                                               
                                  
       
       
    static void close(MessageConsumer mc) {
        close(JmsCloseableFactory.create(mc), "message consumer");
    }

    static void close(JmsCloseable closeable, String name) {
        try {
            closeable.close();
        }
        catch (JMSException ex) {
            handleCloseException(name, ex);
        }
    }

    private static void handleCloseException(String name, JMSException ex) {
        System.err.println("Closing " + name + " failed with " + ex);
        Exception le = ex.getLinkedException();
        if (le != null) {
            System.err.println("linked exception: " + le);
        }
    }

}