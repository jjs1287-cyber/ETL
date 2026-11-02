package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CUploadedJitterpak implements Serializable {
   private String projectGuid;
   private String base64EncodedJitterpak;
   private String tag;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CUploadedJitterpak.class, true);

   public CUploadedJitterpak() {
   }

   public CUploadedJitterpak(String var1, String var2, String var3) {
      this.projectGuid = var1;
      this.base64EncodedJitterpak = var2;
      this.tag = var3;
   }

   public String getProjectGuid() {
      return this.projectGuid;
   }

   public void setProjectGuid(String var1) {
      this.projectGuid = var1;
   }

   public String getBase64EncodedJitterpak() {
      return this.base64EncodedJitterpak;
   }

   public void setBase64EncodedJitterpak(String var1) {
      this.base64EncodedJitterpak = var1;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CUploadedJitterpak)) {
         return false;
      }

      CUploadedJitterpak var2 = (CUploadedJitterpak)var1;
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
      boolean var3 = (this.projectGuid == null && var2.getProjectGuid() == null || this.projectGuid != null && this.projectGuid.equals(var2.getProjectGuid()))
         && (
            this.base64EncodedJitterpak == null && var2.getBase64EncodedJitterpak() == null
               || this.base64EncodedJitterpak != null && this.base64EncodedJitterpak.equals(var2.getBase64EncodedJitterpak())
         )
         && (this.tag == null && var2.getTag() == null || this.tag != null && this.tag.equals(var2.getTag()));
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
      if (this.getProjectGuid() != null) {
         var1 += this.getProjectGuid().hashCode();
      }

      if (this.getBase64EncodedJitterpak() != null) {
         var1 += this.getBase64EncodedJitterpak().hashCode();
      }

      if (this.getTag() != null) {
         var1 += this.getTag().hashCode();
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CUploadedJitterpak")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("projectGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ProjectGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("base64EncodedJitterpak");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Base64EncodedJitterpak")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tag");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Tag"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
