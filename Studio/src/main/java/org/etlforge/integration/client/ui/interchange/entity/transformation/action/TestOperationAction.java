package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTester;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.resources.TransformationResources;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;

public final class TestOperationAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.TestOperation";
   private final TransformationPage page;
   private final OperationTesterFactory testerFactory;

   public TestOperationAction(TransformationPage var1, OperationTesterFactory var2) {
      super("action.ic.tx.TestOperation", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
      this.testerFactory = var2;
      if (this.isWebServiceResponse()) {
         this.decorateForWebService();
      }
   }

   private boolean isWebServiceResponse() {
      Transformation var1 = this.page.getObject();
      DataStructure var2 = var1.getInputStructure();
      return var2 != null && var2.getStructureType() == DataStructureType.WebServiceResponse;
   }

   private void decorateForWebService() {
      this.putValue("Name", TransformationResources.getString("TestWebService.Name"));
      this.putValue("SmallIcon", ClientIcons.TEST_WEBSERVICE_16);
      this.putValue("KongaMediumIconKey", ClientIcons.TEST_WEBSERVICE_24);
      this.putValue("ShortDescription", "Test this web service call");
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestOperationAction.this.saveAndStartTest();
         }
      });
   }

   private void saveAndStartTest() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            OperationTester var1 = TestOperationAction.this.testerFactory.newTester(TestOperationAction.this.page);
            var1.testOperation(TestOperationAction.this.page.getObject());
         }
      });
   }
}
