package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.ui.connector.netsuite.function.wizard.NetSuiteFunctionWizardPageChain;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;

public final class NetSuiteQueryWizardPageChain implements WizardPageChain {
   private final NetSuiteQueryWizard wizard;
   private final NetSuiteFunctionWizardPageChain endpointAndObjectPages;
   private final QueryBuilderModel queryBuilderModel;
   private final AdvancedSearchParaPage searchParaPage;
   private final QueryBuilderPage queryBuilderPage;
   private final SaveSearchPage saveSearchPage;
   private final TestQueryController testController;
   private final PageSizeSelectorAndTestButtonUi testUi;
   private WizardPage lastPage;

   public NetSuiteQueryWizardPageChain(
      NetSuiteQueryWizard var1,
      ObjectSelectionModel var2,
      ObjectSelectionController var3,
      EntityExplorerSupport var4,
      NetSuiteDefinitionQueryBuilderLookup var5,
      NetSuiteCustomFieldsQueryBuilderLookup var6,
      NetSuiteSearchObjectSavedSearchLookup var7,
      NetSuiteDefinitionDesignLookup var8,
      NetSuiteCustomFieldsDesignLookup var9,
      TestNetSuiteQueryApi var10
   ) {
      this.wizard = var1;
      this.endpointAndObjectPages = new NetSuiteFunctionWizardPageChain(var1, var2, var3, var4, var8, var9);
      this.queryBuilderModel = this.endpointAndObjectPages.getQueryBuilderModel();
      this.testController = new TestQueryController(this.queryBuilderModel, var10);
      this.testUi = new PageSizeSelectorAndTestButtonUi(this.queryBuilderModel, this.testController.getTestAction());
      this.queryBuilderPage = new QueryBuilderPage(var1, this.queryBuilderModel, this.testController, this.testUi, var5, var6, var10);
      this.saveSearchPage = new SaveSearchPage(var1, this.queryBuilderModel, this.testController, this.testUi, var5, var7, var10);
      this.searchParaPage = new AdvancedSearchParaPage(var1, var5);
      this.lastPage = null;
   }

   @Override
   public WizardPage getFirstPage() {
      return this.endpointAndObjectPages.getFirstPage();
   }

   @Override
   public WizardPage getLastPage() {
      return this.lastPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 != this.getLastPage();
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.getLastPage()) {
         return null;
      }

      if (var1 == this.searchParaPage) {
         this.prepareQueryBuilderPage();
         return this.getLastPage();
      }

      WizardPage var2 = this.endpointAndObjectPages.getNextPage(var1);
      if (var2 == null) {
         this.prepareSearchParaPage();
         var2 = this.searchParaPage;
      }

      return var2;
   }

   private void prepareSearchParaPage() {
      NetSuiteEndpoint var1 = this.endpointAndObjectPages.getEndpoint();
      NetSuiteConnectorObject var2 = this.endpointAndObjectPages.getSelectedObject();
      this.searchParaPage.setEndpointAndNetSuiteObject(var1, var2);
      this.wizard.getDisplayer().setSection("Search Type");
   }

   private void prepareQueryBuilderPage() {
      NetSuiteEndpoint var1 = this.endpointAndObjectPages.getEndpoint();
      NetSuiteConnectorObject var2 = this.endpointAndObjectPages.getSelectedObject();
      AdvancedSearchPropertyModel var3 = this.searchParaPage.getAdvancedSearchPropertyModel();
      NetSuiteSearchAdvancedProperties var4 = var3.asNetSuiteSearchAdvancedProperties();
      this.queryBuilderModel.setNetSuiteSearchAdvancedProperties(var4);
      if (var3.isSavedSearchOption()) {
         this.lastPage = this.saveSearchPage;
         this.saveSearchPage.setEndpointAndNetSuiteObject(var1, var2);
      } else {
         this.lastPage = this.queryBuilderPage;
         this.queryBuilderPage.setNetSuiteSearchCategory(this.queryBuilderModel.getSearchType());
         this.queryBuilderPage.setEndpointAndNetSuiteObject(var1, var2);
      }

      this.wizard.getDisplayer().setSection("Build Query");
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 == this.getLastPage() || var1 == this.searchParaPage || this.endpointAndObjectPages.hasPreviousPage(var1);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.getLastPage()) {
         this.wizard.getDisplayer().setSection("Search Type");
         return this.searchParaPage;
      } else if (var1 == this.searchParaPage) {
         this.wizard.getDisplayer().setSection("Object");
         return this.endpointAndObjectPages.getLastPage();
      } else {
         return this.endpointAndObjectPages.getPreviousPage(var1);
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.getLastPage() && var1.canContinue();
   }

   public void applyTo(NetSuiteQuery var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      this.endpointAndObjectPages.applyTo(var1, var2);
      if (this.getLastPage() == this.queryBuilderPage) {
         this.queryBuilderPage.applyTo(var1);
      } else {
         this.saveSearchPage.applyTo(var1);
      }
   }

   public SchemaRequestData getSchemaRequestData() {
      return this.endpointAndObjectPages.getSchemaRequestData();
   }
}
