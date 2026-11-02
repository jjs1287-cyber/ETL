package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.wait.WaitService;

public final class HeaderMappingsController {
   private final HeaderMappingsModel model;
   private final ImportExportController importExportController;
   private final Action saveAction;
   private final Action autoMapAction;
   private Optional<? extends Action> updateSourceHeaderAction = Optional.absent();
   private Optional<? extends Action> updateTargetFieldsAction = Optional.absent();
   private WaitService waitService = WaitService.NONE;

   public HeaderMappingsController(HeaderMappingsModel var1) {
      this.model = var1;
      this.importExportController = new ImportExportController(var1);
      this.saveAction = new SaveMappingsAction(this.importExportController);
      this.autoMapAction = new AutoMapAction(var1);
      this.bindActionsEnabledState();
   }

   private void bindActionsEnabledState() {
      HeaderMappingsController.ActionsEnabledBinding var1 = new HeaderMappingsController.ActionsEnabledBinding();
      var1.syncUi();
   }

   public void installSourceHeaderUpdater(SourceHeaderUpdater var1) {
      this.updateSourceHeaderAction = Optional.of(new HeaderMappingsController.UpdateSourceHeaderAction(var1));
   }

   public void installTargetFieldUpdater(TargetFieldsUpdater var1) {
      this.updateTargetFieldsAction = Optional.of(new HeaderMappingsController.UpdateTargetFieldsAction(var1));
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
      this.importExportController.setWaitService(var1);
   }

   public Optional<? extends Action> getUpdateSourceHeaderAction() {
      return this.updateSourceHeaderAction;
   }

   public Optional<? extends Action> getUpdateTargetFieldsAction() {
      return this.updateTargetFieldsAction;
   }

   public Action getAutoMapAction() {
      return this.autoMapAction;
   }

   public Action getImportMappingsAction() {
      return new ImportMappingsAction(this.importExportController);
   }

   public Action getExportMappingsAction() {
      return this.saveAction;
   }

   private class ActionsEnabledBinding extends AbstractBinding<List<Mapping>> {
      public ActionsEnabledBinding() {
         super(HeaderMappingsController.this.model, "mappings");
      }

      protected void updateUiWithValue(List<Mapping> var1) {
         HeaderMappingsController.this.saveAction.setEnabled(!var1.isEmpty());
         HeaderMappingsController.this.autoMapAction.setEnabled(HeaderMappingsController.this.model.hasUnmappedSourceFields());
      }
   }

   private class UpdateSourceHeaderAction extends AbstractAction {
      private final SourceHeaderUpdater sourceHeaderUpdater;

      public UpdateSourceHeaderAction(SourceHeaderUpdater var2) {
         super("&Update");
         this.sourceHeaderUpdater = var2;
         this.putValue("ShortDescription", "Edits the list of source header columns");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               UpdateSourceHeaderAction.this.edit();
            }
         });
      }

      private void edit() {
         List var1 = HeaderMappingsController.this.model.getSourceHeader();
         List var2 = this.sourceHeaderUpdater.getSourceHeader(var1);
         if (!var2.equals(var1)) {
            HeaderMappingsController.this.model.updateAndRetainMappings(var2, HeaderMappingsController.this.model.getTargetFields());
         }
      }
   }

   private class UpdateTargetFieldsAction extends AbstractAction {
      private final TargetFieldsUpdater updater;

      public UpdateTargetFieldsAction(TargetFieldsUpdater var2) {
         super("U&pdate");
         this.updater = var2;
         this.putValue("ShortDescription", "Updates the list of target fields for the selected object, in case custom fields have been added or removed.");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               UpdateTargetFieldsAction.this.update();
            }
         });
      }

      private void update() {
         TargetFieldUpdateWorker var1 = new TargetFieldUpdateWorker(
            HeaderMappingsController.this.model, this.updater, HeaderMappingsController.this.waitService
         );
         var1.execute();
      }
   }
}
