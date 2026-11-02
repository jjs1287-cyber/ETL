package com.etlforge.integration.client.ui.sfdc.wizard.login;

import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class LoginPage extends AbstractWizardPage implements Disposable {
   private final ConnectorSelector ui;
   private final LastConnectorMemento lastConnectorMemento;

   public LoginPage(Wizard<?> var1, ApplicationWindow var2, EntityExplorerSupport var3, SalesforceConnectionFactory var4) {
      super(var1, "Login");
      this.lastConnectorMemento = new LastConnectorMemento(var3.getProject());
      this.ui = ConnectorSelector.newSelector(var2, var3, var4);
      this.ui.setValidStateReceiver(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            LoginPage.this.setCanContinue(var1);
         }
      });
      this.ui.setSelectedConnector(this.lastConnectorMemento.getLast());
      this.setCanContinue(this.ui.isInputValid());
   }

   public SalesforceConnector getSelectedConnector() {
      return this.ui.getSelectedConnector();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      LeavePageWorker var1 = new LeavePageWorker((SalesforceWizard)this.getWizard(), this.ui, this.lastConnectorMemento);
      return var1.leavePage();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Salesforce Login");
      var1.add(InvisiblePanel.of(this.ui), true, false);
      JPanel var2 = var1.asNorthBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }

   @Override
   public void dispose() {
      this.ui.dispose();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LoginPage.this.ui.requestFocus();
         }
      });
   }

   @Override
   protected String getForwardNavigationMessage() {
      return "Logging in to Salesforce";
   }
}
