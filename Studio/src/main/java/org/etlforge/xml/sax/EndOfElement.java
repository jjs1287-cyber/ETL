package org.etlforge.xml.sax;

import org.etlforge.xml.KongaXmlUtils;

public final class EndOfElement {
   private final String uri;
   private final String localName;
   private final String qName;
   private final String value;

   public EndOfElement(String var1, String var2, String var3, String var4) {
      this.uri = var1;
      this.localName = var2;
      this.qName = var3;
      this.value = var4;
   }

   public String uri() {
      return this.uri;
   }

   public String localName() {
      return this.localName;
   }

   public String qName() {
      return this.qName;
   }

   public String value() {
      return this.value;
   }

   public Boolean valueAsBoolean() {
      return this.value == null ? null : KongaXmlUtils.isTrue(this.value);
   }

   public Integer valueAsInteger() {
      return this.value == null ? null : Integer.valueOf(this.value);
   }

   public Long valueAsLong() {
      return this.value == null ? null : Long.valueOf(this.value);
   }

   public Double valueAsDouble() {
      return this.value == null ? null : Double.valueOf(this.value);
   }
}
