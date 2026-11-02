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

public class PermissionsHolder implements Serializable {
   private CUserPermissions[] userPermissions;
   private CGroupPermissions[] groupPermissions;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(PermissionsHolder.class, true);

   public PermissionsHolder() {
   }

   public PermissionsHolder(CUserPermissions[] var1, CGroupPermissions[] var2) {
      this.userPermissions = var1;
      this.groupPermissions = var2;
   }

   public CUserPermissions[] getUserPermissions() {
      return this.userPermissions;
   }

   public void setUserPermissions(CUserPermissions[] var1) {
      this.userPermissions = var1;
   }

   public CGroupPermissions[] getGroupPermissions() {
      return this.groupPermissions;
   }

   public void setGroupPermissions(CGroupPermissions[] var1) {
      this.groupPermissions = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof PermissionsHolder)) {
         return false;
      }

      PermissionsHolder var2 = (PermissionsHolder)var1;
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
            this.userPermissions == null && var2.getUserPermissions() == null
               || this.userPermissions != null && Arrays.equals(this.userPermissions, var2.getUserPermissions())
         )
         && (
            this.groupPermissions == null && var2.getGroupPermissions() == null
               || this.groupPermissions != null && Arrays.equals(this.groupPermissions, var2.getGroupPermissions())
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
      if (this.getUserPermissions() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getUserPermissions()); var2++) {
            Object var3 = Array.get(this.getUserPermissions(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getGroupPermissions() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getGroupPermissions()); var4++) {
            Object var5 = Array.get(this.getGroupPermissions(), var4);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "PermissionsHolder")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("userPermissions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "UserPermissions"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CUserPermissions"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("groupPermissions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "GroupPermissions"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CGroupPermissions"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
