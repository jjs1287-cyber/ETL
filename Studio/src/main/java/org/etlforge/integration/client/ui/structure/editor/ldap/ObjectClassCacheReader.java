package org.etlforge.integration.client.ui.structure.editor.ldap;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Semaphore;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.cache.LdapStructureCache;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;

final class ObjectClassCacheReader {
   private final ObjectClassSelectionModel model;
   private final Semaphore lock;
   private final LdapStructureServerCache ldapCache;

   private ObjectClassCacheReader(ObjectClassSelectionModel var1, LdapStructureServerCache var2, Semaphore var3) {
      this.model = var1;
      this.ldapCache = var2;
      this.lock = var3;
      var1.addPropertyChangeListener("location", new ObjectClassCacheReader.ModelListener());
   }

   static ObjectClassCacheReader install(ObjectClassSelectionModel var0, LdapStructureServerCache var1, Semaphore var2) {
      return new ObjectClassCacheReader(var0, var1, var2);
   }

   private void readCachedObjectClasses(LdapLocation var1) {
      if (var1 != null) {
         ObjectClassCacheReader.CacheReaderTask var2 = new ObjectClassCacheReader.CacheReaderTask(var1);
         ApplicationWorker var3 = Application.getWorker();
         var3.submitForSequential(var2);
      } else {
         this.model.setObjectClasses(new ObjectClassDefinition[0]);
      }
   }

   private final class CacheReaderTask implements Runnable {
      private final LdapLocation loc;

      public CacheReaderTask(LdapLocation var2) {
         this.loc = var2;
      }

      @Override
      public void run() {
         if (this.acquireLock()) {
            ObjectClassDefinition[] var1;
            try {
               LdapStructureCache var2 = ObjectClassCacheReader.this.ldapCache.getCache(this.loc.getServer());
               var1 = var2.getObjectClasses();
            } finally {
               ObjectClassCacheReader.this.lock.release();
            }

            this.updateModel(var1);
         }
      }

      private boolean acquireLock() {
         try {
            ObjectClassCacheReader.this.lock.acquire();
            return true;
         } catch (InterruptedException var2) {
            var2.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
         }
      }

      private void updateModel(final ObjectClassDefinition[] var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ObjectClassCacheReader.this.model.setObjectClasses(var1);
            }
         });
      }
   }

   private final class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         LdapLocation var2 = (LdapLocation)var1.getNewValue();
         ObjectClassCacheReader.this.readCachedObjectClasses(var2);
      }
   }
}
