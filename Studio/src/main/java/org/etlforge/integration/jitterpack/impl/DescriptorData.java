package org.etlforge.integration.jitterpack.impl;

import java.util.Properties;
import org.etlforge.collections.Pair;

final class DescriptorData {
   private final Pair<String, Properties> data;
   private final boolean encrypted;

   public DescriptorData(String var1, Properties var2, boolean var3) {
      this.data = Pair.of(var1, var2);
      this.encrypted = var3;
   }

   public String getProjectFolderName() {
      return this.data.first;
   }

   public Properties getProperties() {
      return this.data.second;
   }

   public boolean isEncrypted() {
      return this.encrypted;
   }
}
