package org.etlforge.application.ui.text.editor;

import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextField;

public final class GotoLineNumberAction extends AbstractAction {
   private final JTextComponent tc;

   public GotoLineNumberAction(JTextComponent var1) {
      super("Go To Line");
      this.tc = var1;
   }

   public void installKeyboardShortcut(KeyStroke var1) {
      String var2 = "gotoLine";
      this.tc.getInputMap().put(var1, var2);
      this.tc.getActionMap().put(var2, this);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      int var2 = new GotoLineNumberAction.Prompt(SwingUtilities.getWindowAncestor(this.tc), this.getLineCount()).getLineNumber();
      if (var2 > 0 && var2 <= this.getLineCount()) {
         this.gotoLine(var2);
      }
   }

   private void gotoLine(int var1) {
      this.tc.setCaretPosition(DocumentUtils.getRowStart(this.tc, var1));
   }

   private int getLineCount() {
      return this.tc.getDocument().getDefaultRootElement().getElementCount();
   }

   private static class Prompt {
      private final KongaDialog dlg;
      private final int lineCount;
      private final JLabel messageLabel;
      private final JTextField field;

      public Prompt(Window var1, int var2) {
         this.dlg = new KongaDialog(var1, "Go to Line");
         this.lineCount = var2;
         this.messageLabel = new JLabel(" ");
         this.field = new KongaTextField();
      }

      public int getLineNumber() {
         this.layoutdialog();
         this.addFieldListener();
         this.checkInputValue();
         this.dlg.setResizable(false);
         this.dlg.manageLocation(GotoLineNumberAction.class);
         this.dlg.setOkButtonAsDefault();
         this.dlg.setVisible(true);
         return this.dlg.wasOkPressed() ? this.getLineNumberFromField() : -1;
      }

      private void addFieldListener() {
         this.field.getDocument().addDocumentListener(new DocumentContentChangeListener() {
            @Override
            public void contentChanged(DocumentEvent var1) {
               Prompt.this.checkInputValue();
            }
         });
      }

      private Integer getLineNumberFromField() throws NumberFormatException {
         String var1 = this.field.getText();
         return var1.isEmpty() ? null : Integer.parseInt(var1);
      }

      private void checkInputValue() {
         String var1 = null;
         boolean var2 = false;

         try {
            Integer var3 = this.getLineNumberFromField();
            if (var3 == null) {
               var1 = " ";
               var2 = false;
            } else if (var3 >= 1 && var3 <= this.lineCount) {
               var1 = " ";
               var2 = true;
            } else {
               var1 = "Line number out of range";
               var2 = false;
            }
         } catch (NumberFormatException var4) {
            var1 = "Not a number";
            var2 = false;
         }

         this.messageLabel.setText(var1);
         this.dlg.getOKButton().setEnabled(var2);
      }

      private void layoutdialog() {
         JLabel var1 = new JLabel("Enter line number (1.." + this.lineCount + "):");
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3).north(var1).center(this.createInputArea());
         this.dlg.standardLayout(var2.container(), KongaDialog.OK_CANCEL);
      }

      private JComponent createInputArea() {
         return new BorderLayoutBuilder().north(this.field).south(this.messageLabel).container();
      }
   }
}
