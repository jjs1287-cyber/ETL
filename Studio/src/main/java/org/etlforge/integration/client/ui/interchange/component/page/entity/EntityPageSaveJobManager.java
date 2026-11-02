package org.etlforge.integration.client.ui.interchange.component.page.entity;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Set;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

class EntityPageSaveJobManager {
   private final IntegrationEntityPage page;
   private final HashMap<String, EntityPageSaveJob> registeredJobs;
   private final Set<EntityPageSaveJob> savedJobs;
   private final Set<EntityPageSaveJob> jobsToRevert;

   public EntityPageSaveJobManager(IntegrationEntityPage var1) {
      assert var1 != null;
      this.page = var1;
      this.registeredJobs = Maps.newHashMap();
      this.savedJobs = Sets.newHashSet();
      this.jobsToRevert = Sets.newHashSet();
   }

   public void add(EntityPageSaveJob var1) {
      EntityPageSaveJob var2 = this.registeredJobs.put(var1.getId(), var1);
      this.page.forceInputChangeEvent(this);
      this.page.undoableEditHappened(new UndoableEditEvent(this, new EntityPageSaveJobManager.Edit(var1, var2)));
   }

   public void doSave() {
      for (EntityPageSaveJob var2 : this.jobsToRevert) {
         var2.postSaveRevert();
      }

      try {
         for (EntityPageSaveJob var7 : this.registeredJobs.values()) {
            if (!this.jobsToRevert.contains(var7) && !this.savedJobs.contains(var7)) {
               var7.save();
               this.savedJobs.add(var7);
            }
         }
      } finally {
         this.jobsToRevert.clear();
      }
   }

   private class Edit extends AbstractUndoableEdit {
      private final EntityPageSaveJob newJob;
      private final EntityPageSaveJob replacedJob;

      public Edit(EntityPageSaveJob var2, EntityPageSaveJob var3) {
         this.newJob = var2;
         this.replacedJob = var3;
      }

      @Override
      public String getPresentationName() {
         return this.newJob.getDescription();
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         EntityPageSaveJobManager.this.registeredJobs.put(this.newJob.getId(), this.newJob);
         EntityPageSaveJobManager.this.jobsToRevert.remove(this.newJob);
         this.newJob.redoWithoutSaving();
         if (this.replacedJob != null && EntityPageSaveJobManager.this.savedJobs.remove(this.replacedJob)) {
            EntityPageSaveJobManager.this.jobsToRevert.add(this.replacedJob);
         }
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         String var1 = this.newJob.getId();
         EntityPageSaveJobManager.this.registeredJobs.remove(var1);
         this.newJob.undoWithoutSaving();
         if (EntityPageSaveJobManager.this.savedJobs.remove(this.newJob)) {
            EntityPageSaveJobManager.this.jobsToRevert.add(this.newJob);
         }

         if (this.replacedJob != null) {
            EntityPageSaveJobManager.this.registeredJobs.put(var1, this.replacedJob);
            EntityPageSaveJobManager.this.jobsToRevert.remove(this.replacedJob);
         }
      }
   }
}
