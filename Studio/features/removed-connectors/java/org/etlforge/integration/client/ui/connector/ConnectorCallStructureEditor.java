package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Supplier;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionWizard;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.client.ui.structure.editor.EditorMemento;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.ui.UiThreadSupplier;

public final class ConnectorCallStructureEditor<T extends IntegrationEntity & ConnectorCall> implements StructureEditor {
   private final Class<T> entityClass;
   private final XsdBasedStructure structureToEdit;
   private final SourceTarget sourceTarget;
   private final EntityExplorerSupport projectExplorer;

   public static <E extends IntegrationEntity & ConnectorCall> ConnectorCallStructureEditor<E> create(
      Class<E> var0, XsdBasedStructure var1, SourceTarget var2, EntityExplorerSupport var3
   ) {
      return new ConnectorCallStructureEditor<>(var0, var1, var2, var3);
   }

   public ConnectorCallStructureEditor(Class<T> var1, XsdBasedStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      this.entityClass = var1;
      this.structureToEdit = var2;
      this.sourceTarget = var3;
      this.projectExplorer = var4;
   }

   @Override
   public EditedStructure edit(Wizard var1) {
      ConnectorCallStructureEditor.WizardCallback var2 = new ConnectorCallStructureEditor.WizardCallback();
      EntitySelectionWizard var3 = this.startWizard(var1, var2);
      var2.waitUntilDone();
      if (!Thread.currentThread().isInterrupted()) {
         IntegrationEntity var4 = var3.getSelectedEntity();
         if (var4 != null && !var4.getID().equals(this.structureToEdit.getFunctionCallId())) {
            return new ConnectorCallStructureEditor.EditedStructureImpl((T)var4);
         }
      }

      return null;
   }

   private EntitySelectionWizard<T> startWizard(final Wizard var1, final ConnectorCallStructureEditor.WizardCallback<T> var2) {
      return UiThreadSupplier.call(new Supplier<EntitySelectionWizard<T>>() {
         public EntitySelectionWizard<T> get() {
            EntitySelectionWizard var1x = ConnectorCallStructureEditor.this.createSelectionWizard();
            var1x.addWizardListener(var2);
            var1.addNestedWizard(var1x);
            var1.start();
            return var1x;
         }
      });
   }

   private EntitySelectionWizard<T> createSelectionWizard() {
      EntityType var1 = this.structureToEdit.getFunctionCallId().getEntityType();
      EntitySelectionWizard var2 = new EntitySelectionWizard<>(this.entityClass, var1, this.projectExplorer);
      var2.setSelectedEntityId(this.structureToEdit.getFunctionCallId());
      var2.setCaption("Select the " + var1.getDisplayName() + " that defines the structure you want to use in the transformation");
      return var2;
   }

   @Override
   public Object getInitialState(Transformation var1) {
      return new ConnectorCallStructureEditor.Memento(var1, this.sourceTarget);
   }

   @Override
   public void restoreInitialState(Object var1) {
      if (var1 instanceof ConnectorCallStructureEditor.Memento) {
         ((ConnectorCallStructureEditor.Memento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento");
      }
   }

   private class EditedStructureImpl implements EditedStructure {
      private final T connectorCall;
      private final boolean isRequest;

      public EditedStructureImpl(T var2) {
         this.connectorCall = (T)var2;
         this.isRequest = ConnectorCallStructureEditor.this.structureToEdit.getStructureType() == DataStructureType.ConnectorCallRequest;
      }

      @Override
      public void apply(Transformation var1) {
         boolean var2 = ConnectorCallStructureEditor.this.sourceTarget == SourceTarget.Source;
         XsdBasedStructure var3 = this.isRequest ? this.connectorCall.getInputStructure() : this.connectorCall.getOutputStructure();
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

   private static class WizardCallback<T extends IntegrationEntity & ConnectorCall> implements WizardListener<T> {
      private final CountDownLatch latch = new CountDownLatch(1);

      private WizardCallback() {
      }

      public void wizardCompleted(T var1) {
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
