/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

   
                          
                         
   
             
   
                                                 
   
public class ConnectionTester {

       
                                                                                          
                                                       
       
                               
                                                                
       
    public static void testConnection(LocationProperties connectivityProps) throws JMSException, NamingException {
        Session session = null;
        Connection connection = null;
        try {
            InitialDirContext ctx = LocationFactory.getInitialContextFactory(connectivityProps);
            connection = LocationFactory.getConnection(connectivityProps, ctx);
            session = LocationFactory.getSession(connection);
            Destination dest = (Destination) ctx.lookup(connectivityProps.getDestinationName());
            String selector = connectivityProps.getProperty("Jms.MessageSelector");
            if (dest instanceof Queue) {
                testConnectionToQueue(session, (Queue) dest, selector);
            }
            else {
                testConnectionToTopic(session, dest, selector);
            }
            session.rollback();
        }
        finally {
            Closer.close(session);
            Closer.close(connection);
        }
    }

    private static void testConnectionToQueue(Session session, Queue queue, String selector) throws JMSException {
        QueueBrowser browser = null;
        try {
            browser = session.createBrowser(queue, selector);
            browser.getEnumeration();
        }
        finally {
            Closer.close(browser);
        }
    }

    private static void testConnectionToTopic(Session session, Destination dest, String selector) throws JMSException {
        MessageConsumer consumer = null;
        try {
            consumer = session.createConsumer(dest, selector);
            consumer.receive(0);
        }
        finally {
            Closer.close(consumer);
        }
    }
}
