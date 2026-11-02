package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsTestFunctionInput implements Serializable {
   private int id;
   private String field1;
   private String field2;
   private String field3;
   private String field4;
   private String field5;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsTestFunctionInput.class, true);

   public WsTestFunctionInput() {
   }

   public WsTestFunctionInput(int var1, String var2, String var3, String var4, String var5, String var6) {
      this.id = var1;
      this.field1 = var2;
      this.field2 = var3;
      this.field3 = var4;
      this.field4 = var5;
      this.field5 = var6;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public String getField1() {
      return this.field1;
   }

   public void setField1(String var1) {
      this.field1 = var1;
   }

   public String getField2() {
      return this.field2;
   }

   public void setField2(String var1) {
      this.field2 = var1;
   }

   public String getField3() {
      return this.field3;
   }

   public void setField3(String var1) {
      this.field3 = var1;
   }

   public String getField4() {
      return this.field4;
   }

   public void setField4(String var1) {
      this.field4 = var1;
   }

   public String getField5() {
      return this.field5;
   }

   public void setField5(String var1) {
      this.field5 = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsTestFunctionInput)) {
         return false;
      }

      WsTestFunctionInput var2 = (WsTestFunctionInput)var1;
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
      boolean var3 = this.id == var2.getId()
         && (this.field1 == null && var2.getField1() == null || this.field1 != null && this.field1.equals(var2.getField1()))
         && (this.field2 == null && var2.getField2() == null || this.field2 != null && this.field2.equals(var2.getField2()))
         && (this.field3 == null && var2.getField3() == null || this.field3 != null && this.field3.equals(var2.getField3()))
         && (this.field4 == null && var2.getField4() == null || this.field4 != null && this.field4.equals(var2.getField4()))
         && (this.field5 == null && var2.getField5() == null || this.field5 != null && this.field5.equals(var2.getField5()));
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
      var1 += this.getId();
      if (this.getField1() != null) {
         var1 += this.getField1().hashCode();
      }

      if (this.getField2() != null) {
         var1 += this.getField2().hashCode();
      }

      if (this.getField3() != null) {
         var1 += this.getField3().hashCode();
      }

      if (this.getField4() != null) {
         var1 += this.getField4().hashCode();
      }

      if (this.getField5() != null) {
         var1 += this.getField5().hashCode();
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
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsTestFunctionInput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("id");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Id"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("field1");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Field1"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("field2");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Field2"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("field3");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Field3"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("field4");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Field4"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("field5");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Field5"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
