package org.etlforge.integration.client.ui.interchange.content;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.window.AbstractWindowElement;
import org.etlforge.application.ui.window.AddContentCallback;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementResource;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.ui.interchange.HotKeyActivator;
import org.etlforge.integration.client.ui.interchange.InterchangeView;
import org.etlforge.integration.client.ui.interchange.component.tree.MultiTreeContentViewer;
import org.etlforge.integration.client.ui.interchange.component.tree.SingleTreeContentViewer;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.ProcessFilterProvider;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitLock;

public final class DefaultProjectContentViewerManager implements ProjectContentViewerManager {
   private final ApplicationWindow appWin;
   private final WindowSection windowSection;
   private InterchangeView view;
   private final ProjectContentViewerFactory factory;
   private SingleTreeContentViewer singleTreeViewer;
   private MultiTreeContentViewer multiTreeViewer;
   private ProjectContentViewer currentViewer;
   private ProjectContentViewerType type;
   private ProcessFilter processFilter;

   public DefaultProjectContentViewerManager(ApplicationWindow var1, ProjectContentViewerFactory var2) {
      this.appWin = var1;
      this.windowSection = var1.getWindowSection(ApplicationWindow.WEST);
      this.factory = var2;
   }

   public void install(InterchangeView var1) {
      this.view = var1;
      this.factory.setPopupMenuManager(var1.getPopupMenuManager());
      this.currentViewer = this.createNewViewer();
      ProjectContentViewerPreference.INSTANCE.addListener(new DefaultProjectContentViewerManager.TypePreferenceListener());
      this.installHotKeyActivator();
   }

