package org.etlforge.integration.server.engine.connector.netsuite.config;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;
import org.etlforge.integration.design.util.XmlElementUtils;

public class NetSuiteCustomSearchSubtypeFormat {
   private final List<NetSuiteCustomSearchSubtype> types;

   NetSuiteCustomSearchSubtypeFormat(List<NetSuiteCustomSearchSubtype> var1) {
      this.types = var1;
   }

   void format(StringBuilder var1) {
      Collections.sort(this.types);

      for (NetSuiteCustomSearchSubtype var3 : this.types) {
         format(var1, var3);
      }
   }

   private static void format(StringBuilder var0, NetSuiteCustomSearchSubtype var1) {
      XmlElementUtils.startElementTag(var0, "customsearch");
      XmlElementUtils.elementValue(var0, "searchobjectkey", var1.getSearchObject());
      XmlElementUtils.elementValue(var0, "searchsubtype", var1.getSearchNetSuiteSubType());

      for (String var3 : var1.getSearchResponseObjectNames()) {
         XmlElementUtils.elementValue(var0, "searchreponseobject", var3);
      }

      XmlElementUtils.endElementTag(var0, "customsearch");
   }
}
