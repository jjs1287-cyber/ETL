package org.etlforge.integration.data.project;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.system.Disposable;

public final class TrashCan {
   private final IntegrationProject project;
   private final IntegrationEntityMapLookup dataStore;
   private final Object dataStoreLock;

   public TrashCan(IntegrationProject var1) {
      this.project = var1;
      this.dataStore = new IntegrationEntityMapLookup();
      this.dataStoreLock = new Object();
   }

   public void throwAway(IntegrationEntity var1) {
      synchronized (this.dataStoreLock) {
         this.dataStore.addItem(var1);
      }
   }

   public void empty() {
      synchronized (this.dataStoreLock) {
         this.dataStore.clear();
      }
   }

   public void revive(List<IntegrationEntity> var1, EntityRevivorCallback var2) {
      TrashCan.Revivor var3 = new TrashCan.Revivor(var1, var2);

      try {
         var3.revive();
      } finally {
         var3.dispose();
      }
   }

   public List<IntegrationEntity> getContents() {
      ArrayList var1 = Lists.newArrayList();
      synchronized (this.dataStoreLock) {
         for (IntegrationEntity var4 : this.dataStore.getAllInstances()) {
            var1.add(var4);
         }

         return var1;
      }
   }

   private void reviveImpl(final List<IntegrationEntity> var1, EntityRevivorCallback var2) {
      try {
         IntegrationProjectLock.acquire();
         this.project.reviveItems(var1, var2);
      } finally {
         IntegrationProjectLock.release();
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            for (IntegrationEntity var2x : var1) {
               if (!var2x.isDeleted()) {
                  synchronized (TrashCan.this.dataStoreLock) {
                     TrashCan.this.dataStore.removeItem(var2x);
                  }
               }
            }
         }
      });
   }

   private void reviveEntity(IntegrationEntity var1, EntityRevivorCallback var2) {
      List var3 = Collections.singletonList(var1);
      this.reviveImpl(var3, var2);
   }

   private class Revivor implements Disposable {
      private final List<IntegrationEntity> toRevive;
      private final EntityRevivorCallback callback;
      private final IntegrationEntityMapLookup dataLookup;
      private final Dependencies links;
      private final DependencyStore dependencyStore;

      public Revivor(List<IntegrationEntity> var2, EntityRevivorCallback var3) {
         this.toRevive = var2;
         this.callback = var3;
         if (var2.size() > 1) {
            this.dataLookup = new IntegrationEntityMapLookup();

            for (IntegrationEntity var5 : var2) {
               this.dataLookup.addItem(var5);
            }

            this.links = new Dependencies();

            for (IntegrationEntity var9 : var2) {
               for (EntityLink var7 : var9.getLinks(this.dataLookup)) {
                  this.links.addLink(var7);
               }
            }

            this.dependencyStore = new DependencyStore(this.links);
         } else {
            this.dataLookup = null;
            this.links = null;
            this.dependencyStore = null;
         }
      }

      @Override
      public void dispose() {
         if (this.dependencyStore != null) {
            this.dependencyStore.dispose();
         }
      }

      public void revive() {
         int var1 = this.toRevive.size();
         if (var1 != 0) {
            if (var1 == 1) {
               TrashCan.this.reviveEntity(this.toRevive.get(0), this.callback);
            } else {
               ArrayList var2 = Lists.newArrayList();

               while (!this.toRevive.isEmpty()) {
                  Iterator var3 = this.toRevive.iterator();

                  while (var3.hasNext()) {
                     IntegrationEntity var4 = (IntegrationEntity)var3.next();
                     if (this.qualifiesForRevival(var4)) {
                        var2.add(var4);
                        var3.remove();
                        break;
                     }
                  }

                  var2.addAll(this.toRevive);
                  this.toRevive.clear();
               }

               TrashCan.this.reviveImpl(var2, this.callback);
            }
         }
      }

      private boolean qualifiesForRevival(IntegrationEntity var1) {
         for (IntegrationEntity var3 : this.toRevive) {
            if (var1 != var3) {
               for (IntegrationEntity var4 = var1.getParent(); var4 != null && !(var4 instanceof RootFolder); var4 = var4.getParent()) {
                  if (this.toRevive.contains(var4)) {
                     return false;
                  }
               }

               for (IntegrationEntity var6 : this.dependencyStore.getRequiredObjects(var1)) {
                  if (this.toRevive.contains(var6)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }
}
