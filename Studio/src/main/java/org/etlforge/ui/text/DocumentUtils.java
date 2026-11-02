package org.etlforge.ui.text;

import com.google.common.base.Preconditions;
import java.text.BreakIterator;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public final class DocumentUtils {
   public static String getText(Document var0, Interval var1) {
      return getText(var0, var1.startsAt(), var1.length());
   }

   public static String getText(Document var0, int var1, int var2) {
      try {
         return var0.getText(var1, var2);
      } catch (BadLocationException var4) {
         return null;
      }
   }

   public static int getRowNumber(JTextComponent var0, int var1) {
      return getRowNumber(var0.getDocument(), var1);
   }

   public static int getRowNumber(Document var0, int var1) {
      Element var2 = var0.getDefaultRootElement();
      return var2.getElementIndex(var1) + 1;
   }

   public static int getColumnNumber(JTextComponent var0, int var1) {
      try {
         return var1 - Utilities.getRowStart(var0, var1) + 1;
      } catch (BadLocationException var3) {
         return -1;
      }
   }

   public static String getRowText(Document var0, int var1) {
      Interval var2 = rowAsInterval(var0, var1);
      return getText(var0, var2);
   }

   public static int getRowStart(JTextComponent var0, int var1) {
      return getRowStart(var0.getDocument(), var1);
   }

   public static int getRowStart(Document var0, int var1) {
      Element var2 = getElementForRow(var0, var1);
      return var2.getStartOffset();
   }

   public static int getRowEnd(Document var0, int var1) {
      Element var2 = getElementForRow(var0, var1);
      return var2.getEndOffset();
   }

   public static Interval rowAsInterval(Document var0, int var1) {
      Element var2 = getElementForRow(var0, var1);
      return IntervalFactory.fromStartAndEnd(var2.getStartOffset(), var2.getEndOffset());
   }

   public static int getNumberOfRows(Document var0) {
      return getRowNumber(var0, var0.getLength());
   }

   private static Element getElementForRow(Document var0, int var1) {
      Element var2 = var0.getDefaultRootElement();
      return var2.getElement(var1 - 1);
   }

   public static int getWordStart(Document var0, int var1) {
      Preconditions.checkArgument(var1 >= 0, "offset must be >= 0 (was " + var1 + ")");
      Preconditions.checkArgument(var1 <= var0.getLength(), "offset greater than length of Document");
      int var2 = getRowNumber(var0, var1);
      int var3 = getRowStart(var0, var2);
      if (var3 == var1) {
         return var1;
      }

      int var4 = var1 - var3;
      String var5 = getText(var0, var3, var4) + "X";
      BreakIterator var6 = BreakIterator.getWordInstance();
      var6.setText(var5);
      int var7 = var6.last();
      if (var7 == -1) {
         return var3;
      }

      if (var7 == var4 + 1) {
         var7 = var6.previous();
      }

      return var3 + var7;
   }

   private DocumentUtils() {
   }
}
