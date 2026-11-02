package org.etlforge.integration.json.xml;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.json.ScalarValueType;
import org.etlforge.json.dom.JsonDomParser;
import org.etlforge.json.dom.RootNode;
import org.etlforge.json.parser.StreamingJsonParser;
import org.etlforge.system.ArgCheck;

public final class JsonToXmlGenerator {
   private StreamingJsonParser parser;
   private XmlWriter writer;
   private String rootName = "json";
   private String objectName = "object";
   private String arrayName = "array";
   private String arrayItemName = "item";
   private Stack<ArrayMode> arrays;
   private final XmlWriter.Attribute xsAttribute = new XmlWriter.Attribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
   private final XmlWriter.Attribute xsiAttribute = new XmlWriter.Attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
   private final XmlWriter.Attribute arrayAttribute = new XmlWriter.Attribute("array", "true");
   private final XmlWriter.Attribute simpleArrayAttribute = new XmlWriter.Attribute("simple", "true");
   private final XmlWriter.Attribute objectAttribute = new XmlWriter.Attribute("object", "true");
   private boolean includeSchemaTypeForStrings;
   private DomWalker domWalker;
   private boolean useDomTree;
   private File xsdFile;
   private XsdInfo xsdInfo;
   private ElementPath elementPath;

   void setUseDomTree(boolean var1) {
      this.useDomTree = var1;
   }

   public void setXsdFile(File var1) {
      this.xsdFile = var1;
   }

   public void setRootName(String var1) {
      ArgCheck.checkNotEmpty(var1, "rootName");
      this.rootName = var1;
   }

   public void setObjectName(String var1) {
      this.objectName = var1;
   }

   public void setArrayName(String var1) {
      this.arrayName = var1;
   }

   public void setArrayItemName(String var1) {
      this.arrayItemName = var1;
   }

   public void setIncludeSchemaTypeForStrings(boolean var1) {
      this.includeSchemaTypeForStrings = var1;
   }

   public void generateXml(File var1, File var2) throws JsonParseException, IOException {
      this.init();

      try {
         this.createParserAndWriter(var1, var2);
         this.generateXmlImpl();
      } finally {
         KongaIoUtils.close(this.parser);
         KongaIoUtils.close(this.writer);
      }
   }

   private void createParserAndWriter(File var1, File var2) throws IOException {
      this.parser = StreamingJsonParser.parseFile(var1);
      this.writer = new XmlWriter(var2);
      this.writer.setIncludeSchemaTypes(this.includeSchemaTypeForStrings);
      if (this.useDomTree) {
         this.loadDomTree(var1);
      }
   }

   private void loadDomTree(File var1) throws JsonParseException, IOException {
      JsonDomParser var2 = new JsonDomParser();
      RootNode var3 = var2.parse(var1);
      this.domWalker = new DomWalker(var3);
   }

   private void init() {
      this.writer = null;
      this.parser = null;
      this.elementPath = new ElementPath();
      if (this.xsdFile != null) {
         try {
            this.xsdInfo = new XsdInfo(this.xsdFile);
         } catch (Exception var2) {
            throw Throwables.propagate(var2);
         }
      }
   }

   private void generateXmlImpl() throws JsonParseException, IOException {
      this.arrays = new Stack<>();

      for (JsonToken var1 = this.parser.nextToken(); var1 != null; var1 = this.parser.nextToken()) {
         this.handleToken(var1);
      }
   }

   private void handleToken(JsonToken var1) throws IOException, JsonParseException {
      switch (var1) {
         case START_ARRAY:
            this.startArray(this.arrayName);
            break;
         case START_OBJECT:
            this.startObject();
            break;
         case FIELD_NAME:
            this.handleField();
            break;
         case END_ARRAY:
         case END_OBJECT:
            this.arrays.pop();
            if (!this.isProcessingSimpleArrayItem()) {
               this.writer.closeElement();
               this.elementPath.pop();
            }

            if (this.domWalker != null) {
               this.domWalker.endObjectOrArray();
            }
            break;
         default:
            if (var1.isScalarValue()) {
               this.handleScalarArrayItem(var1);
            }
      }
   }

