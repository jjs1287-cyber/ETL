package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CWsdlFile implements Serializable {
   private String wsdlLocator;
   private int status;
   private String MD5;
   private String zippedBase64Contents;
   private int type;
   private String applicationMessage;
   private String userComment;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CWsdlFile.class, true);

   public CWsdlFile() {
   }

   public CWsdlFile(String var1, int var2, String var3, String var4, int var5, String var6, String var7) {
      this.wsdlLocator = var1;
      this.status = var2;
      this.MD5 = var3;
      this.zippedBase64Contents = var4;
      this.type = var5;
      this.applicationMessage = var6;
      this.userComment = var7;
   }

   public String getWsdlLocator() {
      return this.wsdlLocator;
   }

   public void setWsdlLocator(String var1) {
      this.wsdlLocator = var1;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int var1) {
      this.status = var1;
   }

   public String getMD5() {
      return this.MD5;
   }

   public void setMD5(String var1) {
      this.MD5 = var1;
   }

   public String getZippedBase64Contents() {
      return this.zippedBase64Contents;
   }

   public void setZippedBase64Contents(String var1) {
      this.zippedBase64Contents = var1;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public String getApplicationMessage() {
      return this.applicationMessage;
   }

   public void setApplicationMessage(String var1) {
      this.applicationMessage = var1;
   }

   public String getUserComment() {
      return this.userComment;
   }

   public void setUserComment(String var1) {
      this.userComment = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CWsdlFile)) {
         return false;
      }

      CWsdlFile var2 = (CWsdlFile)var1;
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
      boolean var3 = (this.wsdlLocator == null && var2.getWsdlLocator() == null || this.wsdlLocator != null && this.wsdlLocator.equals(var2.getWsdlLocator()))
         && this.status == var2.getStatus()
         && (this.MD5 == null && var2.getMD5() == null || this.MD5 != null && this.MD5.equals(var2.getMD5()))
         && (
            this.zippedBase64Contents == null && var2.getZippedBase64Contents() == null
               || this.zippedBase64Contents != null && this.zippedBase64Contents.equals(var2.getZippedBase64Contents())
         )
         && this.type == var2.getType()
         && (
            this.applicationMessage == null && var2.getApplicationMessage() == null
               || this.applicationMessage != null && this.applicationMessage.equals(var2.getApplicationMessage())
         )
         && (this.userComment == null && var2.getUserComment() == null || this.userComment != null && this.userComment.equals(var2.getUserComment()));
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
      if (this.getWsdlLocator() != null) {
         var1 += this.getWsdlLocator().hashCode();
      }

      var1 += this.getStatus();
      if (this.getMD5() != null) {
         var1 += this.getMD5().hashCode();
      }

      if (this.getZippedBase64Contents() != null) {
         var1 += this.getZippedBase64Contents().hashCode();
      }

      var1 += this.getType();
      if (this.getApplicationMessage() != null) {
         var1 += this.getApplicationMessage().hashCode();
      }

      if (this.getUserComment() != null) {
         var1 += this.getUserComment().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("wsdlLocator");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WsdlLocator"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "Status"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MD5");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "MD5"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("zippedBase64Contents");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "ZippedBase64Contents")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("type");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "Type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("applicationMessage");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "ApplicationMessage")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userComment");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "UserComment"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
