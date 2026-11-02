package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.EditorMemento;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.ThreeValuedBool;

public final class DatabaseStructureEditor implements StructureEditor {
   private final Transformation transformation;
   private final Supplier<StructureToEdit> structureProvider;
   private final SourceTarget sourceTarget;
   private final EntityExplorerSupport explorerSupport;

   public DatabaseStructureEditor(Transformation var1, Supplier<StructureToEdit> var2, SourceTarget var3, EntityExplorerSupport var4) {
      sanityCheck(var1, var3);
      this.transformation = var1;
      this.structureProvider = var2;
      this.explorerSupport = var4;
      this.sourceTarget = var3;
   }

   private static void sanityCheck(Transformation var0, SourceTarget var1) {
      DataStructure var2 = var1 == SourceTarget.Source ? var0.getInputStructure() : var0.getOutputStructure();
      if (!(var2 instanceof DatabaseStructure)) {
         throw new IllegalArgumentException("The edited transformation does not have database as " + var1.name().toLowerCase() + " structure.");
      }
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new DatabaseStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof DatabaseStructureEditor.Memento) {
         ((DatabaseStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   @Override
   public EditedStructure edit(final Wizard var1) {
      final DatabaseStructureEditor.WizardCallback var2 = new DatabaseStructureEditor.WizardCallback();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DatabaseStructureWizard var1x = DatabaseStructureEditor.this.createStructureWizard();
            var1x.addWizardListener(var2);
            var1.addNestedWizard(var1x);
            var1.start();
         }
      });
      return var2.getEditedStructure();
   }

   private DatabaseStructureWizard createStructureWizard() {
      StructureToEdit var1 = this.getStructureToEdit();
      DatabaseStructureWizard var2 = new DatabaseStructureWizard(var1, this.explorerSupport);
      if (this.sourceTarget == SourceTarget.Source) {
         ThreeValuedBool var3 = this.isUpdateSourceTableAvailable();
         var2.setUpdateSourceTableAvailable(var3);
      }

      return var2;
   }

   private StructureToEdit getStructureToEdit() {
      DatabaseStructure var1 = this.getDatabaseStructure();
      return var1.hasDatabaseObjectInformation() ? this.deriveStructureToEditFromExistingData(var1) : this.structureProvider.get();
   }

   private StructureToEdit deriveStructureToEditFromExistingData(DatabaseStructure var1) {
      DatabaseObject[] var2 = var1.getDatabaseObjects();
      return new StructureToEdit(this.transformation, var1, var2, this.sourceTarget);
   }

   private DatabaseStructure getDatabaseStructure() {
      DataStructure var1 = this.sourceTarget == SourceTarget.Source ? this.transformation.getInputStructure() : this.transformation.getOutputStructure();
      return (DatabaseStructure)var1;
   }

   private ThreeValuedBool isUpdateSourceTableAvailable() {
      assert this.sourceTarget == SourceTarget.Source;
      return this.transformation.isTargetFlat() ? ThreeValuedBool.TRUE : ThreeValuedBool.FALSE;
   }

   private static class Memento extends EditorMemento {
      private final DbInsertMode insertMode;

      public Memento(Transformation var1, SourceTarget var2) {
         super(var1, var2);
         this.insertMode = var2 == SourceTarget.Target ? var1.getDbInsertMode() : null;
      }

      @Override
      public void restore() {
         super.restore();
         if (this.getUse() == SourceTarget.Target) {
            this.getTransformation().setDbInsertMode(this.insertMode);
         }
      }
   }

   private static class WizardCallback implements WizardListener<EditedStructure> {
      private final CountDownLatch latch = new CountDownLatch(1);
      private EditedStructure newStructure;

      private WizardCallback() {
      }

      public void wizardCompleted(EditedStructure var1) {
         this.newStructure = var1;
         this.latch.countDown();
      }

      @Override
      public void wizardCancelled() {
         this.handleWizardCancelledOrFailed();
      }

      @Override
      public void wizardFailed(Throwable var1) {
         this.handleWizardCancelledOrFailed();
      }

      private void handleWizardCancelledOrFailed() {
         this.latch.countDown();
      }

      public EditedStructure getEditedStructure() {
         try {
            this.latch.await();
            return this.newStructure;
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return null;
         }
      }
   }
}
