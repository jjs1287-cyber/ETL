package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.wsdl.ObjectSchemaLookup;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public final class Controller {
   private final RelationalFieldSelectorModel model;
   private final ObjectSchemaLookup schemaLookup;
   private WaitService waitService = WaitService.NONE;

   public Controller(RelationalFieldSelectorModel var1, ObjectSchemaLookup var2) {
      this.model = Preconditions.checkNotNull(var1);
      this.schemaLookup = Preconditions.checkNotNull(var2);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   public void selectParent(final FieldOwner var1, final Relationship var2) {
      this.runComputation(new Callable<ParentRelationship>() {
         public ParentRelationship call() throws Exception {
            SObjectSchema var1x = Controller.this.describeObject(var2.getObjectType());
            return new ParentRelationship(var2.getRelationshipName(), var1, var1x);
         }
      });
   }

   private <T extends FieldOwner> void runComputation(Callable<T> var1) {
      Computation.create(var1, this.getSelectObjectConsumer()).swingWorkerBuilder().withWaitService(this.waitService).buildAndExecute();
   }

   private SObjectSchema describeObject(String var1) throws SalesforceException {
      SObjectSchema var2 = this.model.getSchema(var1);
      if (var2 == null) {
         var2 = this.schemaLookup.apply(var1);
         this.model.storeSchema(var2);
      }

      return var2;
   }

   private Receiver<FieldOwner> getSelectObjectConsumer() {
      return new Receiver<FieldOwner>() {
         public void handle(FieldOwner var1) {
            Controller.this.model.setSelectedObject(var1);
         }
      };
   }

   public void selectChild(final RootObject var1, final Relationship var2) {
      this.runComputation(new Callable<ChildRelationship>() {
         public ChildRelationship call() throws Exception {
            SObjectSchema var1x = Controller.this.describeObject(var2.getObjectType());
            return new ChildRelationship(var2.getRelationshipName(), var1, var1x);
         }
      });
   }
}
