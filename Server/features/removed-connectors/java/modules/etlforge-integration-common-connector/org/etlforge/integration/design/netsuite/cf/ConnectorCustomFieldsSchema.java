package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.exception.ConnectorRuntimeException;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;

public class ConnectorCustomFieldsSchema {
   private final List<ConnectorCustomFieldSchema> NONENSCF = Lists.newArrayList();
   private final Map<String, List<ConnectorCustomFieldSchema>> customfields = Maps.newHashMap();
   private final String customObjectId;
   private final NetSuiteWsdlVersion wsdlVersion;

   public ConnectorCustomFieldsSchema(NetSuiteWsdlVersion var1, String var2) {
      this.wsdlVersion = var1;
      this.customObjectId = var2;
   }

   public NetSuiteWsdlVersion getNetSuiteWsdlVersion() {
      return this.wsdlVersion;
   }

   public String getCustomObjectId() {
      return this.customObjectId;
   }

   public boolean isEmpty() {
      return this.customfields.isEmpty();
   }

   public Map<String, List<ConnectorCustomFieldSchema>> getCustomFields() {
      return this.customfields;
   }

   public boolean hasColumns(String var1) {
      return this.customfields.containsKey(var1);
   }

   public List<ConnectorCustomFieldSchema> getCustomFields(String var1) {
      List var2 = this.customfields.get(var1);
      return var2 == null ? this.NONENSCF : var2;
   }

   public void put(String var1, ConnectorCustomFieldSchema var2) {
      List var3 = this.customfields.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.customfields.put(var1, var3);
      }

      var3.add(var2);
   }

   public List<ConnectorCustomFieldSchema> asList() {
      if (this.customfields.isEmpty()) {
         return this.NONENSCF;
      }

      if (this.customfields.size() == 1) {
         Iterator var1 = this.customfields.entrySet().iterator();
         if (var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            return (List<ConnectorCustomFieldSchema>)var2.getValue();
         }
      }

      throw new ConnectorRuntimeException("fatal error: the custom field return too much data");
   }
}
