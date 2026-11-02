package org.etlforge.integration.server.implementation.webservice.useradmin.client;

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

public class UsersAndGroups implements Serializable {
   private User[] users;
   private Group[] groups;
   private UserGroup[] userGroups;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(UsersAndGroups.class, true);

   public UsersAndGroups() {
   }

   public UsersAndGroups(User[] var1, Group[] var2, UserGroup[] var3) {
      this.users = var1;
      this.groups = var2;
      this.userGroups = var3;
   }

   public User[] getUsers() {
      return this.users;
   }

   public void setUsers(User[] var1) {
      this.users = var1;
   }

   public Group[] getGroups() {
      return this.groups;
   }

   public void setGroups(Group[] var1) {
      this.groups = var1;
   }

   public UserGroup[] getUserGroups() {
      return this.userGroups;
   }

   public void setUserGroups(UserGroup[] var1) {
      this.userGroups = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof UsersAndGroups)) {
         return false;
      }

      UsersAndGroups var2 = (UsersAndGroups)var1;
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
      boolean var3 = (this.users == null && var2.getUsers() == null || this.users != null && Arrays.equals(this.users, var2.getUsers()))
         && (this.groups == null && var2.getGroups() == null || this.groups != null && Arrays.equals(this.groups, var2.getGroups()))
         && (this.userGroups == null && var2.getUserGroups() == null || this.userGroups != null && Arrays.equals(this.userGroups, var2.getUserGroups()));
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
      if (this.getUsers() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getUsers()); var2++) {
            Object var3 = Array.get(this.getUsers(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getGroups() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getGroups()); var4++) {
            Object var6 = Array.get(this.getGroups(), var4);
            if (var6 != null && !var6.getClass().isArray()) {
               var1 += var6.hashCode();
            }
         }
      }

      if (this.getUserGroups() != null) {
         for (int var5 = 0; var5 < Array.getLength(this.getUserGroups()); var5++) {
            Object var7 = Array.get(this.getUserGroups(), var5);
            if (var7 != null && !var7.getClass().isArray()) {
               var1 += var7.hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UsersAndGroups"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("users");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Users"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "User"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("groups");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Groups"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Group"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userGroups");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UserGroups"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UserGroup"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
