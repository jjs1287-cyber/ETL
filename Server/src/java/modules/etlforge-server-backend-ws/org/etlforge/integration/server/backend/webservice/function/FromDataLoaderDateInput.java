package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class FromDataLoaderDateInput implements Serializable {
   private String inputValue;
   private String timeZoneId;
   private boolean europeanFormat;
   private String lastMatchingFormat;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(FromDataLoaderDateInput.class, true);

   public FromDataLoaderDateInput() {
   }

   public FromDataLoaderDateInput(String var1, String var2, boolean var3, String var4) {
      this.inputValue = var1;
      this.timeZoneId = var2;
      this.europeanFormat = var3;
      this.lastMatchingFormat = var4;
   }

   public String getInputValue() {
      return this.inputValue;
   }

   public void setInputValue(String var1) {
      this.inputValue = var1;
   }

   public String getTimeZoneId() {
      return this.timeZoneId;
   }

   public void setTimeZoneId(String var1) {
      this.timeZoneId = var1;
   }

   public boolean isEuropeanFormat() {
      return this.europeanFormat;
   }

   public void setEuropeanFormat(boolean var1) {
      this.europeanFormat = var1;
   }

   public String getLastMatchingFormat() {
      return this.lastMatchingFormat;
   }

   public void setLastMatchingFormat(String var1) {
      this.lastMatchingFormat = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof FromDataLoaderDateInput)) {
         return false;
      }

      FromDataLoaderDateInput var2 = (FromDataLoaderDateInput)var1;
      if (var1 == null) {
         return false;
      }

      if (this == var1) {
         return true;
      }

      if (this.__equalsCalc != null) {
         return this.__equalsCalc == var1;
      }

      this.__equalsCalc = var1;
      boolean var3 = (this.inputValue == null && var2.getInputValue() == null || this.inputValue != null && this.inputValue.equals(var2.getInputValue()))
         && (this.timeZoneId == null && var2.getTimeZoneId() == null || this.timeZoneId != null && this.timeZoneId.equals(var2.getTimeZoneId()))
         && this.europeanFormat == var2.isEuropeanFormat()
         && (
            this.lastMatchingFormat == null && var2.getLastMatchingFormat() == null
               || this.lastMatchingFormat != null && this.lastMatchingFormat.equals(var2.getLastMatchingFormat())
         );
      this.__equalsCalc = null;
      return var3;
   }

   @Override
   public synchronized int hashCode() {
      if (this.__hashCodeCalc) {
         return 0;
      }

      this.__hashCodeCalc = true;
      int var1 = 1;
      if (this.getInputValue() != null) {
         var1 += this.getInputValue().hashCode();
      }

      if (this.getTimeZoneId() != null) {
         var1 += this.getTimeZoneId().hashCode();
      }

      var1 += (this.isEuropeanFormat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getLastMatchingFormat() != null) {
         var1 += this.getLastMatchingFormat().hashCode();
      }

      this.__hashCodeCalc = false;
      return var1;
   }

   public static TypeDesc getTypeDesc() {
      return typeDesc;
   }

   public static Serializer getSerializer(String var0, Class var1, QName var2) {
      return new BeanSerializer(var1, var2, typeDesc);
   }

   public static Deserializer getDeserializer(String var0, Class var1, QName var2) {
      return new BeanDeserializer(var1, var2, typeDesc);
   }

   static {
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromDataLoaderDateInput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("inputValue");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "InputValue"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("timeZoneId");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "TimeZoneId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("europeanFormat");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "EuropeanFormat"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lastMatchingFormat");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "LastMatchingFormat"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
