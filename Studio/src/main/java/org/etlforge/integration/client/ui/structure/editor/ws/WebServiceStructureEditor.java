package org.etlforge.integration.client.ui.structure.editor.ws;

import com.google.common.base.Supplier;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionWizard;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.EditorMemento;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.WebServiceStructure;
import org.etlforge.ui.UiThreadSupplier;

public final class WebServiceStructureEditor implements StructureEditor {
   private final WebServiceStructure toEdit;
   private final SourceTarget sourceTarget;
   private final EntityExplorerSupport explorerSupport;

   public WebServiceStructureEditor(WebServiceStructure var1, SourceTarget var2, EntityExplorerSupport var3) {
      this.toEdit = var1;
      this.explorerSupport = var3;
      this.sourceTarget = var2;
   }

   @Override
   public EditedStructure edit(Wizard var1) {
      WebServiceStructureEditor.WizardCallback var2 = new WebServiceStructureEditor.WizardCallback();
      EntitySelectionWizard var3 = this.startWizard(var1, var2);
      var2.waitUntilDone();
      if (!Thread.currentThread().isInterrupted()) {
         WebServiceCall var4 = (WebServiceCall)var3.getSelectedEntity();
         if (var4 != null && !var4.getID().equals(this.toEdit.getFunctionCallId())) {
            return new WebServiceStructureEditor.EditedStructureImpl(var4);
         }
      }

      return null;
   }

   private EntitySelectionWizard<WebServiceCall> startWizard(final Wizard var1, final WebServiceStructureEditor.WizardCallback var2) {
      return UiThreadSupplier.call(new Supplier<EntitySelectionWizard<WebServiceCall>>() {
         public EntitySelectionWizard<WebServiceCall> get() {
            EntitySelectionWizard var1x = WebServiceStructureEditor.this.createSelectionWizard();
            var1x.addWizardListener(var2);
            var1.addNestedWizard(var1x);
            var1.start();
            return var1x;
         }
      });
   }

   private EntitySelectionWizard<WebServiceCall> createSelectionWizard() {
      EntitySelectionWizard var1 = new EntitySelectionWizard<>(WebServiceCall.class, EntityType.WebServiceCall, this.explorerSupport);
      var1.setSelectedEntityId(this.toEdit.getFunctionCallId());
      var1.setCaption(StructureEditorResources.getString("WebService.SelectionWizard.SubCaption"));
      return var1;
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new WebServiceStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof WebServiceStructureEditor.Memento) {
         ((WebServiceStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   private class EditedStructureImpl implements EditedStructure {
      private final WebServiceCall wsCall;
      private final boolean isRequest;

      public EditedStructureImpl(WebServiceCall var2) {
         this.wsCall = var2;
         this.isRequest = WebServiceStructureEditor.this.toEdit.getStructureType() == DataStructureType.WebServiceRequest;
      }

      @Override
      public void apply(Transformation var1) {
         boolean var2 = WebServiceStructureEditor.this.sourceTarget == SourceTarget.Source;
         WebServiceStructure var3 = this.isRequest ? this.wsCall.getInputStructure() : this.wsCall.getOutputStructure();
         if (var2) {
            var1.setInputStructure(var3);
         } else {
            var1.setOutputStructure(var3);
         }
      }
   }

   private static final class Memento extends EditorMemento {
      public Memento(Transformation var1, SourceTarget var2) {
         super(var1, var2);
      }

      @Override
      public void restore() {
         super.restore();
      }
   }

   private static class WizardCallback implements WizardListener<WebServiceCall> {
      private final CountDownLatch latch = new CountDownLatch(1);

      private WizardCallback() {
      }

      public void wizardCompleted(WebServiceCall var1) {
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
