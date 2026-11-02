package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.ui.interchange.entity.webservice.CallStructureRetriever;
import org.etlforge.integration.client.ui.interchange.entity.webservice.GetJtrJob;
import org.etlforge.integration.client.ui.interchange.entity.webservice.prefs.ShowWebServiceStructuresPreference;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

final class OperationSelectionHandler implements ActionListener {
   private final WebServiceWizardOperationPage page;
   private Timer timer;
   private UiJob getJtrProcess;

   public OperationSelectionHandler(WebServiceWizardOperationPage var1) {
      this.page = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      WebServiceOperationInfo var2 = this.page.getUi().getSelectedOperation();
      if (var2 != this.page.getCurrentOp()) {
         this.stopTimer();
         if (var2 == null) {
            this.handleNoSelection();
         } else {
            this.handleNewSelection(var2);
         }
      }
   }

   private void handleNoSelection() {
      this.page.setCurrentOpInfo(null);
   }

   private void handleNewSelection(WebServiceOperationInfo var1) {
      this.page.setCurrentOpInfo(var1);
      if (ShowWebServiceStructuresPreference.showStructures()) {
         StructureCache var2 = this.page.getWizard().getStructureCache();
         WebServiceCallStructures var3 = var2.get(var1);
         if (var3 != null) {
            this.page.getUi().displayTreeStructures(var3);
         } else {
            this.startTimer(var1);
         }
      }
   }

   private void stopTimer() {
      if (this.timer != null) {
         this.timer.stop();
      }
   }

   private void startTimer(WebServiceOperationInfo var1) {
      this.timer = new Timer(300, new OperationSelectionHandler.TimerTask(var1));
      this.timer.setRepeats(false);
      this.timer.start();
   }

   private final class TimerTask implements ActionListener, CallStructureRetriever {
      private WebServiceOperationInfo opInfo;

      public TimerTask(WebServiceOperationInfo var2) {
         this.opInfo = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (OperationSelectionHandler.this.getJtrProcess != null) {
            ApplicationUi.getJobManager().cancelJob(OperationSelectionHandler.this.getJtrProcess);
         }

         OperationSelectionHandler.this.getJtrProcess = new GetJtrJob(this, this.opInfo, OperationSelectionHandler.this.page.getWaitService());
         OperationSelectionHandler.this.getJtrProcess.submit();
      }

      @Override
      public void onJTRsRetrieved(WebServiceCallStructures var1) {
         OperationSelectionHandler.this.page.onCallStructuresRetrieved(this.opInfo, var1);
      }
   }
}
