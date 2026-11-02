package org.etlforge.util;

public class NameValuePair {
   private String name;
   private String value;

   NameValuePair(String var1, String var2) {
      this.name = var1.trim();
      this.value = var2.trim();
   }

   public String getKey() {
      return this.name.toUpperCase();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }
}
