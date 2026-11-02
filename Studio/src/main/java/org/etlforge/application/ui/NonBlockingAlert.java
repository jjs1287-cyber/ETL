package org.etlforge.application.ui;

import java.awt.Component;
import java.awt.EventQueue;
import org.etlforge.ui.util.UiUtils;

public final class NonBlockingAlert {
   public static void info(String var0, String var1) {
      info(UiUtils.getActiveFrame(), var0, var1);
   }

   public static void info(final Component var0, final String var1, final String var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Alert.info(var0, var1, var2);
         }
      });
   }

   public static void warning(String var0, String var1) {
      warning(UiUtils.getActiveFrame(), var0, var1);
   }

   public static void warning(final Component var0, final String var1, final String var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Alert.warning(var0, var1, var2);
         }
      });
   }

   public static void error(String var0, String var1) {
      error(UiUtils.getActiveFrame(), var0, var1);
   }

   public static void error(final Component var0, final String var1, final String var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Alert.error(var0, var1, var2);
         }
      });
   }

   public static void message(final Component var0, final Object var1, final String var2, final Alert.Type var3) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Alert.message(var0, var1, var2, var3);
         }
      });
   }

   private NonBlockingAlert() {
   }
}
