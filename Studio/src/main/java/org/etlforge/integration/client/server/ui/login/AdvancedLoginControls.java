package org.etlforge.integration.client.server.ui.login;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class AdvancedLoginControls {
   private final LoginModel model;
   private final DropDownPanel dropDown;
   private final JCheckBox httpsSwitch;
   private final PortPanel portPanel;

   public AdvancedLoginControls(LoginModel var1) {
      this.model = var1;
      this.httpsSwitch = this.createHttpsBox();
      this.portPanel = new PortPanel(var1);
      this.dropDown = this.createDropDown(var1);
      BooleanBinding.bindAndSyncUi(var1, "useHttps", this.httpsSwitch);
      var1.addPropertyChangeListener("server", new AdvancedLoginControls.DropDownExpander());
   }

   private JCheckBox createHttpsBox() {
      KongaCheckBox var1 = new KongaCheckBox(PackageResources.Labels.HTTPS);
      var1.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            if (var1 && AdvancedLoginControls.this.dropDown != null) {
               AdvancedLoginControls.this.dropDown.expand();
            }
         }
      });
      return var1;
   }

   private DropDownPanel createDropDown(LoginModel var1) {
      JPanel var2 = this.createDropDownContent();
      boolean var3 = this.shouldDropDownBeExpanded(var1);
      DropDownPanel var4 = new DropDownPanel(var2, PackageResources.Labels.ADVANCED, var3);
      this.decorateTitle(var4);
      var4.setRepackDialogWhenExpansionStateChanges(true);
      var4.setOpaque(false);
      return var4;
   }

   private boolean shouldDropDownBeExpanded(LoginModel var1) {
      return var1.isUsingNonDefaultSettings();
   }

   private JPanel createDropDownContent() {
      return new OneColumnPanel().withInsets(3, 5, 10, 10).of(this.httpsSwitch, this.portPanel.getDisplayer()).deeplyNonOpaque().invisible();
   }

   private void decorateTitle(DropDownPanel var1) {
      var1.setTitleFont(TextStyles.AdminHint.getFont());
      var1.setTitleColor(TextStyles.AdminHint.getColor());
   }

   public JComponent getUi() {
      return this.dropDown.getDisplayer();
   }

   private class DropDownExpander implements PropertyChangeListener {
      private DropDownExpander() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.expandDropDownIfNeeded();
      }

      private void expandDropDownIfNeeded() {
         DelayedJob.createAndSchedule(125, new Runnable() {
            @Override
            public void run() {
               if (!DropDownExpander.this.isButtonFocused()) {
                  boolean var1 = AdvancedLoginControls.this.shouldDropDownBeExpanded(AdvancedLoginControls.this.model);
                  if (var1) {
                     AdvancedLoginControls.this.dropDown.expand();
                  }
               }
            }
         });
      }

      private boolean isButtonFocused() {
         KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
         Component var2 = var1.getFocusOwner();
         return var2 instanceof AbstractButton;
      }
   }
}
