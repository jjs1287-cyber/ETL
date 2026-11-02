package org.etlforge.integration.client.ui.connector.netsuite.function.txwiz;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Stack;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.connector.NetSuiteCreate;
import org.etlforge.integration.data.connector.NetSuiteDelete;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.data.connector.NetSuiteUpdate;
import org.etlforge.integration.data.connector.NetSuiteUpsert;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;

final class NetSuiteFunctionSelectionPageChain implements TransformationStructurePageChain {
   private final TransformationWizard wizard;
   private final FunctionTypeSelectionPage typeSelectionPage;
   private final Map<NetSuiteFunction.FunctionType, EntitySelectionPage<? extends NetSuiteFunction>> functionSelectionPages = Maps.newHashMap();
   private EntitySelectionPage<? extends NetSuiteFunction> functionSelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private final FunctionStructureGetter<NetSuiteFunction> structureGetter;
   private final TransformationStructureSetter structureSetter;
   private final boolean isRequest;

   public NetSuiteFunctionSelectionPageChain(
      TransformationWizard var1, FunctionStructureGetter<NetSuiteFunction> var2, TransformationStructureSetter var3, boolean var4
   ) {
      this.wizard = var1;
      this.structureGetter = var2;
      this.structureSetter = var3;
      this.isRequest = var4;
      this.typeSelectionPage = new FunctionTypeSelectionPage(var1, var4);
      this.pageStack.push(this.typeSelectionPage);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.typeSelectionPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.pageStack.peek();
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 == this.typeSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.functionSelectionPage) {
         return null;
      }

      assert var1 == this.typeSelectionPage;
      NetSuiteFunction.FunctionType var2 = this.typeSelectionPage.getSelectedValue();
      EntitySelectionPage var3 = this.functionSelectionPages.get(var2);
      if (var3 == null) {
         var3 = this.createFunctionSelectionPage(var2);
         this.functionSelectionPages.put(var2, var3);
      }

      this.functionSelectionPage = var3;
      this.pageStack.push(var3);
      return var3;
   }

   private EntitySelectionPage<? extends NetSuiteFunction> createFunctionSelectionPage(NetSuiteFunction.FunctionType var1) {
      boolean var2 = this.isRequest && NetSuiteFunction.FunctionType.QUERY == var1;
      if (var2) {
         throw new RuntimeException("function type: " + var1 + " not applicable for netsuite request");
      }

      switch (var1) {
         case CREATE:
            return this.createFunctionSelectionPage(EntityType.NetSuiteCreate, NetSuiteCreate.class);
         case UPDATE:
            return this.createFunctionSelectionPage(EntityType.NetSuiteUpdate, NetSuiteUpdate.class);
         case UPSERT:
            return this.createFunctionSelectionPage(EntityType.NetSuiteUpsert, NetSuiteUpsert.class);
         case DELETE:
            return this.createFunctionSelectionPage(EntityType.NetSuiteDelete, NetSuiteDelete.class);
         case QUERY:
            return this.createFunctionSelectionPage(EntityType.NetSuiteQuery, NetSuiteQuery.class);
         default:
            throw new RuntimeException("Unexpected function type: " + var1);
      }
   }

   private <T extends NetSuiteFunction> EntitySelectionPage<T> createFunctionSelectionPage(EntityType var1, Class<T> var2) {
      String var3 = String.format("%s %s Definition", var1.getIndefiniteArticle(), var1.getDisplayName());
      String var4 = "Select " + var3;
      EntitySelectionPage var5 = new EntitySelectionPage(this.wizard, var4, this.wizard.getEntityExplorerSupport(), var1, var2, null);
      var5.setMainCaption(var4);
      var5.setSelectorLabel("&Available " + var3);
      return var5;
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.typeSelectionPage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.typeSelectionPage) {
         return null;
      }

      this.pageStack.pop();
      return this.pageStack.peek();
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.functionSelectionPage && var1.canContinue();
   }

   @Override
   public void applyTo(Transformation var1) throws WizardPageException {
      NetSuiteFunction var2 = this.functionSelectionPage.getSelectedEntity();
      DataStructure var3 = this.structureGetter.getStructure(var2);
      this.structureSetter.setStructure(var1, var3);
   }

   @Override
   public void setOpposingStructureNature(WizardType.Nature var1) {
   }
}
