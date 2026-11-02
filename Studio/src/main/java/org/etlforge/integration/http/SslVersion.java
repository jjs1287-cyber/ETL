package org.etlforge.integration.http;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import org.etlforge.collections.EnumSets;
import org.etlforge.system.EnumWithId;

public enum SslVersion implements EnumWithId<Integer> {
   TLSV1_SSLV3(0),
   TLSV1_ONLY(1),
   SSLV2(2),
   SSLV3_ONLY(3);

   private final int id;

   SslVersion(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }

   public boolean isSupported() {
      return this != SSLV2;
   }

   public static SslVersion defaultVersion() {
      SslVersion var0 = TLSV1_SSLV3;
      assert var0.isSupported();
      return var0;
   }

   public static ImmutableSet<SslVersion> getSupportedVersions() {
      return EnumSets.filteredSet(SslVersion.class, new Predicate<SslVersion>() {
         public boolean apply(SslVersion var1) {
            return var1.isSupported();
         }
      });
   }
}
