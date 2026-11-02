package org.etlforge.integration.data.entity;

public final class ETLForgeConnectNames {
   public static final String ETLFORGE_CONNECT_WITHOUT_TM = "ETLForge Connect";
   public static final String ETLFORGE_CONNECT_WITH_MNEMONIC = "ETLForge &Connect";
   public static final String ETLFORGE_CONNECT = "ETLForge Connect\u2122";
   public static final String QUERIES_ROOT_NAME = EntityType.SalesforceQuery.getPlural();
   public static final String UPSERTS_ROOT_NAME = EntityType.SalesforceUpsert.getPlural();
   public static final String CREATES_ROOT_NAME = EntityType.SalesforceCreate.getPlural();
   public static final String UPDATES_ROOT_NAME = EntityType.SalesforceUpdate.getPlural();
   public static final String DELETES_ROOT_NAME = EntityType.SalesforceDelete.getPlural();
   public static final String BULK_LOADS_ROOT_NAME = EntityType.SalesforceBulkLoad.getPlural();
   private static final String SALESFORCE_SHARED_OBJECTS = "Salesforce Shared ";
   public static final String DATABASE_TO_DATABASE = "Database To Database";
   public static final String DATABASE_TO_TEXT = "Database To Text";
   public static final String DATABASE_TO_XML = "Database To XML";
   public static final String TEXT_TO_DATABASE = "Text To Database";
   public static final String XML_TO_DATABASE = "XML To Database";

   public static boolean isEtlforgeConnectFolder(Folder var0) {
      return var0.getName().equals("ETLForge Connect\u2122");
   }

   public static boolean isConnectWizardFolder(Folder var0) {
      IntegrationEntity var1 = var0.getParent();
      if (var1 instanceof Folder && isEtlforgeConnectFolder((Folder)var1)) {
         String var2 = var0.getName();
         return var2.equals("Database To Database")
            || var2.equals("Database To Text")
            || var2.equals("Database To XML")
            || var2.equals("Text To Database")
            || var2.equals("XML To Database")
            || isSalesforceWizardsFolder(var0);
      } else {
         return false;
      }
   }

   public static boolean isSalesforceWizardsFolder(Folder var0) {
      IntegrationEntity var1 = var0.getParent();
      if (var1 instanceof Folder && isEtlforgeConnectFolder((Folder)var1)) {
         String var2 = var0.getName();
         String var3 = getSharedSalesforceItemsFolderName(var0.getEntityType());
         return var2.equals(QUERIES_ROOT_NAME)
            || var2.equals(UPSERTS_ROOT_NAME)
            || var2.equals(CREATES_ROOT_NAME)
            || var2.equals(UPDATES_ROOT_NAME)
            || var2.equals(BULK_LOADS_ROOT_NAME)
            || var2.equals(DELETES_ROOT_NAME)
            || var2.equals(var3);
      } else {
         return false;
      }
   }

   public static boolean isSharedSalesforceItemsFolder(Folder var0) {
      IntegrationEntity var1 = var0.getParent();
      if (var1 instanceof Folder && isEtlforgeConnectFolder((Folder)var1)) {
         String var2 = var0.getName();
         EntityType var3 = var0.getItemType();
         return var2.equals(getSharedSalesforceItemsFolderName(var3));
      } else {
         return false;
      }
   }

   public static String getSharedSalesforceItemsFolderName(EntityType var0) {
      return "Salesforce Shared " + var0.getPlural();
   }

   private ETLForgeConnectNames() {
   }
}
