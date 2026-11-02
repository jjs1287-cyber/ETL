package org.etlforge.integration.client.server.ui.login;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.undo.UndoRedoInstallerFactory;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.DialogLayout;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

public class StandardLoginUi implements LoginUi {
   private final LoginModel model;
   private Receiver<LoginCredentials> credentialsReceiver = NoReceiver.create();
   private Receiver<Boolean> validStateMonitor = NoReceiver.create();
   private final JPanel inputPanel;
   private final ServerSelector serverSelector;
   private final UserCredentialsUi userCredentialsUi;
   private final AdvancedLoginControls advancedControls;
   private UndoRedoInstaller undoRedoInstaller;

   public StandardLoginUi(LoginModel var1) {
      this.model = var1;
      this.serverSelector = new ServerSelector(var1);
      this.serverSelector.setDefaultAction(new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            StandardLoginUi.this.credentialsReceiver.handle(StandardLoginUi.this.getCredentials());
         }
      });
      this.userCredentialsUi = new UserCredentialsUi(var1);
      this.advancedControls = new AdvancedLoginControls(var1);
      this.inputPanel = this.createInputPanel();
      this.installUndoRedo();
      this.bindModel();
      var1.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            StandardLoginUi.this.notifyValidStateMonitor();
         }
      });
   }

   private void bindModel() {
      if (this.model.isServerSelectionAllowed()) {
         TextBinding.bindAndSyncUi(this.model, "temporaryServer", this.serverSelector.getEditor());
      } else {
         this.model.setServer(ClientInfo.getDataLoaderServer());
      }
   }

   @Override
   public final void setCredentialsReceiver(Receiver<LoginCredentials> var1) {
      this.credentialsReceiver = var1;
   }

   @Override
   public final void setValidStateMonitor(Receiver<Boolean> var1) {
      this.validStateMonitor = var1;
      this.notifyValidStateMonitor();
   }

   private void notifyValidStateMonitor() {
      boolean var1 = this.isInputValid();
      this.validStateMonitor.handle(var1);
   }

   @Override
   public void setCapsLockListener(JComponent var1) {
   }

   @Override
   public void dispose() {
      this.disposeUndoRedo();
   }

   @Override
   public final void decorate(UiCanvas var1) {
      UiUtils.setOpaqueRecursivelySkipDefaults(this.inputPanel, false);
      var1.display(this.inputPanel);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            StandardLoginUi.this.requestFocus();
         }
      });
   }

   @Override
   public final void requestFocus() {
      this.getComponentToFocus().requestFocusInWindow();
   }

   private JComponent getComponentToFocus() {
      return !this.model.hasPassword() && this.model.hasServer() ? this.userCredentialsUi.getComponentToFocus() : this.serverSelector.getUi();
   }

   @Override
   public final boolean isInputValid() {
      return this.model.isInputValid();
   }

   @Override
   public final LoginCredentials getCredentials() {
      return this.model.getCredentials();
   }

   private void installUndoRedo() {
      this.undoRedoInstaller = UndoRedoInstallerFactory.newInstaller();
      this.undoRedoInstaller.install(this.serverSelector.getUi());
      this.userCredentialsUi.installUndoRedo(this.undoRedoInstaller);
   }

   private void disposeUndoRedo() {
      this.undoRedoInstaller.dispose();
   }

   protected JPanel createInputPanel() {
      JPanel var1 = new JPanel(new DialogLayout());
      this.layoutServerSelector(var1);
      this.userCredentialsUi.layoutIn(var1);
      this.layoutAdvancedControls(var1);
      var1.setBorder(Empty.border(20, 10, 5, 10));
      return var1;
   }

   private void layoutServerSelector(JPanel var1) {
      if (this.model.isServerSelectionAllowed()) {
         KongaLabel var2 = KongaLabel.withMnemonic(ServerUiResources.getFormattedMessage("Login.Server", ApplicationConfiguration.getCompanyName()));
         var2.setIcon(ClientIcons.APPLICATION_16);
         this.serverSelector.setLabel(var2);
         var1.add(var2);
         var1.add(this.serverSelector.getUi());
      }
   }

   private void layoutAdvancedControls(JPanel var1) {
      if (this.model.isServerSelectionAllowed()) {
         var1.add(new JLabel(" "));
         var1.add(this.advancedControls.getUi());
      }
   }
}
