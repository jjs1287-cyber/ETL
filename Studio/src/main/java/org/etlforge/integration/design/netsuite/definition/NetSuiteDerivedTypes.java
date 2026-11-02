package org.etlforge.integration.design.netsuite.definition;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteDerivedTypes implements Serializable {
   private final Map<String, XsdGlobalElementName> baseTypes = Maps.newHashMap();
   private final Map<XsdGlobalElementName, Set<XsdGlobalElementName>> derivedTypes = Maps.newHashMap();

   public Set<XsdGlobalElementName> getDeriveTypes(String var1) {
      XsdGlobalElementName var2 = this.getBaseTypeExists(var1);
      return this.derivedTypes.get(var2);
   }

   public Set<XsdGlobalElementName> getDeriveTypes(XsdGlobalElementName var1) {
      return this.derivedTypes.get(var1);
   }

   private void putBaseTypeOrErrorOnDuplicate(XsdGlobalElementName var1) {
      XsdGlobalElementName var2 = this.baseTypes.get(var1);
      if (var2 != null) {
         if (var2 != var1) {
            throw new ConnectorNetSuiteDefinitionError("duplicate base types: " + var2 + " ;" + var1);
         }
      } else {
         this.baseTypes.put(var1.getLocalName(), var1);
      }
   }

   public void put(XsdGlobalElementName var1, Set<XsdGlobalElementName> var2) {
      this.putBaseTypeOrErrorOnDuplicate(var1);
      this.derivedTypes.put(var1, var2);
   }

   public void putDerivedType(XsdGlobalElementName var1, XsdGlobalElementName var2) {
      this.putBaseTypeOrErrorOnDuplicate(var1);
      Set var3 = this.getDeriveTypes(var1);
      if (var3 == null) {
         var3 = Sets.newHashSet();
         this.derivedTypes.put(var1, var3);
      }

      var3.add(var2);
   }

   public void remove(XsdGlobalElementName var1) {
      this.baseTypes.remove(var1.getLocalName());
      this.derivedTypes.remove(var1);
   }

   public boolean hasBaseType(String var1) {
      return this.baseTypes.containsKey(var1);
   }

   public XsdGlobalElementName getBaseTypeExists(String var1) {
      XsdGlobalElementName var2 = this.baseTypes.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDefinitionError("no base type with name " + var1);
      } else {
         return var2;
      }
   }
}
