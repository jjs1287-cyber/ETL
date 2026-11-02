package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;

public final class SoapWizardWorkflow<T extends IntegrationEntity & SalesforceWizardEntity> implements ObjectSummary<T> {
   private final IntegrationProject project;
   private final ImplementingObject<T> wizardObject;
   private final ImplementingObject<Operation> op;
   private final ImplementingObject<Transformation> request;
   private final ImplementingObject<Transformation> response;
   private final ImmutableList<ImplementingObject<?>> otherObjectsCreatedByWizard;

   public SoapWizardWorkflow(
      IntegrationProject var1,
      ImplementingObject<T> var2,
      ImplementingObject<Operation> var3,
      ImplementingObject<Transformation> var4,
      ImplementingObject<Transformation> var5,
      List<ImplementingObject<?>> var6
   ) {
      this.project = var1;
      this.wizardObject = var2;
      this.op = var3;
      this.request = var4;
      this.response = var5;
      this.otherObjectsCreatedByWizard = ImmutableList.copyOf(var6);
   }

   @Override
   public T getWizardObject() {
      return this.wizardObject.getObject();
   }

   @Override
   public List<IntegrationEntity> getItemsCreatedByWizard() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(this.op.getObject());
      var1.add(this.request.getObject());
      var1.add(this.response.getObject());

      for (ImplementingObject var3 : this.otherObjectsCreatedByWizard) {
         var1.add(var3.getObject());
      }

      return var1;
   }

   @Override
   public void setNewWizardName(String var1) {
      this.wizardObject.suggestNewName(var1);
      this.op.suggestNewName(var1);
      this.request.suggestNewName(var1 + " Request");
      this.response.suggestNewName(var1 + " Response");
   }

   @Override
   public void insertIntoProject() {
      ArrayList var1 = Lists.newArrayList(this.wizardObject, this.op, this.request, this.response);
      var1.addAll(this.otherObjectsCreatedByWizard);
      WizardItemsInserter var2 = new WizardItemsInserter(this.project, var1);
      var2.insertAllItems();
   }
}
