package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import java.awt.EventQueue;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.resources.TransformationResources;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.structure.editor.db.TransformationDatabaseInfo;

public final class WhereClauseEditor {
   private final TransformationPage page;
   private final TreeMapper treeMapper;
   private final WhereClauseEditorUi ui;
   private WhereClauseEditorUi.State lastSaved;

   public WhereClauseEditor(TransformationPage var1) {
      this.page = var1;
      this.treeMapper = var1.getTreeMapper();
      this.ui = this.createUi(this.treeMapper);
      this.lastSaved = this.ui.getMemento();
   }

   private WhereClauseEditorUi createUi(TreeMapper var1) {
      TransformationDatabaseInfoFactory var2 = TransformationDatabaseInfoFactory.fromSourceTree(var1);
      TransformationDatabaseInfo var3 = var2.createInfo();
      return WhereClauseEditorUiFactory.createEditorUi(var3);
   }

   public void edit() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            WhereClauseEditor.this.displayDialog();
         }
      });
   }

   private void displayDialog() {
      WhereClauseEditorUi.State var1 = this.ui.getMemento();
      WhereClauseEditor.EditorDialog var2 = new WhereClauseEditor.EditorDialog(this.page.getWindow(), this.ui);
      var2.setVisible(true);
      if (var2.wasOkPressed()) {
         this.handleNewInput(var1);
      } else {
         this.ui.setMemento(var1);
      }
   }

   private void handleNewInput(WhereClauseEditorUi.State var1) {
      WhereClauseEditorUi.State var2 = this.ui.getMemento();
      if (!var2.equals(var1)) {
         this.applyToTransformation(var2);
      }
   }

   private void applyToTransformation(WhereClauseEditorUi.State var1) {
      WhereClauseEditor.SaveJob var2 = new WhereClauseEditor.SaveJob(var1);
      this.page.addSaveJob(var2);
   }

   private static class EditorDialog extends WizardStyleDialog {
      private final WhereClauseEditorUi ui;

      public EditorDialog(ApplicationWindow var1, WhereClauseEditorUi var2) {
         super(var1.getFrame(), TransformationResources.getString("WhereClauseEditor.Title"), wrapInScroll(var2));
         this.ui = var2;
         this.setFocusedComponent(var2);
         this.pack();
         this.tweakSizeIfNecessary();
         this.manageLocation();
         this.setOkButtonAsDefault();
      }

      private void tweakSizeIfNecessary() {
         int var1 = this.getWidth();
         int var2 = this.getHeight();
         this.setSize(Math.min(var1, 800), Math.min(var2, 600));
      }

      @Override
      protected void onOk() {
         this.ui.terminateEdit();
         super.onOk();
      }
   }

   private class SaveJob implements EntityPageSaveJob {
      private final WhereClauseEditorUi.State previousState = WhereClauseEditor.this.lastSaved;
      private final WhereClauseEditorUi.State newState;

      public SaveJob(WhereClauseEditorUi.State var2) {
         this.newState = var2;
      }

      @Override
      public String getId() {
         return WhereClauseEditor.class.getName();
      }

      @Override
      public String getDescription() {
         return TransformationResources.getString("WhereClauseEditor.JobDescription");
      }

      @Override
      public void postSaveRevert() {
         this.applyState(this.previousState);
      }

      @Override
      public void save() {
         this.applyState(this.newState);
      }

      @Override
      public void redoWithoutSaving() {
         WhereClauseEditor.this.ui.setMemento(this.newState);
      }

      @Override
      public void undoWithoutSaving() {
         WhereClauseEditor.this.ui.setMemento(this.previousState);
      }

      private void applyState(WhereClauseEditorUi.State var1) {
         WhereClauseEditor.this.ui.setMemento(var1);
         WhereClauseEditor.this.ui.applyTo(WhereClauseEditor.this.treeMapper.getTransformation());
         WhereClauseEditor.this.lastSaved = var1;
         this.removeTestData();
         this.refreshMappingTrees();
      }

      private void removeTestData() {
         MappingTreeDataService var1 = WhereClauseEditor.this.page.getTreeDataService();
         var1.removeAllTestData();
      }

      private void refreshMappingTrees() {
         WhereClauseEditor.this.page.requestPostSaveMappingTreeRefresh();
      }
   }
}
