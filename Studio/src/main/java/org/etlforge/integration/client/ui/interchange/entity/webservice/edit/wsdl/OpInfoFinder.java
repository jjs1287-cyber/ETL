package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

final class OpInfoFinder {
   private static final String[] PROPERTIES = new String[]{"Service", "Port", "Binding", "Operation"};

   public static WebServiceOperationInfo findMatchingOpInfo(WebServiceCall var0, WebServiceInfo var1) {
      WebServiceOperationInfo var2 = var0.getOperationInfo();
      Set var3 = extractValuesToCompare(var2);

      for (WebServiceOperationInfo var5 : var1.getOperations(true)) {
         if (var3.equals(extractValuesToCompare(var5))) {
            return var5;
         }
      }

      return null;
   }

   private static Set<String> extractValuesToCompare(WebServiceOperationInfo var0) {
      HashSet var1 = new HashSet();

      for (String var5 : PROPERTIES) {
         var1.add(var0.getProperty(var5));
      }

      return var1;
   }

   private OpInfoFinder() {
   }
}
