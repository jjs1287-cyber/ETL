package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.integration.activity.ui.files.ViewOperationFileAction;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.wait.WaitService;

public final class BulkLoadHistoryView implements UiProvider {
   private final BulkLoadHistoryModel model;
   private final BulkLoadHistoryController controller;
   private final BulkLoadHistoryView.DownloadInstancesAction downloadInstancesAction;
   private final BulkLoadHistoryView.DownloadBatchesAction downloadBatchesAction;
   private final BulkLoadInstanceTable instanceTable;
   private final ViewOperationFileAction viewSuccessFileAction;
   private final ViewOperationFileAction viewFailureFileAction;
   private final WaitStateBinding waitStateBinding;
   private final BatchListView batchList;

   public BulkLoadHistoryView(BulkLoadHistoryModel var1, BulkLoadHistoryController var2) {
      this.model = Preconditions.checkNotNull(var1);
      this.controller = Preconditions.checkNotNull(var2);
      this.waitStateBinding = new BulkLoadHistoryView.WaitStateBindingImpl();
      this.instanceTable = new BulkLoadInstanceTable();
      this.instanceTable.addSelectionListener(new BulkLoadHistoryView.SelectedInstanceListener());
      this.instanceTable.setDefaultAction(new BulkLoadHistoryView.DownloadBatchesAction(true));
      this.batchList = new BatchListView();
      this.downloadInstancesAction = new BulkLoadHistoryView.DownloadInstancesAction();
      this.downloadBatchesAction = new BulkLoadHistoryView.DownloadBatchesAction(false);
      this.downloadBatchesAction.setSelectionSource(this.instanceTable);
      this.viewSuccessFileAction = this.createViewFileAction(FileCategory.SFDC_SUCCESS);
      this.viewFailureFileAction = this.createViewFileAction(FileCategory.SFDC_FAILURE);
      this.instanceTable.installKeyBinding(this.downloadInstancesAction);
      this.batchList.installKeyBinding(this.downloadBatchesAction);
      this.bindToModel();
   }

   private ViewOperationFileAction createViewFileAction(FileCategory var1) {
      ViewOperationFileAction var2 = new ViewOperationFileAction(this.controller, var1);
      var2.setSelectionSource(this.instanceTable);
      return var2;
   }

   private void bindToModel() {
      Bindings var1 = new Bindings(new BulkLoadHistoryView.InstanceBinding(), new BulkLoadHistoryView.TimeZoneBinding(), this.waitStateBinding);
      var1.syncUi();
      this.model.addPropertyChangeListener("batches", new BulkLoadHistoryView.BatchesBinding());
   }

