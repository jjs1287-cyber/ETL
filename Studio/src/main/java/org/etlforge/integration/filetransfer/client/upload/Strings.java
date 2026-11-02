package org.etlforge.integration.filetransfer.client.upload;

import java.text.MessageFormat;
import org.etlforge.integration.filetransfer.client.resources.FileTransferResources;

final class Strings {
   static String get(String var0) {
      return FileTransferResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
