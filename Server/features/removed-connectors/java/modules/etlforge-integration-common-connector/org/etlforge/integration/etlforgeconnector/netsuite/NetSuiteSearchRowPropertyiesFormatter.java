package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.collect.Maps;
import java.util.TreeMap;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldSchema;
import org.etlforge.integration.design.util.XmlElementUtils;

public class NetSuiteSearchRowPropertyiesFormatter {
   private final NetSuiteSearchRowPropertyies searchRows;
   private final StringBuilder sb;

   public NetSuiteSearchRowPropertyiesFormatter(NetSuiteSearchRowPropertyies var1) {
      this.searchRows = var1;
      this.sb = new StringBuilder();
   }

   public String toXml() {
      this.toXmlImpl();
      return this.sb.toString();
   }

   private void toXmlImpl() {
      if (this.searchRows != null) {
         if (this.searchRows.wsdlVersion != null) {
            if (this.searchRows.hasSearchRows()) {
               XmlElementUtils.startElementTag(this.sb, "searchrows");
               XmlElementUtils.elementValue(this.sb, "wsdlversion", this.searchRows.wsdlVersion.toWsdlVersion());

               for (String var3 : this.searchRows.getKeys()) {
                  XmlElementUtils.startElementTag(this.sb, "searchrow");
                  XmlElementUtils.elementValue(this.sb, "name", var3);
                  this.appendNetSuiteSearchRow(var3, this.searchRows);
                  XmlElementUtils.endElementTag(this.sb, "searchrow");
               }

               XmlElementUtils.endElementTag(this.sb, "searchrows");
            }
         }
      }
   }

   private void appendNetSuiteSearchRow(String var1, NetSuiteSearchRowPropertyies var2) {
      for (String var4 : var2.getFields(var1)) {
         XmlElementUtils.elementValue(this.sb, "field", var4);
      }

      for (ConnectorCustomFieldSchema var5 : var2.getCustomFields(var1)) {
         TreeMap var6 = Maps.newTreeMap();
         var6.put("netsuiteid", var5.getNetsuiteId());
         var6.put("netsuitetype", var5.getNetsuiteType());
         XmlElementUtils.startElementTagAttributeMap(this.sb, "customfield", var6, null);
         XmlElementUtils.appendXmlValue(this.sb, var5.getCustomFieldXmlName());
         XmlElementUtils.endElementTag(this.sb, "customfield");
      }
   }
}
