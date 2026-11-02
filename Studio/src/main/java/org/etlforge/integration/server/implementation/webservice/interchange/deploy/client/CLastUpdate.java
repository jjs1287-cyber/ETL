package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CLastUpdate implements Serializable {
   private CIntegrationEntity item;
   private String userGuid;
   private String userLogin;
   private String modifiedDate;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CLastUpdate.class, true);

   public CLastUpdate() {
   }

   public CLastUpdate(CIntegrationEntity var1, String var2, String var3, String var4) {
      this.item = var1;
      this.userGuid = var2;
      this.userLogin = var3;
      this.modifiedDate = var4;
   }

   public CIntegrationEntity getItem() {
      return this.item;
   }

   public void setItem(CIntegrationEntity var1) {
      this.item = var1;
   }

   public String getUserGuid() {
      return this.userGuid;
   }

   public void setUserGuid(String var1) {
      this.userGuid = var1;
   }

   public String getUserLogin() {
      return this.userLogin;
   }

   public void setUserLogin(String var1) {
      this.userLogin = var1;
   }

   public String getModifiedDate() {
      return this.modifiedDate;
   }

   public void setModifiedDate(String var1) {
      this.modifiedDate = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CLastUpdate)) {
         return false;
      }

      CLastUpdate var2 = (CLastUpdate)var1;
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
      boolean var3 = (this.item == null && var2.getItem() == null || this.item != null && this.item.equals(var2.getItem()))
         && (this.userGuid == null && var2.getUserGuid() == null || this.userGuid != null && this.userGuid.equals(var2.getUserGuid()))
         && (this.userLogin == null && var2.getUserLogin() == null || this.userLogin != null && this.userLogin.equals(var2.getUserLogin()))
         && (this.modifiedDate == null && var2.getModifiedDate() == null || this.modifiedDate != null && this.modifiedDate.equals(var2.getModifiedDate()));
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
      if (this.getItem() != null) {
         var1 += this.getItem().hashCode();
      }

      if (this.getUserGuid() != null) {
         var1 += this.getUserGuid().hashCode();
      }

      if (this.getUserLogin() != null) {
         var1 += this.getUserLogin().hashCode();
      }

      if (this.getModifiedDate() != null) {
         var1 += this.getModifiedDate().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CLastUpdate"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("item");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Item"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CIntegrationEntity")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "UserGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userLogin");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "UserLogin"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("modifiedDate");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ModifiedDate"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
