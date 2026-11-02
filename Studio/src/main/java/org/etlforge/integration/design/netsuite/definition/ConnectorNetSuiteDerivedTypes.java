package org.etlforge.integration.design.netsuite.definition;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class ConnectorNetSuiteDerivedTypes implements Serializable {
   private final XsdGlobalElementName baseType;
   private final Map<String, XsdGlobalElementName> derivedTypeRefs;

   public ConnectorNetSuiteDerivedTypes(String var1, NetSuiteDerivedTypes var2) {
      this.baseType = var2.getBaseTypeExists(var1);
      this.derivedTypeRefs = Maps.newHashMap();

      for (XsdGlobalElementName var5 : var2.getDeriveTypes(this.baseType)) {
         this.addDeriveTypeRef(var5);
      }
   }

   public XsdGlobalElementName getDerivedObjectExists(String var1) {
      XsdGlobalElementName var2 = this.derivedTypeRefs.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDefinitionError(var1 + " is not a derived type of {" + this.baseType.getNamespace() + "}" + this.baseType.getLocalName());
      } else {
         return var2;
      }
   }

   public boolean isBaseType(XsdGlobalElementName var1) {
      return this.baseType.equals(var1);
   }

   public XsdGlobalElementName getBaseType() {
      return this.baseType;
   }

   private void addDeriveTypeRef(XsdGlobalElementName var1) {
      this.derivedTypeRefs.put(var1.getLocalName(), var1);
   }

   public boolean containsKey(String var1) {
      return this.derivedTypeRefs.containsKey(var1);
   }

   public boolean isEmpty() {
      return this.derivedTypeRefs.isEmpty();
   }

   public Set<String> getKeySet() {
      return this.derivedTypeRefs.keySet();
   }

   public Map<String, String> getRecordTypes(Set<String> var1) {
      HashMap var2 = Maps.newHashMap();

      for (Entry var4 : this.derivedTypeRefs.entrySet()) {
         if (var1 == null || var1.contains(var4.getKey())) {
            var2.put(var4.getKey(), ((XsdGlobalElementName)var4.getValue()).getNamespace());
         }
      }

      return var2;
   }
}