   private void startArray(String var1) throws IOException {
      if (this.isRoot()) {
         var1 = this.rootName;
      }

      this.elementPath.push(var1);
      boolean var2 = false;
      if (this.domWalker != null) {
         this.domWalker.down();
         var2 = this.domWalker.isSimpleArray();
      } else if (this.xsdInfo != null) {
         var2 = this.xsdInfo.isSimpleArray(this.elementPath);
      }

      ArrayList var3 = Lists.newArrayList(this.arrayAttribute);
      if (var2) {
         var3.add(this.simpleArrayAttribute);
      }

      if (this.isRoot()) {
         Collections.addAll(var3, this.xsAttribute, this.xsiAttribute);
      }

      this.writer.openElement(var1, var3);
      this.arrays.push(var2 ? ArrayMode.SIMPLE : ArrayMode.NORMAL);
   }

   private boolean isRoot() {
      return this.arrays.isEmpty();
   }

   private void startObject() throws JsonParseException, IOException {
      if (this.domWalker != null) {
         this.domWalker.down();
      }

      ArrayList var1 = Lists.newArrayList(this.objectAttribute);
      String var2;
      if (this.isRoot()) {
         var2 = this.rootName;
         Collections.addAll(var1, this.xsAttribute, this.xsiAttribute);
      } else {
         var2 = this.parser.getCurrentName();
         if (var2 == null) {
            var2 = this.isProcessingArrayItem() ? this.arrayItemName : this.objectName;
         }
      }

      if (!this.isProcessingSimpleArrayItem()) {
         this.writer.openElement(var2, var1);
         this.elementPath.push(var2);
      }

      this.arrays.push(ArrayMode.NOT_ARRAY);
   }

   private boolean isProcessingArrayItem() {
      return !this.arrays.isEmpty() && this.arrays.peek().isArray();
   }

   private boolean isProcessingSimpleArrayItem() {
      return !this.arrays.isEmpty() && this.arrays.peek() == ArrayMode.SIMPLE;
   }

   private void handleField() throws JsonParseException, IOException {
      if (this.domWalker != null) {
         this.domWalker.startField();
      }

      JsonToken var1 = this.parser.nextToken();
      if (var1 == null) {
         throw new JsonParseException("Expected value, array, or object.", this.parser.getCurrentLocation());
      }

      if (var1.isScalarValue()) {
         String var2 = this.parser.getCurrentName();
         this.handleScalarToken(var1, var2);
         if (this.domWalker != null) {
            this.domWalker.endField();
         }
      } else {
         String var3 = this.parser.getCurrentName();
         if (var1 == JsonToken.START_ARRAY) {
            this.startArray(var3);
         } else {
            if (var1 != JsonToken.START_OBJECT) {
               throw new JsonParseException("Expected value, array, or object.", this.parser.getCurrentLocation());
            }

            this.startObject();
         }
      }
   }

   private void handleScalarToken(JsonToken var1, String var2) throws JsonParseException, IOException {
      ScalarValueType var3 = ScalarValueType.fromJsonToken(var1);
      String var4 = this.getScalarValueToOutput(var3);
      if (var4 == null) {
         this.writer.writeNilElement(var2);
      } else {
         this.writer.writeScalarField(var2, var3, var4);
      }
   }

   private String getScalarValueToOutput(ScalarValueType var1) throws IOException {
      Serializable var2;
      switch (var1) {
         case STRING:
            var2 = this.parser.getText();
            break;
         case NUMBER:
            var2 = this.parser.getNumberValue();
            break;
         case BOOLEAN:
            var2 = this.parser.getBooleanValue();
            break;
         case NULL:
            return null;
         default:
            throw new RuntimeException("Unexpected type: " + var1);
      }

      return var2.toString();
   }

   private void handleScalarArrayItem(JsonToken var1) throws JsonParseException, IOException {
      this.handleScalarToken(var1, this.arrayItemName);
      if (this.domWalker != null) {
         this.domWalker.scalarArrayItem();
      }
   }
}
