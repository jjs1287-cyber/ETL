package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPageConfig;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.data.persist.ProjectPersistor;

public class SalesforceBulkLoadPageConfig extends AbstractSalesforceItemPageConfig<SalesforceBulkLoadPage> {
   private Optional<MappingsEditor> mappingsEditor = Optional.absent();
   private Optional<BulkSoqlEditor> soqlEditor = Optional.absent();
   private Optional<ApplicationAction> showHistoryAction = Optional.absent();

   public SalesforceBulkLoadPageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   public void setMappingsEditor(MappingsEditor var1) {
      this.mappingsEditor = Optional.of(var1);
   }

   public void setSoqlEditor(BulkSoqlEditor var1) {
      this.soqlEditor = Optional.of(var1);
   }

   public void setShowHistoryAction(ApplicationAction var1) {
      this.showHistoryAction = Optional.of(var1);
   }

   protected void doAdditionalConfig(SalesforceBulkLoadPage var1) {
      if (this.mappingsEditor.isPresent()) {
         var1.setMappingsEditor(this.mappingsEditor.get());
      }

      if (this.soqlEditor.isPresent()) {
         var1.setSoqlEditor(this.soqlEditor.get());
      }

      if (this.showHistoryAction.isPresent()) {
         ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.showHistoryAction.get());
         var1.setShowHistoryAction(var2);
      }
   }
}
