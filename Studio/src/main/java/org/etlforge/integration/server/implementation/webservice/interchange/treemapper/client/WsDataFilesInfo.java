package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

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

public class WsDataFilesInfo implements Serializable {
   private String guid;
   private WsDataFileInfo[] dataFiles;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsDataFilesInfo.class, true);

   public WsDataFilesInfo() {
   }

   public WsDataFilesInfo(String var1, WsDataFileInfo[] var2) {
      this.guid = var1;
      this.dataFiles = var2;
   }

   public String getGuid() {
      return this.guid;
   }

   public void setGuid(String var1) {
      this.guid = var1;
   }

   public WsDataFileInfo[] getDataFiles() {
      return this.dataFiles;
   }

   public void setDataFiles(WsDataFileInfo[] var1) {
      this.dataFiles = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsDataFilesInfo)) {
         return false;
      }

      WsDataFilesInfo var2 = (WsDataFilesInfo)var1;
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
      boolean var3 = (this.guid == null && var2.getGuid() == null || this.guid != null && this.guid.equals(var2.getGuid()))
         && (this.dataFiles == null && var2.getDataFiles() == null || this.dataFiles != null && Arrays.equals(this.dataFiles, var2.getDataFiles()));
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
      if (this.getGuid() != null) {
         var1 += this.getGuid().hashCode();
      }

      if (this.getDataFiles() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getDataFiles()); var2++) {
            Object var3 = Array.get(this.getDataFiles(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataFilesInfo")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("guid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Guid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dataFiles");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DataFiles"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataFileInfo")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
