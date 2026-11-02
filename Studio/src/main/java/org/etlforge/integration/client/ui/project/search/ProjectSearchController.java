package org.etlforge.integration.client.ui.project.search;

import com.google.common.base.Predicate;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectSearcher;
import org.etlforge.integration.client.ui.project.location.ProjectLocationsTableModel;
import org.etlforge.io.Folder;
import org.etlforge.system.callback.Receiver;

public final class ProjectSearchController {
   private ProjectSearchController.SearchProcess searcher;
   private ProjectSearchUi view;
   private final ProjectLocationsTableModel resultModel;

   public ProjectSearchController(ProjectLocationsTableModel var1) {
      this.resultModel = var1;
   }

   public void setView(ProjectSearchUi var1) {
      this.view = var1;
   }

   public void startSearch(Folder var1, Predicate<String> var2) {
      if (this.searcher == null) {
         this.resultModel.deleteAll();
         this.searcher = new ProjectSearchController.SearchProcess(var1, var2);
         this.searcher.execute();
         this.view.setSearching(true);
      }
   }

   public void stopSearch() {
      if (this.searcher != null) {
         this.searcher.stop();
         this.searchCompleted();
      }
   }

   private void searchCompleted() {
      this.searcher = null;
      this.view.setSearching(false);
      this.view.searchIsDone();
   }

   private class SearchProcess extends SwingWorker<Void, ProjectLocation> implements Receiver<ProjectLocation> {
      private final Folder root;
      private final ProjectSearcher searcher;
      private final ProjectSearchController.StatusPublisher statusPublisher;

      public SearchProcess(Folder var2, Predicate<String> var3) {
         this.root = var2;
         this.searcher = new ProjectSearcher();
         this.searcher.setNameFilter(var3);
         this.statusPublisher = ProjectSearchController.this.new StatusPublisher(this.searcher);
      }

      protected Void doInBackground() throws Exception {
         this.startStatusUpdate();
         this.searcher.search(this.root, this);
         return null;
      }

      private void startStatusUpdate() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SearchProcess.this.statusPublisher.start();
            }
         });
      }

      @Override
      protected void process(List<ProjectLocation> var1) {
         for (ProjectLocation var3 : var1) {
            ProjectSearchController.this.resultModel.addRow(var3);
         }
      }

      @Override
      protected void done() {
         this.statusPublisher.stop();
         ProjectSearchController.this.searchCompleted();
      }

      public void handle(ProjectLocation var1) {
         this.publish(var1);
      }

      public void stop() {
         this.searcher.stop();
         this.cancel(true);
      }
   }

   private class StatusPublisher {
      private final ProjectSearcher searcher;
      private Timer timer;

      public StatusPublisher(ProjectSearcher var2) {
         this.searcher = var2;
      }

      public void start() {
         if (this.timer == null) {
            this.timer = new Timer(125, new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1) {
                  ProjectSearchController.this.view.showStatus(StatusPublisher.this.searcher.getCurrentFolder());
               }
            });
            this.timer.start();
         }
      }

      public void stop() {
         if (this.timer != null) {
            this.timer.stop();
            this.timer = null;
         }
      }
   }
}
