package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

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

public class InterchangeDataRow implements Serializable {
   private String[] fields;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(InterchangeDataRow.class, true);

   public InterchangeDataRow() {
   }

   public InterchangeDataRow(String[] var1) {
      this.fields = var1;
   }

   public String[] getFields() {
      return this.fields;
   }

   public void setFields(String[] var1) {
      this.fields = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof InterchangeDataRow)) {
         return false;
      }

      InterchangeDataRow var2 = (InterchangeDataRow)var1;
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
      boolean var3 = this.fields == null && var2.getFields() == null || this.fields != null && Arrays.equals(this.fields, var2.getFields());
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
      if (this.getFields() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getFields()); var2++) {
            Object var3 = Array.get(this.getFields(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataRow")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("fields");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "fields"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
