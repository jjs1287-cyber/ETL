package org.etlforge.integration.client.mapping;

import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultAdapter;
import org.etlforge.system.callback.CallbackResultWrapper;

public final class DefaultMappingStructureCallback implements GetMappingStructureCallback {
   private final CountDownLatch latch;
   private MappingTreeStructure mappingStructure;
   private final CallbackResult externalCallback;
   private MappingTreeStructureException error;

   public DefaultMappingStructureCallback() {
      this(new CallbackResultAdapter() {});
   }

   public DefaultMappingStructureCallback(CallbackResult var1) {
      this.externalCallback = new CallbackResultWrapper(var1);
      this.latch = new CountDownLatch(1);
   }

   @Override
   public void jtrGenerated(MappingTreeStructure var1) {
      try {
         this.mappingStructure = var1;
         this.externalCallback.succeeded(this.mappingStructure);
      } finally {
         this.latch.countDown();
      }
   }

   @Override
   public void caught(Throwable var1) {
      try {
         var1.printStackTrace();
         if (var1 instanceof MappingTreeStructureException) {
            this.error = (MappingTreeStructureException)var1;
         } else {
            this.error = new MappingTreeStructureException(var1);
         }

         this.externalCallback.failed(var1);
      } finally {
         this.latch.countDown();
      }
   }

   public MappingTreeStructure getMappingStructure() {
      this.await();
      return this.mappingStructure;
   }

   public MappingTreeStructure getMappingStructureOrThrow() throws MappingTreeStructureException {
      this.await();
      if (this.error != null) {
         throw this.error;
      } else {
         return this.mappingStructure;
      }
   }

   @Override
   public void cancelled() {
      try {
         this.externalCallback.cancelled(null);
      } finally {
         this.latch.countDown();
      }
   }

   private void await() {
      try {
         this.latch.await();
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         var2.printStackTrace();
      }
   }
}
