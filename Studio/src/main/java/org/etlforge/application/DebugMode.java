package org.etlforge.application;

import javax.swing.Action;

public final class DebugMode {
   public static final boolean IS_ENABLED = Boolean.getBoolean("DebugMode");
   public static final boolean LOG_KEYSTROKES = IS_ENABLED && Boolean.getBoolean("DebugMode.LogKeys");

   public static void configureDebugModeOnlyAction(Action var0) {
      var0.putValue("DebugMode", Boolean.TRUE);
   }

   public static boolean isDebugModeOnlyAction(Action var0) {
      return Boolean.TRUE.equals(var0.getValue("DebugMode"));
   }

   private DebugMode() {
   }
}
