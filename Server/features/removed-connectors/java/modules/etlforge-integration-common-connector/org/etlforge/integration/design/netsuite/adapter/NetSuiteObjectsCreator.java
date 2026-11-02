package org.etlforge.integration.design.netsuite.adapter;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignRuntimeException;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionConnector;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteObjectsCreator {
   private final NetSuiteDefinitionConnector connectorDef;
   private final NetSuiteConfig config;

   public NetSuiteObjectsCreator(NetSuiteDefinitionConnector var1, NetSuiteConfig var2) {
      this.connectorDef = var1;
      this.config = var2;
   }

   public NetSuiteObjects getNSConnectorObjects(NetSuiteSelectedObject var1) {
      return var1.responseIsSearchRow() ? this.getSearchRowObjects(var1) : this.getRecordObjects(var1);
   }

   private NetSuiteObjects getSearchRowObjects(NetSuiteSelectedObject var1) {
      boolean var2 = !var1.isCustomObject();
      NetSuiteObjects var3 = new NetSuiteObjects(var2, var1);
      String var4 = this.getSearchObjectName(var1.getNetSuiteConnectorObject());
      NetSuiteObject var5 = this.getSearchRowType(var2, var4);
      var3.add(var5);
      return var3;
   }

   private NetSuiteObject getSearchRowType(boolean var1, String var2) {
      String var3 = getTypeSearchRowType(var1, var2);
      XsdGlobalElementName var4 = this.connectorDef.getSearchRowDerivedObjectExists(var3);
      return var1 ? new NetSuiteObject(var4) : new NetSuiteObject(var2, var4);
   }

   public static String getTypeSearchRowType(boolean var0, String var1) {
      String var2 = var0 ? var1 : "CustomRecord";
      return NetSuiteSearchObjectName.getSearchRowFromObjectName(var2);
   }

   public String getSearchObjectName(NetSuiteConnectorObject var1) {
      boolean var2 = !var1.isCustomObject();
      if (var2) {
         NetSuiteCustomSearch var3 = this.getNetSuiteCustomSearch(var1);
         return var3 != null ? var3.getCustomSearchObjectName() : var1.getName();
      } else {
         return "CustomRecord";
      }
   }

   private NetSuiteCustomSearch getNetSuiteCustomSearch(NetSuiteConnectorObject var1) {
      NetSuiteCustomSearch var2 = this.config.getNetSuiteCustomSearch(var1.getKey());
      if (var2 == null && var1.isCustomSearch()) {
         String var3 = var1.getKey() + " is invalid custom search object ";
         throw new ConnectorNetSuiteDesignRuntimeException(var3);
      } else {
         return var2;
      }
   }

   private NetSuiteObjects getRecordObjects(NetSuiteSelectedObject var1) {
      if (var1.isDelete()) {
         return new NetSuiteObjects(true, var1);
      }

      if (var1.isSearch()) {
         String var2 = var1.getNetSuiteConnectorObject().getKey();
         NetSuiteCustomSearch var3 = this.getNetSuiteCustomSearch(var1.getNetSuiteConnectorObject());
         if (var3 != null) {
            NetSuiteCustomSearchSubtype var4 = var3.get(var2);
            if (isGenericCustomSearch(var4)) {
               List var7 = var1.getSearchRequestObjects();
               List var6 = getResponseObjects(var7, var3);
               return this.newStandardNetSuiteConnectorObjects(var1, var6, true);
            }

            List var5 = var4.getSearchResponseObjectNames();
            return this.newStandardNetSuiteConnectorObjects(var1, var5);
         }
      }

      return var1.isCustomObject() ? this.newCustomNetSuiteConnectorObjects(var1) : this.newStandardNetSuiteConnectorObjects(var1);
   }

   private static boolean isGenericCustomSearch(NetSuiteCustomSearchSubtype var0) {
      return var0 == null;
   }

   private static List<String> getResponseObjects(List<String> var0, NetSuiteCustomSearch var1) {
      ArrayList var2 = Lists.newArrayList();

      for (String var4 : var0) {
         NetSuiteCustomSearchSubtype var5 = var1.get(var4);
         if (var5 == null) {
            throw new ConnectorNetSuiteDesignRuntimeException("No custom search def for" + var4);
         }

         var2.addAll(var5.getSearchResponseObjectNames());
      }

      return var2;
   }

   private NetSuiteObject getRecordType(boolean var1, String var2) {
      String var3 = var1 ? var2 : "CustomRecord";
      XsdGlobalElementName var4 = this.connectorDef.getRecordDerivedObjectExists(var3);
      return var1 ? new NetSuiteObject(var4) : new NetSuiteObject(var2, var4);
   }

   private NetSuiteObject getStandardRecordType(String var1) {
      return this.getRecordType(true, var1);
   }

   private NetSuiteObject getCustomRecordType(String var1) {
      return this.getRecordType(false, var1);
   }

   private NetSuiteObjects newStandardNetSuiteConnectorObjects(NetSuiteSelectedObject var1, List<String> var2) {
      return this.newStandardNetSuiteConnectorObjects(var1, var2, false);
   }

   private NetSuiteObjects newStandardNetSuiteConnectorObjects(NetSuiteSelectedObject var1, List<String> var2, boolean var3) {
      NetSuiteObjects var4 = new NetSuiteObjects(true, var1, var3);

      for (String var6 : var2) {
         var4.add(this.getStandardRecordType(var6));
      }

      return var4;
   }

   private NetSuiteObjects newStandardNetSuiteConnectorObjects(NetSuiteSelectedObject var1) {
      String var2 = var1.getNetSuiteConnectorObject().getName();
      return asNSConnectorObjects(var1, true, this.getStandardRecordType(var2));
   }

   private NetSuiteObjects newCustomNetSuiteConnectorObjects(NetSuiteSelectedObject var1) {
      String var2 = var1.getNetSuiteConnectorObject().getName();
      return asNSConnectorObjects(var1, false, this.getCustomRecordType(var2));
   }

   private static NetSuiteObjects asNSConnectorObjects(NetSuiteSelectedObject var0, boolean var1, NetSuiteObject var2) {
      NetSuiteObjects var3 = new NetSuiteObjects(var1, var0);
      var3.add(var2);
      return var3;
   }
}
