package org.etlforge.ui.text;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public final class ToggleCommentAction extends AbstractAction {
   private static final String COMMENT = "//";
   private static final int COMMENT_LENGTH = "//".length();
   private final JTextComponent tc;

   public ToggleCommentAction(JTextComponent var1) {
      super("Toggle Comment");
      this.tc = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      int[] var2 = this.getAffectedRows();
      if (this.isAtLeastOneRowUncommented(var2)) {
         this.comment(var2);
      } else {
         this.uncomment(var2);
      }
   }

   private int[] getAffectedRows() {
      int var1 = this.tc.getSelectionStart();
      int var2 = this.tc.getSelectionEnd();
      if (var1 == var2) {
         return this.getRowsForNoSelection(var1);
      }

      int var3 = DocumentUtils.getRowNumber(this.tc, var1);
      int var4 = DocumentUtils.getRowNumber(this.tc, var2);
      if (var4 > var3 && DocumentUtils.getRowStart(this.tc, var4) == var2) {
         var4--;
      }

      return new int[]{var3, var4};
   }

   private int[] getRowsForNoSelection(int var1) {
      int var2 = DocumentUtils.getRowNumber(this.tc, var1);
      return new int[]{var2, var2};
   }

   private boolean isAtLeastOneRowUncommented(int[] var1) {
      for (int var2 = var1[0]; var2 <= var1[1]; var2++) {
         Element var3 = this.tc.getDocument().getDefaultRootElement().getElement(var2 - 1);
         if (!this.isCommented(var3)) {
            return true;
         }
      }

      return false;
   }

   private boolean isCommented(Element var1) {
      int var2 = var1.getStartOffset();
      int var3 = var1.getEndOffset();
      return var3 - var2 < COMMENT_LENGTH ? false : "//".equals(DocumentUtils.getText(this.tc.getDocument(), var2, COMMENT_LENGTH));
   }

   private void comment(int[] var1) {
      for (int var2 = var1[0]; var2 <= var1[1]; var2++) {
         this.commentOutRow(var2);
      }
   }

   private void commentOutRow(int var1) {
      int var2 = DocumentUtils.getRowStart(this.tc.getDocument(), var1);
      boolean var3 = this.selectionStartsAtCurrentRow(var2);

      try {
         this.tc.getDocument().insertString(var2, "//", null);
         if (var3) {
            this.tc.setSelectionStart(var2);
         }
      } catch (BadLocationException var5) {
         var5.printStackTrace();
      }
   }

   private boolean selectionStartsAtCurrentRow(int var1) {
      return this.tc.getSelectionStart() == var1 && this.tc.getSelectionEnd() > this.tc.getSelectionStart();
   }

   private void uncomment(int[] var1) {
      for (int var2 = var1[0]; var2 <= var1[1]; var2++) {
         int var3 = DocumentUtils.getRowStart(this.tc.getDocument(), var2);

         try {
            this.tc.getDocument().remove(var3, COMMENT_LENGTH);
         } catch (BadLocationException var5) {
            var5.printStackTrace();
         }
      }
   }
}
