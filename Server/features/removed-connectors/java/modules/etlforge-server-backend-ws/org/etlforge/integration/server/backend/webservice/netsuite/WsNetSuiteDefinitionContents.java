package org.etlforge.integration.server.backend.webservice.netsuite;

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
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;

public class WsNetSuiteDefinitionContents implements Serializable {
   private WsStatus status;
   private String configXml;
   private String netsuiteWsdlKey;
   private String[] wsdlConnentsKey;
   private String[] wsdlConnents;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsNetSuiteDefinitionContents.class, true);

   public WsNetSuiteDefinitionContents() {
   }

   public WsNetSuiteDefinitionContents(WsStatus var1, String var2, String var3, String[] var4, String[] var5) {
      this.status = var1;
      this.configXml = var2;
      this.netsuiteWsdlKey = var3;
      this.wsdlConnentsKey = var4;
      this.wsdlConnents = var5;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public String getConfigXml() {
      return this.configXml;
   }

   public void setConfigXml(String var1) {
      this.configXml = var1;
   }

   public String getNetsuiteWsdlKey() {
      return this.netsuiteWsdlKey;
   }

   public void setNetsuiteWsdlKey(String var1) {
      this.netsuiteWsdlKey = var1;
   }

   public String[] getWsdlConnentsKey() {
      return this.wsdlConnentsKey;
   }

   public void setWsdlConnentsKey(String[] var1) {
      this.wsdlConnentsKey = var1;
   }

   public String[] getWsdlConnents() {
      return this.wsdlConnents;
   }

   public void setWsdlConnents(String[] var1) {
      this.wsdlConnents = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsNetSuiteDefinitionContents)) {
         return false;
      }

      WsNetSuiteDefinitionContents var2 = (WsNetSuiteDefinitionContents)var1;
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
      boolean var3 = (this.status == null && var2.getStatus() == null || this.status != null && this.status.equals(var2.getStatus()))
         && (this.configXml == null && var2.getConfigXml() == null || this.configXml != null && this.configXml.equals(var2.getConfigXml()))
         && (
            this.netsuiteWsdlKey == null && var2.getNetsuiteWsdlKey() == null
               || this.netsuiteWsdlKey != null && this.netsuiteWsdlKey.equals(var2.getNetsuiteWsdlKey())
         )
         && (
            this.wsdlConnentsKey == null && var2.getWsdlConnentsKey() == null
               || this.wsdlConnentsKey != null && Arrays.equals(this.wsdlConnentsKey, var2.getWsdlConnentsKey())
         )
         && (
            this.wsdlConnents == null && var2.getWsdlConnents() == null
               || this.wsdlConnents != null && Arrays.equals(this.wsdlConnents, var2.getWsdlConnents())
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
      if (this.getStatus() != null) {
         var1 += this.getStatus().hashCode();
      }

      if (this.getConfigXml() != null) {
         var1 += this.getConfigXml().hashCode();
      }

      if (this.getNetsuiteWsdlKey() != null) {
         var1 += this.getNetsuiteWsdlKey().hashCode();
      }

      if (this.getWsdlConnentsKey() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getWsdlConnentsKey()); var2++) {
            Object var3 = Array.get(this.getWsdlConnentsKey(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getWsdlConnents() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getWsdlConnents()); var4++) {
            Object var5 = Array.get(this.getWsdlConnents(), var4);
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
      typeDesc.setXmlType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteDefinitionContents"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("configXml");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "ConfigXml"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netsuiteWsdlKey");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "NetsuiteWsdlKey"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("wsdlConnentsKey");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsdlConnentsKey"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("wsdlConnents");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsdlConnents"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
