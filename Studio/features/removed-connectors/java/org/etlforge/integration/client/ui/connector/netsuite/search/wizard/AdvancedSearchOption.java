package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import java.util.Set;

public class AdvancedSearchOption {
   final String selectedObject;
   final AdvancedSearchOption.OptionType option;
   Set<String> subTypes = null;

   AdvancedSearchOption(String var1, AdvancedSearchOption.OptionType var2) {
      this.selectedObject = var1;
      this.option = var2;
   }

   void setSubtypes(Set<String> var1) {
      this.subTypes = var1;
   }

   public enum OptionType {
      SearchBasicOnly,
      CustomObjectSearch,
      All;
   }
}
