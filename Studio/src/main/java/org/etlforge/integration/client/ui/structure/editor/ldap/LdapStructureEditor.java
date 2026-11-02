package org.etlforge.integration.client.ui.structure.editor.ldap;

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
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapStructureEditor implements StructureEditor {
   private final LdapStructure toEdit;
   private final SourceTarget sourceTarget;
   private final EntityExplorerSupport explorerSupport;

   public LdapStructureEditor(LdapStructure var1, SourceTarget var2, EntityExplorerSupport var3) {
      this.toEdit = var1;
      this.explorerSupport = var3;
      this.sourceTarget = var2;
   }

   @Override
   public EditedStructure edit(final Wizard var1) {
      final LdapStructureEditor.WizardCallback var2 = new LdapStructureEditor.WizardCallback();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LdapStructureWizard var1x = LdapStructureEditor.this.createLdapWizard();
            var1.addNestedWizard(var1x);
            var1x.addWizardListener(var2);
            var1.start();
         }
      });
      return this.completeWizard(var2);
   }

   private EditedStructure completeWizard(LdapStructureEditor.WizardCallback var1) {
      final LdapStructure var2 = var1.getNewStructure();
      return var2 != null ? new EditedStructure() {
         @Override
         public void apply(Transformation var1) {
            if (LdapStructureEditor.this.isSourceEditor()) {
               var1.setInputStructure(var2);
            } else {
               var1.setOutputStructure(var2);
            }
         }
      } : null;
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new LdapStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof LdapStructureEditor.Memento) {
         ((LdapStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   private LdapStructureWizard createLdapWizard() {
      LdapStructureWizard var1;
      if (this.isSourceEditor()) {
         var1 = LdapStructureWizard.newSourceWizard(this.toEdit, this.explorerSupport);
      } else {
         var1 = LdapStructureWizard.newTargetWizard(this.toEdit, this.explorerSupport);
      }

      return var1;
   }

   private boolean isSourceEditor() {
      return this.sourceTarget == SourceTarget.Source;
   }

   private static final class Memento extends EditorMemento {
      public Memento(Transformation var1, SourceTarget var2) {
         super(var1, var2);
      }
   }

   private static class WizardCallback implements WizardListener<LdapStructure> {
      private final CountDownLatch latch = new CountDownLatch(1);
      private LdapStructure newStructure;

      private WizardCallback() {
      }

      public void wizardCompleted(LdapStructure var1) {
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

      public LdapStructure getNewStructure() {
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
