package org.etlforge.integration.json.xml;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.io.Indentation;
import org.etlforge.json.ScalarValueType;

final class XmlWriter implements Closeable {
   private final BufferedWriter output;
   private final Stack<String> elements;
   private Indentation indentation;
   private boolean includeSchemaTypes = true;
   private final XmlWriter.Attribute stringTypeAttribute = new XmlWriter.Attribute("xsi:type", "xs:string");
   private final XmlWriter.Attribute booleanTypeAttribute = new XmlWriter.Attribute("xsi:type", "xs:boolean");
   private final XmlWriter.Attribute numericTypeAttribute = new XmlWriter.Attribute("xsi:type", "xs:double");
   private final XmlElementNameWriter elementNameWriter = new XmlElementNameWriter("_");

   public XmlWriter(File var1) throws IOException {
      this.output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), Charsets.UTF_8));
      this.elements = new Stack<>();
      this.indentation = new Indentation();
      this.writeProcessingInstruction();
   }

   public void setIncludeSchemaTypes(boolean var1) {
      this.includeSchemaTypes = var1;
   }

   private void writeProcessingInstruction() throws IOException {
      this.output.write("<?xml version=\"1.0\" encoding=\"" + Charsets.UTF_8.name() + "\"?>");
      this.output.newLine();
   }

   public void writeScalarField(String var1, ScalarValueType var2, String var3) throws IOException {
      ArrayList var4 = Lists.newArrayList();
      if (this.includeSchemaTypes) {
         switch (var2) {
            case STRING:
               var4.add(this.stringTypeAttribute);
               break;
            case NUMBER:
               var4.add(this.numericTypeAttribute);
               break;
            case BOOLEAN:
               var4.add(this.booleanTypeAttribute);
         }
      }

      this.writeElement(var1, var3, var4);
   }

   public void openElement(String var1, Collection<XmlWriter.Attribute> var2) throws IOException {
      this.writeElement(var1, null, var2);
   }

   private void writeElement(String var1, String var2, Collection<XmlWriter.Attribute> var3) throws IOException {
      StringBuilder var4 = new StringBuilder("<");
      this.elementNameWriter.appendElementName(var4, var1);

      for (XmlWriter.Attribute var6 : var3) {
         var6.writeTo(var4);
      }

      var4.append(">");
      this.indentation.writeTo(this.output);
      this.output.write(var4.toString());
      if (var2 == null) {
         this.elements.push(var1);
         this.indentation = this.indentation.increase();
      } else {
         this.output.write(StringEscapeUtils.escapeXml(var2));
         this.writeEndTag(var1);
      }

      this.output.newLine();
   }

   private void writeEndTag(String var1) throws IOException {
      this.output.append("</");
      this.elementNameWriter.appendElementName(this.output, var1);
      this.output.append(">");
   }

   public void writeNilElement(String var1) throws IOException {
      StringBuilder var2 = new StringBuilder("<");
      this.elementNameWriter.appendElementName(var2, var1);
      var2.append(" xsi:nil=\"true\"/>");
      this.output.write(var2.toString());
      this.output.newLine();
   }

   public void closeElement() throws IOException {
      String var1 = this.elements.pop();
      this.indentation = this.indentation.decrease();
      this.indentation.writeTo(this.output);
      this.writeEndTag(var1);
      this.output.newLine();
   }

   @Override
   public void close() throws IOException {
      while (!this.elements.isEmpty()) {
         this.closeElement();
      }

      this.output.close();
   }

   static class Attribute {
      private final String name;
      private final String value;

      public Attribute(String var1, String var2) {
         this.name = var1;
         this.value = var2;
      }

      public void writeTo(StringBuilder var1) {
         var1.append(" ").append(this.name).append("=\"").append(this.value).append("\"");
      }
   }
}
