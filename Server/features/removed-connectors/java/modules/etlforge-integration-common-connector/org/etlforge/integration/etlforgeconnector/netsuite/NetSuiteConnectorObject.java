package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import javax.annotation.Nullable;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.string.StringLimiter;

public final class NetSuiteConnectorObject implements ConnectorObject {
   private final NetSuiteConnectorObjectCategory objectCategory;
   private final String name;
   private final String key;
   private final String description;

   public NetSuiteConnectorObject(String var1) {
      this(var1, null);
   }

   public NetSuiteConnectorObject(String var1, @Nullable String var2) {
      this.key = ArgCheck.checkNotEmpty(var1);
      this.description = Strings.nullToEmpty(var2);
      this.objectCategory = NetSuiteConnectorObjectCategory.fromKey(this.key);
      this.name = NetSuiteConnectorObjectCategory.getName(this.key);
   }

   public static NetSuiteConnectorObject castAsNetSuiteConnectorObject(ConnectorObject var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof NetSuiteConnectorObject) {
         return (NetSuiteConnectorObject)var0;
      } else {
         throw new RuntimeException(var0 + " cannot be casted as a netsuite object");
      }
   }

   public String getKey() {
      return this.key;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   public NetSuiteConnectorObjectCategory getCategory() {
      return this.objectCategory;
   }

   public boolean isCustomObject() {
      return this.objectCategory == NetSuiteConnectorObjectCategory.CustomObject;
   }

   public boolean isCustomSearch() {
      return this.objectCategory == NetSuiteConnectorObjectCategory.ItemSearchObject || this.objectCategory == NetSuiteConnectorObjectCategory.TranSearchObject;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.key, this.description);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof NetSuiteConnectorObject)) {
         return false;
      }

      NetSuiteConnectorObject var2 = (NetSuiteConnectorObject)var1;
      return this.key.equals(var2.key) && this.description.equals(var2.description);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("name", this.name)
         .add("description", StringLimiter.limit(this.description, 100))
         .add("category", this.objectCategory)
         .toString();
   }
}
