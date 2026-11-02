/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.messaging;

   
                                                             
   
                          
   
               
   
public class RunnableListener implements Runnable {

    private final Listener listener;

    private final MessageArrivedCallback callback;

    public RunnableListener(Listener listener, MessageArrivedCallback callback) {
        this.listener = listener;
        this.callback = callback;
    }

    @Override
    public void run() {
        listener.listen(callback);
    }

}
