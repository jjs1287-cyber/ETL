package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class NewEntityAction extends AbstractProjectBasedAction {
   private final UiEntityFactory factory;
   private final EntityType entityType;
   private boolean openEditor = true;
   private Optional<Supplier<UiEntityFactoryCallback>> callbackProvider = Optional.absent();

   public NewEntityAction(String var1, CurrentProjectService var2, UiEntityFactory var3, EntityType var4) {
      super(var2, var1);
      this.factory = var3;
      this.entityType = var4;
      this.putValue("ShortDescription", "Create a new " + var4.getDisplayName());
      this.setEnabled(this.getProject() != null);
   }

   public void setOpenEditor(boolean var1) {
      this.openEditor = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NewEntityAction.this.createEntity();
         }
      });
   }

   private void createEntity() {
      this.setEnabled(false);
      Request var1 = Request.builder(this.entityType).openEditor(this.openEditor).build();
      this.factory.createEntity(var1, new NewEntityAction.CallbackImpl());
   }

   @Override
   protected void invokedWhenDisabled() {
      if (this.getProject() == null) {
         Alert.error("Please open a project first.", "No Project Open");
      }
   }

   @Override
   protected void updateEnabledState(IntegrationProject var1) {
      this.setEnabled(this.factory != null);
   }

   public void setCallbackProvider(Supplier<UiEntityFactoryCallback> var1) {
      this.callbackProvider = Optional.of(var1);
   }

   protected UiEntityFactoryCallback getCallback() {
      return new UiEntityFactoryCallback.Adapter();
   }

   private class CallbackImpl extends UiEntityFactoryCallback.Adapter {
      private final UiEntityFactoryCallback wrappee = NewEntityAction.this.callbackProvider.isPresent()
         ? NewEntityAction.this.callbackProvider.get().get()
         : new UiEntityFactoryCallback.Adapter();

      public CallbackImpl() {
      }

      @Override
      public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               boolean var1x = NewEntityAction.this.getProject() != null;
               NewEntityAction.this.setEnabled(var1x);
            }
         });
         return this.wrappee.preProjectInsertion(var1, var2);
      }

      @Override
      public void postProjectInsertion(IntegrationEntity var1) {
         this.wrappee.postProjectInsertion(var1);
      }
   }
}
