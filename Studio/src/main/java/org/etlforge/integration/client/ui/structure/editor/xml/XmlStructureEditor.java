package org.etlforge.integration.client.ui.structure.editor.xml;

import com.google.common.base.Supplier;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.EditorMemento;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizard;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.ui.UiThreadSupplier;

public final class XmlStructureEditor implements StructureEditor {
   private final XmlStructure toEdit;
   private final XsdFileStore xsdFileStore;
   private final SourceTarget sourceTarget;
   private boolean isEdi;

   public XmlStructureEditor(XmlStructure var1, XsdFileStore var2, SourceTarget var3) {
      this.toEdit = var1;
      this.xsdFileStore = var2;
      this.sourceTarget = var3;
   }

   public void useForEdi() {
      this.isEdi = true;
   }

   @Override
   public EditedStructure edit(Wizard var1) {
      XmlStructureEditor.WizardCallback var2 = new XmlStructureEditor.WizardCallback();
      XmlStructureWizard var3 = this.startWizard(var1, var2);
      return this.completeWizard(var3, var2);
   }

   private XmlStructureWizard startWizard(final Wizard var1, final XmlStructureEditor.WizardCallback var2) {
      return UiThreadSupplier.call(new Supplier<XmlStructureWizard>() {
         public XmlStructureWizard get() {
            XmlStructureWizard var1x = XmlStructureEditor.this.createStructureWizard(var1, XmlStructureEditor.this.toEdit);
            var1x.addWizardListener(var2);
            var1.addNestedWizard(var1x);
            var1.start();
            return var1x;
         }
      });
   }

   private XmlStructureWizard createStructureWizard(Wizard var1, XmlStructure var2) {
      XmlStructureWizard var3 = new XmlStructureWizard(var1, var2, this.sourceTarget, this.xsdFileStore);
      if (this.isEdi) {
         var3.useForEdi();
      }

      return var3;
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new XmlStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof XmlStructureEditor.Memento) {
         ((XmlStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   private EditedStructure completeWizard(XmlStructureWizard var1, XmlStructureEditor.WizardCallback var2) {
      var2.waitUntilDone();
      if (!Thread.currentThread().isInterrupted()) {
         XmlStructure var3 = var1.getCreatedStructure();
         if (var3 != null && var3 != this.toEdit) {
            return new XmlStructureEditor.EditedStructureImpl(var3);
         }
      }

      return null;
   }

   private class EditedStructureImpl implements EditedStructure {
      private final XmlStructure xmlStruct;

      public EditedStructureImpl(XmlStructure var2) {
         this.xmlStruct = var2;
      }

      @Override
      public void apply(Transformation var1) {
         if (XmlStructureEditor.this.sourceTarget == SourceTarget.Source) {
            var1.setInputStructure(this.xmlStruct);
         } else {
            var1.setOutputStructure(this.xmlStruct);
         }
      }
   }

   private static final class Memento extends EditorMemento {
      public Memento(Transformation var1, SourceTarget var2) {
         super(var1, var2);
      }
   }

   private static class WizardCallback implements WizardListener<XmlStructure> {
      private final CountDownLatch latch = new CountDownLatch(1);

      private WizardCallback() {
      }

      public void wizardCompleted(XmlStructure var1) {
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
