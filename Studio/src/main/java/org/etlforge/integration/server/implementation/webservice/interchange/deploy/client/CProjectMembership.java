package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CProjectMembership implements Serializable {
   private CIntegrationEntity item;
   private String projectGuid;
   private String projectName;
   private String ownerGuid;
   private String ownerLoginName;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CProjectMembership.class, true);

   public CProjectMembership() {
   }

   public CProjectMembership(CIntegrationEntity var1, String var2, String var3, String var4, String var5) {
      this.item = var1;
      this.projectGuid = var2;
      this.projectName = var3;
      this.ownerGuid = var4;
      this.ownerLoginName = var5;
   }

   public CIntegrationEntity getItem() {
      return this.item;
   }

   public void setItem(CIntegrationEntity var1) {
      this.item = var1;
   }

   public String getProjectGuid() {
      return this.projectGuid;
   }

   public void setProjectGuid(String var1) {
      this.projectGuid = var1;
   }

   public String getProjectName() {
      return this.projectName;
   }

   public void setProjectName(String var1) {
      this.projectName = var1;
   }

   public String getOwnerGuid() {
      return this.ownerGuid;
   }

   public void setOwnerGuid(String var1) {
      this.ownerGuid = var1;
   }

   public String getOwnerLoginName() {
      return this.ownerLoginName;
   }

   public void setOwnerLoginName(String var1) {
      this.ownerLoginName = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CProjectMembership)) {
         return false;
      }

      CProjectMembership var2 = (CProjectMembership)var1;
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
      boolean var3 = (this.item == null && var2.getItem() == null || this.item != null && this.item.equals(var2.getItem()))
         && (this.projectGuid == null && var2.getProjectGuid() == null || this.projectGuid != null && this.projectGuid.equals(var2.getProjectGuid()))
         && (this.projectName == null && var2.getProjectName() == null || this.projectName != null && this.projectName.equals(var2.getProjectName()))
         && (this.ownerGuid == null && var2.getOwnerGuid() == null || this.ownerGuid != null && this.ownerGuid.equals(var2.getOwnerGuid()))
         && (
            this.ownerLoginName == null && var2.getOwnerLoginName() == null
               || this.ownerLoginName != null && this.ownerLoginName.equals(var2.getOwnerLoginName())
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
      if (this.getItem() != null) {
         var1 += this.getItem().hashCode();
      }

      if (this.getProjectGuid() != null) {
         var1 += this.getProjectGuid().hashCode();
      }

      if (this.getProjectName() != null) {
         var1 += this.getProjectName().hashCode();
      }

      if (this.getOwnerGuid() != null) {
         var1 += this.getOwnerGuid().hashCode();
      }

      if (this.getOwnerLoginName() != null) {
         var1 += this.getOwnerLoginName().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CProjectMembership")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("item");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Item"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CIntegrationEntity")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("projectGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ProjectGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("projectName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ProjectName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ownerGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OwnerGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ownerLoginName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OwnerLoginName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
