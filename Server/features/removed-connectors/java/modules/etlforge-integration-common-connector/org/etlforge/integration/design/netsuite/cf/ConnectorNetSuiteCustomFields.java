package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.exception.ConnectorRuntimeException;

public class ConnectorNetSuiteCustomFields {
   private final List<ConnectorNetSuiteCustomField> NONENSCF = Lists.newArrayList();
   private final Map<String, List<ConnectorNetSuiteCustomField>> customfields = Maps.newHashMap();
   private final String customObjectId;

   ConnectorNetSuiteCustomFields(String var1) {
      this.customObjectId = var1;
   }

   public String getCustomObjectId() {
      return this.customObjectId;
   }

   public Map<String, List<ConnectorNetSuiteCustomField>> getCustomFields() {
      return this.customfields;
   }

   public List<ConnectorNetSuiteCustomField> getCustomFields(String var1) {
      List var2 = this.customfields.get(var1);
      return var2 == null ? this.NONENSCF : var2;
   }

   void put(String var1, List<ConnectorNetSuiteCustomField> var2) {
      this.customfields.put(var1, var2);
   }

   public List<ConnectorNetSuiteCustomField> asList() {
      if (this.customfields.isEmpty()) {
         return this.NONENSCF;
      }

      if (this.customfields.size() == 1) {
         Iterator var1 = this.customfields.entrySet().iterator();
         if (var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            return (List<ConnectorNetSuiteCustomField>)var2.getValue();
         }
      }

      throw new ConnectorRuntimeException("fatal error: the custom field return too much data");
   }
}
