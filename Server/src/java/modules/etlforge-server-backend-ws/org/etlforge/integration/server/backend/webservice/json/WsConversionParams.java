package org.etlforge.integration.server.backend.webservice.json;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConversionParams implements Serializable {
   private String jsonFilePath;
   private String xmlFilePath;
   private long transformationId;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConversionParams.class, true);

   public WsConversionParams() {
   }

   public WsConversionParams(String var1, String var2, long var3) {
      this.jsonFilePath = var1;
      this.xmlFilePath = var2;
      this.transformationId = var3;
   }

   public String getJsonFilePath() {
      return this.jsonFilePath;
   }

   public void setJsonFilePath(String var1) {
      this.jsonFilePath = var1;
   }

   public String getXmlFilePath() {
      return this.xmlFilePath;
   }

   public void setXmlFilePath(String var1) {
      this.xmlFilePath = var1;
   }

   public long getTransformationId() {
      return this.transformationId;
   }

   public void setTransformationId(long var1) {
      this.transformationId = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConversionParams)) {
         return false;
      }

      WsConversionParams var2 = (WsConversionParams)var1;
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
      boolean var3 = (
            this.jsonFilePath == null && var2.getJsonFilePath() == null || this.jsonFilePath != null && this.jsonFilePath.equals(var2.getJsonFilePath())
         )
         && (this.xmlFilePath == null && var2.getXmlFilePath() == null || this.xmlFilePath != null && this.xmlFilePath.equals(var2.getXmlFilePath()))
         && this.transformationId == var2.getTransformationId();
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
      if (this.getJsonFilePath() != null) {
         var1 += this.getJsonFilePath().hashCode();
      }

      if (this.getXmlFilePath() != null) {
         var1 += this.getXmlFilePath().hashCode();
      }

      var1 += new Long(this.getTransformationId()).hashCode();
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
      typeDesc.setXmlType(new QName("http://json.webservice.backend.server.integration.etlforge.invalid", "WsConversionParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("jsonFilePath");
      var0.setXmlName(new QName("http://json.webservice.backend.server.integration.etlforge.invalid", "JsonFilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("xmlFilePath");
      var0.setXmlName(new QName("http://json.webservice.backend.server.integration.etlforge.invalid", "XmlFilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationId");
      var0.setXmlName(new QName("http://json.webservice.backend.server.integration.etlforge.invalid", "TransformationId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
