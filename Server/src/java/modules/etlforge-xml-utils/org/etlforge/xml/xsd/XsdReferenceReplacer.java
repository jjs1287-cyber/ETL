package org.etlforge.xml.xsd;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.xml.Elements;
import org.etlforge.xml.SimpleNamespaceContext;
import org.etlforge.xml.XmlFileWriter;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class XsdReferenceReplacer {
   private final Function<String, String> referenceGenerator;
   private final SimpleNamespaceContext ctx = new SimpleNamespaceContext();

   public XsdReferenceReplacer(Function<String, String> var1) {
      this.referenceGenerator = Preconditions.checkNotNull(var1);
      this.ctx.add("xs", "http://www.w3.org/2001/XMLSchema");
   }

   public void replaceReferences(File var1) throws Exception {
      Preconditions.checkNotNull(var1);
      Document var2 = DomParser.parseFile(var1, true);
      boolean var3 = this.processReferences(var2, "import");
      var3 |= this.processReferences(var2, "include");
      if (var3) {
         XmlFileWriter var4 = new XmlFileWriter();
         var4.write(var2, var1);
      }
   }

   private boolean processReferences(Document var1, String var2) throws Exception {
      boolean var3 = false;

      for (Element var5 : Elements.matching("/xs:schema/xs:" + var2, var1, this.ctx)) {
         String var6 = var5.getAttribute("schemaLocation");
         String var7 = this.referenceGenerator.apply(var6);
         if (!var6.equals(var7)) {
            var5.setAttribute("schemaLocation", var7);
            var3 = true;
         }
      }

      return var3;
   }
}
