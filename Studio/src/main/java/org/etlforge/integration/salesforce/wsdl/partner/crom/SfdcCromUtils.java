package org.etlforge.integration.salesforce.wsdl.partner.crom;

import com.google.common.base.Preconditions;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Xml;
import org.etlforge.integration.structure.crom.CromType;

public final class SfdcCromUtils {
   public static CROM createElement(String var0, CROM var1, boolean var2, boolean var3) {
      return createElement(var0, var1, var2, var3, -1);
   }

   private static CROM createElement(String var0, CROM var1, boolean var2, boolean var3, int var4) {
      CROM var5;
      if (var4 == -1) {
         var5 = new CROM(CromType.CROM_t_Xml, var1);
      } else {
         var5 = new CROM(CromType.CROM_t_Xml, null);
         var1.insertChild(var4, var5);
      }

      var5.setName(var0);
      var5.setType(1L);
      CROM_Xml var6 = new CROM_Xml();
      var6.setNSQualify(true);
      var6.setNamespace("http://www.etlforge.invalid/XsdFromWsdl");
      if (var2) {
         var6.setMinUse(0);
      }

      if (var3) {
         var5.addType(8L);
         var6.setMaxUse(-1);
      }

      var5.setExtension(var6);
      return var5;
   }

   public static CROM createSoapHeaderRoot(CROM var0) {
      Preconditions.checkNotNull(var0);
      CROM var1 = createElement("header", var0, true, false, 0);
      CromUtils.makeOptional(var1);
      return var1;
   }

   private SfdcCromUtils() {
   }
}
