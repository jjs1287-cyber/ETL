package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputPanel;

@Deprecated
final class BatchSizeEditor implements UiProvider {
   private final SalesforceQuery query;
   private int originalBatchSize;
   private final BatchSizeField batchSizeField;

   public BatchSizeEditor(SalesforceQuery var1, SfdcMethodConfig var2) {
      this.query = var1;
      BatchSizeGetter var3 = new BatchSizeGetter(var2);
      this.originalBatchSize = var3.getBatchSize(var1);
      this.batchSizeField = new BatchSizeField();
      this.batchSizeField.setBatchSize(this.originalBatchSize);
   }

   public void registerWithInputPanel(InputPanel var1) {
      this.batchSizeField.registerWithInputPanel(var1);
   }

   public void validate() throws InputFieldException {
      this.batchSizeField.validate();
   }

   public int getBatchSize() {
      return this.batchSizeField.getBatchSize();
   }

   public boolean hasChanged() {
      return this.originalBatchSize != this.getBatchSize();
   }

   public void apply() {
      int var1 = this.getBatchSize();
      this.query.setBatchSize(var1);
      this.originalBatchSize = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.batchSizeField.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.batchSizeField.requestFocus();
   }
}
