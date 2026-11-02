package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.SortedMap;
import java.util.TreeMap;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteConstants;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.XmlUtils;

public class NetSuiteRecordRef {
   private String name;
   private String internalId;
   private String externalId;
   private String type;
   private boolean isNotFirst;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getInternalId() {
      return this.internalId;
   }

   public void setInternalId(String var1) {
      this.internalId = var1;
   }

   public String getExternalId() {
      return this.externalId;
   }

   public void setExternalId(String var1) {
      this.externalId = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   private SortedMap<String, String> toAttributes(boolean var1) {
      TreeMap var2 = Maps.newTreeMap();
      if (!Strings.isNullOrEmpty(this.internalId)) {
         var2.put("internalId", this.internalId);
      }

      if (!Strings.isNullOrEmpty(this.externalId)) {
         var2.put("externalId", this.externalId);
      }

      if (!Strings.isNullOrEmpty(this.type)) {
         var2.put(NetSuiteConstants.getTypeElementName(var1), this.type);
      }

      return var2;
   }

   public void toXml(StringBuilder var1, String var2, boolean var3) {
      XmlUtils.startElementTagAttributes(var1, var2, this.toAttributes(var3));
      if (!Strings.isNullOrEmpty(this.name)) {
         XmlUtils.ElementValue(var1, "name", this.name);
      }

      XmlUtils.endElementTag(var1, var2);
   }

   @Override
   public String toString() {
      return this.toString(false);
   }

   private String toString(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      this.isNotFirst = false;
      this.append(var2, "internalId", this.internalId);
      this.append(var2, "externalId", this.externalId);
      this.append(var2, NetSuiteConstants.getTypeElementName(var1), this.type);
      this.append(var2, "name", this.name);
      return var2.toString();
   }

   private void append(StringBuilder var1, String var2, String var3) {
      if (!Strings.isNullOrEmpty(var3)) {
         if (this.isNotFirst) {
            var1.append("; ");
         }

         var1.append(var2);
         var1.append('=');
         var1.append(var3);
         this.isNotFirst = true;
      }
   }
}
