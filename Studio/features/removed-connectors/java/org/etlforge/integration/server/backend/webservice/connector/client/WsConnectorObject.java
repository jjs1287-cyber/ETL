package org.etlforge.integration.server.backend.webservice.connector.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectorObject implements Serializable {
   private String objectName;
   private String objectDesciption;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorObject.class, true);

   public WsConnectorObject() {
   }

   public WsConnectorObject(String var1, String var2) {
      this.objectName = var1;
      this.objectDesciption = var2;
   }

   public String getObjectName() {
      return this.objectName;
   }

   public void setObjectName(String var1) {
      this.objectName = var1;
   }

   public String getObjectDesciption() {
      return this.objectDesciption;
   }

   public void setObjectDesciption(String var1) {
      this.objectDesciption = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorObject)) {
         return false;
      }

      WsConnectorObject var2 = (WsConnectorObject)var1;
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
      boolean var3 = (this.objectName == null && var2.getObjectName() == null || this.objectName != null && this.objectName.equals(var2.getObjectName()))
         && (
            this.objectDesciption == null && var2.getObjectDesciption() == null
               || this.objectDesciption != null && this.objectDesciption.equals(var2.getObjectDesciption())
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
      if (this.getObjectName() != null) {
         var1 += this.getObjectName().hashCode();
      }

      if (this.getObjectDesciption() != null) {
         var1 += this.getObjectDesciption().hashCode();
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObject"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("objectName");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ObjectName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("objectDesciption");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ObjectDesciption"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
