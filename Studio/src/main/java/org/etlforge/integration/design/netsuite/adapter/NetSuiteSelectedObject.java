package org.etlforge.integration.design.netsuite.adapter;

import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedPropertiesParser;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchRowPropertyies;

public class NetSuiteSelectedObject {
   final EntityType operationEntity;
   final NetSuiteConnectorObject connectorObject;
   final NetSuiteSearchAdvancedProperties advancedProperties;

   public NetSuiteSelectedObject(EntityType var1) {
      this.operationEntity = var1;
      this.connectorObject = null;
      this.advancedProperties = null;
   }

   public NetSuiteSelectedObject(EntityType var1, String var2, String var3) {
      this.operationEntity = var1;
      this.connectorObject = new NetSuiteConnectorObject(var2);
      this.advancedProperties = !this.isSearch() ? null : NetSuiteSearchAdvancedPropertiesParser.fromXml(var3);
   }

   public NetSuiteSelectedObject(EntityType var1, NetSuiteConnectorObject var2, NetSuiteSearchAdvancedProperties var3) {
      this.operationEntity = var1;
      this.connectorObject = var2;
      this.advancedProperties = var3;
   }

   public boolean isCustomObject() {
      return this.connectorObject.isCustomObject();
   }

   public boolean isCustomSearch() {
      return this.connectorObject.isCustomSearch();
   }

   public boolean responseIsSearchRow() {
      if (!this.isSearch()) {
         return false;
      }

      if (this.advancedProperties == null) {
         return false;
      }

      NetSuiteSearchCategory var1 = this.advancedProperties.getSearchType();
      return var1.isAdvancedSearch() || var1.isSavedSearch();
   }

   public boolean hasSearchRows() {
      return !this.responseIsSearchRow() ? false : this.advancedProperties.hasSearchRows();
   }

   public NetSuiteSearchRowPropertyies getNetSuiteSearchRowPropertyies() {
      return this.advancedProperties.getNetSuiteSearchRowPropertyies();
   }

   NetSuiteConnectorObject getNetSuiteConnectorObject() {
      return this.connectorObject;
   }

   public List<String> getSearchRequestObjects() {
      return this.advancedProperties.getCustomSearchObjects();
   }

   public boolean isSearch() {
      return this.operationEntity == EntityType.NetSuiteQuery;
   }

   public boolean isDelete() {
      return this.operationEntity == EntityType.NetSuiteDelete;
   }

   public EntityType getOperationEntity() {
      return this.operationEntity;
   }

   public String getNetSuiteOperation() {
      return getNetSuiteOperation(this.operationEntity);
   }

   private static String getNetSuiteOperation(EntityType var0) {
      switch (var0) {
         case NetSuiteCreate:
            return "addList";
         case NetSuiteUpdate:
            return "updateList";
         case NetSuiteUpsert:
            return "upsertList";
         case NetSuiteDelete:
            return "deleteList";
         case NetSuiteQuery:
            return "search";
         default:
            return "undefinedNetSuiteOperation";
      }
   }

   @Override
   public String toString() {
      if (this.connectorObject == null) {
         return this.operationEntity.name();
      } else {
         return this.isSearch()
            ? this.operationEntity.name() + this.connectorObject + this.advancedProperties
            : this.operationEntity.name() + this.connectorObject;
      }
   }
}
