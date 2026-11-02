package org.etlforge.integration.client.ui.structure.editor.text;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionWizard;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.EditorMemento;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.ui.UiThreadSupplier;

public final class TextStructureEditor implements StructureEditor {
   private final TextDocument originalDocument;
   private final SourceTarget sourceTarget;
   private final EntityExplorerSupport explorerSupport;

   public TextStructureEditor(TextDocument var1, SourceTarget var2, EntityExplorerSupport var3) {
      this.originalDocument = var1;
      this.explorerSupport = var3;
      this.sourceTarget = var2;
   }

   @Override
   public EditedStructure edit(Wizard var1) {
      TextStructureEditor.WizardCallback var2 = new TextStructureEditor.WizardCallback();
      this.startWizard(var1, var2);
      TextDocument var3 = var2.getNewDocument();
      return var3 != null && var3 != this.originalDocument ? new TextStructureEditor.EditedStructureImpl(var3) : null;
   }

   private void startWizard(final Wizard var1, final TextStructureEditor.WizardCallback var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EntitySelectionWizard var1x = TextStructureEditor.this.createStructureWizard();
            var1x.addWizardListener(var2);
            var1.addNestedWizard(var1x);
            var1.start();
         }
      });
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new TextStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof TextStructureEditor.Memento) {
         ((TextStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   private EntitySelectionWizard<TextDocument> createStructureWizard() {
      return UiThreadSupplier.call(new Supplier<EntitySelectionWizard<TextDocument>>() {
         public EntitySelectionWizard<TextDocument> get() {
            EntitySelectionWizard var1 = new EntitySelectionWizard<>(TextDocument.class, EntityType.Document, TextStructureEditor.this.explorerSupport);
            var1.setCaption(PackageResources.DocumentSelectionWizard.SUB_CAPTION);
            var1.setSelectedEntity(TextStructureEditor.this.originalDocument);
            return var1;
         }
      });
   }

   private class EditedStructureImpl implements EditedStructure {
      private final TextDocument document;

      public EditedStructureImpl(TextDocument var2) {
         this.document = var2;
      }

      @Override
      public void apply(Transformation var1) {
         TextStructure var2 = this.document.getDataStructure();
         TextDocumentId var3 = this.document.getID();
         if (TextStructureEditor.this.sourceTarget == SourceTarget.Source) {
            var1.setInputStructure(var2);
            var1.setSourceTextDocumentId(var3);
         } else {
            var1.setOutputStructure(var2);
            var1.setTargetTextDocumentId(var3);
         }
      }
   }

   private static final class Memento extends EditorMemento {
      private final TextDocumentId docId;

      public Memento(Transformation var1, SourceTarget var2) {
         super(var1, var2);
         if (var2 == SourceTarget.Source) {
            this.docId = var1.getSourceTextDocumentId();
         } else {
            this.docId = var1.getTargetTextDocumentId();
         }
      }

      @Override
      public void restore() {
         super.restore();
         if (this.getUse() == SourceTarget.Source) {
            this.getTransformation().setSourceTextDocumentId(this.docId);
         } else {
            this.getTransformation().setTargetTextDocumentId(this.docId);
         }
      }
   }

   private static class WizardCallback implements WizardListener<TextDocument> {
      private final CountDownLatch latch = new CountDownLatch(1);
      private TextDocument doc;

      private WizardCallback() {
      }

      public void wizardCompleted(TextDocument var1) {
         this.doc = var1;
         this.latch.countDown();
      }

      @Override
      public void wizardCancelled() {
         this.latch.countDown();
      }

      @Override
      public void wizardFailed(Throwable var1) {
         this.latch.countDown();
      }

      public TextDocument getNewDocument() {
         try {
            this.latch.await();
            return this.doc;
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return null;
         }
      }
   }
}
