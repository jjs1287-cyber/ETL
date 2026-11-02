package org.etlforge.integration.etlforgeconnector.netsuite;

import org.apache.commons.lang.StringUtils;

public class NetSuiteSearchObjectName {
   public static final String SEARCHBASIC = "SearchBasic";
   public static final String SEARCHADVANCED = "SearchAdvanced";
   public static final String SEARCHROW = "SearchRow";
   public static final String SEARCHROWBASIC = "SearchRowBasic";

   public static String getSearchBasicFromObjectName(String var0) {
      return var0 + "SearchBasic";
   }

   public static String getSearchRowBasicFromObjectName(String var0) {
      return var0 + "SearchRowBasic";
   }

   public static String getSearchRowFromObjectName(String var0) {
      return var0 + "SearchRow";
   }

   public static String getSearchAdvancedFromObjectName(String var0) {
      return var0 + "SearchAdvanced";
   }

   public static String getObjectNameFromSearchAdvanced(String var0) {
      return StringUtils.removeEnd(var0, "SearchAdvanced");
   }

   public static String getObjectNameFromSearchBasic(String var0) {
      return StringUtils.removeEnd(var0, "SearchBasic");
   }

   public static boolean isSupportedSearchObject(String var0) {
      return var0.endsWith("SearchBasic");
   }

   public static String getObjectNameFromSearchRowBasic(String var0) {
      return StringUtils.removeEnd(var0, "SearchRowBasic");
   }

   public static String getObjectNameFromSearchRow(String var0) {
      return StringUtils.removeEnd(var0, "SearchRow");
   }

   public static boolean isSearchRow(String var0) {
      return var0.endsWith("SearchRow");
   }

   public static boolean isSearchRowBasic(String var0) {
      return var0.endsWith("SearchRowBasic");
   }

   public static boolean isSearchBasic(String var0) {
      return var0.endsWith("SearchBasic");
   }

   public static boolean isSearchAdvanced(String var0) {
      return var0.endsWith("SearchAdvanced");
   }
}