   private ProjectContentViewer createNewViewer() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "createNewViewer");
      this.type = ProjectContentViewerPreference.get();
      ProjectContentViewer var1;
      switch (this.type) {
         case MULTI_TREE:
            assert this.multiTreeViewer == null : "The multi-tree viewer has already been created";
            this.multiTreeViewer = this.factory.newEmptyMultiTree(this.windowSection);
            var1 = this.multiTreeViewer;
            break;
         case TREE:
            assert this.singleTreeViewer == null : "The single-tree viewer has already been created";
            this.singleTreeViewer = this.createSingleTreeViewer();
            var1 = this.singleTreeViewer;
            break;
         default:
            throw new RuntimeException("Unexpected content viewer type: " + this.type);
      }

      var1.addResource(new DefaultProjectContentViewerManager.ViewActivationListener());
      var1.install();
      return var1;
   }

   private SingleTreeContentViewer createSingleTreeViewer() {
      assert this.singleTreeViewer == null : "The single-tree viewer has already been created";
      SingleTreeContentViewer var1 = this.factory.newEmptyTree(this.windowSection);
      this.appWin.getActiveObjectService().addActiveObjectProvider(var1);
      return var1;
   }

   private void installHotKeyActivator() {
      HotKeyActivator.install(this.appWin, new Supplier<ProjectContentViewer>() {
         public ProjectContentViewer get() {
            return DefaultProjectContentViewerManager.this.currentViewer;
         }
      });
   }

   @Override
   public void giveFocusToFilterField() {
      this.currentViewer.giveFocusToFilterField();
   }

   @Override
   public void setProcessFilterProvider(ProcessFilterProvider var1) {
      var1.addMonitor(new Receiver<ProcessFilter>() {
         public void handle(ProcessFilter var1) {
            DefaultProjectContentViewerManager.this.currentViewer.setProcessFilter(var1);
            DefaultProjectContentViewerManager.this.processFilter = var1;
         }
      });
   }

   @Override
   public ProjectContentViewer getViewer() {
      return this.currentViewer;
   }

   @Override
   public WindowSectionContent getWindowContent() {
      return this.currentViewer.getWindowContent();
   }

   @Override
   public void newProjectOpened(IntegrationProject var1) {
      this.currentViewer.newProjectOpened(var1);
   }

   @Override
   public void projectClosed() {
      this.currentViewer.projectClosed();
   }

   @Override
   public void dispose() {
      if (this.singleTreeViewer != null) {
         this.appWin.getActiveObjectService().removeActiveObjectProvider(this.singleTreeViewer);
      }
   }

   private class TypePreferenceListener implements PreferenceListener<ProjectContentViewerType> {
      private TypePreferenceListener() {
      }

      public void valueChanged(Preference<ProjectContentViewerType> var1, ProjectContentViewerType var2, ProjectContentViewerType var3) {
         if (var3 != DefaultProjectContentViewerManager.this.type) {
            this.switchType(var3);
         }
      }

      private void switchType(ProjectContentViewerType var1) {
         assert var1 != DefaultProjectContentViewerManager.this.type;
         if (DefaultProjectContentViewerManager.this.currentViewer != null) {
            WaitLock var2 = DefaultProjectContentViewerManager.this.appWin.startWait();
            EventQueue.invokeLater(DefaultProjectContentViewerManager.this.new TypeSwitcher(var2));
         }
      }
   }

   private class TypeSwitcher implements Runnable, AddContentCallback {
      private final WaitLock waitLock;

      public TypeSwitcher(WaitLock var2) {
         this.waitLock = var2;
      }

      @Override
      public void run() {
         try {
            this.switchToNewContentViewer();
         } catch (Exception var2) {
            this.handleError(var2);
         }
      }

      private void switchToNewContentViewer() {
         WindowSection var1 = DefaultProjectContentViewerManager.this.appWin.getWindowSection(ApplicationWindow.WEST);
         this.uninstallCurrentViewer(var1);
         DefaultProjectContentViewerManager.this.currentViewer = this.createNewViewer();
         var1.addContent(DefaultProjectContentViewerManager.this.currentViewer.getWindowContent(), this);
      }

      private ProjectContentViewer createNewViewer() {
         ProjectContentViewer var1 = DefaultProjectContentViewerManager.this.createNewViewer();
         IntegrationProject var2 = DefaultProjectContentViewerManager.this.view.getProject();
         if (var2 != null) {
            var1.newProjectOpened(DefaultProjectContentViewerManager.this.view.getProject());
         }

         this.installProcessFilter(var1);
         return var1;
      }

      private void installProcessFilter(final ProjectContentViewer var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (DefaultProjectContentViewerManager.this.processFilter != null) {
                  var1.setProcessFilter(DefaultProjectContentViewerManager.this.processFilter);
               }
            }
         });
      }

      private void uninstallCurrentViewer(WindowSection var1) {
         var1.removeContent(DefaultProjectContentViewerManager.this.currentViewer.getWindowContent());
         DefaultProjectContentViewerManager.this.currentViewer.uninstall();
         DefaultProjectContentViewerManager.this.singleTreeViewer = null;
         DefaultProjectContentViewerManager.this.multiTreeViewer = null;
      }

      private void handleError(Exception var1) {
         this.waitLock.release();
         ErrorLog.attention(DefaultProjectContentViewerManager.class, "An unexpected error occurred while switching the project content viewer.", var1);
      }

      @Override
      public void cancelled() {
         this.waitLock.release();
      }

      @Override
      public void contentAdded(WindowSection var1, WindowSectionContent var2) {
         this.waitLock.release();
      }

      @Override
      public void failed(Exception var1) {
         this.waitLock.release();
      }
   }

   private class ViewActivationListener implements WindowElementResource {
      private ViewActivationListener() {
      }

      @Override
      public WindowElementStateSpan getLifeSpan() {
         return WindowElementStateSpan.WHEN_ACTIVE;
      }

      @Override
      public void create(WindowElement var1) {
         DefaultProjectContentViewerManager.this.view.setActive(true);
      }

      @Override
      public void dispose(WindowElement var1) {
         DefaultProjectContentViewerManager.this.view.setActive(false);
      }
   }
}
