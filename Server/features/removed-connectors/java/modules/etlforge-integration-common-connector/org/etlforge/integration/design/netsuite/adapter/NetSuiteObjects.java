package org.etlforge.integration.design.netsuite.adapter;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignNotSupportedRuntimeException;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedDefinition;

public class NetSuiteObjects {
   private final boolean isStandardObject;
   private final NetSuiteSelectedObject selectedObject;
   private final boolean alwaysOneOf;
   public final List<NetSuiteObject> recordObjects = Lists.newArrayList();

   NetSuiteObjects(boolean var1, NetSuiteSelectedObject var2) {
      this(var1, var2, false);
   }

   NetSuiteObjects(boolean var1, NetSuiteSelectedObject var2, boolean var3) {
      this.isStandardObject = var1;
      this.selectedObject = var2;
      this.alwaysOneOf = var3;
   }

   public NetSuiteSelectedObject getNetSuiteSelectedObject() {
      return this.selectedObject;
   }

   public boolean getAlwaysOneOf() {
      return this.alwaysOneOf;
   }

   public boolean isSearchRow() {
      return this.selectedObject.responseIsSearchRow();
   }

   public boolean isSearch() {
      return this.selectedObject.isSearch();
   }

   void add(NetSuiteObject var1) {
      this.recordObjects.add(var1);
   }

   public List<NetSuiteObject> getRecordObjects() {
      return this.recordObjects;
   }

   public NetSuiteObject asRequestObject() {
      if (this.recordObjects.size() != 1) {
         String var1 = "not supported for multiple with request objects:" + this.recordObjects;
         throw new ConnectorNetSuiteDesignNotSupportedRuntimeException(var1);
      } else {
         return this.recordObjects.get(0);
      }
   }

   public NetSuiteObject asCustomObject() {
      if (this.isStandardObject()) {
         return null;
      } else if (this.recordObjects.size() != 1) {
         String var1 = "not supported for multiple with custom objects:" + this.recordObjects;
         throw new ConnectorNetSuiteDesignNotSupportedRuntimeException(var1);
      } else {
         return this.recordObjects.get(0);
      }
   }

   public boolean isStandardObject() {
      return this.isStandardObject;
   }

   public boolean hasCustomFieldImplemented(NetSuiteCustomFieldImplementedDefinition var1) {
      for (NetSuiteObject var3 : this.recordObjects) {
         if (this.hasCustomFieldImplemented(var1, var3)) {
            return true;
         }
      }

      return false;
   }

   private boolean hasCustomFieldImplemented(NetSuiteCustomFieldImplementedDefinition var1, NetSuiteObject var2) {
      if (!this.isStandardObject) {
         return true;
      }

      String var3 = var2.getTypeLocalName();
      return var1.hasCustomFieldImplementation(var3);
   }
}
