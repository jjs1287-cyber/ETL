package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

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

public class CWsdlFiles implements Serializable {
   private CWsdlFile[] wsdlFiles;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CWsdlFiles.class, true);

   public CWsdlFiles() {
   }

   public CWsdlFiles(CWsdlFile[] var1) {
      this.wsdlFiles = var1;
   }

   public CWsdlFile[] getWsdlFiles() {
      return this.wsdlFiles;
   }

   public void setWsdlFiles(CWsdlFile[] var1) {
      this.wsdlFiles = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CWsdlFiles)) {
         return false;
      }

      CWsdlFiles var2 = (CWsdlFiles)var1;
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
      boolean var3 = this.wsdlFiles == null && var2.getWsdlFiles() == null || this.wsdlFiles != null && Arrays.equals(this.wsdlFiles, var2.getWsdlFiles());
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
      if (this.getWsdlFiles() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getWsdlFiles()); var2++) {
            Object var3 = Array.get(this.getWsdlFiles(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFiles")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("wsdlFiles");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WsdlFiles"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
