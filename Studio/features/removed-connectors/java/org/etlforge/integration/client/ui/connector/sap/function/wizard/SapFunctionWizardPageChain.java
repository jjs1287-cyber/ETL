package org.etlforge.integration.client.ui.connector.sap.function.wizard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.EnumSelectionPage;
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
import org.etlforge.integration.data.connector.SapEndpoint;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public final class SapFunctionWizardPageChain implements WizardPageChain {
   private final SapFunctionWizard wizard;
   private final EntitySelectionPage<SapEndpoint> endpointSelectionPage;
   private final EnumSelectionPage<SapFunction.FunctionType> typeSelectionPage;
   private final ObjectSelectionModel categorySelectionModel = new ObjectSelectionModel();
   private final ObjectSelectionPage categorySelectionPage;
   private final ObjectSelectionModel functionSelectionModel = new ObjectSelectionModel();
   private final ObjectSelectionPage functionSelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private final Map<AbstractWizardPage, String> sections;

   public SapFunctionWizardPageChain(SapFunctionWizard var1, ConnectorCallInfoProvider var2, ConnectorObjectCache var3) {
      this.wizard = var1;
      this.endpointSelectionPage = this.createEndpointSelectionPage(var1);
      this.typeSelectionPage = EnumSelectionPage.allOf(var1, "Select Function Type (BAPI, RFC, or IDOC)", SapFunction.FunctionType.class);
      this.categorySelectionModel.setSelectionPath(Collections.singleton(SapFunction.FunctionType.IDOC));
      this.categorySelectionPage = this.createCategorySelectionPage(var2, var3);
      this.functionSelectionPage = this.createFunctionSelectionPage(var2, var3);
      this.sections = ImmutableMap.of(
         this.endpointSelectionPage, "Endpoint", this.typeSelectionPage, "Type", this.categorySelectionPage, "Category", this.functionSelectionPage, "Function"
      );
      this.pageStack.push(this.endpointSelectionPage);
   }

   private EntitySelectionPage<SapEndpoint> createEndpointSelectionPage(SapFunctionWizard var1) {
      EntitySelectionPage var2 = new EntitySelectionPage<>(
         var1, "SAP Endpoint Selector", var1.getProjectExplorer(), EntityType.SapEndpoint, SapEndpoint.class, EntityFilters.ACCEPTS_ALL
      );
      var2.setMainCaption("Select the SAP Endpoint");
      var2.setSelectorLabel("&Available SAP Endpoints");
      return var2;
   }

   private ObjectSelectionPage createCategorySelectionPage(ConnectorCallInfoProvider var1, ConnectorObjectCache var2) {
      ObjectSelectionController var3 = new ObjectSelectionController(this.categorySelectionModel, var1);
      var3.setCache(var2);
      var3.setWaitMessage("Downloading list of IDOC categories");
      RefreshListAction var4 = new RefreshListAction(var3);
      return new ObjectSelectionPage(this.wizard, "Select IDOC Category", this.categorySelectionModel, var3, var4);
   }

   private ObjectSelectionPage createFunctionSelectionPage(ConnectorCallInfoProvider var1, ConnectorObjectCache var2) {
      ObjectSelectionController var3 = new ObjectSelectionController(this.functionSelectionModel, var1);
      var3.setCache(var2);
      var3.setWaitMessage("Downloading list of functions");
      RefreshListAction var4 = new RefreshListAction(var3);
      return new ObjectSelectionPage(this.wizard, "Select a Function", this.functionSelectionModel, var3, var4);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.endpointSelectionPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.functionSelectionPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 != this.functionSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      AbstractWizardPage var2 = null;
      if (var1 == this.endpointSelectionPage) {
         SapEndpoint var3 = this.endpointSelectionPage.getSelectedEntity();
         this.categorySelectionModel.setEndpoint(var3);
         this.functionSelectionModel.setEndpoint(var3);
         var2 = this.typeSelectionPage;
      } else if (var1 == this.typeSelectionPage) {
         SapFunction.FunctionType var4 = this.typeSelectionPage.getSelectedValue();
         this.functionSelectionModel.setSelectionPath(Collections.singleton(var4));
         if (var4 == SapFunction.FunctionType.IDOC) {
            var2 = this.categorySelectionPage;
         } else {
            var2 = this.functionSelectionPage;
         }
      } else if (var1 == this.categorySelectionPage) {
         ConnectorObject var5 = this.categorySelectionModel.getSelectedObject();
         this.functionSelectionModel.setSelectionPath(ImmutableList.of(SapFunction.FunctionType.IDOC, var5));
         var2 = this.functionSelectionPage;
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
      return var1 == this.functionSelectionPage == var1.canContinue();
   }

   public void applyTo(SapFunction var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      synchronized (var1.getDataLock()) {
         SapEndpoint var4 = this.endpointSelectionPage.getSelectedEntity();
         var1.setEndpoint(var4);
         SapFunction.FunctionType var5 = this.typeSelectionPage.getSelectedValue();
         var1.setFunctionType(var5);
         if (var5 == SapFunction.FunctionType.IDOC) {
            ConnectorObject var6 = this.categorySelectionModel.getSelectedObject();
            var1.setCategory(var6);
         }

         ConnectorObject var11 = this.functionSelectionModel.getSelectedObject();
         var1.setApiFunction(var11);
         String var7 = var11.getName();
         String var8 = EntityUtils.getAvailableChildName(var2, var7);
         var1.setName(var8);
      }
   }

   public SchemaRequestData getSchemaRequestData() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getSchemaRequestData");
      return SchemaRequestData.singleSchemas(
         this.endpointSelectionPage.getSelectedEntity(),
         this.typeSelectionPage.getSelectedValue(),
         this.categorySelectionModel.getSelectedObject(),
         this.functionSelectionModel.getSelectedObject()
      );
   }
}
