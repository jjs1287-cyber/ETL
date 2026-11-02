package org.etlforge.integration.client.ui.jitterpack;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;

final class Strings {
   static String get(String var0) {
      return JitterpackResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   static String getJitterPackString(String var0) {
      return format(var0, "Backup");
   }

   static String getJitterPackInPluralString(String var0) {
      return format(var0, "Jitterpaks");
   }

   static String getEncryptionLabel(PackMode var0) {
      return var0 == PackMode.IMPORT ? get("Security.Import.Encryption") : get("Security.Export.Encryption");
   }

   private Strings() {
   }
}

