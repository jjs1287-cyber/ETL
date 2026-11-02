package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.ui.widget.text.KongaTextArea;

final class QueryBuilderPage extends AbstractWizardPage {
   static final boolean TEST_MODE = false;
   private final KongaTextArea xmlArea = new KongaTextArea(16, 80);
   private final QueryBuilderModel model;
   private final TestQueryController testController;
   private final QueryBuilderUi ui;
   private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;
   private final NetSuiteCustomFieldsQueryBuilderLookup customFieldsLookup;

   public QueryBuilderPage(
      Wizard var1,
      QueryBuilderModel var2,
      TestQueryController var3,
      PageSizeSelectorAndTestButtonUi var4,
      NetSuiteDefinitionQueryBuilderLookup var5,
      NetSuiteCustomFieldsQueryBuilderLookup var6,
      TestNetSuiteQueryApi var7
   ) {
      super(var1, "Build Advanced Query");
      this.model = var2;
      this.testController = var3;
      this.ui = new QueryBuilderUi(var2, var4);
      this.searchDefLookup = var5;
      this.customFieldsLookup = var6;
      this.setCanContinue(true);
   }

   public void setEndpointAndNetSuiteObject(NetSuiteEndpoint var1, NetSuiteConnectorObject var2) {
      ModelPreparer.prepareModel(this.model, var1, var2, this.searchDefLookup, this.customFieldsLookup, this.getWaitService());
      this.testController.setEndpointAndObject(var1, var2);
   }

   public void setNetSuiteSearchCategory(NetSuiteSearchCategory var1) {
      this.ui.setNetSuiteSearchCategory(var1);
   }

   @Override
   protected void onFirstDisplay() {
      this.testController.setWaitService(this.getWaitService());
   }

   public void applyTo(NetSuiteQuery var1) {
      synchronized (var1.getDataLock()) {
         NetSuiteQuery.QueryParams var3 = this.model.getDefinedParams();
         var1.setQueryParams(var3);
      }
   }

   @Override
   public boolean prepareForForwardNavigation() {
      this.testController.cancelRunningJobs();
      return this.ui.isValid();
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      this.testController.cancelRunningJobs();
      return true;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(null);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
