package org.etlforge.integration.server.implementation.webservice;

import java.text.MessageFormat;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.ResourceBundleStringResources;
import org.etlforge.ui.StringResources;

public final class WebServiceMessages {
   private static final StringResources strings = new ResourceBundleStringResources(
      "org.etlforge.integration.server.implementation.webservice.Messages", MnemonicStringFactory.newCharMarkerFactory('&')
   );

   private WebServiceMessages() {
      throw new UnsupportedOperationException();
   }

   public static String getMessage(String var0) {
      return strings.getString(var0);
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getMessage(var0), var1);
   }
}
