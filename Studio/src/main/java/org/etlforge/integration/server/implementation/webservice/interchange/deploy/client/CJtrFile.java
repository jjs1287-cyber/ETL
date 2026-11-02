package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CJtrFile implements Serializable {
   private String transformationGuid;
   private String sourceJtrZippedBase64;
   private String targetJtrZippedBase64;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CJtrFile.class, true);

   public CJtrFile() {
   }

   public CJtrFile(String var1, String var2, String var3) {
      this.transformationGuid = var1;
      this.sourceJtrZippedBase64 = var2;
      this.targetJtrZippedBase64 = var3;
   }

   public String getTransformationGuid() {
      return this.transformationGuid;
   }

   public void setTransformationGuid(String var1) {
      this.transformationGuid = var1;
   }

   public String getSourceJtrZippedBase64() {
      return this.sourceJtrZippedBase64;
   }

   public void setSourceJtrZippedBase64(String var1) {
      this.sourceJtrZippedBase64 = var1;
   }

   public String getTargetJtrZippedBase64() {
      return this.targetJtrZippedBase64;
   }

   public void setTargetJtrZippedBase64(String var1) {
      this.targetJtrZippedBase64 = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CJtrFile)) {
         return false;
      }

      CJtrFile var2 = (CJtrFile)var1;
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
            this.transformationGuid == null && var2.getTransformationGuid() == null
               || this.transformationGuid != null && this.transformationGuid.equals(var2.getTransformationGuid())
         )
         && (
            this.sourceJtrZippedBase64 == null && var2.getSourceJtrZippedBase64() == null
               || this.sourceJtrZippedBase64 != null && this.sourceJtrZippedBase64.equals(var2.getSourceJtrZippedBase64())
         )
         && (
            this.targetJtrZippedBase64 == null && var2.getTargetJtrZippedBase64() == null
               || this.targetJtrZippedBase64 != null && this.targetJtrZippedBase64.equals(var2.getTargetJtrZippedBase64())
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
      if (this.getTransformationGuid() != null) {
         var1 += this.getTransformationGuid().hashCode();
      }

      if (this.getSourceJtrZippedBase64() != null) {
         var1 += this.getSourceJtrZippedBase64().hashCode();
      }

      if (this.getTargetJtrZippedBase64() != null) {
         var1 += this.getTargetJtrZippedBase64().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CJtrFile"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("transformationGuid");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TransformationGuid")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourceJtrZippedBase64");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SourceJtrZippedBase64")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetJtrZippedBase64");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TargetJtrZippedBase64")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
