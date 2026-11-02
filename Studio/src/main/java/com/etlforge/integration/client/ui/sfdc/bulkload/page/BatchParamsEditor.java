package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.bulkload.BatchParamsDialog;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.AbstractEntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class BatchParamsEditor {
   private final SalesforceBulkLoad bulkLoad;
   private final Receiver<EntityPageSaveJob> callback;
   private BatchFileParams currentParams;

   public BatchParamsEditor(SalesforceBulkLoad var1, Receiver<EntityPageSaveJob> var2) {
      this.bulkLoad = var1;
      this.callback = var2;
      this.currentParams = var1.getBatchParams();
   }

   public void openInDialog(Optional<Component> var1) {
      BatchParamsDialog var2 = new BatchParamsDialog(this.currentParams);
      BatchFileParams var3 = var2.open(var1);
      if (!this.currentParams.equals(var3)) {
         this.callback.handle(new BatchParamsEditor.SaveJobImpl(this.currentParams, var3));
         this.currentParams = var3;
      }
   }

   public void layoutIn(TwoColumnLayout var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("&Batch Settings");
      KongaButton var3 = new KongaButton(new BatchParamsEditor.OpenInDialogAction());
      var2.setLabelForButton(var3);
      var1.addRow(var2, InvisiblePanel.of(var3));
   }

   private class OpenInDialogAction extends AbstractAction {
      private OpenInDialogAction() {
         super("Edit");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               OpenInDialogAction.this.openDialog(var1);
            }
         });
      }

      private void openDialog(ActionEvent var1) {
         BatchParamsEditor.this.openInDialog(UiUtils.getComponentSource(var1));
      }
   }

   private class SaveJobImpl extends AbstractEntityPageSaveJob<BatchFileParams> {
      public SaveJobImpl(BatchFileParams var2, BatchFileParams var3) {
         super("bulkload.batch_params", var2, var3);
         this.setDescription("Batch Settings");
         this.setUndoRedoStateReceiver(new Receiver<BatchFileParams>() {
            public void handle(BatchFileParams var1) {
               BatchParamsEditor.this.currentParams = var1;
            }
         });
      }

      protected void apply(BatchFileParams var1) {
         BatchParamsEditor.this.bulkLoad.setBatchParams(var1);
      }
   }
}
