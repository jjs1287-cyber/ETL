package org.etlforge.integration.server.backend.webservice.jdbc;

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

public class WsSPParams implements Serializable {
   private String sourceGuid;
   private String targetGuid;
   private WsConnectionParams connectionParams;
   private String spName;
   private boolean autoCommit;
   private String[] spParameters;
   private int maxNumberOfRows;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSPParams.class, true);

   public WsSPParams() {
   }

   public WsSPParams(String var1, String var2, WsConnectionParams var3, String var4, boolean var5, String[] var6, int var7) {
      this.sourceGuid = var1;
      this.targetGuid = var2;
      this.connectionParams = var3;
      this.spName = var4;
      this.autoCommit = var5;
      this.spParameters = var6;
      this.maxNumberOfRows = var7;
   }

   public String getSourceGuid() {
      return this.sourceGuid;
   }

   public void setSourceGuid(String var1) {
      this.sourceGuid = var1;
   }

   public String getTargetGuid() {
      return this.targetGuid;
   }

   public void setTargetGuid(String var1) {
      this.targetGuid = var1;
   }

   public WsConnectionParams getConnectionParams() {
      return this.connectionParams;
   }

   public void setConnectionParams(WsConnectionParams var1) {
      this.connectionParams = var1;
   }

   public String getSpName() {
      return this.spName;
   }

   public void setSpName(String var1) {
      this.spName = var1;
   }

   public boolean isAutoCommit() {
      return this.autoCommit;
   }

   public void setAutoCommit(boolean var1) {
      this.autoCommit = var1;
   }

   public String[] getSpParameters() {
      return this.spParameters;
   }

   public void setSpParameters(String[] var1) {
      this.spParameters = var1;
   }

   public int getMaxNumberOfRows() {
      return this.maxNumberOfRows;
   }

   public void setMaxNumberOfRows(int var1) {
      this.maxNumberOfRows = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSPParams)) {
         return false;
      }

      WsSPParams var2 = (WsSPParams)var1;
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
      boolean var3 = (this.sourceGuid == null && var2.getSourceGuid() == null || this.sourceGuid != null && this.sourceGuid.equals(var2.getSourceGuid()))
         && (this.targetGuid == null && var2.getTargetGuid() == null || this.targetGuid != null && this.targetGuid.equals(var2.getTargetGuid()))
         && (
            this.connectionParams == null && var2.getConnectionParams() == null
               || this.connectionParams != null && this.connectionParams.equals(var2.getConnectionParams())
         )
         && (this.spName == null && var2.getSpName() == null || this.spName != null && this.spName.equals(var2.getSpName()))
         && this.autoCommit == var2.isAutoCommit()
         && (
            this.spParameters == null && var2.getSpParameters() == null
               || this.spParameters != null && Arrays.equals(this.spParameters, var2.getSpParameters())
         )
         && this.maxNumberOfRows == var2.getMaxNumberOfRows();
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
      if (this.getSourceGuid() != null) {
         var1 += this.getSourceGuid().hashCode();
      }

      if (this.getTargetGuid() != null) {
         var1 += this.getTargetGuid().hashCode();
      }

      if (this.getConnectionParams() != null) {
         var1 += this.getConnectionParams().hashCode();
      }

      if (this.getSpName() != null) {
         var1 += this.getSpName().hashCode();
      }

      var1 += (this.isAutoCommit() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getSpParameters() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getSpParameters()); var2++) {
            Object var3 = Array.get(this.getSpParameters(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      var1 += this.getMaxNumberOfRows();
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
      typeDesc.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sourceGuid");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "sourceGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetGuid");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "targetGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("connectionParams");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "connectionParams"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsConnectionParams"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("spName");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "spName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoCommit");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "autoCommit"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("spParameters");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "spParameters"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("maxNumberOfRows");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "maxNumberOfRows"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
