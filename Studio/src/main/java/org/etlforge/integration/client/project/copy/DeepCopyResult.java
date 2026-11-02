package org.etlforge.integration.client.project.copy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.project.ProjectAlteringProcessProcess;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DeepCopyResult {
   private final IntegrationProject project;
   private final IntegrationEntity startingPoint;
   private final ImmutableList<DeepCopyResult.Copy> summary;
   private final DeleteRequestHandler deleteHandler;
   private boolean saveWhenModified = true;

   DeepCopyResult(IntegrationProject var1, IntegrationEntity var2, Map<IntegrationEntity, IntegrationEntity> var3, DeleteRequestHandler var4) {
      this.project = var1;
      this.startingPoint = var2;
      Builder var5 = ImmutableList.builder();

      for (Entry var7 : var3.entrySet()) {
         var5.add(new DeepCopyResult.Copy((IntegrationEntity)var7.getKey(), (IntegrationEntity)var7.getValue()));
      }

      this.summary = var5.build();
      this.deleteHandler = var4;
   }

   void setSaveWhenModified(boolean var1) {
      this.saveWhenModified = var1;
   }

   public IntegrationEntity getStartingPoint() {
      return this.startingPoint;
   }

   public int getNumberOfCopiedItems() {
      return this.summary.size();
   }

   public Iterable<DeepCopyResult.Copy> getSummary() {
      return this.summary;
   }

   public void undo() {
      if (this.saveWhenModified) {
         (new ProjectAlteringProcessProcess<Void>(this.project) {
            protected Void doWork() {
               DeepCopyResult.this.deleteCopies();
               return null;
            }
         }).run();
      } else {
         this.deleteCopies();
      }
   }

   private void deleteCopies() {
      List var1 = this.getCopies();
      this.project.deleteItems(var1);
   }

   private List<IntegrationEntity> getCopies() {
      ArrayList var1 = Lists.newArrayList();

      for (DeepCopyResult.Copy var3 : this.summary) {
         var1.add(var3.copy);
      }

      return var1;
   }

   public boolean replaceOriginalItems() {
      final DeepCopyResult.ReplaceWorker var1 = new DeepCopyResult.ReplaceWorker();
      return this.saveWhenModified ? (new ProjectAlteringProcessProcess<Boolean>(this.project) {
         protected Boolean doWork() {
            return var1.replace();
         }
      }).run() : var1.replace();
   }

   public static final class Copy {
      public final IntegrationEntity original;
      public final IntegrationEntity copy;

      Copy(IntegrationEntity var1, IntegrationEntity var2) {
         this.original = var1;
         this.copy = var2;
      }

      void transferName() {
         assert this.original.isDeleted();
         this.copy.setName(this.original.getName());
      }
   }

   private final class ReplaceWorker {
      private ReplaceWorker() {
      }

      public Boolean replace() {
         if (this.deleteOriginals()) {
            this.renameCopies();
            return true;
         } else {
            return false;
         }
      }

      private boolean deleteOriginals() {
         List var1 = this.getOriginals();
         return DeepCopyResult.this.deleteHandler.deleteRequested(var1);
      }

      private List<IntegrationEntity> getOriginals() {
         ArrayList var1 = Lists.newArrayList();

         for (DeepCopyResult.Copy var3 : DeepCopyResult.this.summary) {
            var1.add(var3.original);
         }

         return var1;
      }

      private void renameCopies() {
         for (DeepCopyResult.Copy var2 : DeepCopyResult.this.summary) {
            var2.transferName();
         }
      }
   }
}
