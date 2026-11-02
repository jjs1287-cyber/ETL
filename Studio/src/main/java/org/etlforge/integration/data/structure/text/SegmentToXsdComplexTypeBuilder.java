package org.etlforge.integration.data.structure.text;

import org.apache.commons.lang.StringUtils;
import org.etlforge.io.KongaIoUtils;

final class SegmentToXsdComplexTypeBuilder {
   private static final String NEW_LINE = KongaIoUtils.LINE_BREAK;
   private final String xsdPrefix;
   private StringBuilder xsd;

   public SegmentToXsdComplexTypeBuilder(String var1) {
      this.xsdPrefix = var1;
   }

   public String createType(Segment var1) {
      this.xsd = new StringBuilder();
      this.openNamedElement("complexType", var1.getName());
      this.xsd.append(">").append(NEW_LINE);
      this.openElementAndStartNewLine("sequence");

      for (Field var3 : var1) {
         this.addField(var3);
      }

      this.closeElement("sequence");
      this.closeElement("complexType");
      return this.xsd.toString();
   }

   private void openNamedElement(String var1, String var2) {
      this.openElement(var1);
      this.xsd.append(" name=\"").append(var2).append("\"");
   }

   private void openElement(String var1) {
      this.xsd.append("<");
      this.appendPrefix();
      this.xsd.append(var1);
   }

   private void appendPrefix() {
      if (this.hasPrefix()) {
         this.xsd.append(this.xsdPrefix).append(":");
      }
   }

   private boolean hasPrefix() {
      return StringUtils.isNotEmpty(this.xsdPrefix);
   }

   private void openElementAndStartNewLine(String var1) {
      this.openElement(var1);
      this.xsd.append(">").append(NEW_LINE);
   }

   private void addField(Field var1) {
      if (var1.getDataType() != FieldDataType.Identifier) {
         this.openNamedElement("element", var1.getName());
         this.xsd.append(" type=\"");
         this.appendPrefix();
         this.xsd.append("string\" minOccurs=\"0\" />").append(NEW_LINE);
      }
   }

   private void closeElement(String var1) {
      this.xsd.append("</");
      this.appendPrefix();
      this.xsd.append(var1).append(">").append(NEW_LINE);
   }
}
