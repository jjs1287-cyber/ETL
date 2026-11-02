package org.etlforge.xml.xsd;

import com.google.common.base.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SchemaDomBuilder {
   private final Document schema;
   private String xsPrefix = "xs";

   public SchemaDomBuilder(Document var1) {
      this.schema = Preconditions.checkNotNull(var1, "schema");
   }

   public void setSchemaPrefix(String var1) {
      this.xsPrefix = var1;
   }

   public Element optionalUnboundedElement(String var1, String var2, boolean var3) {
      Element var4 = this.schema.createElementNS("http://www.w3.org/2001/XMLSchema", this.qualifyName("element"));
      var4.setAttribute("minOccurs", "0");
      var4.setAttribute("maxOccurs", "unbounded");
      var4.setAttribute("name", var1);
      if (var3) {
         var4.setAttribute("nillable", "true");
      }

      if (var2 != null) {
         var4.setAttribute("type", var2);
      }

      return var4;
   }

   private String qualifyName(String var1) {
      return this.xsPrefix.isEmpty() ? var1 : this.xsPrefix + ":" + var1;
   }

   public Element requiredFixedAttribute(String var1, String var2, String var3) {
      Element var4 = this.schema.createElementNS("http://www.w3.org/2001/XMLSchema", this.qualifyName("attribute"));
      var4.setAttribute("name", var1);
      var4.setAttribute("type", var3);
      var4.setAttribute("fixed", "true");
      var4.setAttribute("use", "required");
      return var4;
   }

   public Element sequence() {
      return this.schema.createElementNS("http://www.w3.org/2001/XMLSchema", this.qualifyName("sequence"));
   }
}
