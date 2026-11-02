package org.etlforge.integration.server.engine.connector.util;

import java.io.IOException;
import java.nio.charset.Charset;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.xml.KongaXmlUtilsHtml;

public class XmlUtils {
   public static String xmlUtf8Header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   public static Charset xmlUtf8Encoding = Charset.forName("UTF-8");

   public static String removeXmlHeader(String var0) {
      if (var0.startsWith("<?xml")) {
         int var1 = var0.indexOf("?>");
         if (var1 > 0) {
            return var0.substring(var1 + 2);
         } else {
            throw new ConnectorServerRuntimeException(var0);
         }
      } else {
         return var0;
      }
   }

   public static boolean isHtml(String var0) {
      return KongaXmlUtilsHtml.isHtml(var0);
   }

   public static boolean isHtmlFile(String var0) throws IOException {
      return KongaXmlUtilsHtml.isHtmlFile(var0, xmlUtf8Encoding);
   }
}
