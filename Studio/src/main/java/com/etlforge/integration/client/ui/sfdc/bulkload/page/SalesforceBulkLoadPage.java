package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPage;
import javax.swing.Action;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.system.callback.Receiver;

public final class SalesforceBulkLoadPage extends AbstractSalesforceItemPage<SalesforceBulkLoad> {
   private MappingsEditor mappingsEditor;
   private final BatchParamsEditor batchParamsEditor;
   private Action showHistoryAction;
   private BulkSoqlEditor soqlEditor;
   private final Receiver<EntityPageSaveJob> saveJobReceiver = new Receiver<EntityPageSaveJob>() {
      public void handle(EntityPageSaveJob var1) {
         SalesforceBulkLoadPage.this.addSaveJob(var1);
      }
   };

   public SalesforceBulkLoadPage(SalesforceBulkLoad var1) {
      super(var1, SfdcMethod.BULK_LOAD);
      this.batchParamsEditor = new BatchParamsEditor(this.getObject(), this.saveJobReceiver);
   }

   protected SalesforceBulkLoadPanel createDataPanel() {
      DateOptionsEditor var1 = this.createDateOptionsEditor();
      SalesforceBulkLoadPanel var2 = new SalesforceBulkLoadPanel(
         this.getWindow(), this.getObject(), this.getEntityExplorerSupport(), this.mappingsEditor, this.soqlEditor, this.batchParamsEditor, var1
      );
      this.doDefaultPanelConfiguration(var2);
      var2.setShowHistoryAction(this.showHistoryAction);
      return var2;
   }

   private DateOptionsEditor createDateOptionsEditor() {
      DateOptionsEditor var1 = new DateOptionsEditor(this.getObject().getDateConversionParams());
      BulkAction var2 = this.getObject().getAction();
      var1.setEnabled(var2.supportsDateOptions());
      return var1;
   }

   public void setSoqlEditor(BulkSoqlEditor var1) {
      this.soqlEditor = var1;
      var1.associateWithPage(this);
      this.addResource(new DisposableWindowElementResource(var1));
   }

   public void setMappingsEditor(MappingsEditor var1) {
      this.mappingsEditor = var1;
      var1.setCallback(this.saveJobReceiver);
   }

   public void setShowHistoryAction(Action var1) {
      this.showHistoryAction = var1;
   }
}
