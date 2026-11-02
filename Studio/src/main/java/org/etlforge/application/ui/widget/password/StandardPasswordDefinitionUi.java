package org.etlforge.application.ui.widget.password;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.DialogLayout;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.util.focus.TextComponentFocusTraversal;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;

class StandardPasswordDefinitionUi implements PasswordDefinitionUi {
   private final String label;
   private final JPanel inputPanel;
   private final PasswordField pwdField_1;
   private final PasswordField pwdField_2;
   private UndoRedoController undoRedoController;
   private boolean includeRetypeField;

   public StandardPasswordDefinitionUi(String var1) {
      this.label = var1;
      this.pwdField_1 = createPasswordField("Sh&ow password");
      this.pwdField_2 = createPasswordField("Sho&w password");
      this.inputPanel = this.createInputPanel();
      this.inputPanel.setFocusTraversalPolicyProvider(true);
      this.inputPanel.setFocusTraversalPolicy(new TextComponentFocusTraversal(this.inputPanel.getFocusTraversalPolicy()));
   }

   private static PasswordField createPasswordField(String var0) {
      PasswordField var1 = new PasswordField();
      var1.setValidationEnabled(false);
      var1.setShowPasswordText(var0);
      return var1;
   }

   protected JPanel createInputPanel() {
      return new JPanel(new DialogLayout());
   }

   @Override
   public final boolean validate() {
      if (this.validatePasswords()) {
         if (this.checkEmptyPassword()) {
            return true;
         }
      } else {
         Alert.error(this.inputPanel, PackageResources.MisMatch.MESSAGE, PackageResources.MisMatch.TITLE);
      }

      return false;
   }

   @Override
   public final char[] getPassword() {
      return this.pwdField_1.getPassword().toCharArray();
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.layoutInputPanel(this.inputPanel, this.label);
      this.createUndoRedoController();
      var1.display(this.inputPanel);
   }

   @Override
   public final void requestFocus() {
      this.pwdField_1.requestFocus();
   }

   @Override
   public void addCapsLockListener(JComponent var1) {
   }

   @Override
   public void removeCapsLockListener(JComponent var1) {
   }

   @Override
   public void dispose() {
      this.undoRedoController.dispose();
   }

   @Override
   public final void setIncludeReTypeField(boolean var1) {
      this.includeRetypeField = var1;
   }

   protected void layoutInputPanel(JPanel var1, String var2) {
      var1.add(new JLabel(PackageResources.Labels.getEnter(var2)));
      var1.add(this.pwdField_1.layoutWithoutLabel());
      if (this.includeRetypeField) {
         var1.add(new JLabel(PackageResources.Labels.getReEnter(var2)));
         var1.add(this.pwdField_2.layoutWithoutLabel());
      }

      var1.setBorder(Empty.border(0, 0, 10, 0));
   }

   private void createUndoRedoController() {
      TextUndoRedoControllerFactory var1 = UndoRedoControllers.textFactory();
      this.undoRedoController = var1.newController(this.inputPanel);
   }

   private boolean validatePasswords() {
      if (this.includeRetypeField) {
         String var1 = this.pwdField_1.getPassword();
         String var2 = this.pwdField_2.getPassword();
         return var1.equals(var2);
      } else {
         return true;
      }
   }

   private boolean checkEmptyPassword() {
      String var1 = this.pwdField_1.getPassword();
      String var2 = this.pwdField_2.getPassword();
      boolean var3 = true;
      if (var1.isEmpty() && var2.isEmpty()) {
         Answer var4 = Ask.yesNoOrCancel(this.inputPanel, PackageResources.Empty.MESSAGE, PackageResources.Empty.TITLE, Alert.Option.YES, Alert.Type.WARNING);
         var3 = var4.isYes();
      }

      return var3;
   }
}
