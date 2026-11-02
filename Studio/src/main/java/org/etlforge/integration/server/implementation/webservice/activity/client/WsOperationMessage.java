package org.etlforge.integration.server.implementation.webservice.activity.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsOperationMessage implements Serializable {
   private String fieldName;
   private String message;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOperationMessage.class, true);

   public WsOperationMessage() {
   }

   public WsOperationMessage(String var1, String var2) {
      this.fieldName = var1;
      this.message = var2;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public void setFieldName(String var1) {
      this.fieldName = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOperationMessage)) {
         return false;
      }

      WsOperationMessage var2 = (WsOperationMessage)var1;
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
      boolean var3 = (this.fieldName == null && var2.getFieldName() == null || this.fieldName != null && this.fieldName.equals(var2.getFieldName()))
         && (this.message == null && var2.getMessage() == null || this.message != null && this.message.equals(var2.getMessage()));
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
      if (this.getFieldName() != null) {
         var1 += this.getFieldName().hashCode();
      }

      if (this.getMessage() != null) {
         var1 += this.getMessage().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "WsOperationMessage"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("fieldName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "FieldName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("message");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Message"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
