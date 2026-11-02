package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public class Breakpoint implements Serializable {
   private BreakLocation location;
   private String conditionScript;
   private WsKeyValuePairs conditionVariables;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(Breakpoint.class, true);

   public Breakpoint() {
   }

   public Breakpoint(BreakLocation var1, String var2, WsKeyValuePairs var3) {
      this.location = var1;
      this.conditionScript = var2;
      this.conditionVariables = var3;
   }

   public BreakLocation getLocation() {
      return this.location;
   }

   public void setLocation(BreakLocation var1) {
      this.location = var1;
   }

   public String getConditionScript() {
      return this.conditionScript;
   }

   public void setConditionScript(String var1) {
      this.conditionScript = var1;
   }

   public WsKeyValuePairs getConditionVariables() {
      return this.conditionVariables;
   }

   public void setConditionVariables(WsKeyValuePairs var1) {
      this.conditionVariables = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof Breakpoint)) {
         return false;
      }

      Breakpoint var2 = (Breakpoint)var1;
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
      boolean var3 = (this.location == null && var2.getLocation() == null || this.location != null && this.location.equals(var2.getLocation()))
         && (
            this.conditionScript == null && var2.getConditionScript() == null
               || this.conditionScript != null && this.conditionScript.equals(var2.getConditionScript())
         )
         && (
            this.conditionVariables == null && var2.getConditionVariables() == null
               || this.conditionVariables != null && this.conditionVariables.equals(var2.getConditionVariables())
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
      if (this.getLocation() != null) {
         var1 += this.getLocation().hashCode();
      }

      if (this.getConditionScript() != null) {
         var1 += this.getConditionScript().hashCode();
      }

      if (this.getConditionVariables() != null) {
         var1 += this.getConditionVariables().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Breakpoint")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("location");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Location"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "BreakLocation")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("conditionScript");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "ConditionScript")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("conditionVariables");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "ConditionVariables")
      );
      var0.setXmlType(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
