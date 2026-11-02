package org.etlforge.ui.text.contentassist;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

final class Utils {
   static String getText(final Document var0, final int var1, final int var2) {
      if (EventQueue.isDispatchThread()) {
         return getTextImpl(var0, var1, var2);
      }

      final StringBuilder var3 = new StringBuilder();

      try {
         EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               var3.append(Utils.getTextImpl(var0, var1, var2));
            }
         });
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         var5.printStackTrace();
      } catch (InvocationTargetException var6) {
         var6.printStackTrace();
      }

      return var3.toString();
   }

   static int getCaretPosition(final JTextComponent var0) {
      if (EventQueue.isDispatchThread()) {
         return var0.getCaretPosition();
      }

      final int[] var1 = new int[1];

      try {
         EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               var1[0] = var0.getCaretPosition();
            }
         });
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var3.printStackTrace();
         var1[0] = -1;
      } catch (InvocationTargetException var4) {
         var4.printStackTrace();
         var1[0] = -1;
      }

      return var1[0];
   }

   private static String getTextImpl(Document var0, int var1, int var2) {
      try {
         return var0.getText(var1, var2);
      } catch (BadLocationException var4) {
         var4.printStackTrace();
         return "";
      }
   }

   private Utils() {
   }
}
