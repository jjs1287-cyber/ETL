package org.etlforge.integration.plugin;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;
import org.etlforge.util.version.Version;

public final class PluginIdentifier {
   private final Pair<String, Version> data;

   public PluginIdentifier(String var1, Version var2) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "name cannot be empty");
      Preconditions.checkNotNull(var2, "version");
      this.data = Pair.of(var1, var2);
   }

   public String getName() {
      return this.data.first;
   }

   public Version getVersion() {
      return this.data.second;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof PluginIdentifier ? this.data.equals(((PluginIdentifier)var1).data) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.data.hashCode();
   }

   @Override
   public String toString() {
      return "[Name = " + this.getName() + " -- Version = " + this.getVersion() + "]";
   }
}
