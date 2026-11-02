package org.etlforge.integration.client.ui.connector.msax.function.wizard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Stack;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionPage;
import org.etlforge.integration.client.ui.connector.wizard.RefreshListAction;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.connector.MSAXEndpoint;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public final class MSAXFunctionWizardPageChain implements WizardPageChain {
   private final MSAXFunctionWizard wizard;
   private final EntitySelectionPage<MSAXEndpoint> endpointSelectionPage;
   private final ObjectSelectionModel wsdlurlSelectionModel = new ObjectSelectionModel();
   private final UrlObjectSelectionPage wsdlurlSelectionPage;
   private final ObjectSelectionModel operationSelectionModel = new ObjectSelectionModel();
   private final ObjectSelectionPage operationSelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private final Map<AbstractWizardPage, String> sections;

   public MSAXFunctionWizardPageChain(MSAXFunctionWizard var1, ConnectorCallInfoProvider var2, ConnectorObjectCache var3) {
      this.wizard = var1;
      this.endpointSelectionPage = this.createEndpointSelectionPage(var1);
      this.wsdlurlSelectionPage = this.createWSDLURLSelectionPage();
      this.operationSelectionPage = this.createOperationSelectionPage(var2, var3);
      this.sections = ImmutableMap.of(this.endpointSelectionPage, "Endpoint", this.wsdlurlSelectionPage, "WSDL URL", this.operationSelectionPage, "Operation");
      this.pageStack.push(this.endpointSelectionPage);
   }

   private EntitySelectionPage<MSAXEndpoint> createEndpointSelectionPage(MSAXFunctionWizard var1) {
      EntitySelectionPage var2 = new EntitySelectionPage<>(
         var1, "MSAX Endpoint Selector", var1.getProjectExplorer(), EntityType.MSAXEndpoint, MSAXEndpoint.class, EntityFilters.ACCEPTS_ALL
      );
      var2.setMainCaption("Select the MSAX Endpoint");
      var2.setSelectorLabel("&Available MSAX Endpoints");
      return var2;
   }

   private UrlObjectSelectionPage createWSDLURLSelectionPage() {
      return new UrlObjectSelectionPage(this.wizard, "Select MSAX WSDL URL", this.wsdlurlSelectionModel);
   }

   private ObjectSelectionPage createOperationSelectionPage(ConnectorCallInfoProvider var1, ConnectorObjectCache var2) {
      ObjectSelectionController var3 = new ObjectSelectionController(this.operationSelectionModel, var1);
      var3.setCache(var2);
      var3.setWaitMessage("Downloading list of Operations");
      RefreshListAction var4 = new RefreshListAction(var3);
      return new ObjectSelectionPage(this.wizard, "Select a Operation", this.operationSelectionModel, var3, var4);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.endpointSelectionPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.operationSelectionPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 != this.operationSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      AbstractWizardPage var2 = null;
      if (var1 == this.endpointSelectionPage) {
         MSAXEndpoint var3 = this.endpointSelectionPage.getSelectedEntity();
         this.wsdlurlSelectionModel.setEndpoint(var3);
         this.operationSelectionModel.setEndpoint(var3);
         this.wsdlurlSelectionPage.populateList();
         var2 = this.wsdlurlSelectionPage;
      } else if (var1 == this.wsdlurlSelectionPage) {
         ConnectorObject var4 = this.wsdlurlSelectionModel.getSelectedObject();
         this.operationSelectionModel.setSelectionPath(ImmutableList.of(var4));
         var2 = this.operationSelectionPage;
      }

      if (var2 != null) {
         if (var2 instanceof ObjectSelectionPage) {
            ((ObjectSelectionPage)var2).populateList();
         }

         this.pageStack.push(var2);
         this.setSection(var2);
      }

      return var2;
   }

   private void setSection(WizardPage var1) {
      String var2 = this.sections.get(var1);
      this.wizard.getDisplayer().setSection(var2);
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.endpointSelectionPage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      this.pageStack.pop();
      WizardPage var2 = this.pageStack.peek();
      this.setSection(var2);
      return var2;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.operationSelectionPage == var1.canContinue();
   }

   public void applyTo(MSAXFunction var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      synchronized (var1.getDataLock()) {
         MSAXEndpoint var4 = this.endpointSelectionPage.getSelectedEntity();
         var1.setEndpoint(var4);
         ConnectorObject var5 = this.wsdlurlSelectionModel.getSelectedObject();
         var1.setWSDLUrl(var5.getName().trim());
         var5 = this.operationSelectionModel.getSelectedObject();
         String var6 = var5.getName();
         var1.setMSAXServiceOperation(var6);
         String var7 = var6;
         String var8 = EntityUtils.getAvailableChildName(var2, var7);
         var1.setName(var8);
      }
   }

   public SchemaRequestData getSchemaRequestData() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getSchemaRequestData");
      return SchemaRequestData.multipleSchemas(
         this.endpointSelectionPage.getSelectedEntity(), this.wsdlurlSelectionModel.getSelectedObject(), this.operationSelectionModel.getSelectedObject()
      );
   }
}
