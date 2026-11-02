package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.List;

public class NetSuiteSearchAdvancedProperties {
   private NetSuiteSearchCategory searchType;
   private final List<String> customSearchSubtypes;
   private NetSuiteSearchRowPropertyies searchRows;

   public NetSuiteSearchAdvancedProperties() {
      this.setSearchType(NetSuiteSearchCategory.NetSuiteSearchBasic);
      this.customSearchSubtypes = Lists.newArrayList();
      this.searchRows = null;
   }

   public NetSuiteSearchRowPropertyies getNetSuiteSearchRowPropertyies() {
      return this.searchRows;
   }

   public boolean hasSearchRows() {
      return this.searchRows == null ? false : this.searchRows.hasSearchRows();
   }

   public void setNetSuiteSearchRowPropertyies(NetSuiteSearchRowPropertyies var1) {
      this.searchRows = var1;
   }

   public boolean hasNoValue() {
      return !this.searchType.isSearchBasic() ? false : this.customSearchSubtypes.isEmpty();
   }

   public void addCustomSearchObject(String var1) {
      this.customSearchSubtypes.add(var1);
   }

   public boolean hasCustomSearchObjects() {
      return !this.customSearchSubtypes.isEmpty();
   }

   public List<String> getCustomSearchObjects() {
      return this.customSearchSubtypes;
   }

   public void setNetSuiteSearchType(NetSuiteSearchCategory var1) {
      this.setSearchType(var1);
   }

   public NetSuiteSearchCategory getSearchType() {
      return this.searchType;
   }

   public void setSearchType(NetSuiteSearchCategory var1) {
      this.searchType = var1;
   }

   public String toXml() {
      NetSuiteSearchAdvancedPropertiesFormatter var1 = new NetSuiteSearchAdvancedPropertiesFormatter(this);
      return var1.toXml();
   }

   @Override
   public String toString() {
      return this.searchType.name() + this.customSearchSubtypes;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.searchType, this.customSearchSubtypes, this.searchRows);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 instanceof NetSuiteSearchAdvancedProperties) {
         NetSuiteSearchAdvancedProperties var2 = (NetSuiteSearchAdvancedProperties)var1;
         if (this.searchType == var2.searchType) {
            return this.customSearchSubtypes.equals(var2.customSearchSubtypes);
         }
      }

      return false;
   }
}
