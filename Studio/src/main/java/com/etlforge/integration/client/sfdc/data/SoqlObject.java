package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;
import org.etlforge.xml.ExpandedName;

public final class SoqlObject {
   private final String queryString;
   private final ExpandedName objectName;
   private final boolean queryAll;

   public SoqlObject(String var1, ExpandedName var2, boolean var3) {
      this.queryString = ArgCheck.checkNotEmpty(var1, "queryString");
      this.objectName = Preconditions.checkNotNull(var2, "objectName");
      this.queryAll = var3;
   }

   public String getQueryString() {
      return this.queryString;
   }

   public ExpandedName getObjectName() {
      return this.objectName;
   }

   public boolean isQueryAll() {
      return this.queryAll;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.queryString, this.objectName, this.queryAll);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SoqlObject)) {
         return false;
      }

      SoqlObject var2 = (SoqlObject)var1;
      return this.queryString.equals(var2.queryString) && this.objectName.equals(var2.objectName) && this.queryAll == var2.queryAll;
   }
}
