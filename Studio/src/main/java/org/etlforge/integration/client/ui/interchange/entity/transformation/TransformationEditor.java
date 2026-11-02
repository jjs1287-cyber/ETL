package org.etlforge.integration.client.ui.interchange.entity.transformation;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeExpansionMemento;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.RefreshStructuresCallback;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingErrorHandler;
import org.etlforge.integration.client.ui.structure.editor.EditStructureException;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactories;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactory;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureEditor;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.ui.UiThreadSupplier;

public final class TransformationEditor {
   private final TransformationPage page;
   private final StructureEditorFactories editorFactories;
   private final Transformation transformation;
   private final EntityPersistor entityPersistor;
   private final EntityExplorerSupport explorerSupport;

   public TransformationEditor(TransformationPage var1, StructureEditorFactories var2) {
      this.page = var1;
      this.editorFactories = var2;
      this.entityPersistor = var1.getEntityPersistor();
      this.transformation = var1.getObject();
      this.explorerSupport = var1.getEntityExplorerSupport();
   }

   public void editInputStructure() {
      StructureEditor var1 = this.getInputEditor();
      this.runEditor(var1);
   }

   private StructureEditor getInputEditor() {
      return this.getEditor(this.transformation.getInputStructure(), SourceTarget.Source);
   }

   private StructureEditor getEditor(DataStructure var1, SourceTarget var2) {
      if (var1 instanceof DatabaseStructure) {
         return this.databaseEditor(var2);
      } else {
         StructureEditorFactory var3 = this.editorFactories.getFactory(var1);
         if (var3 == null) {
            throw new RuntimeException("No editor registered for structures of type " + var1.getStructureType().name() + ". This is a bug.");
         } else {
            return var3.getEditor(this.transformation, var1, var2, this.explorerSupport);
         }
      }
   }

   private StructureEditor databaseEditor(SourceTarget var1) {
      TreeMapperToDatabaseStructure var2 = new TreeMapperToDatabaseStructure(this.page.getTreeMapper(), var1);
      return new DatabaseStructureEditor(this.transformation, var2, var1, this.explorerSupport);
   }

   public void editOutputStructure() {
      StructureEditor var1 = this.getOutputEditor();
      this.runEditor(var1);
   }

   private StructureEditor getOutputEditor() {
      return this.getEditor(this.transformation.getOutputStructure(), SourceTarget.Target);
   }

   private void runEditor(StructureEditor var1) {
      if (var1 == null) {
         this.reportUnsupportedStructure();
      } else {
         Object var2 = var1.getInitialState(this.transformation);
         TransformationEditorWizard var3 = this.createWizard();
         EditedStructure var4 = var1.edit(var3);
         if (var4 != null) {
            this.applyNewStructure(var1, var4, var3, var2, true);
         }
      }
   }

   private TransformationEditorWizard createWizard() {
      return UiThreadSupplier.call(new Supplier<TransformationEditorWizard>() {
         public TransformationEditorWizard get() {
            return new TransformationEditorWizard(TransformationEditor.this.transformation, TransformationEditor.this.page.getWindow());
         }
      });
   }

   private void applyNewStructure(StructureEditor var1, EditedStructure var2, TransformationEditorWizard var3, Object var4, boolean var5) {
      try {
         var2.apply(this.transformation);
         TransformationEditor.ApplyStructureCallback var6 = new TransformationEditor.ApplyStructureCallback(var1, var2, var3, var4, var5);
         this.refreshMappingTree(var6);
      } catch (EditStructureException var7) {
         this.handleException(var7, var1, var4);
      }
   }

