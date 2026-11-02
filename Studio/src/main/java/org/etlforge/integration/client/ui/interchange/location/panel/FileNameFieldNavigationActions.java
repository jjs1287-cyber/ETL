package org.etlforge.integration.client.ui.interchange.location.panel;

import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;
import javax.swing.text.TextAction;

final class FileNameFieldNavigationActions {
   public static void install(JTextComponent var0) {
      var0.getActionMap().put("caret-next-word", new FileNameFieldNavigationActions.NextWordAction(false));
      var0.getActionMap().put("selection-next-word", new FileNameFieldNavigationActions.NextWordAction(true));
      var0.getActionMap().put("caret-previous-word", new FileNameFieldNavigationActions.PreviousWordAction(false));
      var0.getActionMap().put("selection-previous-word", new FileNameFieldNavigationActions.PreviousWordAction(true));
   }

   private static boolean isBreak(char var0) {
      return var0 == '.' || var0 == ' ' || var0 == '-' || var0 == '/' || var0 == '\\';
   }

   private FileNameFieldNavigationActions() {
   }

   private static class NextWordAction extends TextAction {
      private final boolean select;

      public NextWordAction(boolean var1) {
         super("caret-next-word");
         this.select = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JTextComponent var2 = this.getTextComponent(var1);
         if (var2 != null) {
            try {
               int var3 = this.getNextPosition(var2);
               if (this.select) {
                  var2.moveCaretPosition(var3);
               } else {
                  var2.setCaretPosition(var3);
               }
            } catch (BadLocationException var4) {
               var4.printStackTrace();
            }
         }
      }

      private int getNextPosition(JTextComponent var1) throws BadLocationException {
         Document var2 = var1.getDocument();
         int var3 = var1.getCaretPosition();
         int var4 = var2.getLength();
         if (var3 == var4) {
            return var3;
         }

         char var5 = var2.getText(var3, 1).charAt(0);
         if (FileNameFieldNavigationActions.isBreak(var5)) {
            return var3 + 1;
         }

         Segment var6 = new Segment();
         int var7 = var3 + 1;

         for (int var8 = nextChunk(var7, var4); var7 < var4; var8 = nextChunk(var7, var4)) {
            var2.getText(var7, var8 - var7, var6);
            int var9 = 0;

            for (char var10 = var6.first(); var10 != '\uffff'; var9++) {
               if (FileNameFieldNavigationActions.isBreak(var10)) {
                  return var7 + (var10 == 32 ? var9 + 1 : var9);
               }

               var10 = var6.next();
            }

            var7 = var8 + 1;
         }

         return var4;
      }

      private static int nextChunk(int var0, int var1) {
         return Math.min(var0 + 100, var1);
      }
   }

   private static class PreviousWordAction extends TextAction {
      private final boolean select;

      public PreviousWordAction(boolean var1) {
         super("caret-previous-word");
         this.select = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JTextComponent var2 = this.getTextComponent(var1);
         if (var2 != null) {
            try {
               int var3 = this.getPreviousPosition(var2);
               if (this.select) {
                  var2.moveCaretPosition(var3);
               } else {
                  var2.setCaretPosition(var3);
               }
            } catch (BadLocationException var4) {
               var4.printStackTrace();
            }
         }
      }

      private int getPreviousPosition(JTextComponent var1) throws BadLocationException {
         Document var2 = var1.getDocument();
         int var3 = var1.getCaretPosition();
         if (var3 == 0) {
            return var3;
         }

         char var4 = var2.getText(var3 - 1, 1).charAt(0);
         if (FileNameFieldNavigationActions.isBreak(var4) && var4 != ' ') {
            return var3 - 1;
         }

         Segment var5 = new Segment();
         int var6 = var3 - 1;

         for (int var7 = previousChunk(var6); var6 > 0; var7 = previousChunk(var6)) {
            var2.getText(var7, var6 - var7, var5);
            int var8 = 0;

            for (char var9 = var5.last(); var9 != '\uffff'; var8++) {
               if (FileNameFieldNavigationActions.isBreak(var9)) {
                  return var5.getEndIndex() - var8;
               }

               var9 = var5.previous();
            }

            var6 = var7 - 1;
         }

         return 0;
      }

      private static int previousChunk(int var0) {
         return Math.max(0, var0 - 100);
      }
   }
}
