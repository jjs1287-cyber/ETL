package org.etlforge.integration.server.engine.function;

public class ConvertTimeZoneParams {
   private String inputValue;
   private String fromTimeZoneId;
   private String toTimeZoneId;
   private boolean fromIsEuropeanFormat;
   private boolean toIsEuropeanFormat;
   private boolean fromIsDst;
   private boolean toIsDst;

   public ConvertTimeZoneParams() {
   }

   public ConvertTimeZoneParams(String var1, String var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      this.inputValue = var1;
      this.fromTimeZoneId = var2;
      this.toTimeZoneId = var3;
      this.fromIsEuropeanFormat = var4;
      this.toIsEuropeanFormat = var5;
      this.fromIsDst = var6;
      this.toIsDst = var7;
   }

   public String getInputValue() {
      return this.inputValue;
   }

   public void setInputValue(String var1) {
      this.inputValue = var1;
   }

   public String getFromTimeZoneId() {
      return this.fromTimeZoneId;
   }

   public void setFromTimeZoneId(String var1) {
      this.fromTimeZoneId = var1;
   }

   public String getToTimeZoneId() {
      return this.toTimeZoneId;
   }

   public void setToTimeZoneId(String var1) {
      this.toTimeZoneId = var1;
   }

   public boolean isFromIsEuropeanFormat() {
      return this.fromIsEuropeanFormat;
   }

   public void setFromIsEuropeanFormat(boolean var1) {
      this.fromIsEuropeanFormat = var1;
   }

   public boolean isToIsEuropeanFormat() {
      return this.toIsEuropeanFormat;
   }

   public void setToIsEuropeanFormat(boolean var1) {
      this.toIsEuropeanFormat = var1;
   }

   public boolean isFromIsDst() {
      return this.fromIsDst;
   }

   public void setFromIsDst(boolean var1) {
      this.fromIsDst = var1;
   }

   public boolean isToIsDst() {
      return this.toIsDst;
   }

   public void setToIsDst(boolean var1) {
      this.toIsDst = var1;
   }
}
