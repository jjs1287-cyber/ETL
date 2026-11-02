package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Strings;

public class XmlQName {
   private final String localName;
   private final String prefix;

   public XmlQName(String var1) {
      String[] var2 = var1.split(":");
      this.prefix = var2.length == 1 ? "" : var2[0];
      this.localName = var2[var2.length - 1];
   }

   public XmlQName(String var1, String var2) {
      this.prefix = var1 == null ? "" : var1;
      this.localName = var2;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getLocalName() {
      return this.localName;
   }

   public String getQNameFromLocalName(String var1) {
      return getQName(this.prefix, var1);
   }

   public String getQName() {
      return getQName(this.prefix, this.localName);
   }

   public static String getQName(String var0, String var1) {
      return Strings.isNullOrEmpty(var0) ? var1 : var0 + ":" + var1;
   }

   public static String getPrefix(String var0) {
      XmlQName var1 = new XmlQName(var0);
      return var1.prefix;
   }

   public static String getLocalName(String var0) {
      XmlQName var1 = new XmlQName(var0);
      return var1.getLocalName();
   }
}
