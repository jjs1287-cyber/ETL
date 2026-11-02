package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.ImplementingObject;
import com.etlforge.integration.client.sfdc.data.ObjectSummary;
import com.etlforge.integration.client.sfdc.data.WizardItemsInserter;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.project.IntegrationProject;

public final class BulkApiWorkflow implements ObjectSummary<SalesforceBulkLoad> {
   private final IntegrationProject project;
   private final ImplementingObject<SalesforceBulkLoad> wizardObject;
   private final ImplementingObject<Operation> operation;
   private final ImmutableList<ImplementingObject<?>> otherObjectsCreatedByWizard;

   public BulkApiWorkflow(
      IntegrationProject var1, ImplementingObject<SalesforceBulkLoad> var2, ImplementingObject<Operation> var3, List<ImplementingObject<?>> var4
   ) {
      this.project = var1;
      this.wizardObject = var2;
      this.operation = var3;
      this.otherObjectsCreatedByWizard = ImmutableList.copyOf(var4);
   }

   public SalesforceBulkLoad getWizardObject() {
      return this.wizardObject.getObject();
   }

   @Override
   public List<IntegrationEntity> getItemsCreatedByWizard() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(this.operation.getObject());

      for (ImplementingObject var3 : this.otherObjectsCreatedByWizard) {
         var1.add(var3.getObject());
      }

      return var1;
   }

   @Override
   public void setNewWizardName(String var1) {
      this.wizardObject.suggestNewName(var1);
      this.operation.suggestNewName(var1);
   }

   @Override
   public void insertIntoProject() {
      ArrayList var1 = Lists.newArrayList(this.wizardObject, this.operation);
      var1.addAll(this.otherObjectsCreatedByWizard);
      WizardItemsInserter var2 = new WizardItemsInserter(this.project, var1);
      var2.insertAllItems();
   }
}
