package org.etlforge.application.ui;

import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JCheckBox;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public class DoNotShowAgainMessage {
   private final Object message;
   private final String title;
   private final BooleanPreference pref;
   private final JCheckBox doNotShowAgainCheck;

   public DoNotShowAgainMessage(Object var1, String var2, BooleanPreference var3) {
      this.message = var1;
      this.title = var2;
      this.pref = var3;
      this.doNotShowAgainCheck = new KongaCheckBox(ApplicationResources.STRINGS.getString("DoNotShowAgain"));
   }

   public boolean showInDialog(Component var1) {
      if (this.pref.get()) {
         Alert.message(var1, this.createUi(), this.title, Alert.Type.INFORMATION);
         this.updatePreference();
         return true;
      } else {
         return false;
      }
   }

   public void showInDialogLater(final Component var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DoNotShowAgainMessage.this.showInDialog(var1);
         }
      });
   }

   public boolean showAsOverlay(OverlayContainer var1) {
      if (this.pref.get()) {
         OverlayAlert var2 = new OverlayAlert(this.createUi(), this.title) {
            @Override
            protected void onClosed() {
               DoNotShowAgainMessage.this.updatePreference();
            }
         };
         var2.show(var1);
         return true;
      } else {
         return false;
      }
   }

   private void updatePreference() {
      if (this.doNotShowAgainCheck.isSelected()) {
         this.pref.set(false);
      }
   }

   private UiProvider createUi() {
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1) {
            OneColumnPanel var2 = new OneColumnPanel();
            var2.setWeightX(1.0);
            var2.withInsets(0, 0, 20, 0);
            var2.add(Alert.getDisplayMessage(DoNotShowAgainMessage.this.message));
            var2.withInsets(0, 0, 0, 0);
            var2.add(DoNotShowAgainMessage.this.doNotShowAgainCheck);
            var2.decorate(var1);
         }

         @Override
         public void requestFocus() {
            DoNotShowAgainMessage.this.doNotShowAgainCheck.requestFocus();
         }
      };
   }
}
