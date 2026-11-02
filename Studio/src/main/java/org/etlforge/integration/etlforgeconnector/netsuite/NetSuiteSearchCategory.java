package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.base.Strings;

public enum NetSuiteSearchCategory {
   NetSuiteSearch,
   NetSuiteSearchBasic,
   NetSuiteSearchAdvanced,
   NetSuiteSearchAdvancedSavedSearch;

   static NetSuiteSearchCategory fromName(String var0) {
      return Strings.isNullOrEmpty(var0) ? NetSuiteSearchBasic : valueOf(var0);
   }

   public boolean isSavedSearch() {
      return this == NetSuiteSearchAdvancedSavedSearch;
   }

   public boolean isAdvancedSearch() {
      return this == NetSuiteSearchAdvanced;
   }

   public boolean isSearchBasic() {
      return this == NetSuiteSearchBasic;
   }
}
