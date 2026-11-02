package com.etlforge.integration.license.client.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JRootPane;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.label.KongaLabel;

public final class LicenseMessageDialog {
   private final LicenseMessage message;
   private Class<?> preferenceKey;
   private RequestNewLicenseAction requestNewLicenseAction;

   public LicenseMessageDialog(LicenseMessage var1) {
      this.message = var1;
   }

   public void setRequestNewLicenseAction(RequestNewLicenseAction var1) {
      this.requestNewLicenseAction = var1;
   }

   public void setPreferenceKey(Class<?> var1) {
      this.preferenceKey = var1;
   }

   public void open() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            KongaDialog var1 = LicenseMessageDialog.this.createDialog();
            var1.setVisible(true);
         }
      });
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(UiUtils.getActiveFrame(), this.message.getTitle(), true);
      LicenseMessageDialog.CloseAction var2 = new LicenseMessageDialog.CloseAction(var1);
      this.message.setCloseAction(var2);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 5);
      var3.center(this.message).south(this.getCopyMessageInstructions());
      var3.withEmptyBorder(10).asContentPaneFor(var1);
      if (this.requestNewLicenseAction != null) {
         this.requestNewLicenseAction.setDialogToClose(var1);
      }

      var1.pack();
      var1.setResizable(false);
      if (this.preferenceKey != null) {
         var1.manageLocation(this.preferenceKey);
      } else {
         var1.setLocationRelativeTo(null);
      }

      UiUtils.makeDefaultButton(var1.getRootPane(), var2);
      this.installCopyAction(var1);
      this.installCloseButtonAsDefaultHack(var1.getRootPane(), var2);
      return var1;
   }

   private Object getCopyMessageInstructions() {
      KongaLabel var1 = TextStyles.DefaultTextSmall.makeLabel("Use CTRL-C to copy this message");
      var1.setBorder(new BorderBuilder().etched().empty(2));
      return var1;
   }

   private void installCopyAction(KongaDialog var1) {
      var1.getRootPane().getInputMap(2).put(KeyUtils.COPY, "copy");
      var1.getRootPane().getActionMap().put("copy", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            LicenseMessageDialog.this.message.copyInformationToClipboard();
         }
      });
   }

   private void installCloseButtonAsDefaultHack(final JRootPane var1, final Action var2) {
      var1.addPropertyChangeListener("defaultButton", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1x) {
            JButton var2x = var1.getDefaultButton();
            if (var2x != null && var2x.getAction() != var2) {
               UiUtils.makeDefaultButton(var1, var2);
            }
         }
      });
   }

   private static class CloseAction extends AbstractAction {
      private final KongaDialog dialog;

      public CloseAction(KongaDialog var1) {
         super("Close");
         this.dialog = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.dialog.dispose();
      }
   }
}
