package org.etlforge.integration.json.xml;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.etlforge.json.ScalarValueType;
import org.etlforge.xml.KongaXmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class XmlToJsonGenerator {
   private XsdInfo xsdInfo;
   private JsonWriter writer;
   private File outputFile;

   public void setOutputFile(File var1) {
      this.outputFile = var1;
   }

   public File convertToJson(File var1, File var2) throws Exception {
      File var3 = this.getOutputFile(var1);
      this.openWriter(var3);

      try {
         this.xsdInfo = new XsdInfo(var2);
         SAXParserFactory var4 = SAXParserFactory.newInstance();
         var4.setNamespaceAware(true);
         var4.setValidating(true);
         SAXParser var5 = var4.newSAXParser();
         var5.parse(var1, new XmlToJsonGenerator.SaxHandler());
         return var3;
      } finally {
         this.closeWriter();
      }
   }

   private File getOutputFile(File var1) {
      return this.outputFile != null ? this.outputFile : new File(var1.getParent(), var1.getName() + ".json");
   }

   private void openWriter(File var1) throws IOException {
      this.writer = new JsonWriter();
      this.writer.open(var1);
   }

   private void closeWriter() throws IOException {
      this.writer.close();
      this.writer = null;
   }

   private static class Index {
      private int n = 0;

      private Index() {
      }

      public void add() {
         this.n++;
      }

      public boolean isFirst() {
         return this.n == 0;
      }

      @Override
      public String toString() {
         return "" + this.n;
      }
   }

   private final class SaxHandler extends DefaultHandler {
      private final ElementPath elements = new ElementPath();
      private final Stack<JsonGroup> parents = new Stack<>();
      private final Stack<XmlToJsonGenerator.Index> childIndeces = new Stack<>();
      private final Stack<ArrayMode> arrays = new Stack<>();
      private boolean nilElement;
      private StringBuilder currentValue;
      private ScalarValueType valueType;

      private SaxHandler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         try {
            this.startElementImpl(var2, var4);
         } catch (IOException var6) {
            throw new JsonWriterException(var6);
         }
      }

      private void startElementImpl(String var1, Attributes var2) throws IOException {
         this.elements.push(var1);
         this.nilElement = false;
         this.valueType = null;
         this.currentValue = null;
         ArrayMode var3 = ArrayMode.NOT_ARRAY;
         if (this.isArray(var2)) {
            if (XmlToJsonGenerator.this.xsdInfo.isSimpleArray(this.elements)) {
               var3 = ArrayMode.SIMPLE;
            } else {
               var3 = ArrayMode.NORMAL;
            }

            this.startGroup(var1, JsonGroup.ARRAY);
         } else if (this.isObject(var2)) {
            this.startGroup(var1, JsonGroup.OBJECT);
         } else {
            this.startField(var2);
         }

         this.arrays.push(var3);
      }

      private void startGroup(String var1, JsonGroup var2) throws IOException {
         if (this.parents.isEmpty()) {
            XmlToJsonGenerator.this.writer.openGroup(var2);
         } else {
            XmlToJsonGenerator.Index var3 = this.childIndeces.peek();
            if (!var3.isFirst()) {
               XmlToJsonGenerator.this.writer.insertFieldOrItemSeparator();
            }

            if (this.arrays.peek() == ArrayMode.SIMPLE) {
               XmlToJsonGenerator.this.writer.openGroup(JsonGroup.OBJECT);
               XmlToJsonGenerator.this.writer.openField(var1);
            }

            JsonGroup var4 = this.parents.peek();
            if (var4 == JsonGroup.ARRAY) {
               XmlToJsonGenerator.this.writer.openGroup(var2);
            } else if (var4 == JsonGroup.OBJECT) {
               XmlToJsonGenerator.this.writer.openFieldGroup(var1, var2);
            }
         }

         this.parents.push(var2);
         this.childIndeces.push(new XmlToJsonGenerator.Index());
      }

      private boolean isArray(Attributes var1) {
         return KongaXmlUtils.isTrue(var1, "array");
      }

      private boolean isObject(Attributes var1) {
         return KongaXmlUtils.isTrue(var1, "object");
      }

      private void startField(Attributes var1) {
         this.nilElement = this.isNilValue(var1);
         XsdSimpleType var2 = XmlToJsonGenerator.this.xsdInfo.lookupType(this.elements);
         this.valueType = var2.getJsonType();
         this.currentValue = new StringBuilder();
      }

      private boolean isNilValue(Attributes var1) {
         return KongaXmlUtils.isTrue(var1, "xsi:nil");
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         try {
            this.arrays.pop();
            this.endElementImpl(var2);
            this.elements.pop();
         } catch (IOException var5) {
            throw new JsonWriterException(var5);
         }
      }

      private void endElementImpl(String var1) throws IOException {
         boolean var2 = !this.arrays.isEmpty() && this.arrays.peek() == ArrayMode.SIMPLE;
         if (this.currentValue != null) {
            XmlToJsonGenerator.Index var3 = this.childIndeces.peek();
            if (!var3.isFirst()) {
               XmlToJsonGenerator.this.writer.insertFieldOrItemSeparator();
            }

            JsonGroup var4 = this.parents.peek();
            if (var4 == JsonGroup.ARRAY) {
               if (var2) {
                  if (this.nilElement) {
                     XmlToJsonGenerator.this.writer.writeNullObject(var1);
                  } else {
                     XmlToJsonGenerator.this.writer.writeScalarObject(var1, this.valueType, this.currentValue.toString());
                  }
               } else if (this.nilElement) {
                  XmlToJsonGenerator.this.writer.writeNullArrayItem();
               } else {
                  XmlToJsonGenerator.this.writer.writeScalarArrayItem(this.valueType, this.currentValue.toString());
               }
            } else if (this.nilElement) {
               XmlToJsonGenerator.this.writer.writeNullField(var1);
            } else {
               XmlToJsonGenerator.this.writer.writeScalarField(var1, this.valueType, this.currentValue.toString());
            }

            var3.add();
            this.currentValue = null;
         } else {
            if (var2) {
               XmlToJsonGenerator.this.writer.newLine();
               XmlToJsonGenerator.this.writer.decreaseIndent();
               XmlToJsonGenerator.this.writer.writeIndent();
               XmlToJsonGenerator.this.writer.closeGroup(JsonGroup.OBJECT);
            }

            XmlToJsonGenerator.this.writer.newLine();
            XmlToJsonGenerator.this.writer.decreaseIndent();
            XmlToJsonGenerator.this.writer.writeIndent();
            JsonGroup var5 = this.parents.pop();
            XmlToJsonGenerator.this.writer.closeGroup(var5);
            this.childIndeces.pop();
            if (!this.childIndeces.isEmpty()) {
               this.childIndeces.peek().add();
            }
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.currentValue != null) {
            String var4 = new String(var1, var2, var3);
            this.currentValue.append(var4);
         }
      }
   }
}
