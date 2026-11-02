package org.etlforge.ui.text;

import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;

public final class TabOutAction extends AbstractTabIndentAction {
   public static final KeyStroke TRIGGER = KeyUtils.getShiftDown(9);
   private int tabSize;

   public static void install(JTextComponent var0, int var1) {
      TabOutAction var2 = new TabOutAction(var0, var1);
      ActionKeyBinding.install(var2, var0);
   }

   public TabOutAction(JTextComponent var1, int var2) {
      super(var1);
      this.tabSize = var2;
      this.putValue("AcceleratorKey", TRIGGER);
   }

   public void setTabSize(int var1) {
      this.tabSize = var1;
   }

   @Override
   protected void processRow(int var1) {
      this.moveRowOut(var1);
   }

   private void moveRowOut(int var1) {
      Document var2 = this.getDocument();
      String var3 = DocumentUtils.getRowText(var2, var1);
      int var4 = this.firstNonWhiteSpace(var3);
      if (var4 != 0) {
         int var5 = DocumentUtils.getRowStart(var2, var1);
         int var6 = this.startOfRangeToRemove(var3, var4);

         try {
            var2.remove(var5 + var6, var4 - var6);
         } catch (BadLocationException var8) {
            var8.printStackTrace();
         }
      }
   }

   private int firstNonWhiteSpace(String var1) {
      int var2 = 0;

      for (int var3 = var1.length(); var2 < var3; var2++) {
         if (!Character.isWhitespace(var1.charAt(var2))) {
            return var2;
         }
      }

      return var1.length();
   }

   private int startOfRangeToRemove(String var1, int var2) {
      assert var2 > 0;
      int var3 = var2 - 1;
      int var4 = 0;

      while (var3 >= 0) {
         char var5 = var1.charAt(var3);
         if (var5 == '\t') {
            if (var4 > 0) {
               var3++;
            }
            break;
         }

         if (++var4 == this.tabSize) {
            break;
         }

         var3--;
      }

      return Math.max(var3, 0);
   }
}
