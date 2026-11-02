package com.etlforge.integration.server.engine.bulkload.batch;

import com.etlforge.integration.server.engine.bulkload.PersistorException;
import java.util.concurrent.BlockingQueue;

public final class MessageQueueMonitor extends AbstractBatchProcessMonitor {
   private final BlockingQueue<BatchProcessMessage> queue;

   public MessageQueueMonitor(BlockingQueue<BatchProcessMessage> var1) {
      this.queue = var1;
   }

   @Override
   protected void doCustomProcessing(BatchProcessMessage var1) {
      try {
         this.queue.put(var1);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      }
   }

   public void consumeMessages(MessagePersistor var1) throws PersistorException, InterruptedException {
      MessageQueueConsumer var2 = new MessageQueueConsumer(this.queue);
      var2.setPersistor(var1);
      var2.consumeMessages();
   }
}
