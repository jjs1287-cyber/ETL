package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.interchange.entity.message.TestEmailSender;
import org.etlforge.integration.client.ui.interchange.entity.message.TestEmailUiJob;
import org.etlforge.integration.data.entity.EmailMessage;

public final class TestSendEmailAction extends AbstractSingleActiveObjectAction<EmailMessage> {
   public static final String ID = "action.ic.TestEmail";
   private final TestEmailSender emailSender;
   private final EntityPageSaveGate saveGate;

   public TestSendEmailAction(ApplicationWindow var1, TestEmailSender var2, EntityPageSaveGate var3) {
      super(var1.getActiveObjectService(), "action.ic.TestEmail", InterchangeActionDictionary.DICTIONARY, EmailMessage.class);
      this.emailSender = var2;
      this.saveGate = var3;
   }

   protected void runOnObject(final EmailMessage var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestSendEmailAction.this.saveAndStartTest(var1);
         }
      });
   }

   private void saveAndStartTest(final EmailMessage var1) {
      this.saveGate.runInCallingThread(var1, new Runnable() {
         @Override
         public void run() {
            TestSendEmailAction.this.startTest(var1);
         }
      });
   }

   private void startTest(EmailMessage var1) {
      TestEmailUiJob var2 = new TestEmailUiJob(var1, this.emailSender);
      var2.submit();
   }
}
