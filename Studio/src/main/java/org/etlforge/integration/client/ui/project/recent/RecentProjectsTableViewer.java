package org.etlforge.integration.client.ui.project.recent;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.project.location.ProjectLocationsTable;
import org.etlforge.integration.client.ui.project.location.ProjectLocationsTableModel;
import org.etlforge.ui.EnabledStateGate;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;

public final class RecentProjectsTableViewer implements RecentProjectsViewer {
   private final RecentProjects recentProjects;
   private ProjectLocationsTable table;
   private ProjectLocationsTableModel model;
   private final RecentProjectsTableViewer.Builder builder;
   private List<RecentProjectsViewerAction> actions = Collections.emptyList();

   public RecentProjectsTableViewer(RecentProjects var1) {
      this.recentProjects = var1;
      this.builder = new RecentProjectsTableViewer.Builder();
      this.update();
   }

   @Override
   public void update() {
      this.builder.update();
   }

   @Override
   public RecentProjects getRecentProjects() {
      return this.recentProjects;
   }

   public void setActions(Iterable<RecentProjectsViewerAction> var1) {
      for (RecentProjectsViewerAction var3 : var1) {
         if (var3 != null) {
            var3.setViewer(this);
         }
      }

      this.actions = Lists.newArrayList(var1);
      this.installTableController();
   }

   private void installTableController() {
      for (Action var2 : this.actions) {
         if (var2 != null) {
            KeyStroke var3 = (KeyStroke)var2.getValue("AcceleratorKey");
            if (this.isActionKeyInstallable(var2, var3)) {
               this.table.installKeyBinding(var2);
            }
         }
      }

      this.table.setPopupMenuRequestHandler(this.getTablePopupMenuHandler());
   }

   private boolean isActionKeyInstallable(Action var1, KeyStroke var2) {
      return var2 != null && (var1 != this.table.getDefaultAction() || !KeyUtils.ENTER.equals(var2));
   }

   private PopupMenuRequestHandler getTablePopupMenuHandler() {
      EnabledStateGate var1 = new EnabledStateGate() {
         @Override
         public boolean isEnabled() {
            return RecentProjectsTableViewer.this.table.getSelectedProject() != null;
         }
      };
      return PopupMenuRequestHandlers.fromActions(this.actions, var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      CaptionLayout.of(this.layoutNorthPart(), this.table).deeplyNonOpaque().decorate(var1);
   }

   private Object layoutNorthPart() {
      return null;
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RecentProjectsTableViewer.this.setInitialSelection();
         }
      });
   }

   private void setInitialSelection() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RecentProjectsTableViewer.this.table.setInitialSelection();
         }
      });
   }

   @Override
   public ProjectLocation getSelectedProject() {
      return this.table.getSelectedProject();
   }

   @Override
   public OrderedSelection getSelection() {
      return (OrderedSelection)this.table.asSelectionSource().getSelection();
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.table.asSelectionSource().addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.table.asSelectionSource().removeSelectionListener(var1);
   }

   private class Builder {
      private Builder() {
      }

      public void update() {
         this.removeAll();
         this.addLocations();
         this.publish();
      }

      private void removeAll() {
         RecentProjectsTableViewer.this.model = new ProjectLocationsTableModel();
      }

      private void addLocations() {
         for (ProjectLocation var2 : RecentProjectsTableViewer.this.recentProjects.getExistingLocations()) {
            this.addRecentProject(var2);
         }
      }

      private void addRecentProject(ProjectLocation var1) {
         try {
            RecentProjectsTableViewer.this.model.addRow(var1);
         } catch (Exception var3) {
            ErrorLog.log(
               RecentProjectsTableViewer.class,
               "An unexpected error occurred when adding the project "
                  + var1.getProjectName()
                  + " at "
                  + var1.getLocationPath()
                  + " to the list of recent projects: "
                  + var3.getMessage(),
               var3
            );
         }
      }

      private void publish() {
         if (RecentProjectsTableViewer.this.table == null) {
            this.createTable(RecentProjectsTableViewer.this.model);
         } else {
            RecentProjectsTableViewer.this.table.setModel(RecentProjectsTableViewer.this.model);
         }
      }

      private void createTable(ProjectLocationsTableModel var1) {
         RecentProjectsTableViewer.this.table = new ProjectLocationsTable(var1);
         RecentProjectsTableViewer.this.table.asSelectionSource().addSelectionListener(RecentProjectsTableViewer.this.new TableListener());
         RecentProjectsTableViewer.this.installTableController();
      }
   }

   private class TableListener implements SelectionListener {
      private TableListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         ProjectLocation var2 = RecentProjectsTableViewer.this.table.getSelectedProject();
         if (var2 == null) {
            this.onNothingSelected();
         } else {
            this.onProjectSelected(var2);
         }
      }

      private void onProjectSelected(ProjectLocation var1) {
         String var2 = var1.getLocationPath();
         var2 = var2 + " - Last Modified: ";
         var2 = var2 + new SimpleDateFormat(Strings.get("Recent.Table.DateFormat")).format(var1.getLastModified());
         ApplicationMessageEvent.postInformationMessage(RecentProjectsTableViewer.this, var2);
      }

      private void onNothingSelected() {
         ApplicationMessageEvent.postInformationMessage(RecentProjectsTableViewer.this, " ");
      }
   }
}
