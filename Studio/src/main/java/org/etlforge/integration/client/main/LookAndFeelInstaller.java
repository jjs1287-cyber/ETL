package org.etlforge.integration.client.main;

import java.awt.EventQueue;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;

final class LookAndFeelInstaller {
   private static boolean installed;

   public static void install() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LookAndFeelInstaller.installAppearanceOnEventThread();
         }
      });
   }

   private static void installAppearanceOnEventThread() {
      if (!installed) {
         LookAndFeelCustomizer.customize();
         installEtlForgeWindowColors();
         installed = true;
      }
   }

   private static native void applyEtlForgeWindowAppearance(java.awt.Window window);

   private static void installEtlForgeWindowColors() {
      if (!org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS) return;
      try {
         System.load(new java.io.File(System.getProperty("java.home"), "bin/jawt.dll").getAbsolutePath());
         System.load(new java.io.File("dist/bin/ETLForgeWindowTheme.dll").getAbsolutePath());
         java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(new java.awt.event.AWTEventListener() {
            @Override public void eventDispatched(java.awt.AWTEvent event) {
               if (event.getID() == java.awt.event.WindowEvent.WINDOW_OPENED) {
                  applyEtlForgeWindowAppearance(((java.awt.event.WindowEvent) event).getWindow());
               }
            }
         }, java.awt.AWTEvent.WINDOW_EVENT_MASK);
      } catch (UnsatisfiedLinkError ex) {
                                                                                
         System.err.println("Native window title colors are unavailable: " + ex.getMessage());
      }
   }

   private LookAndFeelInstaller() {
   }
}
