package org.etlforge.integration.client.ui.admin.component.project;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Action;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.prefs.DownloadProjectInfoAutomaticallyPreference;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleAction;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModelAction;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleView;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleViewAction;
import org.etlforge.system.Disposable;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class ProjectConsoleController implements Disposable {
   private final ProjectConsoleModel model;
   private final ProjectConsoleView console;
   private final ServerConnectionListener connectionListener;
   private final ServerConnectionService connectionService;
   private final DownloadProjectListAction downloadProjectListAction;
   private final Iterable<ProjectConsoleAction> consoleActions;
   private Predicate<ProjectConsoleAction> consoleActionFilter;
   private ProjectInfoCache cache = ProjectInfoCache.EMPTY;

   public ProjectConsoleController(ProjectConsoleView var1, ProjectConsoleActions var2, ServerConnectionService var3, ProjectInfoProvider var4) {
      this.console = var1;
      this.model = var1.getModel();
      this.connectionService = var3;
      this.connectionListener = new ProjectConsoleController.ConnectionListener();
      this.consoleActions = var2.getActions();
      this.consoleActionFilter = Predicates.alwaysTrue();
      this.downloadProjectListAction = new DownloadProjectListAction(this.model, var4);
      this.prepareActions();
   }

   public void setCache(ProjectInfoCache var1) {
      this.cache = var1;
      this.giveActionsAccessToCache(var1);
   }

   private void giveActionsAccessToCache(ProjectInfoCache var1) {
      this.downloadProjectListAction.setCache(var1);

      for (ProjectConsoleAction var3 : this.consoleActions) {
         Action var4 = var3.getAction();
         if (var4 instanceof ProjectConsoleModelAction) {
            ((ProjectConsoleModelAction)var4).setProjectCache(var1);
         }
      }
   }

   public void activate() {
      this.model.setCurrentServer(this.connectionService.getCurrentServerInfo());
      this.connectionService.addServerConnectionListener(this.connectionListener);
      this.console.setDownloadAction(this.downloadProjectListAction);
      this.console.setConsoleActions(this.getConsoleActions());
      this.console.getProjectsViewer().setContextMenuProvider(new ProjectConsoleController.ContextMenuProvider());
      if (DownloadProjectInfoAutomaticallyPreference.get()) {
         this.downloadProjectInfo(true);
      }
   }

   private List<ProjectConsoleAction> getConsoleActions() {
      return KongaListUtils.filter(this.consoleActions, this.consoleActionFilter);
   }

   public void setDownloadActionName(String var1) {
      this.downloadProjectListAction.putValue("Name", var1);
   }

   public void setDownloadActionDescription(String var1) {
      this.downloadProjectListAction.putValue("ShortDescription", var1);
   }

   public void setConsoleActionFilter(final String var1) {
      if (StringUtils.isEmpty(var1)) {
         this.consoleActionFilter = Predicates.alwaysTrue();
      } else {
         this.consoleActionFilter = new Predicate<ProjectConsoleAction>() {
            public boolean apply(ProjectConsoleAction var1x) {
               return var1x.getCategory().contains(var1);
            }
         };
      }
   }

   @Override
   public void dispose() {
      this.connectionService.removeServerConnectionListener(this.connectionListener);
   }

   public void downloadProjectInfo(boolean var1) {
      GetProjectListWorkflow var2 = new GetProjectListWorkflow(this.console, this.cache, this.downloadProjectListAction);
      var2.getProjectList(var1);
   }

   private void prepareActions() {
      for (ProjectConsoleAction var2 : this.consoleActions) {
         Action var3 = var2.getAction();
         if (var3 instanceof SelectionAction) {
            ((SelectionAction)var3).setSelectionSource(this.console.getProjectsViewer().getSelectionSource());
         }

         if (var3 instanceof ProjectConsoleViewAction) {
            ((ProjectConsoleViewAction)var3).setView(this.console);
         } else if (var3 instanceof ProjectConsoleModelAction) {
            ((ProjectConsoleModelAction)var3).setModel(this.model);
         }
      }
   }

   private class ConnectionListener implements ServerConnectionListener {
      private ConnectionListener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         ProjectConsoleController.this.model.setCurrentServer(var1.getServerInfo());
         if (var1.isConnected() && !ProjectConsoleController.this.model.isDownloadingInfo()) {
            ProjectConsoleController.this.downloadProjectInfo(false);
         }
      }
   }

   private class ContextMenuProvider implements PopupMenuRequestHandler {
      private ContextMenuProvider() {
      }

      @Override
      public boolean popupMenuRequested(final Component var1, MouseEvent var2) {
         final List var3 = ProjectConsoleController.this.getConsoleActions();
         if (var3.isEmpty()) {
            return false;
         }

         final int var4 = var2.getX();
         final int var5 = var2.getY();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ContextMenuProvider.this.createAndShowPopupMenu(var3, var1, var4, var5);
            }
         });
         return true;
      }

      private void createAndShowPopupMenu(List<ProjectConsoleAction> var1, Component var2, int var3, int var4) {
         KongaPopupMenu var5 = null;
         Action var6 = null;

         for (ProjectConsoleAction var8 : var1) {
            Action var9 = var8.getAction();
            if (var9.isEnabled()) {
               if (var5 == null) {
                  var5 = new KongaPopupMenu();
               }

               if (this.isDeleteProjectAction(var9)) {
                  var6 = var9;
               } else {
                  var5.add(var9);
               }
            }
         }

         if (var5 != null) {
            this.addDeleteProject(var5, var6);
            var5.show(var2, var3, var4);
         }
      }

      private boolean isDeleteProjectAction(Action var1) {
         return CommonIcons.DELETE_16 == var1.getValue("SmallIcon");
      }

      private void addDeleteProject(KongaPopupMenu var1, Action var2) {
         if (var2 != null) {
            if (var1.getComponentCount() > 0) {
               var1.addSeparator();
            }

            var1.add(var2);
         }
      }
   }
}
