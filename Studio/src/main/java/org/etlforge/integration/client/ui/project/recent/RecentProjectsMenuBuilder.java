package org.etlforge.integration.client.ui.project.recent;

import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuContribution;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class RecentProjectsMenuBuilder {
   private static final MenuExtensionPoint FILE_MENU_EXTENSION_POINT = MenuExtensionPoint.EXTENSION_POINT_3;
   private static final String CONTRIBUTION_ID = "File.Projects";
   private final ApplicationWindow window;
   private final Receiver<ProjectLocation> selectionHandler;
   private MenuContribution contribution;

   public RecentProjectsMenuBuilder(ApplicationWindow var1, Receiver<ProjectLocation> var2) {
      this.window = var1;
      this.selectionHandler = var2;
      this.contribution = new MenuContribution("File.Projects");
   }

   public void rebuild(Iterable<ProjectLocation> var1) {
      this.removeAll();
      this.addLocations(var1);
      this.publish();
   }

   private void addLocations(Iterable<ProjectLocation> var1) {
      for (ProjectLocation var3 : var1) {
         this.addRecentProject(var3);
      }
   }

   private void removeAll() {
      Menu var1 = this.getFileMenu();
      var1.removeExtension(FILE_MENU_EXTENSION_POINT, "File.Projects");
      this.contribution = new MenuContribution("File.Projects");
   }

   private void addRecentProject(ProjectLocation var1) {
      int var2 = this.contribution.size() + 1;
      if (var2 <= 10) {
         RecentProjectsMenuBuilder.LoadProjectAction var3 = new RecentProjectsMenuBuilder.LoadProjectAction(var1, var2);
         this.contribution.addItem(new KongaMenuItem(var3));
      }
   }

   private void publish() {
      Menu var1 = this.getFileMenu();
      var1.addExtension(FILE_MENU_EXTENSION_POINT, this.contribution);
      JMenu var2 = this.getFileJMenu();
      if (var2 != null) {
         var1.populate(var2);
      }
   }

   private Menu getFileMenu() {
      return this.window.getMenuManager().getMenu("menu.File");
   }

   private JMenu getFileJMenu() {
      return this.window.getMenuManager().getJMenu("menu.File");
   }

   private class LoadProjectAction extends AbstractAction {
      private final ProjectLocation projectLocation;

      public LoadProjectAction(ProjectLocation var2, int var3) {
         this.projectLocation = var2;
         String var4 = this.getName(var2, var3);
         ActionDecorators.decorateWithNameAndMnemonic(this, var4);
      }

      @Override
      public Object getValue(String var1) {
         return "ShortDescription".equals(var1) ? this.createToolTip() : super.getValue(var1);
      }

      private String getName(ProjectLocation var1, int var2) {
         String var3 = "";
         if (var2 < 10) {
            var3 = "&" + var2 + " ";
         }

         return var3 + ApplicationResources.MNEMONIC_FACTORY.escape(var1.getProjectName());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RecentProjectsMenuBuilder.this.selectionHandler.handle(LoadProjectAction.this.projectLocation);
            }
         });
      }

      private String createToolTip() {
         LinkedHashMap var1 = Maps.newLinkedHashMap();
         var1.put(Strings.get("Project.Name") + ":", this.projectLocation.getProjectName());
         var1.put(Strings.get("Project.Location") + ":", this.projectLocation.getLocationPath());
         var1.put(Strings.get("Project.LastAccessed") + ":", new DateCell(this.projectLocation.getLastAccessed()).getDisplayString());
         var1.put(Strings.get("Project.RequiresVersion") + ":", this.projectLocation.getVersionRequired());
         return KeyValueToolTip.createTable(var1);
      }
   }
}
