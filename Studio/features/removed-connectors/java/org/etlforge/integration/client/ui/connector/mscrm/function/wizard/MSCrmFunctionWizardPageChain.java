package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionPage;
import org.etlforge.integration.client.ui.connector.wizard.RefreshListAction;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;

public final class MSCrmFunctionWizardPageChain implements WizardPageChain {
   private final AbstractEntityWizard<? extends MSCrmFunction> wizard;
   private final MSCrmObjectLookupFactory objectLookupFactory;
   private final EntitySelectionPage<MSCrmEndpoint> endpointSelectionPage;
   private final ObjectSelectionModel objectSelectionModel;
   private final MSCrmObjectSelectionPage objectSelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private final Map<AbstractWizardPage, String> sections;

   public MSCrmFunctionWizardPageChain(
      AbstractEntityWizard<? extends MSCrmFunction> var1,
      ObjectSelectionModel var2,
      ObjectSelectionController var3,
      MSCrmObjectLookupFactory var4,
      EntityExplorerSupport var5
   ) {
      this.wizard = var1;
      this.objectSelectionModel = var2;
      this.objectLookupFactory = var4;
      this.endpointSelectionPage = this.createEndpointSelectionPage(var1, var5);
      this.objectSelectionPage = this.createObjectSelectionPage(var3);
      this.sections = ImmutableMap.of(this.endpointSelectionPage, "Endpoint", this.objectSelectionPage, "Object");
      this.pageStack.push(this.endpointSelectionPage);
   }

   private EntitySelectionPage<MSCrmEndpoint> createEndpointSelectionPage(AbstractEntityWizard<? extends MSCrmFunction> var1, EntityExplorerSupport var2) {
      EntitySelectionPage var3 = new EntitySelectionPage<>(
         var1, "MS CRM Endpoint Selector", var2, EntityType.MSCrmEndpoint, MSCrmEndpoint.class, EntityFilters.ACCEPTS_ALL
      );
      var3.setMainCaption("Select the MS CRM  Endpoint");
      var3.setSelectorLabel("&Available MS CRM Endpoints");
      return var3;
   }

   private MSCrmObjectSelectionPage createObjectSelectionPage(ObjectSelectionController var1) {
      RefreshListAction var2 = new RefreshListAction(var1);
      return new MSCrmObjectSelectionPage(this.wizard, this.objectSelectionModel, var1, var2);
   }

   private FunctionType getFunctionType() {
      MSCrmFunction var1 = this.wizard.getEntityBeingCreated();
      return var1.getFunctionType();
   }

   public MSCrmObject getSelectedObject() {
      return this.objectSelectionPage.getSelectedObject();
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
      return var1 != this.objectSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      MSCrmObjectSelectionPage var2 = null;
      if (var1 == this.endpointSelectionPage) {
         MSCrmEndpoint var3 = this.getEndpoint();
         this.objectSelectionModel.setEndpoint(var3);
         MSCrmObjectLookup var4 = this.objectLookupFactory.newLookup(var3);
         this.objectSelectionPage.setObjectLookup(var4);
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

   public MSCrmEndpoint getEndpoint() {
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

   public void applyTo(MSCrmFunction var1, Folder var2) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "applyTo");
      synchronized (var1.getDataLock()) {
         MSCrmEndpoint var4 = this.getEndpoint();
         var1.setEndpoint(var4);
         ConnectorObject var5 = this.objectSelectionModel.getSelectedObject();
         var1.setObjectName(var5.getName());
         String var6 = suggestName(var1, var5);
         String var7 = EntityUtils.getAvailableChildName(var2, var6);
         var1.setName(var7);
      }
   }

   private static String suggestName(MSCrmFunction var0, ConnectorObject var1) {
      String var2 = var0.getEntityType().getDisplayName();
      return var2 + " " + StringUtils.capitalize(var1.getName());
   }

   public SchemaRequestData getSchemaRequestData() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getSchemaRequestData");
      return SchemaRequestData.singleSchemas(this.getEndpoint(), this.getFunctionType(), this.objectSelectionModel.getSelectedObject());
   }
}
