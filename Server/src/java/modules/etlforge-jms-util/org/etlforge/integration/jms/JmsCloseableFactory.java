/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.Session;

   
                                                            
      
                                                                                                 
                                        
   
                    
   
                         
   
               
   
public final class JmsCloseableFactory {

       
                                                                                  
       
               
                                                                                         
                                                                                            
                                                                                         
       
    public static JmsCloseable create(final Connection c) {
        return new JmsCloseable() {

            @Override
            public void close() throws JMSException {
                if (c != null) {
                    c.close();
                }
            }
        };
    }

       
                                                                               
       
               
                                                                                      
                                                                                            
                                                                                      
       
    public static JmsCloseable create(final Session s) {
        return new JmsCloseable() {

            @Override
            public void close() throws JMSException {
                if (s != null) {
                    s.close();
                }
            }
        };
    }

       
                                                                                       
       
                
                                                                                              
                                                                                            
                                                                                              
       
    public static JmsCloseable create(final MessageConsumer mc) {
        return new JmsCloseable() {

            @Override
            public void close() throws JMSException {
                if (mc != null) {
                    mc.close();
                }
            }
        };
    }

       
                                                                                       
       
                
                                                                                              
                                                                                            
                                                                                              
       
    public static JmsCloseable create(final MessageProducer mp) {
        return new JmsCloseable() {

            @Override
            public void close() throws JMSException {
                if (mp != null) {
                    mp.close();
                }
            }
        };
    }

       
                                                                                    
       
                
                                                                                           
                                                                                            
                                                                                           
       
    public static JmsCloseable create(final QueueBrowser qb) {
        return new JmsCloseable() {

            @Override
            public void close() throws JMSException {
                if (qb != null) {
                    qb.close();
                }
            }
        };
    }
    
    private JmsCloseableFactory() {    }
    
}
