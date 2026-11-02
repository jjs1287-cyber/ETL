package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Strings;
import com.sforce.soap.partner.fault.ApiFault;
import com.sforce.ws.SoapFaultException;
import org.etlforge.integration.salesforce.SalesforceException;

public final class ExceptionTranslator {
   public static SalesforceException translate(Exception var0) {
      if (var0 instanceof SoapFaultException) {
         SoapFaultException var4 = (SoapFaultException)var0;
         String var3;
         String var6;
         if (var0 instanceof ApiFault) {
            var6 = ((ApiFault)var0).getExceptionMessage();
            var3 = ((ApiFault)var0).getExceptionCode().name();
         } else {
            var6 = var4.getMessage();
            if (var6 == null) {
               var6 = "";
            }

            var3 = var4.getFaultCode().getLocalPart();
         }

         return new SalesforceException(var6, var4, var3);
      } else {
         Throwable var1 = var0.getCause();
         if (var1 == null) {
            return new SalesforceException(var0);
         }

         String var2 = var0.getMessage();
         if (Strings.isNullOrEmpty(var2)) {
            var2 = var1.getMessage();
         } else {
            if (!var2.endsWith(".")) {
               var2 = var2 + ".";
            }

            var2 = var2 + "\n" + var1.getMessage();
         }

         return new SalesforceException(var2, var1);
      }
   }

   private ExceptionTranslator() {
   }
}
