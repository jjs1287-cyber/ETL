package org.etlforge.integration.structure.mapping;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_Tree;

public final class CromBasedMappingTreeStructure implements MappingTreeStructure {
   private final CROM root;
   private final CROMHeader header;
   private Map<String, CROM> elements;
   private Map<String, CROM> types;

   public CromBasedMappingTreeStructure(CROM var1, CROMHeader var2) {
      this.root = Preconditions.checkNotNull(var1, "root");
      this.header = Preconditions.checkNotNull(var2, "header");
   }

   public CROM_Tree toCromTree() {
      CROM_Tree var1 = new CROM_Tree(this.root);
      if (this.types != null) {
         var1.addElementTypes(this.types);
      }

      if (this.elements != null) {
         var1.addElements(this.elements);
      }

      String var2 = this.header.getBeginQuote();
      String var3 = this.header.getEndQuote();
      var1.setBeginQuote(var2);
      var1.setEndQuote(var3);
      return var1;
   }

   @Override
   public CROM getRoot() {
      return this.root;
   }

   @Override
   public Reader getReader() throws IOException {
      return new StringReader(this.getString());
   }

   @Override
   public String getString() {
      try {
         CromToMappingStructureWriter var1 = this.createStructureWriter();
         StringWriter var2 = new StringWriter();
         var1.writeXml(var2);
         return var2.toString();
      } catch (IOException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private CromToMappingStructureWriter createStructureWriter() {
      CromToMappingStructureWriter var1 = new CromToMappingStructureWriter(this.root, this.header);
      if (this.types != null) {
         var1.setTypeMap(this.types);
      }

      if (this.elements != null) {
         var1.setElementMap(this.elements);
      }

      return var1;
   }

   @Override
   public boolean outOfDate() {
      return !this.header.getTypeString().equals("DB") ? false : this.header.getVersion() < 2;
   }

   @Override
   public CROMHeader getHeader() {
      return this.header;
   }

   public final void setElementMap(Map<String, CROM> var1) {
      this.elements = var1;
   }

   public final void setTypeMap(Map<String, CROM> var1) {
      this.types = var1;
   }

   @Override
   public String toString() {
      String var1;
      if (this.root.hasChildren()) {
         var1 = this.root.getChild(0).getDisplayName();
      } else {
         var1 = "[No root. This is an error.]";
      }

      return "Root: " + var1 + " [type = " + this.header.getTypeString() + "]";
   }
}
