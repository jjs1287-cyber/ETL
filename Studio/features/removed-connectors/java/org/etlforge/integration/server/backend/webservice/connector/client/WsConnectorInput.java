package org.etlforge.integration.server.backend.webservice.connector.client;

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

public class WsConnectorInput implements Serializable {
   private WsEndpointParameters endpointParameters;
   private String[] selectedObjectNames;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorInput.class, true);

   public WsConnectorInput() {
   }

   public WsConnectorInput(WsEndpointParameters var1, String[] var2) {
      this.endpointParameters = var1;
      this.selectedObjectNames = var2;
   }

   public WsEndpointParameters getEndpointParameters() {
      return this.endpointParameters;
   }

   public void setEndpointParameters(WsEndpointParameters var1) {
      this.endpointParameters = var1;
   }

   public String[] getSelectedObjectNames() {
      return this.selectedObjectNames;
   }

   public void setSelectedObjectNames(String[] var1) {
      this.selectedObjectNames = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorInput)) {
         return false;
      }

      WsConnectorInput var2 = (WsConnectorInput)var1;
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
      boolean var3 = (
            this.endpointParameters == null && var2.getEndpointParameters() == null
               || this.endpointParameters != null && this.endpointParameters.equals(var2.getEndpointParameters())
         )
         && (
            this.selectedObjectNames == null && var2.getSelectedObjectNames() == null
               || this.selectedObjectNames != null && Arrays.equals(this.selectedObjectNames, var2.getSelectedObjectNames())
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
      if (this.getEndpointParameters() != null) {
         var1 += this.getEndpointParameters().hashCode();
      }

      if (this.getSelectedObjectNames() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getSelectedObjectNames()); var2++) {
            Object var3 = Array.get(this.getSelectedObjectNames(), var2);
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("endpointParameters");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("selectedObjectNames");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "SelectedObjectNames"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
