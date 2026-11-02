/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

   
   
                                                                           
   
                          
                          
   
             
   
   
public class Listener {
	LocationProperties connectivityProps;
	volatile boolean shouldStopListening = false;

	public Listener(LocationProperties connectivityProps) {
		this.connectivityProps = connectivityProps;
	}
	
	public void stopListening() {
		shouldStopListening = true;
	}
	
	public void listen(MessageArrivedCallback callback) {
		Session session = null;
		Connection connection = null;
		System.out.println("Listener starting at " + new Date() + " - " + connectivityProps.getDestinationName() + 
                        " on server " + connectivityProps.getJndiUrl());

        MessageConsumer consumer = null;
		try {
			InitialDirContext ctx = LocationFactory.getInitialContextFactory(connectivityProps);
			connection = LocationFactory.getConnection(connectivityProps, ctx);
			session = LocationFactory.getSession(connection);

			Destination dest = (Destination) ctx.lookup(connectivityProps.getDestinationName());

			consumer = session.createConsumer(dest, connectivityProps.getProperty("Jms.MessageSelector") );

			Message inMessage = null;
			do {
				inMessage = consumer.receive(5000);                  
				if (inMessage instanceof TextMessage) {
					TextMessage txtMessage = (TextMessage)inMessage;
					System.out.println("Got message: >>" + txtMessage.getText() + "<<");
					try {
						callback.messageArrived(txtMessage);
					} catch(MessagingException e) {
						                                                                             
						System.err.println("An error occurred when processing an incoming JMS message. " + e.toString());
						e.printStackTrace();
					}
                    session.commit();
				}
			} while (!shouldStopListening);

		} catch (NamingException e) {
			System.err.println("A JMS lookup error occurred. Connectivity properties: \n"
					+ connectivityProps.toString() + "\n");
			e.printStackTrace();
		} catch (JMSException e) {
			System.err.println("A JMS error occurred. Connectivity properties: \n"
					+ connectivityProps.toString() + "\n");
			e.printStackTrace();
			Exception le = e.getLinkedException();
			if (le != null) {
				System.err.println("Linked exception: " + le);
				le.printStackTrace();
			}
		} catch (Throwable e) {
			System.err.println("An error occurred while listening for JMS messages. Connectivity properties: \n"
					+ connectivityProps.toString() + "\n");
			e.printStackTrace();
		} finally {
            Closer.close(consumer);
            Closer.close(session);
            Closer.close(connection);
		}
		System.out.println("Listener stopping at " + new Date());
	}
}
