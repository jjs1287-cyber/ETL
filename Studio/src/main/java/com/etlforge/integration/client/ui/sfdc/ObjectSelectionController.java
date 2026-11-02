package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public final class ObjectSelectionController {
   private final ObjectSelectionModel model;
   private final SalesforceFacade facade;
   private WaitService waitService = WaitService.NONE;

   public ObjectSelectionController(ObjectSelectionModel var1, SalesforceFacade var2) {
      this.model = var1;
      this.facade = var2;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void getObjectList() {
      Callable var1 = new Callable<List<SObjectSchema>>() {
         public List<SObjectSchema> call() throws Exception {
            return ObjectSelectionController.this.facade.fetchAvailableSObjects();
         }
      };
      this.retrieveObjects(var1, "Retrieving list of objects");
   }

   private void retrieveObjects(Callable<List<SObjectSchema>> var1, String var2) {
      this.model.setAvailableObjects(Collections.emptyList());
      Receiver var3 = new Receiver<List<SObjectSchema>>() {
         public void handle(List<SObjectSchema> var1) {
            ObjectSelectionController.this.model.setAvailableObjects(var1);
         }
      };
      Computation var4 = Computation.create(var1, var3);
      var4.swingWorkerBuilder().withErrorHandler(new AttentionErrorHandler(this.getClass())).withWaitMessage(this.waitService, var2).buildAndExecute();
   }

   public void refreshObjectList() {
      Callable var1 = new Callable<List<SObjectSchema>>() {
         public List<SObjectSchema> call() throws Exception {
            return ObjectSelectionController.this.facade.refreshObjectList();
         }
      };
      this.retrieveObjects(var1, "Refreshing the list of objects");
   }
}
