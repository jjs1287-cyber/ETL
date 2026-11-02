package com.etlforge.integration.server.engine.bulkload.batch;

import com.etlforge.integration.server.engine.bulkload.PersistorException;

public interface MessagePersistor {
   MessagePersistor NONE = new MessagePersistor() {
      @Override
      public void persistMessage(BatchProcessMessage var1) {
      }
   };
   MessagePersistor CONSOLE = new MessagePersistor() {
      @Override
      public void persistMessage(BatchProcessMessage var1) {
         System.out.println("Now persisting message: " + var1);
      }
   };

   void persistMessage(BatchProcessMessage var1) throws PersistorException;
}
