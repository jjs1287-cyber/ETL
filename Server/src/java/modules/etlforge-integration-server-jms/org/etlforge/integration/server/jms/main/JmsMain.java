/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.main;

import java.util.Date;

import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.integration.server.jms.runtime.JmsRuntime;

public class JmsMain {
	
	   
                                        
    
               
    
	public static void main(String[] args) {
		Date now = new Date();
		System.out.println("##########################################################");
		System.out.println("Starting ETLForge JMS Engine at " + now + ". (stdout)");
		System.out.println("##########################################################");
		System.out.flush();

		System.err.println(" -- Starting ETLForge JMS Engine at " + now + ". (stderr)");
		System.err.flush();
		
		JmsRuntime runtime = new JmsRuntime();
		try {
			runtime.init(args);
			runtime.waitUntilShutdown();
		} catch (ServerConfigurationException e) {
			                 
			e.printStackTrace();
			System.exit(1);
		}
		
		now = new Date();
		System.out.println("Stopping ETLForge JMS Engine at " + now + ". (stdout)");
		System.out.flush();
		System.err.println("Stopping ETLForge JMS Engine at " + now + ". (stderr)");
		System.err.flush();
	}
}
