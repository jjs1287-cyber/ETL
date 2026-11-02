package org.etlforge.integration.server.implementation.webservice.sfdc;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsPicklistEntry implements Serializable {
   private String label;
   private String value;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsPicklistEntry.class, true);

   public WsPicklistEntry() {
   }

   public WsPicklistEntry(String var1, String var2) {
      this.label = var1;
      this.value = var2;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsPicklistEntry)) {
         return false;
      }

      WsPicklistEntry var2 = (WsPicklistEntry)var1;
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
      boolean var3 = (this.label == null && var2.getLabel() == null || this.label != null && this.label.equals(var2.getLabel()))
         && (this.value == null && var2.getValue() == null || this.value != null && this.value.equals(var2.getValue()));
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
      if (this.getLabel() != null) {
         var1 += this.getLabel().hashCode();
      }

      if (this.getValue() != null) {
         var1 += this.getValue().hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsPicklistEntry"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("label");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "label"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("value");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "value"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
