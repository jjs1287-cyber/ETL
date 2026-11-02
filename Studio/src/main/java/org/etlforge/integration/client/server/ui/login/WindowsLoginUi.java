package org.etlforge.integration.client.server.ui.login;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.password.CapsLockWarning;

public final class WindowsLoginUi extends StandardLoginUi {
   public static final String SELECTED_SERVER_PROPERTY = "selected_server";
   private CapsLockWarning capsLockWarning;

   public WindowsLoginUi(LoginModel var1) {
      super(var1);
   }

   @Override
   public void dispose() {
      super.dispose();
      this.capsLockWarning.dispose();
   }

   @Override
   protected JPanel createInputPanel() {
      JPanel var1 = super.createInputPanel();
      this.capsLockWarning = new CapsLockWarning();
      this.addCapsLockWarning(var1);
      return var1;
   }

   @Override
   public void setCapsLockListener(JComponent var1) {
      this.capsLockWarning.activate(var1);
   }

   private void addCapsLockWarning(JPanel var1) {
      var1.add(new JLabel(" "));
      this.capsLockWarning.addToParent(var1, null);
   }
}
