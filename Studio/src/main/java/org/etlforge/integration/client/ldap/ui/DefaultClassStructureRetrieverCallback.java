package org.etlforge.integration.client.ldap.ui;

import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public final class DefaultClassStructureRetrieverCallback implements GetLdapStructuresCallback {
   private final CountDownLatch latch = new CountDownLatch(1);
   private ObjectClassStructure[] structures;
   private Throwable error;
   private boolean cancelled;
   private final Object lock = new Object();

   public ObjectClassStructure[] getStructures() {
      try {
         this.latch.await();
         synchronized (this.lock) {
            return this.structures != null ? (ObjectClassStructure[])this.structures.clone() : null;
         }
      } catch (InterruptedException var6) {
         InterruptedException var1 = var6;
         Thread.currentThread().interrupt();
         synchronized (this.lock) {
            this.error = var1;
            return null;
         }
      }
   }

   @Override
   public void structuresRetrieved(ObjectClassStructure[] var1) {
      synchronized (this.lock) {
         this.structures = var1;
      }

      this.latch.countDown();
   }

   @Override
   public void cancelled() {
      synchronized (this.lock) {
         this.cancelled = true;
      }

      this.latch.countDown();
   }

   @Override
   public void caught(Throwable var1) {
      synchronized (this.lock) {
         this.error = var1;
      }

      this.latch.countDown();
   }

   public boolean wasCancelled() {
      synchronized (this.lock) {
         return this.cancelled;
      }
   }

   public Throwable getError() {
      synchronized (this.lock) {
         return this.error;
      }
   }

   public void transferStateTo(ServerCallback var1) {
      synchronized (this.lock) {
         if (this.wasCancelled()) {
            var1.cancelled();
         } else if (this.error != null) {
            var1.caught(this.error);
         }
      }
   }
}
