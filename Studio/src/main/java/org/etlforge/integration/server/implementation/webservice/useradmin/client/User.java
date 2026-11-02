package org.etlforge.integration.server.implementation.webservice.useradmin.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class User implements Serializable {
   private String userId;
   private String firstName;
   private String lastName;
   private String login;
   private String password;
   private String email;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(User.class, true);

   public User() {
   }

   public User(String var1, String var2, String var3, String var4, String var5, String var6) {
      this.userId = var1;
      this.firstName = var2;
      this.lastName = var3;
      this.login = var4;
      this.password = var5;
      this.email = var6;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String var1) {
      this.userId = var1;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public void setFirstName(String var1) {
      this.firstName = var1;
   }

   public String getLastName() {
      return this.lastName;
   }

   public void setLastName(String var1) {
      this.lastName = var1;
   }

   public String getLogin() {
      return this.login;
   }

   public void setLogin(String var1) {
      this.login = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String var1) {
      this.email = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof User)) {
         return false;
      }

      User var2 = (User)var1;
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
      boolean var3 = (this.userId == null && var2.getUserId() == null || this.userId != null && this.userId.equals(var2.getUserId()))
         && (this.firstName == null && var2.getFirstName() == null || this.firstName != null && this.firstName.equals(var2.getFirstName()))
         && (this.lastName == null && var2.getLastName() == null || this.lastName != null && this.lastName.equals(var2.getLastName()))
         && (this.login == null && var2.getLogin() == null || this.login != null && this.login.equals(var2.getLogin()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && (this.email == null && var2.getEmail() == null || this.email != null && this.email.equals(var2.getEmail()));
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
      if (this.getUserId() != null) {
         var1 += this.getUserId().hashCode();
      }

      if (this.getFirstName() != null) {
         var1 += this.getFirstName().hashCode();
      }

      if (this.getLastName() != null) {
         var1 += this.getLastName().hashCode();
      }

      if (this.getLogin() != null) {
         var1 += this.getLogin().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      if (this.getEmail() != null) {
         var1 += this.getEmail().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "User"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("userId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UserId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("firstName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "FirstName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lastName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "LastName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("login");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Login"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("email");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Email"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
