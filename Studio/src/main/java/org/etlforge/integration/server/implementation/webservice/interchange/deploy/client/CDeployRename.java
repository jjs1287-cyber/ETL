package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CDeployRename implements Serializable {
   private CIntegrationEntity entity;
   private String oldName;
   private String newName;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CDeployRename.class, true);

   public CDeployRename() {
   }

   public CDeployRename(CIntegrationEntity var1, String var2, String var3) {
      this.entity = var1;
      this.oldName = var2;
      this.newName = var3;
   }

   public CIntegrationEntity getEntity() {
      return this.entity;
   }

   public void setEntity(CIntegrationEntity var1) {
      this.entity = var1;
   }

   public String getOldName() {
      return this.oldName;
   }

   public void setOldName(String var1) {
      this.oldName = var1;
   }

   public String getNewName() {
      return this.newName;
   }

   public void setNewName(String var1) {
      this.newName = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CDeployRename)) {
         return false;
      }

      CDeployRename var2 = (CDeployRename)var1;
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
      boolean var3 = (this.entity == null && var2.getEntity() == null || this.entity != null && this.entity.equals(var2.getEntity()))
         && (this.oldName == null && var2.getOldName() == null || this.oldName != null && this.oldName.equals(var2.getOldName()))
         && (this.newName == null && var2.getNewName() == null || this.newName != null && this.newName.equals(var2.getNewName()));
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
      if (this.getEntity() != null) {
         var1 += this.getEntity().hashCode();
      }

      if (this.getOldName() != null) {
         var1 += this.getOldName().hashCode();
      }

      if (this.getNewName() != null) {
         var1 += this.getNewName().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CDeployRename"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("entity");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Entity"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CIntegrationEntity")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("oldName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OldName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("newName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "NewName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
