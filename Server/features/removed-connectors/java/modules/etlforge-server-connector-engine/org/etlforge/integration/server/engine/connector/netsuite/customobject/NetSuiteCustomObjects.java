package org.etlforge.integration.server.engine.connector.netsuite.customobject;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.util.XmlElementNameSanitizer;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteCustomizationRef;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteGetCustomizationId;

public class NetSuiteCustomObjects {
   private static final char prefix = 'o';
   private static final int XMLNAMEMAXLENGTH = 100;
   private static final int XMLNAMEWITHIDMAXLENGTH = 70;
   final Map<String, NetSuiteCustomizationRef> customObjects = Maps.newHashMap();
   static final String customRecordType = "customRecordType";

   public Map<String, String> getCustomObjectList() {
      HashMap var1 = Maps.newHashMap();

      for (Entry var3 : this.customObjects.entrySet()) {
         String var4 = NetSuiteConnectorObjectCategory.toCustomObjectKey((String)var3.getKey());
         var1.put(var4, ((NetSuiteCustomizationRef)var3.getValue()).getInternalId());
      }

      return var1;
   }

   private static String getSanitizedNameWithId(String var0, String var1) {
      if (var0.length() >= 70) {
         var0 = var0.substring(0, 70);
      }

      var0 = var0 + "_" + var1;
      return XmlElementNameSanitizer.getSanitizedName(var0);
   }

   private NetSuiteCustomObjects(List<NetSuiteCustomizationRef> var1) {
      HashSet var2 = Sets.newHashSet();

      for (NetSuiteCustomizationRef var4 : var1) {
         String var5 = XmlElementNameSanitizer.getSanitizedName(var4.getName(), 'o', 100);
         if (this.customObjects.containsKey(var5)) {
            if (!var2.contains(var5)) {
               var2.add(var5);
            }

            String var6 = getSanitizedNameWithId(var5, var4.getInternalId());
            this.customObjects.put(var6, var4);
         } else {
            this.customObjects.put(var5, var4);
         }
      }

      for (Object __etlItem61 : var2) {
         String var8 = (String) __etlItem61;
         NetSuiteCustomizationRef var9 = this.customObjects.remove(var8);
         assert var9 != null;
         String var10 = getSanitizedNameWithId(var8, var9.getInternalId());
         this.customObjects.put(var10, var9);
      }
   }

   NetSuiteCustomizationRef getNetSuiteCustomizationRefExists(String var1) {
      NetSuiteCustomizationRef var2 = this.customObjects.get(var1);
      if (var2 == null) {
         String var3 = "Can not locate the custom object" + var1 + " Maybe it is changed";
         throw new ConnectorServerRuntimeException(var3);
      } else {
         return var2;
      }
   }

   public static NetSuiteCustomObjects retrieve(NetSuitePassport var0) {
      NetSuiteGetCustomizationId var1 = new NetSuiteGetCustomizationId(var0);
      List var2 = var1.getCustomizationId("customRecordType");
      return new NetSuiteCustomObjects(var2);
   }
}
