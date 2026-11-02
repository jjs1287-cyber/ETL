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

public class WsNetSuiteCustomFieldsNew implements Serializable {
   private WsNetSuiteCustomFieldNew[] customFieldList;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsNetSuiteCustomFieldsNew.class, true);

   public WsNetSuiteCustomFieldsNew() {
   }

   public WsNetSuiteCustomFieldsNew(WsNetSuiteCustomFieldNew[] var1) {
      this.customFieldList = var1;
   }

   public WsNetSuiteCustomFieldNew[] getCustomFieldList() {
      return this.customFieldList;
   }

   public void setCustomFieldList(WsNetSuiteCustomFieldNew[] var1) {
      this.customFieldList = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsNetSuiteCustomFieldsNew)) {
         return false;
      }

      WsNetSuiteCustomFieldsNew var2 = (WsNetSuiteCustomFieldsNew)var1;
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
      boolean var3 = this.customFieldList == null && var2.getCustomFieldList() == null
         || this.customFieldList != null && Arrays.equals(this.customFieldList, var2.getCustomFieldList());
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
      if (this.getCustomFieldList() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getCustomFieldList()); var2++) {
            Object var3 = Array.get(this.getCustomFieldList(), var2);
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
      typeDesc.setXmlType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldsNew"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("customFieldList");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "CustomFieldList"));
      var0.setXmlType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldNew"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
