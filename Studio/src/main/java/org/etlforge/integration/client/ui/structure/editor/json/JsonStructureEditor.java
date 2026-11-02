package org.etlforge.integration.client.ui.structure.editor.json;

import com.google.common.base.Supplier;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.EditorMemento;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.ui.UiThreadSupplier;

public final class JsonStructureEditor implements StructureEditor {
   private final JsonStructure toEdit;
   private final XsdFileStore clientXsdFiles;
   private final SourceTarget sourceTarget;

   public JsonStructureEditor(JsonStructure var1, XsdFileStore var2, SourceTarget var3) {
      this.toEdit = var1;
      this.clientXsdFiles = var2;
      this.sourceTarget = var3;
   }

   @Override
   public EditedStructure edit(Wizard var1) {
      JsonStructureEditor.WizardCallback var2 = new JsonStructureEditor.WizardCallback();
      JsonStructureWizard var3 = this.startWizard(var1, var2);
      return this.completeWizard(var3, var2);
   }

   private JsonStructureWizard startWizard(final Wizard var1, final JsonStructureEditor.WizardCallback var2) {
      return UiThreadSupplier.call(new Supplier<JsonStructureWizard>() {
         public JsonStructureWizard get() {
            JsonStructureWizard var1x = new JsonStructureWizard(JsonStructureEditor.this.toEdit, JsonStructureEditor.this.clientXsdFiles);
            var1x.addWizardListener(var2);
            var1.addNestedWizard(var1x);
            var1.start();
            return var1x;
         }
      });
   }

   private EditedStructure completeWizard(JsonStructureWizard var1, JsonStructureEditor.WizardCallback var2) {
      var2.waitUntilDone();
      if (!Thread.currentThread().isInterrupted()) {
         JsonStructure var3 = var1.getCreatedStructure();
         if (var3 != null && var3 != this.toEdit) {
            return new JsonStructureEditor.EditedStructureImpl(var3);
         }
      }

      return null;
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new JsonStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof JsonStructureEditor.Memento) {
         ((JsonStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   private class EditedStructureImpl implements EditedStructure {
      private final JsonStructure jsonStruct;

      public EditedStructureImpl(JsonStructure var2) {
         this.jsonStruct = var2;
      }

      @Override
      public void apply(Transformation var1) {
         if (JsonStructureEditor.this.sourceTarget == SourceTarget.Source) {
            var1.setInputStructure(this.jsonStruct);
         } else {
            var1.setOutputStructure(this.jsonStruct);
         }
      }
   }

   private static final class Memento extends EditorMemento {
      public Memento(Transformation var1, SourceTarget var2) {
         super(var1, var2);
      }
   }

   private static class WizardCallback implements WizardListener<JsonStructure> {
      private final CountDownLatch latch = new CountDownLatch(1);

      private WizardCallback() {
      }

      public void wizardCompleted(JsonStructure var1) {
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

      public void waitUntilDone() {
         try {
            this.latch.await();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }
      }
   }
}
