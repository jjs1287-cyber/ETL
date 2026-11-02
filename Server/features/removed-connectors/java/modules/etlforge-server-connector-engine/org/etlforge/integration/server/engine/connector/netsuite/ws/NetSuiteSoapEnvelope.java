package org.etlforge.integration.server.engine.connector.netsuite.ws;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;

public class NetSuiteSoapEnvelope {
   private static Map<String, String> prefixNamespaces = Maps.newTreeMap();
   private static final String envelopQName = getSoapQName("Envelope");
   private static final String headerQName = getSoapQName("Header");
   private static final String bodyQName = getSoapQName("Body");

   private static String getSoapQName(String var0) {
      return XmlQName.getQName("soapenv", var0);
   }

   private static void setStartEnvelope(StringBuilder var0, String var1) {
      XmlElementUtils.startElementTag(var0, envelopQName, null, prefixNamespaces);
      if (!Strings.isNullOrEmpty(var1)) {
         XmlElementUtils.startElementTag(var0, headerQName);
         var0.append(var1);
         XmlElementUtils.endElementTag(var0, headerQName);
      }

      XmlElementUtils.startElementTag(var0, bodyQName);
   }

   private static void setEndEnvelope(StringBuilder var0) {
      XmlElementUtils.endElementTag(var0, bodyQName);
      XmlElementUtils.endElementTag(var0, envelopQName);
   }

   public static String getSoapEnvelope(String var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      setStartEnvelope(var2, var0);
      var2.append(var1);
      setEndEnvelope(var2);
      return var2.toString();
   }

   static void getSoapEnvelopeFile(String var0, File var1, File var2) {
      XmlWriter var3 = new XmlWriter(var2);

      try {
         StringBuilder var4 = new StringBuilder();
         setStartEnvelope(var4, var0);
         var3.append(var4.toString());
         var3.appendFileUtf8(var1);
         var4 = new StringBuilder();
         setEndEnvelope(var4);
         var3.append(var4.toString());
      } catch (IOException var8) {
         throw new ConnectorServerRuntimeException(var8);
      } finally {
         var3.dispose();
      }
   }

   static {
      prefixNamespaces.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
      prefixNamespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
   }
}
