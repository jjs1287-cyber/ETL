package com.etlforge.integration.client.ui.sfdc.page.v4;

import com.etlforge.integration.client.sfdc.data.LoginScriptExpression;
import com.etlforge.integration.client.ui.sfdc.InterchangeKnownWsdlsStore;
import com.etlforge.integration.client.ui.sfdc.LoginCredentialsUi;
import com.etlforge.integration.salesforce.wsdl.KnownWsdlsStore;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.undo.UndoRedoInstallerFactory;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class CredentialsEditor implements UiProvider {
   private final SalesforceWizardEntity entity;
   private final EntityPersistor persistor;
   private final Receiver<EntityPageSaveJob> callback;
   private LoginCredentials credentials;
   private final Script loginScript;
   private final Action editAction;

   public CredentialsEditor(SalesforceWizardEntity var1, EntityPersistor var2, Receiver<EntityPageSaveJob> var3) {
      this(var1, locateLoginScript(var1), var2, var3);
   }

   private static Script locateLoginScript(SalesforceWizardEntity var0) {
      WizardItemBrowser var1 = new WizardItemBrowser(var0);
      return var1.getLoginScript();
   }

   public CredentialsEditor(SalesforceWizardEntity var1, Script var2, EntityPersistor var3, Receiver<EntityPageSaveJob> var4) {
      this.entity = var1;
      this.persistor = var3;
      this.callback = var4;
      this.loginScript = var2;
      if (var2 == null) {
         this.editAction = new CredentialsEditor.EditCredentialsAction();
         this.editAction.setEnabled(false);
      } else {
         this.credentials = this.getOriginalCredentials();
         this.editAction = new CredentialsEditor.EditCredentialsAction();
      }
   }

   private LoginCredentials getOriginalCredentials() {
      String var1 = this.loginScript.getScript();
      return LoginScriptExpression.getCredentials(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(InvisiblePanel.of(this.editAction));
   }

   @Override
   public void requestFocus() {
   }

   private final class EditCredentialsAction extends AbstractAction {
      private KongaDialog dialog;
      private LoginCredentialsUi ui;

      public EditCredentialsAction() {
         super("Change credentials...");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditCredentialsAction.this.openDialog();
            }
         });
      }

      private void openDialog() {
         this.ui = new LoginCredentialsUi();
         this.ui.setCredentials(CredentialsEditor.this.credentials);
         this.dialog = this.createDialog();
         this.bindOkButtonToValidState();
         this.dialog.setVisible(true);
         if (this.dialog.wasOkPressed() && this.ui.isInputValid()) {
            this.handleNewCredentials();
         }

         this.ui = null;
         this.dialog = null;
      }

      private KongaDialog createDialog() {
         KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), "Login Credentials") {
            @Override
            protected void onDialogOpened() {
               EditCredentialsAction.this.ui.requestFocus();
            }
         };
         var1.standardLayout(this.ui, KongaDialog.OK_CANCEL);
         var1.setResizable(false);
         var1.manageLocation(CredentialsEditor.class);
         var1.setOkButtonAsDefault();
         this.installUndoRedo(var1);
         return var1;
      }

      private void installUndoRedo(KongaDialog var1) {
         UndoRedoInstaller var2 = UndoRedoInstallerFactory.newInstaller();
         var2.installIntoComponentTree(var1.getContentPane());
      }

      private void bindOkButtonToValidState() {
         this.ui.addChangeListener(new DocumentContentChangeListener() {
            @Override
            public void contentChanged(DocumentEvent var1) {
               EditCredentialsAction.this.updateOkButton();
            }
         });
         this.updateOkButton();
      }

      private void updateOkButton() {
         boolean var1 = this.ui.isInputValid();
         this.dialog.getOKButton().setEnabled(var1);
      }

      private void handleNewCredentials() {
         LoginCredentials var1 = CredentialsEditor.this.credentials;
         CredentialsEditor.this.credentials = this.ui.getCredentials(var1.sandbox);
         if (!var1.equals(CredentialsEditor.this.credentials)) {
            this.notifyCallback(var1);
         }
      }

      private void notifyCallback(LoginCredentials var1) {
         CredentialsEditor.WsdlCredentialsUpdaterImpl var2 = CredentialsEditor.this.new WsdlCredentialsUpdaterImpl();
         Receiver var3 = new Receiver<LoginCredentials>() {
            public void handle(LoginCredentials var1) {
               CredentialsEditor.this.credentials = var1;
            }
         };
         CredentialsSaveJob var4 = new CredentialsSaveJob(
            CredentialsEditor.this.loginScript, CredentialsEditor.this.persistor, var2, var1, CredentialsEditor.this.credentials, var3
         );
         CredentialsEditor.this.callback.handle(var4);
      }
   }

   private class WsdlCredentialsUpdaterImpl implements WsdlsStoreCredentialsUpdater, ApplicationWorker.SafeRunnable {
      private WsdlCredentialsUpdaterImpl() {
      }

      @Override
      public void updateCredentials(LoginCredentials var1) {
         Application.getWorker().submitForParallel(this);
      }

      @Override
      public void run() {
         String var1 = this.getWsdlName();
         if (var1 != null) {
            KnownWsdlsStore var2 = InterchangeKnownWsdlsStore.getStore();
            var2.updateCredentials(var1, CredentialsEditor.this.credentials);
         }
      }

      private String getWsdlName() {
         WizardItemBrowser var1 = new WizardItemBrowser(CredentialsEditor.this.entity);
         WebServiceCall var2 = var1.getWebServiceCall();
         return var2 != null ? var2.getWsdlFile().getLocator() : null;
      }
   }
}
