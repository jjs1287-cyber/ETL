package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldSchema;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldsSchema;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersionFactory;
import org.etlforge.system.ArgCheck;

public class NetSuiteSearchRowPropertyies {
   private static final Set<String> none = Sets.newTreeSet();
   final NetSuiteWsdlVersion wsdlVersion;
   private Map<String, Set<String>> basicFields = Maps.newHashMap();
   private final ConnectorCustomFieldsSchema customFields;

   public NetSuiteSearchRowPropertyies(String var1, String var2) {
      this(NetSuiteWsdlVersionFactory.fromVersionedName(var1), var2);
   }

   public NetSuiteSearchRowPropertyies(NetSuiteWsdlVersion var1, String var2) {
      this.wsdlVersion = var1;
      this.basicFields = Maps.newHashMap();
      this.customFields = new ConnectorCustomFieldsSchema(var1, var2);
   }

   public boolean hasSearchRows() {
      return this.wsdlVersion == null ? false : !this.basicFields.isEmpty() || !this.customFields.isEmpty();
   }

   public ConnectorCustomFieldsSchema getConnectorCustomFieldsSchema() {
      return this.customFields;
   }

   public Set<String> getKeys() {
      TreeSet var1 = Sets.newTreeSet();
      var1.addAll(this.basicFields.keySet());
      var1.addAll(this.customFields.getCustomFields().keySet());
      return var1;
   }

   public boolean hasColumns(String var1) {
      return this.basicFields.containsKey(var1) ? true : this.customFields.hasColumns(var1);
   }

   public boolean hasCustomFields(String var1) {
      return this.customFields.hasColumns(var1);
   }

   public Set<String> getFields(String var1) {
      Set var2 = this.basicFields.get(var1);
      return var2 == null ? none : var2;
   }

   public List<ConnectorCustomFieldSchema> getCustomFields(String var1) {
      return this.customFields.getCustomFields(var1);
   }

   public void putField(String var1, String var2) {
      ArgCheck.checkNotEmpty(var1, "category");
      ArgCheck.checkNotEmpty(var2, "fieldName");
      Set var3 = this.basicFields.get(var1);
      if (var3 == null) {
         var3 = Sets.newTreeSet();
         this.basicFields.put(var1, var3);
      }

      var3.add(var2);
   }

   public void putCustomField(String var1, String var2, String var3, String var4) {
      boolean var5 = this.wsdlVersion.usingScriptIdForCustomField();
      ConnectorCustomFieldSchema var6 = new ConnectorCustomFieldSchema(var2, var5, var3, var4);
      this.putCustomField(var1, var6);
   }

   private void putCustomField(String var1, ConnectorCustomFieldSchema var2) {
      ArgCheck.checkNotEmpty(var1, "category");
      this.customFields.put(var1, var2);
   }
}
