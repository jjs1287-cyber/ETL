package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import java.util.Stack;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.ui.connector.mscrm.ConnectorCallInfoProviderAdapter;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectCache;
import org.etlforge.integration.client.ui.connector.mscrm.function.wizard.MSCrmFunctionWizardPageChain;
import org.etlforge.integration.client.ui.connector.mscrm.function.wizard.ObjectSelectionModelFactory;
import org.etlforge.integration.client.ui.connector.mscrm.query.SchemaRequestData;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderController;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderRootModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.data.connector.MSCrmQuery;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;

final class MSCrmQueryWizardPageChain implements WizardPageChain {
   private final MSCrmQueryWizard wizard;
   private final MSCrmFunctionWizardPageChain endpointAndObjectSelectionPages;
   private final ObjectSelectionModel objectSelectionModel = ObjectSelectionModelFactory.newModel();
   private final EnumSelectionPage<QueryBuildMode> buildModeSelectionPage;
   private final QuerySelectionModel querySelectionModel = new QuerySelectionModel();
   private final QuerySelectionPage querySelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private QueryBuilderRootModel queryBuilderModel;
   private QueryBuilderPage queryBuilderPage;

   public MSCrmQueryWizardPageChain(MSCrmQueryWizard var1, MSCrmDesignInfoProvider var2, MSCrmObjectCache var3) {
      this.wizard = var1;
      ObjectSelectionController var4 = new ObjectSelectionController(this.objectSelectionModel, new ConnectorCallInfoProviderAdapter(var2));
      var4.setCache(var3.getBasicDetailsCache());
      var4.setWaitMessage("Downloading list of objects");
      this.endpointAndObjectSelectionPages = new MSCrmFunctionWizardPageChain(
         var1, this.objectSelectionModel, var4, var1.getObjectLookupFactory(), var1.getProjectExplorer()
      );
      this.buildModeSelectionPage = EnumSelectionPage.allOf(var1, "Query Building Options", QueryBuildMode.class);
      this.querySelectionPage = this.createQuerySelectionPage(var2);
      this.pageStack.push(this.endpointAndObjectSelectionPages.getFirstPage());
   }

   private QuerySelectionPage createQuerySelectionPage(MSCrmDesignInfoProvider var1) {
      QuerySelectionController var2 = new QuerySelectionController(this.querySelectionModel, var1);
      return new QuerySelectionPage(this.wizard, this.querySelectionModel, var2);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.endpointAndObjectSelectionPages.getFirstPage();
   }

   @Override
   public WizardPage getLastPage() {
      return this.pageStack.peek();
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return !this.isFinalPage(var1);
   }

   private boolean isFinalPage(WizardPage var1) {
      return var1 == this.queryBuilderPage || var1 == this.querySelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      WizardPage var2;
      if (this.isFinalPage(var1)) {
         var2 = null;
      } else if (var1 == this.buildModeSelectionPage) {
         var2 = this.moveIntoQueryBuildingStage();
      } else {
         var2 = this.endpointAndObjectSelectionPages.getNextPage(var1);
         if (var2 == null) {
            var2 = this.buildModeSelectionPage;
         }
      }

      if (var2 != null) {
         this.pageStack.push(var2);
      }

      return var2;
   }

   private WizardPage moveIntoQueryBuildingStage() {
      this.setSection("Query");
      QueryBuildMode var1 = this.buildModeSelectionPage.getSelectedValue();
      switch (var1) {
         case BUILDER:
            this.prepareQueryBuilderPage();
            assert this.queryBuilderPage != null;
            return this.queryBuilderPage;
         case DOWNLOAD:
            this.prepareQuerySelectionPage();
            return this.querySelectionPage;
         default:
            throw new RuntimeException("Unexpected build mode: " + var1);
      }
   }

   private void prepareQueryBuilderPage() {
      MSCrmObject var1 = this.endpointAndObjectSelectionPages.getSelectedObject();
      if (this.queryBuilderPage == null || var1 != this.queryBuilderModel.getObject()) {
         this.queryBuilderModel = new QueryBuilderRootModel(var1);
         MSCrmEndpoint var2 = this.endpointAndObjectSelectionPages.getEndpoint();
         MSCrmObjectLookup var3 = this.wizard.getObjectLookupFactory().newLookup(var2);
         QueryBuilderController var4 = new QueryBuilderController(var3);
         this.queryBuilderPage = new QueryBuilderPage(this.wizard, this.queryBuilderModel, var4);
      }
   }

   private void prepareQuerySelectionPage() {
      MSCrmEndpoint var1 = (MSCrmEndpoint)this.objectSelectionModel.getEndpoint();
      this.querySelectionModel.setEndpoint(var1);
      ConnectorObject var2 = this.objectSelectionModel.getSelectedObject();
      this.querySelectionModel.setObjectName(var2.getName());
   }

   private void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      if (this.isFinalPage(var1)) {
         return true;
      } else {
         return var1 == this.buildModeSelectionPage ? true : this.endpointAndObjectSelectionPages.hasPreviousPage(var1);
      }
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      this.pageStack.pop();
      if (this.isFinalPage(var1)) {
         return this.buildModeSelectionPage;
      } else if (var1 == this.buildModeSelectionPage) {
         this.setSection("Object");
         return this.endpointAndObjectSelectionPages.getLastPage();
      } else {
         return this.endpointAndObjectSelectionPages.getPreviousPage(var1);
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return this.isFinalPage(var1) && var1.canContinue();
   }

   public void applyTo(MSCrmQuery var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      this.endpointAndObjectSelectionPages.applyTo(var1, var2);
      String var3 = this.getQueryXml();
      synchronized (var1.getDataLock()) {
         var1.setQueryXml(var3);
      }
   }

   private String getQueryXml() {
      QueryBuildMode var1 = this.buildModeSelectionPage.getSelectedValue();
      switch (var1) {
         case BUILDER:
            return this.queryBuilderModel.buildXml();
         case DOWNLOAD:
            return this.querySelectionPage.getDefinedQueryXml();
         default:
            throw new RuntimeException("Unexpected build mode: " + var1);
      }
   }

   public SchemaRequestData getSchemaRequestData() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      return new SchemaRequestData(this.endpointAndObjectSelectionPages.getEndpoint(), this.getQueryXml());
   }
}
