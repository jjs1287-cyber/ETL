package org.etlforge.integration.design.util;

import javax.xml.transform.TransformerException;
import org.etlforge.xml.Elements;
import org.etlforge.xml.SimpleNamespaceContext;
import org.etlforge.xml.XmlPrettyPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SchemaRenameParser {
   private final SimpleNamespaceContext ctx = new SimpleNamespaceContext();
   private final SchemaRenameStrategy rename;
   private final Document dom;
   private boolean changed;

   public SchemaRenameParser(SchemaRenameStrategy var1, Document var2) {
      this.rename = var1;
      this.dom = var2;
      this.ctx.add("xs", "http://www.w3.org/2001/XMLSchema");
   }

   public boolean process() throws Exception {
      this.processReferences("import");
      this.processReferences("include");
      return this.changed;
   }

   private void processReferences(String var1) throws Exception {
      for (Element var3 : Elements.matching("//xs:schema/xs:" + var1, this.dom, this.ctx)) {
         String var4 = var3.getAttribute("schemaLocation");
         this.rename.checkLocation(var1, var4);
         String var5 = this.rename.newName(var4);
         var3.setAttribute("schemaLocation", var5);
         this.changed = true;
      }
   }

   public String domToString() throws TransformerException {
      XmlPrettyPrinter var1 = new XmlPrettyPrinter(0);
      return var1.prettyPrintToString(this.dom);
   }
}
