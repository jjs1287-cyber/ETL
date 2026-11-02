package org.etlforge.integration.data.access;

import java.util.EnumSet;

public enum Permission {
   OWNER(8, "Owner"),
   READ(4, "Read"),
   WRITE(2, "Write"),
   EXECUTE(1, "Execute"),
   DELETE(16, "Delete"),
   CHANGE_PERMISSIONS(32, "Change permissions");

   private static final EnumSet<Permission> FULL_ACCESS = EnumSet.of(READ, WRITE, EXECUTE, DELETE, CHANGE_PERMISSIONS);
   private final int code;
   private final String shortString;

   public static EnumSet<Permission> getAllNonOwnerPermissions() {
      return FULL_ACCESS.clone();
   }

   public static boolean isFullAccess(EnumSet<Permission> var0) {
      return var0.contains(OWNER) || FULL_ACCESS.equals(var0);
   }

   @Override
   public String toString() {
      return this.shortString;
   }

   public static EnumSet<Permission> fromBitCode(int var0) {
      EnumSet var1 = EnumSet.noneOf(Permission.class);

      for (Permission var5 : values()) {
         if ((var0 & var5.code) != 0) {
            var1.add(var5);
         }
      }

      return var1;
   }

   public static int toBitCode(EnumSet<Permission> var0) {
      int var1 = 0;

      for (Permission var3 : var0) {
         var1 |= var3.code;
      }

      return var1;
   }

   Permission(int var3, String var4) {
      this.code = var3;
      this.shortString = var4;
   }
}
