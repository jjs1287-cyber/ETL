package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WebServiceJtr implements Serializable {
   private String inputJtr;
   private String outputJtr;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WebServiceJtr.class, true);

   public WebServiceJtr() {
   }

   public WebServiceJtr(String var1, String var2) {
      this.inputJtr = var1;
      this.outputJtr = var2;
   }

   public String getInputJtr() {
      return this.inputJtr;
   }

   public void setInputJtr(String var1) {
      this.inputJtr = var1;
   }

   public String getOutputJtr() {
      return this.outputJtr;
   }

   public void setOutputJtr(String var1) {
      this.outputJtr = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WebServiceJtr)) {
         return false;
      }

      WebServiceJtr var2 = (WebServiceJtr)var1;
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
      boolean var3 = (this.inputJtr == null && var2.getInputJtr() == null || this.inputJtr != null && this.inputJtr.equals(var2.getInputJtr()))
         && (this.outputJtr == null && var2.getOutputJtr() == null || this.outputJtr != null && this.outputJtr.equals(var2.getOutputJtr()));
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
      if (this.getInputJtr() != null) {
         var1 += this.getInputJtr().hashCode();
      }

      if (this.getOutputJtr() != null) {
         var1 += this.getOutputJtr().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceJtr")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("inputJtr");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "InputJtr"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("outputJtr");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "OutputJtr"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
