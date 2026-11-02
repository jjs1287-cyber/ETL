package org.etlforge.integration.client.ui.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class ModificationMonitorUi implements UiProvider, Disposable {
   private IntegrationProject project;
   private final ModificationMonitorUi.ModifiedAtListener modifiedAtListener;
   private final ModificationMonitorUi.ProjectContentListener projectListener = new ModificationMonitorUi.ProjectContentListener();
   private final ModifiedAtTable table;

   public ModificationMonitorUi() {
      this.modifiedAtListener = new ModificationMonitorUi.ModifiedAtListener();
      this.table = new ModifiedAtTable();
   }

   private void listenTo(IntegrationEntity var1) {
      var1.addPropertyChangeListener("IntegrationEntity#MofidiedAt", this.modifiedAtListener);
   }

   private void forgetAbout(IntegrationEntity var1) {
      var1.removePropertyChangeListener("IntegrationEntity#MofidiedAt", this.modifiedAtListener);
   }

   public synchronized void setProject(IntegrationProject var1) {
      if (this.project != null) {
         this.dispose();
      }

      if (var1 != null) {
         this.listenToProject(var1);
      }

      this.table.setProject(var1);
      this.project = var1;
   }

   private void listenToProject(IntegrationProject var1) {
      var1.addProjectListener(this.projectListener);

      for (IntegrationEntity var3 : var1.getAllEntities()) {
         this.listenTo(var3);
      }
   }

   @Override
   public synchronized void dispose() {
      if (this.project != null) {
         this.project.removeProjectListener(this.projectListener);

         for (IntegrationEntity var2 : this.project.getAllEntities()) {
            this.forgetAbout(var2);
         }

         this.project = null;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private class ModifiedAtListener implements PropertyChangeListener {
      private ModifiedAtListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ModificationMonitorUi.this.table.updateModifiedDate((IntegrationEntity)var1.getSource());
      }
   }

   private class ProjectContentListener extends ProjectListener.Adapter {
      private ProjectContentListener() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            ModificationMonitorUi.this.listenTo(var3);
         }

         this.rebuildTable();
      }

      private void rebuildTable() {
         ModificationMonitorUi.this.table.rebuild();
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            ModificationMonitorUi.this.forgetAbout(var3);
         }

         this.rebuildTable();
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            ModificationMonitorUi.this.listenTo(var3);
         }

         this.rebuildTable();
      }
   }
}
