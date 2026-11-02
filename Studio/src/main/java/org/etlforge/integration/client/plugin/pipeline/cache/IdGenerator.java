package org.etlforge.integration.client.plugin.pipeline.cache;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

abstract class IdGenerator {
   static IdGenerator newGenerator() {
      return new IdGenerator.MD5_Implementation();
   }

   public abstract String getIdFromPluginName(String var1);

   @VisibleForTesting
   static final class MD5_Implementation extends IdGenerator {
      private final HashFunction md5 = Hashing.md5();

      @Override
      public String getIdFromPluginName(String var1) {
         return this.createIdFromName(var1);
      }

      private String createIdFromName(String var1) {
         return this.md5.hashBytes(var1.getBytes(Charsets.UTF_8)).toString();
      }
   }
}
