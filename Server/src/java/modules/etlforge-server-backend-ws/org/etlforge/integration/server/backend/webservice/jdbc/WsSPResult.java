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

public class WsSPResult implements Serializable {
   private WsJdbcError returnValue;
   private WsResultSet resultSet;
   private WsSPParameter[] outputPrms;
   private String SPReturnValue;
   private int updateCount;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSPResult.class, true);

   public WsSPResult() {
   }

   public WsSPResult(WsJdbcError var1, WsResultSet var2, WsSPParameter[] var3, String var4, int var5) {
      this.returnValue = var1;
      this.resultSet = var2;
      this.outputPrms = var3;
      this.SPReturnValue = var4;
      this.updateCount = var5;
   }

   public WsJdbcError getReturnValue() {
      return this.returnValue;
   }

   public void setReturnValue(WsJdbcError var1) {
      this.returnValue = var1;
   }

   public WsResultSet getResultSet() {
      return this.resultSet;
   }

   public void setResultSet(WsResultSet var1) {
      this.resultSet = var1;
   }

   public WsSPParameter[] getOutputPrms() {
      return this.outputPrms;
   }

   public void setOutputPrms(WsSPParameter[] var1) {
      this.outputPrms = var1;
   }

   public String getSPReturnValue() {
      return this.SPReturnValue;
   }

   public void setSPReturnValue(String var1) {
      this.SPReturnValue = var1;
   }

   public int getUpdateCount() {
      return this.updateCount;
   }

   public void setUpdateCount(int var1) {
      this.updateCount = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSPResult)) {
         return false;
      }

      WsSPResult var2 = (WsSPResult)var1;
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
      boolean var3 = (this.returnValue == null && var2.getReturnValue() == null || this.returnValue != null && this.returnValue.equals(var2.getReturnValue()))
         && (this.resultSet == null && var2.getResultSet() == null || this.resultSet != null && this.resultSet.equals(var2.getResultSet()))
         && (this.outputPrms == null && var2.getOutputPrms() == null || this.outputPrms != null && Arrays.equals(this.outputPrms, var2.getOutputPrms()))
         && (this.SPReturnValue == null && var2.getSPReturnValue() == null || this.SPReturnValue != null && this.SPReturnValue.equals(var2.getSPReturnValue()))
         && this.updateCount == var2.getUpdateCount();
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
      if (this.getReturnValue() != null) {
         var1 += this.getReturnValue().hashCode();
      }

      if (this.getResultSet() != null) {
         var1 += this.getResultSet().hashCode();
      }

      if (this.getOutputPrms() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getOutputPrms()); var2++) {
            Object var3 = Array.get(this.getOutputPrms(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getSPReturnValue() != null) {
         var1 += this.getSPReturnValue().hashCode();
      }

      var1 += this.getUpdateCount();
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
      typeDesc.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("returnValue");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "returnValue"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("resultSet");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "resultSet"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsResultSet"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("outputPrms");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "outputPrms"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPParameter"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("SPReturnValue");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "SPReturnValue"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("updateCount");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "updateCount"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
