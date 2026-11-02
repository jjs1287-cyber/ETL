package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.integration.debug.client.de.DataElementUtils;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataElement;

final class DataElementBuilder {
   public static DataElement build(WsDataElement var0) {
      DataElement[] var1 = null;
      String var2 = var0.getName();
      String var3 = ClientServerCommunicationUtils.decodeString(var2);
      String var4 = null;
      if (var0.isIsArray()) {
         WsDataElement[] var5 = var0.getArrValue();
         if (var5 == null) {
            var1 = new DataElement[0];
         } else {
            int var6 = var5.length;
            var1 = new DataElement[var6];

            for (int var7 = 0; var7 < var6; var7++) {
               var1[var7] = build(var5[var7]);
            }
         }

         var4 = DataElementUtils.getArrayValueString(var1);
      } else {
         String var9 = var0.getValue();
         var4 = ClientServerCommunicationUtils.decodeString(var9);
      }

      String var10 = var0.getType();
      DataElementScope var11 = var0.getScope() != null ? DataElementScope.valueOf(var0.getScope().toUpperCase()) : DataElementScope.GLOBAL;
      DataElement var12 = new DataElement(var3, var11, var10, var1);
      var12.setValue(var4);
      return var12;
   }

   private DataElementBuilder() {
   }
}