   public void setWaitService(WaitService var1) {
      this.waitStateBinding.setWaitService(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BulkLoadHistoryView.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.instanceTable.requestFocus();
   }

   private class BatchesBinding implements PropertyChangeListener {
      private BatchesBinding() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "batches".equals(var1.getPropertyName());
         final BulkLoadInstance var2 = (BulkLoadInstance)var1.getNewValue();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               BatchesBinding.this.updateUi(var2);
            }
         });
      }

      private void updateUi(BulkLoadInstance var1) {
         if (var1 == BulkLoadHistoryView.this.instanceTable.getSelectedInstance()) {
            BulkLoadHistoryView.this.batchList.display(var1);
            BulkLoadHistoryView.this.batchList.selectFirstRow();
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  BulkLoadHistoryView.this.batchList.requestFocus();
               }
            });
         }
      }
   }

   private class DownloadBatchesAction extends AbstractSingleObjectSelectionAction<BulkLoadInstance> {
      private boolean downloadOnlyIfEmpty;

      public DownloadBatchesAction(boolean var2) {
         super(BulkLoadInstance.class, "Get List of &Batches");
         this.putValue("AcceleratorKey", KeyUtils.REFRESH);
         this.putValue("ShortDescription", "Refresh the list of batches");
      }

      protected boolean isEnabledFor(BulkLoadInstance var1) {
         return !BulkLoadHistoryView.this.model.isTalkingToServer();
      }

      public void updateEnabledState(boolean var1) {
         this.setEnabled(!var1 && this.getSelectedObject() != null);
      }

      protected void run(final BulkLoadInstance var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DownloadBatchesAction.this.downloadBatches(var1);
            }
         });
      }

      private void downloadBatches(BulkLoadInstance var1) {
         boolean var2 = var1.getBatches().isEmpty() || !this.downloadOnlyIfEmpty;
         if (var2) {
            BulkLoadHistoryView.this.controller.downloadBatches(var1);
         }
      }
   }

   private class DownloadInstancesAction extends AbstractAction {
      public DownloadInstancesAction() {
         super("Refresh &History");
         this.putValue("AcceleratorKey", KeyUtils.REFRESH);
         this.putValue("ShortDescription", "Refresh the Result History");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DownloadInstancesAction.this.downloadInstances();
            }
         });
      }

      private void downloadInstances() {
         BulkLoadHistoryView.this.controller.downloadHistory();
      }
   }

   private class InstanceBinding extends AbstractBinding<List<BulkLoadInstance>> {
      public InstanceBinding() {
         super(BulkLoadHistoryView.this.model.getProperty("instances"));
      }

      protected void updateUiWithValue(List<BulkLoadInstance> var1) {
         BulkLoadHistoryView.this.instanceTable.setInstances(var1);
         BulkLoadHistoryView.this.instanceTable.selectFirstRow();
         BulkLoadHistoryView.this.instanceTable.requestFocus();
      }
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 10);

      public Layout() {
         this.layoutInstances();
         this.layout.center(BulkLoadHistoryView.this.batchList);
      }

      private void layoutInstances() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
         BoxBuilder var2 = this.createControlPanel();
         String var3 = this.caption();
         var1.northToSouth(var3, BulkLoadHistoryView.this.instanceTable, var2);
         this.layout.north(var1);
      }

      private BoxBuilder createControlPanel() {
         return BoxBuilder.horizontal()
            .addAllWithSpace(10, BulkLoadHistoryView.this.downloadInstancesAction, BulkLoadHistoryView.this.downloadBatchesAction)
            .glue()
            .addAllWithSpace(10, BulkLoadHistoryView.this.viewSuccessFileAction, BulkLoadHistoryView.this.viewFailureFileAction);
      }

      private String caption() {
         return "Bulk Load History: "
            + BulkLoadHistoryView.this.model.getBulkLoad().getName()
            + " [Double-click on a job to see a list of the individual batches]";
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }

   private class SelectedInstanceListener implements SelectionListener {
      private SelectedInstanceListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         BulkLoadInstance var2 = BulkLoadHistoryView.this.instanceTable.getSelectedInstance();
         if (var2 != null) {
            BulkLoadHistoryView.this.batchList.display(var2);
         }
      }
   }

   private class TimeZoneBinding extends AbstractBinding<TimeZoneMode> {
      public TimeZoneBinding() {
         super(BulkLoadHistoryView.this.model.getProperty("timeZone"));
      }

      protected void updateUiWithValue(TimeZoneMode var1) {
         BulkLoadHistoryView.this.instanceTable.setTimeZone(var1.getTimeZone());
         BulkLoadHistoryView.this.batchList.setTimeZone(var1.getTimeZone());
      }
   }

   private class WaitStateBindingImpl extends WaitStateBinding {
      public WaitStateBindingImpl() {
         super(BulkLoadHistoryView.this.model.getProperty("busy"));
      }

      @Override
      protected void doAdditionalUiUpdate(boolean var1) {
         BulkLoadHistoryView.this.downloadInstancesAction.setEnabled(!var1);
         BulkLoadHistoryView.this.downloadBatchesAction.updateEnabledState(var1);
      }
   }
}
