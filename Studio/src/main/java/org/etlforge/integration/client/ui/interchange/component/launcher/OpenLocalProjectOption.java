package org.etlforge.integration.client.ui.interchange.component.launcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.project.IntegrationProjectFactory;
import org.etlforge.integration.client.ui.project.browser.ProjectBrowser;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsViewerAction;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class OpenLocalProjectOption extends AbstractLaunchOption {
   private final ProjectManager projectManager;
   private final ProjectBrowser browser;
   private ProjectLocation projectToOpen;
   private volatile boolean isOpeningProject;

   public OpenLocalProjectOption(ProjectManager var1, List<RecentProjectsViewerAction> var2) {
      super(Strings.get("Launcher.Option.Local.Name"), ApplicationResources.getIcon("Browse.32"));
      this.projectManager = var1;
      this.browser = this.createBrowser(var2);
      this.browser.setOpenHandler(new Receiver<ProjectLocation>() {
         public void handle(ProjectLocation var1) {
            OpenLocalProjectOption.this.openProject(var1);
         }
      });
   }

   private ProjectBrowser createBrowser(List<RecentProjectsViewerAction> var1) {
      RecentProjects var2 = this.projectManager.getRecentProjects();
      ProjectBrowser var3 = new ProjectBrowser(var2, false, var1);
      var3.setCurrentProjectLocation(this.projectManager.getCurrentProjectLocation());
      var3.setDefaultSearchFolder(this.projectManager.getProjectDirectory().getLocation());
      return var3;
   }

   private void openProject(ProjectLocation var1) {
      if (!this.isOpeningProject) {
         this.projectToOpen = var1;
         this.launchProject();
         ApplicationMessageEvent.postInformationMessage(this, " ");
         this.isOpeningProject = true;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      OpenLocalProjectOption.Layout var2 = new OpenLocalProjectOption.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.browser.requestFocus();
   }

   @Override
   public void activated() {
      this.requestFocus();
   }

   @Override
   public boolean needsExternalScroll() {
      return false;
   }

   @Override
   public void wakeUp() {
      this.isOpeningProject = false;
      this.browser.refresh();
      this.browser.setCurrentProjectLocation(this.projectManager.getCurrentProjectLocation());
   }

   @Override
   public boolean canBeDefaultChoice() {
      RecentProjects var1 = this.projectManager.getRecentProjects();
      int var2 = var1.getNumberOfRecentProjects();
      return var2 > 0;
   }

   @Override
   public IntegrationProject getProject() throws Exception {
      try {
         IntegrationProjectFactory var1 = new IntegrationProjectFactory(this.projectManager);
         return var1.loadExistingProject(this.projectToOpen);
      } finally {
         this.isOpeningProject = false;
      }
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 5);

      public Layout() {
         this.layoutCaption();
         this.layoutTable();
         this.layout.deeplyNonOpaque();
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      private void layoutCaption() {
         JLabel var1 = OpenLocalProjectOption.this.createCaption();
         this.layout.north(var1);
      }

      private void layoutTable() {
         OpenLocalProjectOption.TablePanel var1 = new OpenLocalProjectOption.TablePanel(OpenLocalProjectOption.this.browser);
         this.layout.center(var1);
      }
   }

   private static class TablePanel extends JPanel {
      private final int screenBasedWidth;

      public TablePanel(UiProvider var1) {
         super(new BorderLayout());
         int var2 = Toolkit.getDefaultToolkit().getScreenSize().width;
         this.screenBasedWidth = Math.min(1000, var2 - 200);
         ContainerCanvas.decorate(var1, this, "Center");
         this.setOpaque(false);
      }

      @Override
      public Dimension getPreferredSize() {
         Dimension var1 = super.getPreferredSize();
         var1.width = Math.max(var1.width, this.screenBasedWidth);
         return var1;
      }
   }
}
