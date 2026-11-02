package org.etlforge.integration.structure.mapping;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;

public final class CromToMappingStructureWriter {
   private final CROM root;
   private final CROMHeader header;
   private Map<String, CROM> elements;
   private Map<String, CROM> elementTypes;

   public CromToMappingStructureWriter(CROM var1, CROMHeader var2) {
      this.root = Preconditions.checkNotNull(var1);
      this.header = Preconditions.checkNotNull(var2);
   }

   public void setElementMap(Map<String, CROM> var1) {
      this.elements = var1;
   }

   public void setTypeMap(Map<String, CROM> var1) {
      this.elementTypes = var1;
   }

   public void writeXml(Writer var1) throws IOException {
      StringIndexMap var2 = new StringIndexMap();
      this.writePreamble(var1);
      this.writeElementMap(var1, var2);
      this.writeTypeMap(var1, var2);
      this.root.writeXml(var1, var2);
      var2.writeXml(var1);
      this.writeEnd(var1);
   }

   public void writeToConsole() {
      try {
         PrintWriter var1 = new PrintWriter(System.out, true);
         this.writeXml(var1);
         var1.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   private void writePreamble(Writer var1) throws IOException {
      StringBuilder var2 = new StringBuilder("<?xml version=\"1.0\" ?>\n");
      var2.append('<').append("JTR");
      JtrXmlFormatUtils.addAttribute(var2, "Type", this.header.getTypeString());
      JtrXmlFormatUtils.addAttribute(var2, "Version", this.header.getVersion(), 0);
      JtrXmlFormatUtils.addAttribute(var2, "BeginQuote", this.header.getBeginQuote());
      JtrXmlFormatUtils.addAttribute(var2, "EndQuote", this.header.getEndQuote());
      var2.append(">\n");
      var1.write(var2.toString());
   }

   private void writeElementMap(Writer var1, StringIndexMap var2) throws IOException {
      if (this.elements != null && !this.elements.isEmpty()) {
         var1.write("<" + "Elements" + ">\n");

         for (CROM var4 : this.elements.values()) {
            var4.writeXml(var1, var2);
         }

         var1.write("</" + "Elements" + ">\n");
      }
   }

   private void writeTypeMap(Writer var1, StringIndexMap var2) throws IOException {
      if (this.elementTypes != null && !this.elementTypes.isEmpty()) {
         var1.write("<" + "ElementTypes" + ">\n");

         for (Entry var4 : this.elementTypes.entrySet()) {
            StringBuilder var5 = new StringBuilder();
            var5.append('<').append("ElementType");
            String var6 = (String)var4.getKey();
            JtrXmlFormatUtils.addAttribute(var5, "Name", var2.store(var6));
            var5.append(">\n");
            var1.write(var5.toString());
            ((CROM)var4.getValue()).writeXml(var1, var2);
            var1.write("</" + "ElementType" + ">\n");
         }

         var1.write("</" + "ElementTypes" + ">\n");
      }
   }

   private void writeEnd(Writer var1) throws IOException {
      var1.write("</" + "JTR" + ">\n");
   }
}
