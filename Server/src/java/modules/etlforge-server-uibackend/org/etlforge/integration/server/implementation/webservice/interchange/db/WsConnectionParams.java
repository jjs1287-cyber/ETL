package org.etlforge.integration.server.implementation.webservice.interchange.db;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectionParams implements Serializable {
   private String server;
   private int port;
   private String database;
   private String user;
   private String password;
   private String manualConnection;
   private String additionalParams;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectionParams.class, true);

   public WsConnectionParams() {
   }

   public WsConnectionParams(String var1, int var2, String var3, String var4, String var5, String var6, String var7) {
      this.server = var1;
      this.port = var2;
      this.database = var3;
      this.user = var4;
      this.password = var5;
      this.manualConnection = var6;
      this.additionalParams = var7;
   }

   public String getServer() {
      return this.server;
   }

   public void setServer(String var1) {
      this.server = var1;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public String getDatabase() {
      return this.database;
   }

   public void setDatabase(String var1) {
      this.database = var1;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String var1) {
      this.user = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getManualConnection() {
      return this.manualConnection;
   }

   public void setManualConnection(String var1) {
      this.manualConnection = var1;
   }

   public String getAdditionalParams() {
      return this.additionalParams;
   }

   public void setAdditionalParams(String var1) {
      this.additionalParams = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectionParams)) {
         return false;
      }

      WsConnectionParams var2 = (WsConnectionParams)var1;
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
      boolean var3 = (this.server == null && var2.getServer() == null || this.server != null && this.server.equals(var2.getServer()))
         && this.port == var2.getPort()
         && (this.database == null && var2.getDatabase() == null || this.database != null && this.database.equals(var2.getDatabase()))
         && (this.user == null && var2.getUser() == null || this.user != null && this.user.equals(var2.getUser()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && (
            this.manualConnection == null && var2.getManualConnection() == null
               || this.manualConnection != null && this.manualConnection.equals(var2.getManualConnection())
         )
         && (
            this.additionalParams == null && var2.getAdditionalParams() == null
               || this.additionalParams != null && this.additionalParams.equals(var2.getAdditionalParams())
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
      if (this.getServer() != null) {
         var1 += this.getServer().hashCode();
      }

      var1 += this.getPort();
      if (this.getDatabase() != null) {
         var1 += this.getDatabase().hashCode();
      }

      if (this.getUser() != null) {
         var1 += this.getUser().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      if (this.getManualConnection() != null) {
         var1 += this.getManualConnection().hashCode();
      }

      if (this.getAdditionalParams() != null) {
         var1 += this.getAdditionalParams().hashCode();
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
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsConnectionParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("server");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "server"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("port");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "port"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("database");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "database"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("user");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "user"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("manualConnection");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "manualConnection"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("additionalParams");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "additionalParams"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
