package org.etlforge.integration.design.netsuite.definition;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteDerivedTypeSearchRowHandler extends NetSuiteDerivedTypeSimpleHandler {
   private static final String basic = "basic";
   private boolean isReplaced = false;
   private String searchObjectName = null;

   public static boolean isObjectElementName(String var0) {
      return Strings.isNullOrEmpty(var0) ? false : "basic".equalsIgnoreCase(var0);
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.isObjectType(var4)) {
         Map var5 = this.convertBasic(var4);
         XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var5, null);
         this.isReplaced = true;
      } else {
         super.startElement(var1, var2, var3, var4);
      }
   }

   @Override
   public void setObjectName(String var1) {
      this.isReplaced = false;
      this.searchObjectName = NetSuiteSearchObjectName.getObjectNameFromSearchRow(var1);
   }

   private boolean isObjectType(Attributes var1) {
      if (this.isReplaced) {
         return false;
      }

      String var2 = var1.getValue("name");
      return isObjectElementName(var2);
   }

   private Map<String, String> convertBasic(Attributes var1) {
      HashMap var2 = Maps.newHashMap();

      for (int var3 = 0; var3 < var1.getLength(); var3++) {
         String var4 = var1.getQName(var3);
         if (var4.equals("name")) {
            var2.put(var4, this.searchObjectName);
         } else {
            var2.put(var4, var1.getValue(var3));
         }
      }

      return var2;
   }
}
