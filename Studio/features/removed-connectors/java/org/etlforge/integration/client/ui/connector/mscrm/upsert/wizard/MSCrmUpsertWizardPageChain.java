package org.etlforge.integration.client.ui.connector.mscrm.upsert.wizard;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.connector.mscrm.ConnectorCallInfoProviderAdapter;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.client.ui.connector.mscrm.function.wizard.MSCrmFunctionWizardPageChain;
import org.etlforge.integration.client.ui.connector.mscrm.function.wizard.ObjectSelectionModelFactory;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.data.connector.MSCrmUpsert;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

final class MSCrmUpsertWizardPageChain implements WizardPageChain {
   private final MSCrmUpsertWizard wizard;
   private final MSCrmFunctionWizardPageChain endpointAndObjectSelectionPages;
   private final ObjectSelectionModel objectSelectionModel = ObjectSelectionModelFactory.newModel();
   private final Stack<WizardPage> pageStack = new Stack<>();
   private UpsertBuilderPage upsertBuilderPage;

   public MSCrmUpsertWizardPageChain(MSCrmUpsertWizard var1, MSCrmDesignInfoProvider var2, ConnectorObjectCache var3, MSCrmObjectLookupFactory var4) {
      this.wizard = var1;
      ObjectSelectionController var5 = new ObjectSelectionController(this.objectSelectionModel, new ConnectorCallInfoProviderAdapter(var2));
      var5.setCache(var3);
      var5.setWaitMessage("Downloading list of objects");
      this.endpointAndObjectSelectionPages = new MSCrmFunctionWizardPageChain(var1, this.objectSelectionModel, var5, var4, var1.getProjectExplorer());
      this.pageStack.push(this.endpointAndObjectSelectionPages.getFirstPage());
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
      return var1 == this.upsertBuilderPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      WizardPage var2 = null;
      if (!this.isFinalPage(var1)) {
         var2 = this.endpointAndObjectSelectionPages.getNextPage(var1);
         if (var2 == null) {
            var2 = this.moveIntoUpsertBuildingStage();
         }
      }

      if (var2 != null) {
         this.pageStack.push(var2);
      }

      return var2;
   }

   private WizardPage moveIntoUpsertBuildingStage() {
      this.setSection("Upsert");
      this.prepareUpsertBuilderPage();
      assert this.upsertBuilderPage != null;
      return this.upsertBuilderPage;
   }

   private void prepareUpsertBuilderPage() {
      this.upsertBuilderPage = new UpsertBuilderPage(this.wizard, null);
   }

   private void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return this.isFinalPage(var1) ? true : this.endpointAndObjectSelectionPages.hasPreviousPage(var1);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      this.pageStack.pop();
      if (this.isFinalPage(var1)) {
         return this.upsertBuilderPage;
      }

      this.setSection("Object");
      return this.endpointAndObjectSelectionPages.getLastPage();
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return this.isFinalPage(var1) && var1.canContinue();
   }

   public void applyTo(MSCrmUpsert var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      this.endpointAndObjectSelectionPages.applyTo(var1, var2);
      List var3 = this.getUpsertList();
      synchronized (var1.getDataLock()) {
         var1.setUpsertKeyNames(var3);
      }
   }

   private List<String> getUpsertList() {
      LinkedList var1 = new LinkedList();
      MSCrmObject var2 = (MSCrmObject)this.objectSelectionModel.getSelectedObject();

      for (MSCrmAttribute var4 : var2.getAttributes()) {
         if (var4.isValidForUpsert()) {
            var1.add(var4.getDisplayName());
         }
      }

      return var1;
   }

   private String getUpsertRequestString() {
      List var1 = this.getUpsertList();
      String var2 = "";

      for (int var3 = 0; var3 < var1.size(); var3++) {
         String var4 = (String)var1.get(var3);
         var2 = var2 + var4.trim();
         if (var3 != var1.size() - 1) {
            var2 = var2 + ",";
         }
      }

      return var2;
   }

   public SchemaRequestData getSchemaRequestData() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      return SchemaRequestData.singleSchemas(
         this.endpointAndObjectSelectionPages.getEndpoint(), FunctionType.UPSERT, new SimpleConnectorObject(this.getUpsertRequestString())
      );
   }
}
