package org.etlforge.ui.text;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.etlforge.util.Interval;

public final class TabInAction extends AbstractTabIndentAction {
   private boolean insert;

   public static TabInAction install(JTextComponent var0) {
      TabInAction var1 = new TabInAction(var0);
      var0.getActionMap().put("insert-tab", var1);
      return var1;
   }

   public TabInAction(JTextComponent var1) {
      super(var1);
   }

   @Override
   protected void checkSelection(Interval var1, int[] var2) {
      this.insert = this.checkIfInsert(var1, var2);
   }

   private boolean checkIfInsert(Interval var1, int[] var2) {
      if (var1 != null) {
         int var3 = var2[0];
         int var4 = var2[1];
         if (var4 > var3) {
            return true;
         }

         int var5 = DocumentUtils.getRowStart(this.getDocument(), var3);
         int var6 = DocumentUtils.getRowEnd(this.getDocument(), var3);
         return this.getSelectionStart() == var5 ? this.getSelectionEnd() >= var6 - 1 : this.getSelectionEnd() == var6;
      } else {
         return false;
      }
   }

   @Override
   protected void processRow(int var1) {
      if (this.insert) {
         this.insertTab(var1);
      } else {
         this.getTextPane().replaceSelection("\t");
      }
   }

   private void insertTab(int var1) {
      try {
         int var2 = DocumentUtils.getRowStart(this.getDocument(), var1);
         boolean var3 = this.selectionStartsAtCurrentRow(var2);
         this.getDocument().insertString(var2, "\t", null);
         if (var3) {
            this.getTextPane().setSelectionStart(var2);
         }
      } catch (BadLocationException var4) {
         var4.printStackTrace();
      }
   }

   private boolean selectionStartsAtCurrentRow(int var1) {
      assert this.getSelectionEnd() > this.getSelectionStart() : "This method should be called only if there is a selection";
      return this.getSelectionStart() == var1;
   }
}
