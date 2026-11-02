package org.etlforge.integration.design.netsuite.definition;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteDefinitionConnector implements Serializable {
   private ConnectorNetSuiteDerivedTypes recordDeriveType = null;
   private ConnectorNetSuiteDerivedTypes searchRowDeriveType = null;
   private ConnectorNetSuiteDerivedTypes recordRefDeriveType = null;
   private Set<String> searchRecordTypeNamePattern = null;

   public void setRecordDeriveType(ConnectorNetSuiteDerivedTypes var1) {
      this.recordDeriveType = var1;
   }

   public ConnectorNetSuiteDerivedTypes getRecordDeriveType() {
      return this.recordDeriveType;
   }

   public void setSearchRowDeriveType(ConnectorNetSuiteDerivedTypes var1) {
      this.searchRowDeriveType = var1;
   }

   public void setRecordRefDeriveType(ConnectorNetSuiteDerivedTypes var1) {
      this.recordRefDeriveType = var1;
   }

   public ConnectorNetSuiteDerivedTypes getRecordRefDeriveType() {
      return this.recordRefDeriveType;
   }

   public boolean isRecordRefBaseType(XsdGlobalElementName var1) {
      return this.recordRefDeriveType == null ? false : this.recordRefDeriveType.isBaseType(var1);
   }

   private Set<XsdGlobalElementName> getRecordRefDerivedTypes() {
      if (this.recordRefDeriveType == null) {
         return null;
      }

      HashSet var1 = Sets.newHashSet();

      for (String var3 : this.recordRefDeriveType.getKeySet()) {
         XsdGlobalElementName var4 = this.recordRefDeriveType.getDerivedObjectExists(var3);
         var1.add(var4);
      }

      return var1;
   }

   public void setSearchRecordTypeNamePattern(Set<String> var1) {
      this.searchRecordTypeNamePattern = var1;
   }

   public boolean isReplacedBaseType(boolean var1, XsdGlobalElementName var2) {
      return var1 ? this.isSearchRowBaseType(var2) : this.isRecordBaseType(var2);
   }

   public boolean isRecordBaseType(XsdGlobalElementName var1) {
      return this.recordDeriveType.isBaseType(var1);
   }

   public boolean isSearchRowBaseType(XsdGlobalElementName var1) {
      return this.searchRowDeriveType.isBaseType(var1);
   }

   public XsdGlobalElementName getSearchRowDerivedObjectExists(String var1) {
      return this.searchRowDeriveType.getDerivedObjectExists(var1);
   }

   public XsdGlobalElementName getRecordDerivedObjectExists(String var1) {
      return this.recordDeriveType.getDerivedObjectExists(var1);
   }

   public boolean isStardardObject(boolean var1, String var2, NetSuiteConfig var3) {
      if (var1) {
         return this.searchRecordTypeNamePattern.contains(var2) ? true : var3.isCustomSearchObject(var2);
      } else {
         return this.recordDeriveType.containsKey(var2);
      }
   }

   public Map<String, String> getRecordTypes(boolean var1) {
      return this.recordDeriveType.getRecordTypes(var1 ? this.searchRecordTypeNamePattern : null);
   }

   public Set<String> getSearchRecordTypeNamePattern() {
      return this.searchRecordTypeNamePattern;
   }
}
