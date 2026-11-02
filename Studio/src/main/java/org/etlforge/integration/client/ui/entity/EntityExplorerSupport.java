package org.etlforge.integration.client.ui.entity;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.client.ui.entity.wizard.EditorPanelService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;

public abstract class EntityExplorerSupport implements EntityPageLocator {
   public abstract ApplicationWindow getWindow();

   public abstract IntegrationProject getProject();

   public abstract Editor createPage(IntegrationEntity var1);

   public abstract Editor openPageFor(IntegrationEntity var1);

   public final void editInModalDialog(IntegrationEntity var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "editInModalDialog");
      EditorPanel var2 = new EditorPanel(this.getWindow());
      EditorPanelService var3 = new EditorPanelService(this, var2, true);
      var3.openEditorFor(var1);
   }

   @Override
   public abstract Editor findOpenPage(IntegrationEntity var1);

   public abstract UiEntityFactory getEntityFactory();

   public final Action getCreateNewEntityAction(EntityType var1, Request.Source var2, UiEntityFactoryCallback var3) {
      Request var4 = Request.builder(var1).from(var2).openEditor(false).build();
      return new EntityExplorerSupport.CreateNewAction(this, var4, var3);
   }

   public final SelectionAction getEditSelectedEntityAction(EntityType var1) {
      return new EntityExplorerSupport.EditAction(this, var1);
   }

   private static final class CreateNewAction extends AbstractAction {
      private final EntityExplorerSupport explorer;
      private final Request request;
      private final UiEntityFactoryCallback callback;

      public CreateNewAction(EntityExplorerSupport var1, Request var2, UiEntityFactoryCallback var3) {
         super(EntityResources.getString("EntityExplorer.New.Name"));
         this.explorer = var1;
         this.request = var2;
         this.callback = var3;
         this.setEnabled(var1.getEntityFactory() != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ApplicationWorker var2 = Application.getWorker();
         var2.submitForParallel(new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               UiEntityFactory var1x = CreateNewAction.this.explorer.getEntityFactory();
               var1x.createEntity(CreateNewAction.this.request, CreateNewAction.this.callback);
            }
         });
      }
   }

   private static final class EditAction extends AbstractSingleObjectSelectionAction<IntegrationEntity> {
      private final EntityExplorerSupport explorer;
      private final EntityType entityType;

      public EditAction(EntityExplorerSupport var1, EntityType var2) {
         super(IntegrationEntity.class, ApplicationResources.getString("Edit"));
         this.explorer = var1;
         this.entityType = var2;
      }

      protected boolean isEnabledFor(IntegrationEntity var1) {
         return var1.getEntityType() == this.entityType;
      }

      protected void run(IntegrationEntity var1, ActionEvent var2) {
         this.explorer.openPageFor(var1);
      }
   }
}
