package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CDeployWarning implements Serializable {
   private CIntegrationEntity entity;
   private String warning;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CDeployWarning.class, true);

   public CDeployWarning() {
   }

   public CDeployWarning(CIntegrationEntity var1, String var2) {
      this.entity = var1;
      this.warning = var2;
   }

   public CIntegrationEntity getEntity() {
      return this.entity;
   }

   public void setEntity(CIntegrationEntity var1) {
      this.entity = var1;
   }

   public String getWarning() {
      return this.warning;
   }

   public void setWarning(String var1) {
      this.warning = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CDeployWarning)) {
         return false;
      }

      CDeployWarning var2 = (CDeployWarning)var1;
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
         && (this.warning == null && var2.getWarning() == null || this.warning != null && this.warning.equals(var2.getWarning()));
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

      if (this.getWarning() != null) {
         var1 += this.getWarning().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CDeployWarning")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("entity");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Entity"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CIntegrationEntity")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("warning");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Warning"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
