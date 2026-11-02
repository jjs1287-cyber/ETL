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

public class CInterchangeProjects implements Serializable {
   private CInterchangeProject[] interchangeProjects;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CInterchangeProjects.class, true);

   public CInterchangeProjects() {
   }

   public CInterchangeProjects(CInterchangeProject[] var1) {
      this.interchangeProjects = var1;
   }

   public CInterchangeProject[] getInterchangeProjects() {
      return this.interchangeProjects;
   }

   public void setInterchangeProjects(CInterchangeProject[] var1) {
      this.interchangeProjects = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CInterchangeProjects)) {
         return false;
      }

      CInterchangeProjects var2 = (CInterchangeProjects)var1;
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
      boolean var3 = this.interchangeProjects == null && var2.getInterchangeProjects() == null
         || this.interchangeProjects != null && Arrays.equals(this.interchangeProjects, var2.getInterchangeProjects());
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
      if (this.getInterchangeProjects() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getInterchangeProjects()); var2++) {
            Object var3 = Array.get(this.getInterchangeProjects(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CInterchangeProjects")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("interchangeProjects");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeProjects")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CInterchangeProject")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
