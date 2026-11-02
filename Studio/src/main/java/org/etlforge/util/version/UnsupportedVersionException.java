package org.etlforge.util.version;

public final class UnsupportedVersionException extends Exception {
   private final Version version;

   public UnsupportedVersionException(Version var1) {
      this.version = var1;
   }

   public UnsupportedVersionException(Version var1, String var2, Throwable var3) {
      super(var2, var3);
      this.version = var1;
   }

   public UnsupportedVersionException(Version var1, String var2) {
      super(var2);
      this.version = var1;
   }

   public UnsupportedVersionException(Version var1, Throwable var2) {
      super(var2);
      this.version = var1;
   }

   public Version getVersion() {
      return this.version;
   }
}
