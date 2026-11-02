package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CRequiredFile implements Serializable {
   private String path;
   private String MD5;
   private int type;
   private long modifiedTime;
   private String zippedBase64Contents;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CRequiredFile.class, true);

   public CRequiredFile() {
   }

   public CRequiredFile(String var1, String var2, int var3, long var4, String var6) {
      this.path = var1;
      this.MD5 = var2;
      this.type = var3;
      this.modifiedTime = var4;
      this.zippedBase64Contents = var6;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public String getMD5() {
      return this.MD5;
   }

   public void setMD5(String var1) {
      this.MD5 = var1;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public long getModifiedTime() {
      return this.modifiedTime;
   }

   public void setModifiedTime(long var1) {
      this.modifiedTime = var1;
   }

   public String getZippedBase64Contents() {
      return this.zippedBase64Contents;
   }

   public void setZippedBase64Contents(String var1) {
      this.zippedBase64Contents = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CRequiredFile)) {
         return false;
      }

      CRequiredFile var2 = (CRequiredFile)var1;
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
      boolean var3 = (this.path == null && var2.getPath() == null || this.path != null && this.path.equals(var2.getPath()))
         && (this.MD5 == null && var2.getMD5() == null || this.MD5 != null && this.MD5.equals(var2.getMD5()))
         && this.type == var2.getType()
         && this.modifiedTime == var2.getModifiedTime()
         && (
            this.zippedBase64Contents == null && var2.getZippedBase64Contents() == null
               || this.zippedBase64Contents != null && this.zippedBase64Contents.equals(var2.getZippedBase64Contents())
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
      if (this.getPath() != null) {
         var1 += this.getPath().hashCode();
      }

      if (this.getMD5() != null) {
         var1 += this.getMD5().hashCode();
      }

      var1 += this.getType();
      var1 += new Long(this.getModifiedTime()).hashCode();
      if (this.getZippedBase64Contents() != null) {
         var1 += this.getZippedBase64Contents().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CRequiredFile"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("path");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Path"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MD5");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MD5"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("type");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("modifiedTime");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ModifiedTime"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("zippedBase64Contents");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ZippedBase64Contents")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
