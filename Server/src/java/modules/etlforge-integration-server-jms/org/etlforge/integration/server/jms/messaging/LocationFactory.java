/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang.StringUtils;

   
                                                                            
   
                          
   
                 
   
   
public class LocationFactory {
	
    public static InitialDirContext getInitialContextFactory(LocationProperties connectivityProps) throws NamingException, JMSException {
		if( StringUtils.isEmpty(connectivityProps.getInitialContextFactory()) ||
			StringUtils.isEmpty(connectivityProps.getJndiUrl()) ) {
			return new InitialDirContext();                                                           
		} else {
			Hashtable<String, Object> environment = new Hashtable<String, Object>();
			for( Map.Entry<String, String> keyValue : connectivityProps.entrySet()) {
				if( keyValue.getKey().startsWith("Jndi.") )
					environment.put(keyValue.getKey().substring("Jndi.".length()), keyValue.getValue());
			}
			return new InitialDirContext(environment);
		}
	}
	
    public static Connection getConnection(LocationProperties connectivityProps, InitialDirContext ctx) throws NamingException, JMSException {
		Connection connection = null;

		Object dirObj = ctx.lookup(connectivityProps.getConnectionFactory());
		if( dirObj instanceof javax.naming.Reference ) {
			Reference namingRef = (Reference)dirObj;
			System.err.println("JNDI lookup of '" + connectivityProps.getConnectionFactory() + "' " +
					"returned a javax.naming.Reference: \n\t" + namingRef.toString() + "\n" +
							"This usually means that the JMS provider classes are not in the classpath.\n\n");
		}
		ConnectionFactory connFactory = (ConnectionFactory) dirObj;
		if( connectivityProps.getConnectionUserName() == null ) {
			connection = connFactory.createConnection();
		} else {
			connection = connFactory.createConnection(
					connectivityProps.getConnectionUserName(),
					connectivityProps.getConnectionPassword() == null ? "" : connectivityProps.getConnectionPassword() );
		}
		connection.start();
		return connection;
	}

    public static Session getSession(Connection connection) throws JMSException {
		boolean transacted = true;
		return connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);		
	}
}
