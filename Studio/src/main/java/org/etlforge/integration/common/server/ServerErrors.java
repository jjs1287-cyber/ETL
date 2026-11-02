package org.etlforge.integration.common.server;

import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Element;

public final class ServerErrors {
   public static final String CAUSE_STACKTRACE = "Cause stacktrace: ";
   private static final String SERVER_METHOD = "ServerMethod";
   private static final String CAUSE = "CausedBy";

   public static void appendStacktrace(Throwable var0, StringBuilder var1) {
      String var2 = ExceptionUtils.getStackTrace(var0).replace('\u0000', ' ');
      var1.append("\n\n").append("Cause stacktrace: ").append(var2);
   }

   public static String getServerMethodForErrorMessage(RemoteException var0) {
      if (var0 instanceof AxisFault) {
         AxisFault var1 = (AxisFault)var0;

         for (Element var5 : var1.getFaultDetails()) {
            if ("ServerMethod".equals(var5.getNodeName())) {
               return "\n\nThis error occurred when calling the server method \"" + KongaXmlUtils.getElementValue(var5) + "\".";
            }
         }
      }

      return "";
   }

   public static String getCausedByCascade(RemoteException var0) {
      StringBuilder var1 = new StringBuilder("\n\n");
      if (var0 instanceof AxisFault) {
         AxisFault var2 = (AxisFault)var0;

         for (Element var6 : var2.getFaultDetails()) {
            if ("CausedBy".equals(var6.getNodeName())) {
               var1.append(KongaXmlUtils.getElementValue(var6)).append("\n\n");
            }
         }
      }

      String var7 = var1.toString();
      return StringUtils.isNotBlank(var7) ? var7 : "";
   }

   public static AxisFault makeAxisFault(Exception var0, String var1) {
      AxisFault var2 = AxisFault.makeFault(var0);
      var2.addFaultDetail(new QName("ServerMethod"), var1);
      fillInCauses(var2, var0);
      return var2;
   }

   private static void fillInCauses(AxisFault var0, Exception var1) {
      byte var2 = 1;
      int var3 = 0;

      for (Throwable var4 = var1.getCause(); var4 != null && var3 < var2; var3++) {
         StringBuilder var5 = new StringBuilder("Casued by: ").append(var4);
         StackTraceElement[] var6 = var4.getStackTrace();
         int var7 = Math.min(var6.length, 10);

         for (int var8 = 0; var8 < var7; var8++) {
            var5.append("\n at ").append(var6[var8]);
         }

         var0.addFaultDetail(new QName("CausedBy"), var5.toString());
         var4 = var4.getCause();
      }
   }

   private ServerErrors() {
   }
}
