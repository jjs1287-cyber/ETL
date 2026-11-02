package com.etlforge.integration.server.implementation.webservice.projectadmin.backup;

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

public class WsGetBackupListResult implements Serializable {
   private WsProjectBackupDescriptor[] backups;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsGetBackupListResult.class, true);

   public WsGetBackupListResult() {
   }

   public WsGetBackupListResult(WsProjectBackupDescriptor[] var1) {
      this.backups = var1;
   }

   public WsProjectBackupDescriptor[] getBackups() {
      return this.backups;
   }

   public void setBackups(WsProjectBackupDescriptor[] var1) {
      this.backups = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsGetBackupListResult)) {
         return false;
      }

      WsGetBackupListResult var2 = (WsGetBackupListResult)var1;
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
      boolean var3 = this.backups == null && var2.getBackups() == null || this.backups != null && Arrays.equals(this.backups, var2.getBackups());
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
      if (this.getBackups() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getBackups()); var2++) {
            Object var3 = Array.get(this.getBackups(), var2);
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
      typeDesc.setXmlType(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "WsGetBackupListResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("backups");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "backups"));
      var0.setXmlType(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "WsProjectBackupDescriptor"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
