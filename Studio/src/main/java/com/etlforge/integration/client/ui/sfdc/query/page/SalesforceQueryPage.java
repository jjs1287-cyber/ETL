package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPage;
import com.etlforge.integration.client.ui.sfdc.page.soql.AutomaticPageSaver;
import com.etlforge.integration.client.ui.sfdc.page.soql.EditSoqlController;
import com.etlforge.integration.client.ui.sfdc.page.soql.ErrorAlert;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlEditor;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.parser.ParsedQuery;

public final class SalesforceQueryPage extends AbstractSalesforceItemPage<SalesforceQuery> {
   private final SoqlModel soqlModel;
   private Optional<SoqlEditor> soqlEditor = Optional.absent();
   private Optional<EditSoqlController> editSoqlController = Optional.absent();

   public SalesforceQueryPage(SalesforceQuery var1, SoqlModel var2) {
      super(var1, SfdcMethod.QUERY);
      this.soqlModel = var2;
   }

   public SoqlModel getSoqlModel() {
      return this.soqlModel;
   }

   public void setSoqlEditor(SoqlEditor var1, EditSoqlController var2) {
      this.soqlEditor = Optional.of(var1);
      this.editSoqlController = Optional.of(var2);
      this.addResource(new DisposableWindowElementResource(var1));
   }

   protected SalesforceQueryPanel createDataPanel() {
      SalesforceQueryPanel var1 = new SalesforceQueryPanel(this.getWindow(), this.getObject(), this.soqlModel, this.getEntityExplorerSupport());
      this.installSoqlEditor(var1);
      this.doDefaultPanelConfiguration(var1);
      this.getPageModel().bindProperties(var1.getBoundProperties());
      return var1;
   }

   private void installSoqlEditor(SalesforceQueryPanel var1) {
      if (this.soqlEditor.isPresent()) {
         ErrorAlert var2 = new ErrorAlert(this.getMainPanel());
         this.editSoqlController.get().withConnectorSupplier(this.getConnectorSupplier()).withErrorReporter(var2).withWaitService(this.getWaitService());
         AutomaticPageSaver var3 = AutomaticPageSaver.install(this.soqlModel, this);
         var3.setErrorReporter(var2);
         var1.setSoqlEditor(this.soqlEditor.get(), this.editSoqlController.get());
      }
   }

   protected SalesforceQueryPanel getDetailsPanel() {
      return (SalesforceQueryPanel)super.getDetailsPanel();
   }

   boolean isQueryAll() {
      return this.getDetailsPanel().isQueryAll();
   }

   String getQueryString() {
      return this.soqlModel.getQueryString();
   }

   ParsedQuery getParsedQuery() {
      return this.soqlModel.getParsedQuery();
   }
}
