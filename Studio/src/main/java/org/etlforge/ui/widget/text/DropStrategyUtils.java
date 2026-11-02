package org.etlforge.ui.widget.text;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

final class DropStrategyUtils {
   private DropStrategyUtils() {
      throw new UnsupportedOperationException();
   }

   static void highlightAll(JTextComponent var0) {
      try {
         Highlighter var1 = var0.getHighlighter();
         var1.removeAllHighlights();
         var1.addHighlight(0, var0.getDocument().getLength(), new DefaultHighlightPainter(var0.getSelectionColor()));
      } catch (BadLocationException var2) {
         var2.printStackTrace();
      }
   }

   static boolean insertAtCursor(JTextComponent var0, String var1) {
      try {
         Document var2 = var0.getDocument();
         int var3 = var0.getCaretPosition();
         var2.insertString(var3, var1, null);
         return true;
      } catch (BadLocationException var4) {
         var4.printStackTrace();
         return false;
      }
   }
}