   private void refreshMappingTree(final RefreshStructuresCallback var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TransformationEditor.this.page.getTreeDataService().removeAllTestData();
            TransformationEditor.this.page.getTreeMapper().refreshTreeStructures(var1);
         }
      });
   }

   private void handleException(Exception var1, StructureEditor var2, Object var3) {
      ErrorLog.attention(TransformationEditor.class, "Failed to save the changes", var1);
      if (var2 != null && var3 != null) {
         this.restorePreviousState(var2, var3);
      }
   }

   private void restorePreviousState(StructureEditor var1, Object var2) {
      var1.restoreInitialState(var2);
      this.page.getTreeMapper().prepareStructureRestoration();
      TransformationEditor.ApplyStructureCallback var3 = new TransformationEditor.ApplyStructureCallback();
      this.refreshMappingTree(var3);
   }

   private void reportUnsupportedStructure() {
      Alert.error(
         this.page.getWindow().getFrame(), "The requested structure cannot be edited. This functionality will soon be in place.", "Unsupported Operation"
      );
   }

   private class ApplyStructureCallback implements RefreshStructuresCallback {
      private final StructureEditor editor;
      private final EditedStructure structure;
      private final MappingErrorHandler mappingErrorHandler;
      private final Object memento;
      private final boolean sendEdit;
      private final NodeExpansionMemento expansionState;

      public ApplyStructureCallback() {
         this.editor = null;
         this.structure = null;
         this.memento = null;
         this.sendEdit = false;
         this.mappingErrorHandler = MappingErrorHandler.IGNORE;
         this.expansionState = new NodeExpansionMemento(TransformationEditor.this.page.getTreeMapper());
      }

      public ApplyStructureCallback(StructureEditor var2, EditedStructure var3, TransformationEditorWizard var4, Object var5, boolean var6) {
         this.editor = var2;
         this.structure = var3;
         this.mappingErrorHandler = var4 != null && var4.isAutmaticMigrationSelected()
            ? TransformationEditor.this.new MappingConflictResolver(var4)
            : MappingErrorHandler.IGNORE;
         this.memento = var5;
         this.sendEdit = var6;
         this.expansionState = new NodeExpansionMemento(TransformationEditor.this.page.getTreeMapper());
      }

      @Override
      public void cancelled() {
         this.completeAndSave();
      }

      @Override
      public void done() {
         this.completeAndSave();
      }

      @Override
      public MappingErrorHandler getMappingErrorHandler() {
         return this.mappingErrorHandler;
      }

      private void completeAndSave() {
         try {
            this.expansionState.restore();
            TransformationEditor.this.page.getTreeMapper().saveMaps();
            TransformationEditor.this.entityPersistor.save(TransformationEditor.this.transformation);
            if (this.sendEdit) {
               TransformationEditor.Edit var1 = TransformationEditor.this.new Edit(this.editor, this.memento, this.structure);
               TransformationEditor.this.page.undoableEditHappened(new UndoableEditEvent(this, var1));
            }

            this.setPageClean();
         } catch (Exception var2) {
            TransformationEditor.this.handleException(var2, this.editor, this.memento);
         }
      }

      private void setPageClean() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               TransformationEditor.this.page.setClean();
            }
         });
      }
   }

   private class Edit extends AbstractUndoableEdit {
      private final StructureEditor editor;
      private final Object memento;
      private final EditedStructure newStructure;

      public Edit(StructureEditor var2, Object var3, EditedStructure var4) {
         this.editor = var2;
         this.memento = var3;
         this.newStructure = var4;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         TransformationEditor.this.applyNewStructure(this.editor, this.newStructure, null, this.memento, false);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         TransformationEditor.this.restorePreviousState(this.editor, this.memento);
      }

      @Override
      public String getPresentationName() {
         return Strings.get("StructureEdit.Name");
      }
   }

   private class MappingConflictResolver implements MappingErrorHandler {
      private final TransformationEditorWizard wizard;

      public MappingConflictResolver(TransformationEditorWizard var2) {
         this.wizard = var2;
      }

      public void caught(TreeMapperException var1) {
         try {
            Transformation var2 = this.wizard.getBackup();
            this.doMigration(var2);
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            this.logFailureToLaunchMigration("The call was interrupted before the backup copy could be created.", var3);
         }
      }

      private void doMigration(Transformation var1) {
         TransformationPage var2 = this.openBackupPage(var1);
         if (var2 != null) {
            this.openMigrationTool(var2);
            this.switchToOriginalPage();
         } else {
            this.logFailureToLaunchMigration("Could not open the backup page.", new Exception());
         }
      }

      private TransformationPage openBackupPage(Transformation var1) {
         Editor var2 = UiCompanion.of(var1).openPage();
         return var2 instanceof TransformationPage ? (TransformationPage)var2 : null;
      }

      private void openMigrationTool(TransformationPage var1) {
         TreeMapper var2 = var1.getTreeMapper();
         var2.migration(TransformationEditor.this.transformation);
      }

      private void switchToOriginalPage() {
         TransformationEditor.this.page.getWindow().getEditorService().openEditor(TransformationEditor.this.page);
      }

      private void logFailureToLaunchMigration(String var1, Throwable var2) {
         String var3 = "Failed to launch the Migration tool. Reason: " + var1 + "\n\nPlease contact ETLForge Support.";
         ErrorLog.attention(TransformationEditor.class, var3, var2);
      }
   }
}
