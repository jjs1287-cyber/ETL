package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ValidStateBinding;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.Binding;

final class QuerySelectionPage extends AbstractWizardPage {
   private final QuerySelectionUi view;
   private final QuerySelectionController controller;

   public QuerySelectionPage(Wizard var1, QuerySelectionModel var2, QuerySelectionController var3) {
      super(var1, "Select a Query");
      this.controller = var3;
      this.view = new QuerySelectionUi(var2);
      this.view.setDownloadAction(new QuerySelectionPage.DownloadSavedQueriesAction(var3));
      var3.setWaitService(this.getWaitService());
      Binding var4 = ValidStateBinding.notEmpty(this, var2, "xml");
      var4.syncUi();
   }

   public String getDefinedQueryXml() {
      return this.view.getDefinedQueryXml();
   }

   public boolean canWizardForward() {
      return this.view.canWizardForward();
   }

   @Override
   protected void dispose() {
      this.controller.cancelRunningJobs();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      try {
         if (!this.view.canWizardForward()) {
            return false;
         }

         String var1 = this.view.getDefinedQueryXml();
         this.controller.validateQueryXml(var1);
         return true;
      } catch (Exception var2) {
         NonBlockingAlert.error(this.getMainPanel(), var2.getMessage(), "Invaild Query");
         return false;
      }
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      this.controller.cancelRunningJobs();
      return true;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle(), "Download a saved query, or paste a query XML into the text area below.");
      JPanel var2 = BorderLayoutBuilder.fillWith(this.view);
      var1.add(var2, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      this.view.requestFocus();
   }

   private static class DownloadSavedQueriesAction extends AbstractAction {
      private final QuerySelectionController controller;

      public DownloadSavedQueriesAction(QuerySelectionController var1) {
         super("&Download Saved Queries");
         this.controller = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DownloadSavedQueriesAction.this.controller.getQueryList();
            }
         });
      }
   }
}
