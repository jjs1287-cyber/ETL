package org.etlforge.integration.client.ui.connector.mscrm.function.txwiz;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Stack;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.connector.MSCrmCreate;
import org.etlforge.integration.data.connector.MSCrmDelete;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.connector.MSCrmQuery;
import org.etlforge.integration.data.connector.MSCrmUpdate;
import org.etlforge.integration.data.connector.MSCrmUpsert;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.mscrm.FunctionType;

final class MSCrmFunctionSelectionPageChain implements TransformationStructurePageChain {
   private final TransformationWizard wizard;
   private final FunctionTypeSelectionPage typeSelectionPage;
   private final Map<FunctionType, EntitySelectionPage<? extends MSCrmFunction>> functionSelectionPages = Maps.newHashMap();
   private EntitySelectionPage<? extends MSCrmFunction> functionSelectionPage;
   private final Stack<WizardPage> pageStack = new Stack<>();
   private final FunctionStructureGetter<MSCrmFunction> structureGetter;
   private final TransformationStructureSetter structureSetter;

   public MSCrmFunctionSelectionPageChain(
      TransformationWizard var1, FunctionStructureGetter<MSCrmFunction> var2, TransformationStructureSetter var3, boolean var4
   ) {
      this.wizard = var1;
      this.structureGetter = var2;
      this.structureSetter = var3;
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
      FunctionType var2 = this.typeSelectionPage.getSelectedValue();
      EntitySelectionPage var3 = this.functionSelectionPages.get(var2);
      if (var3 == null) {
         var3 = this.createFunctionSelectionPage(var2);
         this.functionSelectionPages.put(var2, var3);
      }

      this.functionSelectionPage = var3;
      this.pageStack.push(var3);
      return var3;
   }

   private EntitySelectionPage<? extends MSCrmFunction> createFunctionSelectionPage(FunctionType var1) {
      switch (var1) {
         case CREATE:
            return this.createFunctionSelectionPage(EntityType.MSCrmCreate, MSCrmCreate.class);
         case UPDATE:
            return this.createFunctionSelectionPage(EntityType.MSCrmUpdate, MSCrmUpdate.class);
         case DELETE:
            return this.createFunctionSelectionPage(EntityType.MSCrmDelete, MSCrmDelete.class);
         case RETRIEVE:
            return this.createFunctionSelectionPage(EntityType.MSCrmQuery, MSCrmQuery.class);
         case UPSERT:
            return this.createFunctionSelectionPage(EntityType.MSCrmUpsert, MSCrmUpsert.class);
         default:
            throw new RuntimeException("Unexpected function type: " + var1);
      }
   }

   private <T extends MSCrmFunction> EntitySelectionPage<T> createFunctionSelectionPage(EntityType var1, Class<T> var2) {
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
      MSCrmFunction var2 = this.functionSelectionPage.getSelectedEntity();
      DataStructure var3 = this.structureGetter.getStructure(var2);
      this.structureSetter.setStructure(var1, var3);
   }

   @Override
   public void setOpposingStructureNature(WizardType.Nature var1) {
   }
}
