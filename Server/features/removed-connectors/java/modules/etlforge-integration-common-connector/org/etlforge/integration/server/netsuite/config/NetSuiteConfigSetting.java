package org.etlforge.integration.server.netsuite.config;

import java.util.List;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldType;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomFieldObject;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;

public class NetSuiteConfigSetting {
   private final int executionCacheSize;
   private final int designCacheSize;
   private final List<NetSuiteCustomFieldType> customFieldTypes;
   private final List<NetSuiteCustomFieldObject> customFieldObjects;
   private final List<NetSuiteCustomSearchSubtype> itemSearch;
   private final List<NetSuiteCustomSearchSubtype> transactionSearch;
   private static final String configError = "Check NetSuite config file: ";

   public NetSuiteConfigSetting(
      int var1,
      int var2,
      List<NetSuiteCustomFieldType> var3,
      List<NetSuiteCustomFieldObject> var4,
      List<NetSuiteCustomSearchSubtype> var5,
      List<NetSuiteCustomSearchSubtype> var6
   ) {
      this.executionCacheSize = var1;
      this.designCacheSize = var2;
      this.customFieldTypes = var3;
      this.customFieldObjects = var4;
      this.itemSearch = var5;
      this.transactionSearch = var6;
   }

   public int getExecutionCacheSize() {
      return this.executionCacheSize;
   }

   public int getDesignCacheSize() {
      return this.designCacheSize;
   }

   public List<NetSuiteCustomFieldType> getCustomFieldTypes() {
      return this.customFieldTypes;
   }

   public List<NetSuiteCustomFieldObject> getCustomFieldObjects() {
      return this.customFieldObjects;
   }

   public List<NetSuiteCustomSearchSubtype> getItemSearch() {
      return this.itemSearch;
   }

   public List<NetSuiteCustomSearchSubtype> getTransactionSearch() {
      return this.transactionSearch;
   }

   public String getMissingErrorMessage() {
      if (this.customFieldTypes.isEmpty()) {
         return "Check NetSuite config file: No Custom Field Type Info!";
      } else if (this.customFieldObjects.isEmpty()) {
         return "Check NetSuite config file: No Custom Field Object Info!";
      } else if (this.itemSearch.isEmpty()) {
         return "Check NetSuite config file: No Item Search Info!";
      } else {
         return this.transactionSearch.isEmpty() ? "Check NetSuite config file: No Transaction Search Info!" : null;
      }
   }
}
