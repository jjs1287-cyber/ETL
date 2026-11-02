package org.etlforge.application.ui.job;

import java.text.MessageFormat;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.ResourceBundleStringResources;
import org.etlforge.ui.StringResources;

public final class UiJobMessages {
   private static final StringResources strings = new ResourceBundleStringResources(
      "org.etlforge.application.ui.job.messages", MnemonicStringFactory.newCharMarkerFactory('&')
   );
   public static final String DONE = get("Done");
   public static final String WAITING = get("Waiting");
   public static final String CANCELLED = get("Cancelled");
   public static final String ERROR = get("Error");
   public static final String UPDATE_UI = get("UpdateUI");

   public static String get(String var0) {
      return strings.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private UiJobMessages() {
   }
}
