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

public class CDeployProjectCheckResult implements Serializable {
   private CProjectMembership[] projectMembershipInfo;
   private CLastUpdate[] lastUpdated;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CDeployProjectCheckResult.class, true);

   public CDeployProjectCheckResult() {
   }

   public CDeployProjectCheckResult(CProjectMembership[] var1, CLastUpdate[] var2) {
      this.projectMembershipInfo = var1;
      this.lastUpdated = var2;
   }

   public CProjectMembership[] getProjectMembershipInfo() {
      return this.projectMembershipInfo;
   }

   public void setProjectMembershipInfo(CProjectMembership[] var1) {
      this.projectMembershipInfo = var1;
   }

   public CLastUpdate[] getLastUpdated() {
      return this.lastUpdated;
   }

   public void setLastUpdated(CLastUpdate[] var1) {
      this.lastUpdated = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CDeployProjectCheckResult)) {
         return false;
      }

      CDeployProjectCheckResult var2 = (CDeployProjectCheckResult)var1;
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
            this.projectMembershipInfo == null && var2.getProjectMembershipInfo() == null
               || this.projectMembershipInfo != null && Arrays.equals(this.projectMembershipInfo, var2.getProjectMembershipInfo())
         )
         && (this.lastUpdated == null && var2.getLastUpdated() == null || this.lastUpdated != null && Arrays.equals(this.lastUpdated, var2.getLastUpdated()));
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
      if (this.getProjectMembershipInfo() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getProjectMembershipInfo()); var2++) {
            Object var3 = Array.get(this.getProjectMembershipInfo(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getLastUpdated() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getLastUpdated()); var4++) {
            Object var5 = Array.get(this.getLastUpdated(), var4);
            if (var5 != null && !var5.getClass().isArray()) {
               var1 += var5.hashCode();
            }
         }
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CDeployProjectCheckResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("projectMembershipInfo");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ProjectMembershipInfo")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CProjectMembership")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lastUpdated");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "LastUpdated"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CLastUpdate"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
