package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.util.Property;
import org.etlforge.xml.XmlStringBuilder;

public final class QueryBuilderRootModel extends AbstractQueryBuilderModel {
   public static final String COUNT = "count";
   private final Property<Integer> count = this.newProperty("count");

   public QueryBuilderRootModel(MSCrmObject var1) {
      super(var1);
   }

   @Override
   public String buildXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      var1.setProcessingInstruction(null);
      this.addFetchElement(var1);
      this.addEntityElement(var1);
      super.appendToXml(var1);
      var1.closeElement();
      var1.closeElement();
      return var1.getXml();
   }

   private void addFetchElement(XmlStringBuilder var1) {
      var1.addElement("fetch");
      var1.addAttribute("version", "1.0");
      var1.addAttribute("output-format", "xml-platform");
      var1.addAttribute("mapping", "logical");
      if (!this.count.isNull()) {
         var1.addAttribute("count", this.count.get().toString());
      }
   }

   private void addEntityElement(XmlStringBuilder var1) {
      var1.addElement("entity");
      var1.addAttribute("name", this.getObject().getName());
   }
}
