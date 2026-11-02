package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CDeployResults implements Serializable {
   private CDeployWarning[] warnings;
   private CDeployRename[] renamedItems;
   private int deployStatus;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CDeployResults.class, true);

   public CDeployResults() {
   }

   public CDeployResults(CDeployWarning[] var1, CDeployRename[] var2, int var3) {
      this.warnings = var1;
      this.renamedItems = var2;
      this.deployStatus = var3;
   }

   public CDeployWarning[] getWarnings() {
      return this.warnings;
   }

   public void setWarnings(CDeployWarning[] var1) {
      this.warnings = var1;
   }

   public CDeployRename[] getRenamedItems() {
      return this.renamedItems;
   }

   public void setRenamedItems(CDeployRename[] var1) {
      this.renamedItems = var1;
   }

   public int getDeployStatus() {
      return this.deployStatus;
   }

   public void setDeployStatus(int var1) {
      this.deployStatus = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CDeployResults)) {
         return false;
      }

      CDeployResults var2 = (CDeployResults)var1;
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
      boolean var3 = (this.warnings == null && var2.getWarnings() == null || this.warnings != null && Arrays.equals(this.warnings, var2.getWarnings()))
         && (
            this.renamedItems == null && var2.getRenamedItems() == null
               || this.renamedItems != null && Arrays.equals(this.renamedItems, var2.getRenamedItems())
         )
         && this.deployStatus == var2.getDeployStatus();
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
      if (this.getWarnings() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getWarnings()); var2++) {
            Object var3 = Array.get(this.getWarnings(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getRenamedItems() != null) {
         for (int var5 = 0; var5 < Array.getLength(this.getRenamedItems()); var5++) {
            Object var6 = Array.get(this.getRenamedItems(), var5);
            if (var6 != null && !var6.getClass().isArray()) {
               var1 += var6.hashCode();
            }
         }
      }

      var1 += this.getDeployStatus();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CDeployResults")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("warnings");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Warnings"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CDeployWarning"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("renamedItems");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "RenamedItems"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CDeployRename"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("deployStatus");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DeployStatus"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
