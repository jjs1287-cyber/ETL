package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.PersistorException;
import java.util.concurrent.BlockingQueue;

final class MessageQueueConsumer {
   private final BlockingQueue<BatchProcessMessage> queue;
   private MessagePersistor persistor = MessagePersistor.NONE;

   public MessageQueueConsumer(BlockingQueue<BatchProcessMessage> var1) {
      this.queue = Preconditions.checkNotNull(var1);
   }

   public void setPersistor(MessagePersistor var1) {
      this.persistor = Preconditions.checkNotNull(var1);
   }

   public void consumeMessages() throws PersistorException, InterruptedException {
      BatchProcessMessage var1;
      do {
         var1 = this.queue.take();
         this.persistor.persistMessage(var1);
      } while (var1 != BatchProcessMessage.LAST_BATCH_SENT);
   }
}
