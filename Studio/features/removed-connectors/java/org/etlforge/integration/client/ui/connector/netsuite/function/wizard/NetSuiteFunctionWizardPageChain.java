package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.ui.connector.netsuite.function.SchemaRequestDataNetSuite;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionPage;
import org.etlforge.integration.client.ui.connector.wizard.RefreshListAction;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;

public final class NetSuiteFunctionWizardPageChain implements WizardPageChain {
   private final AbstractEntityWizard<? extends NetSuiteFunction> wizard;
   private final EntitySelectionPage<NetSuiteEndpoint> endpointSelectionPage;
   private final ObjectSelectionModel objectSelectionModel;
   private final ObjectSelectionPage objectSelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private final Map<AbstractWizardPage, String> sections;
   private final QueryBuilderModel queryBuilderModel;
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private static final NetSuiteSearchAdvancedProperties dummy = new NetSuiteSearchAdvancedProperties();

   public NetSuiteFunctionWizardPageChain(
      AbstractEntityWizard<? extends NetSuiteFunction> var1,
      ObjectSelectionModel var2,
      ObjectSelectionController var3,
      EntityExplorerSupport var4,
      NetSuiteDefinitionDesignLookup var5,
      NetSuiteCustomFieldsDesignLookup var6
   ) {
      this.wizard = var1;
      this.objectSelectionModel = var2;
      this.queryBuilderModel = new QueryBuilderModel();
      this.defLookup = var5;
      this.cfLookup = var6;
      this.endpointSelectionPage = this.createEndpointSelectionPage(var1, var4);
      this.objectSelectionPage = this.createObjectSelectionPage(var3);
      this.sections = ImmutableMap.of(this.endpointSelectionPage, "Endpoint", this.objectSelectionPage, "Object");
      this.pageStack.push(this.endpointSelectionPage);
   }

   private EntitySelectionPage<NetSuiteEndpoint> createEndpointSelectionPage(AbstractEntityWizard<? extends NetSuiteFunction> var1, EntityExplorerSupport var2) {
      EntitySelectionPage var3 = new EntitySelectionPage<>(
         var1, "NetSuite  Endpoint Selector", var2, EntityType.NetSuiteEndpoint, NetSuiteEndpoint.class, EntityFilters.ACCEPTS_ALL
      );
      var3.setMainCaption("Select the NetSuite Endpoint");
      var3.setSelectorLabel("&Available NetSuite Endpoints");
      return var3;
   }

   private ObjectSelectionPage createObjectSelectionPage(ObjectSelectionController var1) {
      RefreshListAction var2 = new RefreshListAction(var1);
      return new ObjectSelectionPage(this.wizard, "Select an Object", this.objectSelectionModel, var1, var2, true);
   }

   private NetSuiteFunction.FunctionType getFunctionType() {
      NetSuiteFunction var1 = this.wizard.getEntityBeingCreated();
      return var1.getFunctionType();
   }

   public NetSuiteConnectorObject getSelectedObject() {
      return NetSuiteConnectorObject.castAsNetSuiteConnectorObject(this.objectSelectionModel.getSelectedObject());
   }

   @Override
   public WizardPage getFirstPage() {
      return this.endpointSelectionPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.objectSelectionPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return this.getFunctionType() == NetSuiteFunction.FunctionType.DELETE ? false : var1 != this.objectSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      ObjectSelectionPage var2 = null;
      if (var1 == this.endpointSelectionPage) {
         NetSuiteEndpoint var3 = this.getEndpoint();
         this.objectSelectionModel.setEndpoint(var3);
         var2 = this.objectSelectionPage;
      }

      if (var2 != null) {
         if (var2 instanceof ObjectSelectionPage) {
            var2.populateList();
         }

         this.pageStack.push(var2);
         this.setSection(var2);
      }

      return var2;
   }

   public NetSuiteEndpoint getEndpoint() {
      return this.endpointSelectionPage.getSelectedEntity();
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
      return var1 == this.objectSelectionPage == var1.canContinue();
   }

   public void applyTo(NetSuiteFunction var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      synchronized (var1.getDataLock()) {
         NetSuiteEndpoint var4 = this.getEndpoint();
         var1.setEndpoint(var4);
         if (this.getFunctionType() != NetSuiteFunction.FunctionType.DELETE) {
            ConnectorObject var5 = this.objectSelectionModel.getSelectedObject();
            NetSuiteConnectorObject var6 = NetSuiteConnectorObject.castAsNetSuiteConnectorObject(var5);
            var1.setObjectName(var6.getKey());
            String var7 = suggestName(var1, var6);
            String var8 = EntityUtils.getAvailableChildName(var2, var7);
            var1.setName(var8);
         }
      }
   }

   private static String suggestName(NetSuiteFunction var0, ConnectorObject var1) {
      String var2 = var0.getEntityType().getDisplayName();
      return var2 + " " + StringUtils.capitalize(var1.getName());
   }

   private NetSuiteSearchAdvancedProperties getNetSuiteSearchAdvancedProperties() {
      return NetSuiteFunction.FunctionType.QUERY == this.getFunctionType() ? this.queryBuilderModel.getNetSuiteSearchAdvancedProperties() : dummy;
   }

   public QueryBuilderModel getQueryBuilderModel() {
      return this.queryBuilderModel;
   }

   public SchemaRequestData getSchemaRequestData() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getSchemaRequestData");
      return SchemaRequestDataNetSuite.multipleSchemasNetSuite(
         this.getEndpoint(), this.getFunctionType(), this.getSelectedObject(), this.getNetSuiteSearchAdvancedProperties(), this.defLookup, this.cfLookup
      );
   }
}
