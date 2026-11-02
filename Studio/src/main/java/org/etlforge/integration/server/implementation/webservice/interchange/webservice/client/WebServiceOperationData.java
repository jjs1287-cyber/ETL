package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WebServiceOperationData implements Serializable {
   private String wsdlLocator;
   private String bindingName;
   private String operationName;
   private String operationInputName;
   private String operationOutputName;
   private String soapStyle;
   private String bodyUse;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WebServiceOperationData.class, true);

   public WebServiceOperationData() {
   }

   public WebServiceOperationData(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      this.wsdlLocator = var1;
      this.bindingName = var2;
      this.operationName = var3;
      this.operationInputName = var4;
      this.operationOutputName = var5;
      this.soapStyle = var6;
      this.bodyUse = var7;
   }

   public String getWsdlLocator() {
      return this.wsdlLocator;
   }

   public void setWsdlLocator(String var1) {
      this.wsdlLocator = var1;
   }

   public String getBindingName() {
      return this.bindingName;
   }

   public void setBindingName(String var1) {
      this.bindingName = var1;
   }

   public String getOperationName() {
      return this.operationName;
   }

   public void setOperationName(String var1) {
      this.operationName = var1;
   }

   public String getOperationInputName() {
      return this.operationInputName;
   }

   public void setOperationInputName(String var1) {
      this.operationInputName = var1;
   }

   public String getOperationOutputName() {
      return this.operationOutputName;
   }

   public void setOperationOutputName(String var1) {
      this.operationOutputName = var1;
   }

   public String getSoapStyle() {
      return this.soapStyle;
   }

   public void setSoapStyle(String var1) {
      this.soapStyle = var1;
   }

   public String getBodyUse() {
      return this.bodyUse;
   }

   public void setBodyUse(String var1) {
      this.bodyUse = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WebServiceOperationData)) {
         return false;
      }

      WebServiceOperationData var2 = (WebServiceOperationData)var1;
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
      boolean var3 = (this.wsdlLocator == null && var2.getWsdlLocator() == null || this.wsdlLocator != null && this.wsdlLocator.equals(var2.getWsdlLocator()))
         && (this.bindingName == null && var2.getBindingName() == null || this.bindingName != null && this.bindingName.equals(var2.getBindingName()))
         && (this.operationName == null && var2.getOperationName() == null || this.operationName != null && this.operationName.equals(var2.getOperationName()))
         && (
            this.operationInputName == null && var2.getOperationInputName() == null
               || this.operationInputName != null && this.operationInputName.equals(var2.getOperationInputName())
         )
         && (
            this.operationOutputName == null && var2.getOperationOutputName() == null
               || this.operationOutputName != null && this.operationOutputName.equals(var2.getOperationOutputName())
         )
         && (this.soapStyle == null && var2.getSoapStyle() == null || this.soapStyle != null && this.soapStyle.equals(var2.getSoapStyle()))
         && (this.bodyUse == null && var2.getBodyUse() == null || this.bodyUse != null && this.bodyUse.equals(var2.getBodyUse()));
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
      if (this.getWsdlLocator() != null) {
         var1 += this.getWsdlLocator().hashCode();
      }

      if (this.getBindingName() != null) {
         var1 += this.getBindingName().hashCode();
      }

      if (this.getOperationName() != null) {
         var1 += this.getOperationName().hashCode();
      }

      if (this.getOperationInputName() != null) {
         var1 += this.getOperationInputName().hashCode();
      }

      if (this.getOperationOutputName() != null) {
         var1 += this.getOperationOutputName().hashCode();
      }

      if (this.getSoapStyle() != null) {
         var1 += this.getSoapStyle().hashCode();
      }

      if (this.getBodyUse() != null) {
         var1 += this.getBodyUse().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceOperationData")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("wsdlLocator");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WsdlLocator"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("bindingName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "BindingName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationName");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "OperationName")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationInputName");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "OperationInputName")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationOutputName");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "OperationOutputName")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("soapStyle");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "SoapStyle"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("bodyUse");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "BodyUse"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
