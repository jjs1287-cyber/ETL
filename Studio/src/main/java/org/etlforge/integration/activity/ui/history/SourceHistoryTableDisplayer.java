package org.etlforge.integration.activity.ui.history;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ui.component.filter.FilterDescriptionPanel;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class SourceHistoryTableDisplayer implements UiProvider {
   private final SourceHistoryModel model;
   private SourceHistoryTableModel tableModel;
   private final SourceHistoryTable table;
   private final SourceHistoryNavigationControl navigationControl;
   private final FilterDescriptionPanel filterDescriptionPanel;
   private DeleteSourceHistoryControlPanel deleteControls;

   public SourceHistoryTableDisplayer(SourceHistoryModel var1, SourceHistoryNavigationControl var2) {
      this.model = var1;
      this.navigationControl = var2;
      this.filterDescriptionPanel = new FilterDescriptionPanel(var2.getFilters());
      this.table = new SourceHistoryTable(var1.isDeleteSupported());
      var1.addPropertyChangeListener("entries", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            SourceHistoryTableDisplayer.this.syncWithModel();
         }
      });
   }

   public void syncWithModel() {
      SourceHistoryTableDisplayer.TableModelProducer var1 = new SourceHistoryTableDisplayer.TableModelProducer();
      SourceHistoryTableDisplayer.TableModelConsumer var2 = new SourceHistoryTableDisplayer.TableModelConsumer();
      Computation var3 = Computation.create(var1, var2);
      var3.swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, this.getClass())
         .withWaitService(this.model.getTalkingToServerWaitService())
         .buildAndExecute();
   }

   public void setDeleteControls(DeleteSourceHistoryControlPanel var1) {
      this.deleteControls = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.northToSouth(this.navigationControl.getDisplayer(), this.layoutTableAndButtons(), this.filterDescriptionPanel);
      var2.decorate(var1);
   }

   private JComponent layoutTableAndButtons() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      var1.center(this.table);
      if (this.deleteControls != null) {
         var1.south(this.deleteControls.getUi());
      }

      return var1.container();
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public String getTitle() {
      return this.navigationControl.getTitle();
   }

   public void setDisplayedSubject(ActivitySubject var1) {
      this.navigationControl.setDisplayedSubject(var1);
   }

   public void setSubjectIcon(Icon var1) {
      this.navigationControl.setSubjectIcon(var1);
   }

   private class TableModelConsumer implements Receiver<SourceHistoryTableModel> {
      private TableModelConsumer() {
      }

      public void handle(SourceHistoryTableModel var1) {
         if (SourceHistoryTableDisplayer.this.tableModel != null) {
            SourceHistoryTableDisplayer.this.tableModel.removeDeleteSelectionListener(SourceHistoryTableDisplayer.this.model);
         }

         var1.addDeleteSelectionListener(SourceHistoryTableDisplayer.this.model);
         SourceHistoryTableDisplayer.this.table.setModel(var1);
         if (SourceHistoryTableDisplayer.this.deleteControls != null) {
            SourceHistoryTableDisplayer.this.deleteControls.setTableModel(var1);
         }

         SourceHistoryTableDisplayer.this.tableModel = var1;
         SourceHistoryTableDisplayer.this.table.requestFocusInWindow();
      }
   }

   private class TableModelProducer implements Callable<SourceHistoryTableModel> {
      private TableModelProducer() {
      }

      public SourceHistoryTableModel call() throws Exception {
         boolean var1 = SourceHistoryTableDisplayer.this.model.isDeleteSupported();
         SourceHistoryTableModel var2 = SourceHistoryTableModel.emptyModel(var1);
         var2.addEntries(SourceHistoryTableDisplayer.this.model.getFilteredList());
         return var2;
      }
   }
}
