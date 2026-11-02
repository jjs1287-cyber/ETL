package org.etlforge.application.ui.widget.password;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

final class WindowsPasswordDefinitionUi extends StandardPasswordDefinitionUi {
   private CapsLockWarning capsLockWarning;

   public WindowsPasswordDefinitionUi(String var1) {
      super(var1);
   }

   @Override
   public void addCapsLockListener(JComponent var1) {
      this.capsLockWarning.activate(var1);
   }

   @Override
   protected JPanel createInputPanel() {
      this.capsLockWarning = new CapsLockWarning();
      return super.createInputPanel();
   }

   @Override
   protected void layoutInputPanel(JPanel var1, String var2) {
      super.layoutInputPanel(var1, var2);
      var1.add(new JLabel(" "));
      this.capsLockWarning.addToParent(var1, null);
   }

   @Override
   public void removeCapsLockListener(JComponent var1) {
      this.capsLockWarning.dispose();
   }

   @Override
   public void dispose() {
      super.dispose();
      this.capsLockWarning.dispose();
   }
}
