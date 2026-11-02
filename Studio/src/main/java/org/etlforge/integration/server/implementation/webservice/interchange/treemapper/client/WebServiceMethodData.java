package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WebServiceMethodData implements Serializable {
   private String wsGuid;
   private String wsdlGuid;
   private String binding;
   private String opName;
   private String opInput;
   private String opOutput;
   private String opStyle;
   private String soapBodyUse;
   private boolean isTarget;
   private boolean isWSRequest;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WebServiceMethodData.class, true);

   public WebServiceMethodData() {
   }

   public WebServiceMethodData(
      String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9, boolean var10
   ) {
      this.wsGuid = var1;
      this.wsdlGuid = var2;
      this.binding = var3;
      this.opName = var4;
      this.opInput = var5;
      this.opOutput = var6;
      this.opStyle = var7;
      this.soapBodyUse = var8;
      this.isTarget = var9;
      this.isWSRequest = var10;
   }

   public String getWsGuid() {
      return this.wsGuid;
   }

   public void setWsGuid(String var1) {
      this.wsGuid = var1;
   }

   public String getWsdlGuid() {
      return this.wsdlGuid;
   }

   public void setWsdlGuid(String var1) {
      this.wsdlGuid = var1;
   }

   public String getBinding() {
      return this.binding;
   }

   public void setBinding(String var1) {
      this.binding = var1;
   }

   public String getOpName() {
      return this.opName;
   }

   public void setOpName(String var1) {
      this.opName = var1;
   }

   public String getOpInput() {
      return this.opInput;
   }

   public void setOpInput(String var1) {
      this.opInput = var1;
   }

   public String getOpOutput() {
      return this.opOutput;
   }

   public void setOpOutput(String var1) {
      this.opOutput = var1;
   }

   public String getOpStyle() {
      return this.opStyle;
   }

   public void setOpStyle(String var1) {
      this.opStyle = var1;
   }

   public String getSoapBodyUse() {
      return this.soapBodyUse;
   }

   public void setSoapBodyUse(String var1) {
      this.soapBodyUse = var1;
   }

   public boolean isIsTarget() {
      return this.isTarget;
   }

   public void setIsTarget(boolean var1) {
      this.isTarget = var1;
   }

   public boolean isIsWSRequest() {
      return this.isWSRequest;
   }

   public void setIsWSRequest(boolean var1) {
      this.isWSRequest = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WebServiceMethodData)) {
         return false;
      }

      WebServiceMethodData var2 = (WebServiceMethodData)var1;
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
      boolean var3 = (this.wsGuid == null && var2.getWsGuid() == null || this.wsGuid != null && this.wsGuid.equals(var2.getWsGuid()))
         && (this.wsdlGuid == null && var2.getWsdlGuid() == null || this.wsdlGuid != null && this.wsdlGuid.equals(var2.getWsdlGuid()))
         && (this.binding == null && var2.getBinding() == null || this.binding != null && this.binding.equals(var2.getBinding()))
         && (this.opName == null && var2.getOpName() == null || this.opName != null && this.opName.equals(var2.getOpName()))
         && (this.opInput == null && var2.getOpInput() == null || this.opInput != null && this.opInput.equals(var2.getOpInput()))
         && (this.opOutput == null && var2.getOpOutput() == null || this.opOutput != null && this.opOutput.equals(var2.getOpOutput()))
         && (this.opStyle == null && var2.getOpStyle() == null || this.opStyle != null && this.opStyle.equals(var2.getOpStyle()))
         && (this.soapBodyUse == null && var2.getSoapBodyUse() == null || this.soapBodyUse != null && this.soapBodyUse.equals(var2.getSoapBodyUse()))
         && this.isTarget == var2.isIsTarget()
         && this.isWSRequest == var2.isIsWSRequest();
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
      if (this.getWsGuid() != null) {
         var1 += this.getWsGuid().hashCode();
      }

      if (this.getWsdlGuid() != null) {
         var1 += this.getWsdlGuid().hashCode();
      }

      if (this.getBinding() != null) {
         var1 += this.getBinding().hashCode();
      }

      if (this.getOpName() != null) {
         var1 += this.getOpName().hashCode();
      }

      if (this.getOpInput() != null) {
         var1 += this.getOpInput().hashCode();
      }

      if (this.getOpOutput() != null) {
         var1 += this.getOpOutput().hashCode();
      }

      if (this.getOpStyle() != null) {
         var1 += this.getOpStyle().hashCode();
      }

      if (this.getSoapBodyUse() != null) {
         var1 += this.getSoapBodyUse().hashCode();
      }

      var1 += (this.isIsTarget() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += (this.isIsWSRequest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WebServiceMethodData")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("wsGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("wsdlGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsdlGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("binding");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Binding"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("opName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "OpName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("opInput");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "OpInput"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("opOutput");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "OpOutput"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("opStyle");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "OpStyle"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("soapBodyUse");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "SoapBodyUse"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isTarget");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "IsTarget"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isWSRequest");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "IsWSRequest"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
