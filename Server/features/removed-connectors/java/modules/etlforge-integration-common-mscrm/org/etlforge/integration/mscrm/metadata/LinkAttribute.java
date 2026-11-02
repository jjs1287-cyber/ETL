package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Objects;
import java.io.Serializable;
import org.etlforge.system.ArgCheck;

public final class LinkAttribute implements Serializable {
   private static final long serialVersionUID = -477517588809235412L;
   private final String entityName;
   private final String attributeName;

   public LinkAttribute(String var1, String var2) {
      this.entityName = ArgCheck.checkNotEmpty(var1, "entityName");
      this.attributeName = ArgCheck.checkNotEmpty(var2, "attributeName");
   }

   public String getEntityName() {
      return this.entityName;
   }

   public String getAttributeName() {
      return this.attributeName;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.entityName, this.attributeName);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof LinkAttribute)) {
         return false;
      }

      LinkAttribute var2 = (LinkAttribute)var1;
      return this.entityName.equals(var2.entityName) && this.attributeName.equals(var2.attributeName);
   }

   @Override
   public String toString() {
      return this.entityName + "." + this.attributeName;
   }
}
