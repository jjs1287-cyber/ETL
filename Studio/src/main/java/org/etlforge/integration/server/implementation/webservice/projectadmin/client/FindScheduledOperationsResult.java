package org.etlforge.integration.server.implementation.webservice.projectadmin.client;

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

public class FindScheduledOperationsResult implements Serializable {
   private ScheduledOperation[] scheduledOperations;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(FindScheduledOperationsResult.class, true);

   public FindScheduledOperationsResult() {
   }

   public FindScheduledOperationsResult(ScheduledOperation[] var1) {
      this.scheduledOperations = var1;
   }

   public ScheduledOperation[] getScheduledOperations() {
      return this.scheduledOperations;
   }

   public void setScheduledOperations(ScheduledOperation[] var1) {
      this.scheduledOperations = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof FindScheduledOperationsResult)) {
         return false;
      }

      FindScheduledOperationsResult var2 = (FindScheduledOperationsResult)var1;
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
      boolean var3 = this.scheduledOperations == null && var2.getScheduledOperations() == null
         || this.scheduledOperations != null && Arrays.equals(this.scheduledOperations, var2.getScheduledOperations());
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
      if (this.getScheduledOperations() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getScheduledOperations()); var2++) {
            Object var3 = Array.get(this.getScheduledOperations(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperationsResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("scheduledOperations");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduledOperations"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduledOperation"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
