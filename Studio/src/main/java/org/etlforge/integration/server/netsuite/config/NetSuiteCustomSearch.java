package org.etlforge.integration.server.netsuite.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObjectCategory;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;

public class NetSuiteCustomSearch {
   private static final String itemNetSuiteSearchObject = NetSuiteCustomSearchDefinition.ITEM;
   private static final String itemSearchObjectPrefix = NetSuiteConnectorObjectCategory.ItemSearchObject.getPrefix();
   private static final String tranNetSuiteSearchObject = NetSuiteCustomSearchDefinition.TRANSACTION;
   private static final String tranSearchObjectPrefix = NetSuiteConnectorObjectCategory.TranSearchObject.getPrefix();
   private final String searchNetSuiteObject;
   private final String prefix;
   private final NetSuiteCustomSearchSubtype subtype;

   private NetSuiteCustomSearch(String var1, String var2, NetSuiteCustomSearchSubtype var3) {
      this.prefix = var2;
      this.searchNetSuiteObject = var1;
      this.subtype = var3;
   }

   public String getSearchNetSuiteObject() {
      return this.searchNetSuiteObject;
   }

   public String getSearchObject() {
      return this.prefix + this.subtype.getSearchObject();
   }

   public String getSearchNetSuiteSubtype() {
      return this.subtype.getSearchNetSuiteSubType();
   }

   public List<String> getSearchResponseObjectNames() {
      return this.subtype.getSearchResponseObjectNames();
   }

   public static List<NetSuiteCustomSearch> getConfigs(NetSuiteConfigSetting var0) {
      ArrayList var1 = Lists.newArrayList();
      add(var1, itemNetSuiteSearchObject, itemSearchObjectPrefix, var0.getItemSearch());
      add(var1, tranNetSuiteSearchObject, tranSearchObjectPrefix, var0.getTransactionSearch());
      return var1;
   }

   private static void add(List<NetSuiteCustomSearch> var0, String var1, String var2, List<NetSuiteCustomSearchSubtype> var3) {
      NetSuiteCustomSearchSubtype var4 = new NetSuiteCustomSearchSubtype(var1);
      var0.add(new NetSuiteCustomSearch(var1, var2, var4));

      for (NetSuiteCustomSearchSubtype var6 : var3) {
         var0.add(new NetSuiteCustomSearch(var1, var2, var6));
      }
   }
}
