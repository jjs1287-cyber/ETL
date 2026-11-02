package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public class AdvancedSearchPropertyModel extends AbstractPropertyBasedModel {
   static final String SELECTEDOBJECT = "selectedObject";
   static final String searchOptionKey = "searchOption";
   static final String searchCategoryKey = "searchCategory";
   static final String subtypesKey = "subtypes";
   static final String selectedSubtypesKey = "selectedSubtypes";
   private final Property<String> selectedObject = this.newProperty("selectedObject");
   private final Property<AdvancedSearchOption.OptionType> searchOption = this.newProperty("searchOption");
   private final Property<NetSuiteSearchCategory> searchCategory = this.newProperty("searchCategory");
   private final ListProperty<NetSuiteCustomSearchObject> subtypes = this.newListProperty("subtypes");
   private final ListProperty<NetSuiteCustomSearchObject> selectedSubtypes = this.newListProperty("selectedSubtypes");
   private Set<String> subtypeSet = null;

   void setSearchOption(String var1, AdvancedSearchOption.OptionType var2) {
      this.selectedObject.set(var1);
      this.searchOption.set(var2);
   }

   public NetSuiteSearchCategory getNetSuiteSearchCategory() {
      return this.searchCategory.get();
   }

   boolean isSavedSearchOption() {
      return this.searchCategory.get() == NetSuiteSearchCategory.NetSuiteSearchAdvancedSavedSearch;
   }

   public boolean hasSubtypes() {
      return this.subtypes.get() != null && !this.subtypes.get().isEmpty();
   }

   public boolean isValueValid() {
      if (this.isSavedSearchOption()) {
         return true;
      }

      if (!this.hasSubtypes()) {
         return true;
      }

      List var1 = this.selectedSubtypes.get();
      return var1 != null && !var1.isEmpty();
   }

   NetSuiteSearchAdvancedProperties asNetSuiteSearchAdvancedProperties() {
      NetSuiteSearchAdvancedProperties var1 = new NetSuiteSearchAdvancedProperties();
      var1.setNetSuiteSearchType(this.getNetSuiteSearchCategory());
      List var2 = this.getSelectedTypes();
      if (var2 != null && !var2.isEmpty()) {
         for (Object __etlItem61 : var2) {
            NetSuiteCustomSearchObject var4 = (NetSuiteCustomSearchObject) __etlItem61;
            var1.addCustomSearchObject(var4.getKey());
         }
      }

      return var1;
   }

   public List<NetSuiteCustomSearchObject> getSelectedTypes() {
      return this.selectedSubtypes.get();
   }

   void serSearchCategory(NetSuiteSearchCategory var1) {
      this.searchCategory.set(var1);
   }

   void setSubtypes(Set<String> var1) {
      if (this.subtypeSet != var1) {
         this.subtypeSet = var1;
         this.resetSubtypes();
      }
   }

   private void resetSubtypes() {
      ArrayList var1 = Lists.newArrayList();
      if (this.subtypeSet != null) {
         for (String var3 : this.subtypeSet) {
            var1.add(new NetSuiteCustomSearchObject(var3));
         }
      }

      this.subtypes.set(var1);
   }
}
