package org.etlforge.application;

import org.etlforge.util.version.BuildVersion;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class VersionFactory {
   public static Version fromNumber(long var0) throws IllegalVersionException {
      return BuildVersion.fromNumber(var0);
   }

   public static Version fromString(String var0) throws IllegalVersionException {
      return BuildVersion.fromString(var0);
   }

   public static Version newVersion(int var0, int var1, int var2) {
      return newVersion(var0, var1, var2, 0);
   }

   public static Version newVersion(int var0, int var1, int var2, int var3) {
      return new BuildVersion(var0, var1, var2, var3);
   }

   public static Version newVersion(int var0, int var1, int var2, int var3, String var4) {
      return new BuildVersion(var0, var1, var2, var3, var4);
   }

   private VersionFactory() {
   }
}
