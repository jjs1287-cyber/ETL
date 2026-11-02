package org.etlforge.integration.structure.crom;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import org.etlforge.integration.structure.mapping.CromToMappingStructureWriter;
import org.etlforge.integration.structure.mapping.MappingStructureReadingHandler;
import org.etlforge.integration.structure.mapping.MappingStructureToCromConverter;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public final class CROM_Tree {
   private CROM root;
   private final Map<String, CROM> elementTypes = Maps.newHashMap();
   private final Map<String, CROM> elements = Maps.newHashMap();
   private String crom_type_name = "Unknown";
   private String begin_quote = "";
   private String end_quote = "";

   public static CROM_Tree fromMappingStructure(MappingTreeStructure var0) throws CromTreeException {
      CROM var1 = var0.getRoot();
      if (var1 == null) {
         CROM_Tree var2 = new CROM_Tree();
         var2.parseImpl(var0);
         return var2;
      } else {
         return new CROM_Tree(var1);
      }
   }

   public static CROM_Tree parse(MappingTreeStructure var0) throws CromTreeException {
      CROM_Tree var1 = new CROM_Tree();
      var1.parseImpl(var0);
      return var1;
   }

   public CROM_Tree() {
   }

   public CROM_Tree(CROM var1) {
      this.root = var1;
   }

   public String getBeginQuote() {
      return this.begin_quote;
   }

   public void setBeginQuote(String var1) {
      this.begin_quote = var1;
   }

   public String getEndQuote() {
      return this.end_quote;
   }

   public void setEndQuote(String var1) {
      this.end_quote = var1;
   }

   private void parseImpl(MappingTreeStructure var1) throws CromTreeException {
      MappingStructureToCromConverter var2 = new MappingStructureToCromConverter();
      var2.convert(var1, new MappingStructureReadingHandler() {
         @Override
         public void addElement(CROM var1) {
            CROM_Tree.this.addElement(var1);
         }

         @Override
         public void addType(String var1, CROM var2x) {
            CROM_Tree.this.addElementType(var1, var2x);
         }

         @Override
         public void setRoot(CROM var1) {
            CROM_Tree.this.root = var1;
         }

         @Override
         public void checkDerivedTypeHolder(CROM var1) {
            var1.checkDerivedTypeHolder(CROM_Tree.this.elementTypes);
         }

         @Override
         public void setCromType(String var1) {
            CROM_Tree.this.crom_type_name = var1;
         }

         @Override
         public String getCromType() {
            return CROM_Tree.this.crom_type_name;
         }

         @Override
         public void setDBQuotes(String var1, String var2x) {
            CROM_Tree.this.begin_quote = var1;
            CROM_Tree.this.end_quote = var2x;
         }
      });
   }

   public void writeXml(Writer var1, CromToMappingStructureWriter var2) throws IOException {
      var2.setElementMap(this.elements);
      var2.setTypeMap(this.elementTypes);
      var2.writeXml(var1);
   }

   public CROM getRoot() {
      return this.root;
   }

   public void setRoot(CROM var1) {
      this.root = var1;
   }

   public void setCromTypeName(String var1) {
      this.crom_type_name = var1;
   }

   public String getCromTypeName() {
      return this.crom_type_name;
   }

   public void addElementTypes(Map<String, CROM> var1) {
      this.elementTypes.putAll(var1);
   }

   private void addElementType(String var1, CROM var2) {
      this.elementTypes.put(var1, var2);
   }

   private void addElement(CROM var1) {
      this.elements.put(var1.getUniversalName(), var1);
   }

   public void addElements(Map<String, CROM> var1) {
      this.elements.putAll(var1);
   }

   public Map<String, CROM> getElements() {
      return Collections.unmodifiableMap(this.elements);
   }

   public Map<String, CROM> getElementTypeMap() {
      return Collections.unmodifiableMap(this.elementTypes);
   }

   public CROM getTypeDefinition(String var1) {
      return this.elementTypes == null ? null : this.elementTypes.get(var1);
   }
}
