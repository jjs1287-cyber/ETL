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

public class CGroupPermissions implements Serializable {
   private String groupId;
   private CPermission[] permissions;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CGroupPermissions.class, true);

   public CGroupPermissions() {
   }

   public CGroupPermissions(String var1, CPermission[] var2) {
      this.groupId = var1;
      this.permissions = var2;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String var1) {
      this.groupId = var1;
   }

   public CPermission[] getPermissions() {
      return this.permissions;
   }

   public void setPermissions(CPermission[] var1) {
      this.permissions = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CGroupPermissions)) {
         return false;
      }

      CGroupPermissions var2 = (CGroupPermissions)var1;
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
      boolean var3 = (this.groupId == null && var2.getGroupId() == null || this.groupId != null && this.groupId.equals(var2.getGroupId()))
         && (this.permissions == null && var2.getPermissions() == null || this.permissions != null && Arrays.equals(this.permissions, var2.getPermissions()));
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
      if (this.getGroupId() != null) {
         var1 += this.getGroupId().hashCode();
      }

      if (this.getPermissions() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getPermissions()); var2++) {
            Object var3 = Array.get(this.getPermissions(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CGroupPermissions")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("groupId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "GroupId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("permissions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Permissions"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CPermission"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
