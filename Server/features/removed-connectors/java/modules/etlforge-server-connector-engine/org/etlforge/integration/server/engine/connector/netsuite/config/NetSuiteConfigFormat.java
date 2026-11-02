package org.etlforge.integration.server.engine.connector.netsuite.config;

import java.io.File;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.integration.server.netsuite.config.NetSuiteConfigSetting;

public class NetSuiteConfigFormat {
   static void formatToFile(NetSuiteConfigSetting var0, File var1) {
      ConnectorExecutionSystem.writeFileUTF8(var1, format(var0));
   }

   static String format(NetSuiteConfigSetting var0) {
      NetSuiteCustomFieldTypeFormat var1 = new NetSuiteCustomFieldTypeFormat(var0.getCustomFieldTypes());
      NetSuiteCustomFieldObjectFormat var2 = new NetSuiteCustomFieldObjectFormat(var0.getCustomFieldObjects());
      NetSuiteCustomSearchSubtypeFormat var3 = new NetSuiteCustomSearchSubtypeFormat(var0.getItemSearch());
      NetSuiteCustomSearchSubtypeFormat var4 = new NetSuiteCustomSearchSubtypeFormat(var0.getTransactionSearch());
      StringBuilder var5 = new StringBuilder(2000);
      var5.append(XmlUtils.xmlUtf8Header);
      XmlElementUtils.startElementTag(var5, "config");
      XmlElementUtils.startElementTag(var5, "cachesize");
      XmlElementUtils.elementValue(var5, "execution", "5");
      XmlElementUtils.elementValue(var5, "design", "3");
      XmlElementUtils.endElementTag(var5, "cachesize");
      XmlElementUtils.startElementTag(var5, "searchconfig");
      XmlElementUtils.startElementTag(var5, "customfieldtypes");
      var1.format(var5);
      XmlElementUtils.endElementTag(var5, "customfieldtypes");
      XmlElementUtils.startElementTag(var5, "customfieldobjects");
      var2.format(var5);
      XmlElementUtils.endElementTag(var5, "customfieldobjects");
      XmlElementUtils.startElementTag(var5, "itemsearch");
      var3.format(var5);
      XmlElementUtils.endElementTag(var5, "itemsearch");
      XmlElementUtils.startElementTag(var5, "transactionsearch");
      var4.format(var5);
      XmlElementUtils.endElementTag(var5, "transactionsearch");
      XmlElementUtils.endElementTag(var5, "searchconfig");
      XmlElementUtils.endElementTag(var5, "config");
      return var5.toString();
   }

   public static void main(String[] var0) {
   }
}
