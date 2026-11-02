package org.etlforge.integration.server.implementation.webservice.useradmin.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class Group implements Serializable {
   private String groupId;
   private String groupName;
   private String description;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(Group.class, true);

   public Group() {
   }

   public Group(String var1, String var2, String var3) {
      this.groupId = var1;
      this.groupName = var2;
      this.description = var3;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String var1) {
      this.groupId = var1;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String var1) {
      this.groupName = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof Group)) {
         return false;
      }

      Group var2 = (Group)var1;
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
         && (this.groupName == null && var2.getGroupName() == null || this.groupName != null && this.groupName.equals(var2.getGroupName()))
         && (this.description == null && var2.getDescription() == null || this.description != null && this.description.equals(var2.getDescription()));
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

      if (this.getGroupName() != null) {
         var1 += this.getGroupName().hashCode();
      }

      if (this.getDescription() != null) {
         var1 += this.getDescription().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Group"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("groupId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "GroupId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("groupName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "GroupName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("description");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Description"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
