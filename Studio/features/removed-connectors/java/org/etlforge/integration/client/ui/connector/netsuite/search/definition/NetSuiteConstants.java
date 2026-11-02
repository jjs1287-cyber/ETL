package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

public class NetSuiteConstants {
   public static final String xsiPrefix = "xsi";
   public static final String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
   public static final String xsiType = "xsi:type";
   public static final String criteriaElementName = "criteria";
   public static final String columnsElementName = "columns";
   public static final String savedSearchIdElementName = "savedSearchId";
   public static final String predefinedSearchValueElementName = "predefinedSearchValue";
   public static final String searchValueElementName = "searchValue";
   public static final String searchValue2ElementName = "searchValue2";
   public static final String internalIdElementName = "internalId";
   public static final String scriptIdElementName = "scriptId";
   public static final String externalIdElementName = "externalId";
   public static final String nameElementName = "name";
   public static final String customFieldElementName = "customField";
   public static final String customFieldListElementName = "customFieldList";
   private static final String typeElementName = "type";
   private static final String typeIdElementName = "typeId";

   public static final String getTypeElementName(boolean var0) {
      return var0 ? "typeId" : "type";
   }
}
