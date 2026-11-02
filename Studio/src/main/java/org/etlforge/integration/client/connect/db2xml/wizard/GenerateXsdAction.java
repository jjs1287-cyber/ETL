package org.etlforge.integration.client.connect.db2xml.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

final class GenerateXsdAction extends AbstractAction {
   private final XsdGeneratorModel model;
   private final Receiver<DbToXsdFactory.XSD> callback;
   private WaitService waitService = WaitService.NONE;

   public GenerateXsdAction(XsdGeneratorModel var1, Receiver<DbToXsdFactory.XSD> var2) {
      super("&Generate XSD...");
      this.model = var1;
      this.callback = var2;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            GenerateXsdAction.this.model.setNotReady();
            Computation var1x = GenerateXsdAction.this.createJob();
            GenerateXsdAction.this.startJobInBackgroundThread(var1x);
         }
      });
   }

   private Computation<DbToXsdFactory.XSD> createJob() {
      XsdProducer var1 = new XsdProducer(this.model);
      return Computation.create(var1, this.callback);
   }

   private void startJobInBackgroundThread(Computation<DbToXsdFactory.XSD> var1) {
      var1.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, GenerateXsdPage.class).withWaitService(this.waitService).buildAndExecute();
   }
}
