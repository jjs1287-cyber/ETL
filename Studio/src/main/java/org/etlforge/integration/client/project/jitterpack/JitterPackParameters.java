package org.etlforge.integration.client.project.jitterpack;

public abstract class JitterPackParameters {
   private final char[] password;

   public JitterPackParameters(char[] var1) {
      this.password = clone(var1);
   }

   private static char[] clone(char[] var0) {
      return var0 != null ? (char[])var0.clone() : null;
   }

   public final char[] getPassword() {
      return clone(this.password);
   }
}
