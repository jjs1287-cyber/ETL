package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.application.ui.window.action.AbstractActiveObjectAction;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.ui.interchange.CurrentProjectListenerResource;
import org.etlforge.integration.client.ui.project.permissions.PermissionsView;
import org.etlforge.integration.client.ui.project.permissions.PermissionsViewFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;

public final class SetPermissionsAction extends AbstractActiveObjectAction {
   public static final String ID = "action.ic.Permissions";
   private final ApplicationWindow appWin;
   private final CurrentProjectService currentProjectService;
   private final UserManager userManager;

   public SetPermissionsAction(ApplicationWindow var1, CurrentProjectService var2, UserManager var3) {
      super(var1.getActiveObjectService(), "action.ic.Permissions", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.currentProjectService = var2;
      this.userManager = var3;
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof IntegrationEntity && !(var1 instanceof RootFolder);
   }

   @Override
   protected void updateEnabledState(List<? extends Object> var1) {
      this.setEnabled(var1 != null && !var1.isEmpty());
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SetPermissionsAction.this.openPermissionsView();
         }
      });
   }

   private void openPermissionsView() {
      PermissionsViewFactory var1 = new PermissionsViewFactory(this.userManager);
      WindowSectionContentUpdater var2 = new WindowSectionContentUpdater() {
         @Override
         public void update(WindowSectionContent var1) {
            ((PermissionsView)var1).setAccessedEntity(SetPermissionsAction.this.getAccessedEntity());
         }
      };
      this.appWin.displayContent(new SetPermissionsAction.ViewWrapper(var1), ApplicationWindow.SOUTH, var2, null);
   }

   private IntegrationEntity getAccessedEntity() {
      List var1 = this.getActiveObjects(IntegrationEntity.class);
      if (var1.isEmpty()) {
         return null;
      } else {
         IntegrationEntity var2 = (IntegrationEntity)var1.get(0);
         if (!var2.isEditable()) {
            Alert.error("Permissions cannot be set for this item.", "Not Editable");
            return null;
         } else {
            return var2;
         }
      }
   }

   private class ViewWrapper implements WindowSectionContentProvider {
      private final PermissionsViewFactory delegate;
      private PermissionsView view;

      public ViewWrapper(PermissionsViewFactory var2) {
         this.delegate = var2;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         assert this.view == null : "This WindowContentProvider has already provided its content";
         this.view = this.delegate.getContent(var1);
         ManagedProject var2 = SetPermissionsAction.this.currentProjectService.getManagedProject();
         if (var2 != null) {
            this.view.setPermissions(var2.getPermissions());
         }

         this.view.setAccessedEntity(SetPermissionsAction.this.getAccessedEntity());
         this.view.addResource(new SetPermissionsAction.ViewWrapper.ProjectListening());
         return this.view;
      }

      @Override
      public String getContentId() {
         return this.delegate.getContentId();
      }

      private class ProjectListening extends CurrentProjectListenerResource {
         public ProjectListening() {
            super(SetPermissionsAction.this.currentProjectService);
         }

         @Override
         public void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
            if (ViewWrapper.this.view != null) {
               boolean var2 = ViewWrapper.this.view.setAccessedEntity(null);
               if (!var2) {
                  throw new CloseProjectVetoException("The user declined the close request.");
               }

               ViewWrapper.this.view.setPermissions(null);
            }
         }

         @Override
         public void projectOpened(CurrentProjectEvent var1) {
            if (ViewWrapper.this.view != null) {
               ManagedProject var2 = var1.getManagedProject();
               ViewWrapper.this.view.setPermissions(var2.getPermissions());
               ViewWrapper.this.view.setAccessedEntity(null);
            }
         }
      }
   }
}
