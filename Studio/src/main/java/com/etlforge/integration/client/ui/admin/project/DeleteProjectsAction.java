package com.etlforge.integration.client.ui.admin.project;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.etlforge.integration.client.admin.project.DeleteIntegrationProjectResult;
import com.etlforge.integration.client.admin.project.DeleteProjectsCallback;
import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.prefs.DeleteProjectsWarningPreference;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.ui.admin.component.project.ProjectConsolePage;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModelAction;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;

public final class DeleteProjectsAction extends AbstractSingleTypeSelectionAction<IntegrationProject> implements ProjectConsoleModelAction {
   private final ApplicationWindow appWin;
   private final ProjectAdministration serverApi;
   private ProjectConsoleModel model;

   public DeleteProjectsAction(ApplicationWindow var1, ProjectAdministration var2) {
      super(IntegrationProject.class, EnterpriseAdminResources.getString("DeleteProjectsAction.Name"), CommonIcons.DELETE_16);
      this.appWin = var1;
      this.serverApi = var2;
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("DeleteProjectsAction.ToolTip"));
      this.putValue("KongaMediumIconKey", CommonIcons.DELETE_24);
      this.putValue("SwingLargeIconKey", ApplicationResources.ICONS.getIcon("Delete.32"));
   }

   @Override
   public void setProjectCache(ProjectInfoCache var1) {
   }

   @Override
   public void setModel(ProjectConsoleModel var1) {
      this.model = var1;
   }

   @Override
   protected void run(final List<IntegrationProject> var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeleteProjectsAction.this.confirmAndDelete(var1);
         }
      });
   }

   private void confirmAndDelete(List<IntegrationProject> var1) {
      DoNotShowAgainQuestion var2 = new DoNotShowAgainQuestion(
         this.getPromptUi(var1),
         ApplicationResources.STRINGS.getString("AreYouSure"),
         DeleteProjectsWarningPreference.INSTANCE,
         Alert.Option.YES_NO,
         Alert.Option.YES,
         Alert.Type.WARNING
      );
      if (var2.ask(this.appWin.getFrame())) {
         DeleteProjectsAction.Deleter var3 = new DeleteProjectsAction.Deleter(var1);
         var3.submit();
      }
   }

   private UiProvider getPromptUi(List<IntegrationProject> var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
      var2.north(EnterpriseAdminResources.getString("DeleteProjects.Prompt"));
      var2.center(new IntegrationEntityList(var1));
      return var2;
   }

   private class Deleter extends UiJob {
      private final DeleteProjectsCallback callback = new DeleteProjectsAction.Deleter.Callback();
      private final List<IntegrationProject> projects;

      public Deleter(List<IntegrationProject> var2) {
         super(EnterpriseAdminResources.getString("DeleteProjects.Job.Name"));
         this.projects = var2;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         DeleteProjectsAction.this.model.setTalkingToServer(true);
         DeleteProjectsAction.this.serverApi.deleteProjects(this.getProjectIds(), this.callback);
      }

      private List<IntegrationProjectId> getProjectIds() {
         ArrayList var1 = Lists.newArrayList();

         for (IntegrationProject var3 : this.projects) {
            var1.add(var3.getID());
         }

         return var1;
      }

      @Override
      protected void onJobCancelled() {
         this.callback.cancelled();
      }

      private Map<IntegrationProjectId, String> createProjectNameMap() {
         HashMap var1 = Maps.newHashMap();

         for (IntegrationProject var3 : this.projects) {
            var1.put(var3.getID(), var3.getName());
         }

         return var1;
      }

      private class Callback implements DeleteProjectsCallback {
         private Callback() {
         }

         @Override
         public void projectsWereDeleted(final List<DeleteIntegrationProjectResult> var1, final ServerInfo var2) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  DeleteProjectsAction.this.model.setTalkingToServer(false);
                  ProjectConsolePage var1x = Callback.this.getProjectConsole();
                  Callback.this.displayResult(var1, var1x);
                  if (var1x != null) {
                     var1x.refreshIfStillConnected(var2.getGuid());
                  }
               }
            });
         }

         private void displayResult(List<DeleteIntegrationProjectResult> var1, ProjectConsolePage var2) {
            DeleteProjectResultPresenter var3 = new DeleteProjectResultPresenter(var2, Deleter.this.createProjectNameMap());
            var3.show(var1);
         }

         @Override
         public void cancelled() {
            DeleteProjectsAction.this.model.setTalkingToServer(false);
         }

         @Override
         public void caught(Throwable var1) {
            DeleteProjectsAction.this.model.setTalkingToServer(false);
            ErrorLog.attention(DeleteProjectsAction.class, EnterpriseAdminResources.getString("DeleteProjects.Error"), var1);
         }

         private ProjectConsolePage getProjectConsole() {
            for (Editor var4 : DeleteProjectsAction.this.appWin.getEditorService().getOpenEditors()) {
               if (var4 instanceof ProjectConsolePage) {
                  return (ProjectConsolePage)var4;
               }
            }

            return null;
         }
      }
   }
}
