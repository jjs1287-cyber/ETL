package com.etlforge.integration.client.ui.sfdc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTester;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;

public final class TestOperationAction extends AbstractAction {
   private final Operation op;
   private final OperationTester tester;
   private final IntegrationEntityPage page;

   public TestOperationAction(String var1, Operation var2, OperationTester var3, IntegrationEntityPage var4) {
      super("&Test " + var1, ClientIcons.TEST_WEBSERVICE_16);
      this.tester = var3;
      this.op = var2;
      this.page = var4;
      this.putValue("KongaMediumIconKey", ClientIcons.TEST_WEBSERVICE_24);
      this.putValue("SwingLargeIconKey", ClientIcons.TEST_WEBSERVICE_24);
      if (this.isTestable()) {
         this.setEnabled(var3 != null && var2 != null);
      } else {
         this.setEnabled(false);
         this.putValue("ShortDescription", "This wizard cannot be tested since it does not have a mapping tree for its response.");
      }
   }

   private boolean isTestable() {
      if (this.op.getOperationType() != WebServiceOperationType.TYPE) {
         return false;
      }

      WebServicePipelineBrowser var1 = new WebServicePipelineBrowser(this.op.getPipeline());
      Transformation var2 = var1.getResponse();
      return var2 != null && var2 != SystemTransformation.SALESFORCE_STATUS_RESPONSE;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (TestOperationAction.this.isTestable()) {
               TestOperationAction.this.test();
            } else {
               NonBlockingAlert.info("This wizard cannot be tested.", "Not testable");
            }
         }
      });
   }

   private void test() {
      boolean var1 = this.savePageIfDirty();
      if (var1) {
         this.tester.testOperation(this.op);
      }
   }

   private boolean savePageIfDirty() {
      if (this.page.isDirty()) {
         try {
            this.page.save();
         } catch (SaveEditorException var2) {
            ErrorLog.attention(this.getClass(), null, var2);
            return false;
         }
      }

      return true;
   }
}
