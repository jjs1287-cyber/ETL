package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import java.awt.Window;
import javax.swing.JFrame;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.resources.TransformationResources;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;

public final class TargetUpdateKeysEditor {
   private final TransformationPage transformationPage;
   private final TreeMapper treeMapper;
   private final TargetUpdateKeysEditorUi ui;
   private TargetUpdateKeysEditorUi.State lastSaved;

   public TargetUpdateKeysEditor(TransformationPage var1) {
      this.transformationPage = var1;
      this.treeMapper = var1.getTreeMapper();
      this.ui = new TargetUpdateKeysEditorUi(this.treeMapper);
      this.lastSaved = this.ui.getMemento();
   }

   public void edit() {
      JFrame var1 = this.transformationPage.getWindow().getFrame();
      TargetUpdateKeysEditorUi.State var2 = this.ui.getMemento();
      if (TargetUpdateKeysEditor.Dialog.open(this.ui, var1)) {
         TargetUpdateKeysEditorUi.State var3 = this.ui.getMemento();
         if (!var3.equals(var2)) {
            this.saveTransformation(var3);
         }
      } else {
         this.ui.setMemento(var2);
      }
   }

   private void saveTransformation(TargetUpdateKeysEditorUi.State var1) {
      TargetUpdateKeysEditor.SaveJob var2 = new TargetUpdateKeysEditor.SaveJob(var1);
      this.transformationPage.addSaveJob(var2);
   }

   private static class Dialog {
      public static boolean open(TargetUpdateKeysEditorUi var0, Window var1) {
         WizardStyleDialog var2 = new WizardStyleDialog(var1, TransformationResources.getString("TargetUpdateKeysEditor.Title"), var0);
         var2.setResizable(false);
         var2.setOkButtonAsDefault();
         var2.manageLocation(TargetUpdateKeysEditor.class);
         var2.setVisible(true);
         return var2.wasOkPressed();
      }
   }

   private final class SaveJob implements EntityPageSaveJob {
      private final TargetUpdateKeysEditorUi.State previousState = TargetUpdateKeysEditor.this.lastSaved;
      private final TargetUpdateKeysEditorUi.State newState;

      public SaveJob(TargetUpdateKeysEditorUi.State var2) {
         this.newState = var2;
      }

      @Override
      public String getDescription() {
         return TransformationResources.getString("TargetUpdateKey.JobDescription");
      }

      @Override
      public String getId() {
         return TargetUpdateKeysEditor.class.getName();
      }

      @Override
      public void postSaveRevert() {
         TargetUpdateKeysEditor.this.ui.setMemento(this.previousState);
         TargetUpdateKeysEditor.this.ui.applyTo(TargetUpdateKeysEditor.this.treeMapper.getTransformation());
         TargetUpdateKeysEditor.this.lastSaved = this.previousState;
      }

      @Override
      public void save() {
         TargetUpdateKeysEditor.this.ui.setMemento(this.newState);
         TargetUpdateKeysEditor.this.ui.applyTo(TargetUpdateKeysEditor.this.treeMapper.getTransformation());
         TargetUpdateKeysEditor.this.lastSaved = this.newState;
         this.refreshMappingTrees();
      }

      @Override
      public void redoWithoutSaving() {
         TargetUpdateKeysEditor.this.ui.setMemento(this.newState);
      }

      @Override
      public void undoWithoutSaving() {
         TargetUpdateKeysEditor.this.ui.setMemento(this.previousState);
      }

      private void refreshMappingTrees() {
         TargetUpdateKeysEditor.this.transformationPage.requestPostSaveMappingTreeRefresh();
      }
   }
}
