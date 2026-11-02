package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConvertTimeZoneInput implements Serializable {
   private String inputValue;
   private String fromTimeZoneId;
   private String toTimeZoneId;
   private boolean fromIsEuropeanFormat;
   private boolean toIsEuropeanFormat;
   private boolean fromIsDst;
   private boolean toIsDst;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConvertTimeZoneInput.class, true);

   public WsConvertTimeZoneInput() {
   }

   public WsConvertTimeZoneInput(String var1, String var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7) {
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

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConvertTimeZoneInput)) {
         return false;
      }

      WsConvertTimeZoneInput var2 = (WsConvertTimeZoneInput)var1;
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
         && (
            this.fromTimeZoneId == null && var2.getFromTimeZoneId() == null
               || this.fromTimeZoneId != null && this.fromTimeZoneId.equals(var2.getFromTimeZoneId())
         )
         && (this.toTimeZoneId == null && var2.getToTimeZoneId() == null || this.toTimeZoneId != null && this.toTimeZoneId.equals(var2.getToTimeZoneId()))
         && this.fromIsEuropeanFormat == var2.isFromIsEuropeanFormat()
         && this.toIsEuropeanFormat == var2.isToIsEuropeanFormat()
         && this.fromIsDst == var2.isFromIsDst()
         && this.toIsDst == var2.isToIsDst();
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

      if (this.getFromTimeZoneId() != null) {
         var1 += this.getFromTimeZoneId().hashCode();
      }

      if (this.getToTimeZoneId() != null) {
         var1 += this.getToTimeZoneId().hashCode();
      }

      var1 += (this.isFromIsEuropeanFormat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += (this.isToIsEuropeanFormat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += (this.isFromIsDst() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += (this.isToIsDst() ? Boolean.TRUE : Boolean.FALSE).hashCode();
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
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsConvertTimeZoneInput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("inputValue");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "InputValue"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fromTimeZoneId");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromTimeZoneId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("toTimeZoneId");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ToTimeZoneId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fromIsEuropeanFormat");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromIsEuropeanFormat"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("toIsEuropeanFormat");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ToIsEuropeanFormat"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fromIsDst");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromIsDst"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("toIsDst");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ToIsDst"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
