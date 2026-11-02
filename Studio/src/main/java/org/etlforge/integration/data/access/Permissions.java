package org.etlforge.integration.data.access;

import java.util.EnumSet;

public final class Permissions {
   private EnumSet<Permission> allowed;
   private EnumSet<Permission> denied;
   private static final EnumSet<Permission> READ_SET = EnumSet.of(Permission.READ);

   public static Permissions fullControl() {
      return new Permissions(EnumSet.allOf(Permission.class), EnumSet.noneOf(Permission.class));
   }

   public Permissions() {
      this(EnumSet.noneOf(Permission.class), EnumSet.noneOf(Permission.class));
   }

   public Permissions(EnumSet<Permission> var1, EnumSet<Permission> var2) {
      this.allowed = var1 == null ? null : var1.clone();
      this.denied = var2 == null ? null : var2.clone();
      this.checkRead();
   }

   public Permissions(Permissions var1) {
      this(var1.allowed, var1.denied);
   }

   public Permissions(int var1, int var2) {
      this(Permission.fromBitCode(var1), Permission.fromBitCode(var2));
   }

   private Permissions(EnumSet<Permission> var1, EnumSet<Permission> var2, boolean var3) {
      this.allowed = var1;
      this.denied = var2;
      this.checkRead();
   }

   private void checkRead() {
      if (this.isDenied(Permission.READ)) {
         this.denied = Permission.getAllNonOwnerPermissions();
      } else if (!this.isAllowed(Permission.READ) && this.allowed != null) {
         this.allowed.removeAll(Permission.getAllNonOwnerPermissions());
      }

      if (this.isOwner()) {
         if (this.allowed == null) {
            this.allowed = EnumSet.of(Permission.READ);
         } else if (!this.allowed.contains(Permission.READ)) {
            this.allowed.add(Permission.READ);
         }

         if (this.isDenied(Permission.READ)) {
            this.denied.remove(Permission.READ);
         }
      }
   }

   public boolean isOwner() {
      return this.allowed != null && this.allowed.contains(Permission.OWNER);
   }

   public boolean isFullAccess() {
      return this.allowed != null && Permission.isFullAccess(this.allowed);
   }

   public boolean isAllowed(Permission var1) {
      return !this.isDenied(var1) && this.allowed != null && this.allowed.contains(var1);
   }

   public boolean isDenied(Permission var1) {
      return this.denied != null && this.denied.contains(var1);
   }

   public boolean isEverythingDenied() {
      return this.denied != null && Permission.isFullAccess(this.denied);
   }

   public EnumSet<Permission> getEffectivePermissions() {
      if (this.allowed == null) {
         return EnumSet.noneOf(Permission.class);
      }

      EnumSet var1 = this.allowed.contains(Permission.OWNER) ? EnumSet.allOf(Permission.class) : this.allowed.clone();
      if (this.denied != null) {
         var1.removeAll(this.denied);
      }

      return var1;
   }

   public EnumSet<Permission> getAllowedPermissions() {
      return this.allowed == null ? EnumSet.noneOf(Permission.class) : this.allowed.clone();
   }

   public EnumSet<Permission> getDeniedPermissions() {
      return this.denied == null ? EnumSet.noneOf(Permission.class) : this.denied.clone();
   }

   public int getAllowedBits() {
      return this.allowed == null ? 0 : Permission.toBitCode(this.allowed);
   }

   public int getDeniedBits() {
      return this.denied == null ? 0 : Permission.toBitCode(this.denied);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 instanceof Permissions) {
         Permissions var2 = (Permissions)var1;
         boolean var3 = this.allowed == null ? var2.allowed == null : this.allowed.equals(var2.allowed);
         if (var3) {
            return this.denied == null ? var2.denied == null : this.denied.equals(var2.denied);
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 37 * var1 + (this.allowed == null ? 0 : this.allowed.hashCode());
      return 37 * var1 + (this.denied == null ? 0 : this.denied.hashCode());
   }

   public static Permissions add(Permissions var0, EnumSet<Permission> var1, EnumSet<Permission> var2) {
      EnumSet var3 = null;
      if (var0.allowed == null) {
         var3 = var1;
      } else {
         var3 = var0.allowed.clone();
         if (var1 != null) {
            var3.addAll(var1);
         }
      }

      EnumSet var4 = null;
      if (var0.denied == null) {
         var4 = var2;
      } else {
         var4 = var0.denied.clone();
         if (var2 != null) {
            var4.addAll(var2);
         }
      }

      return new Permissions(var3, var4);
   }

   public static Permissions add(Permissions var0, Permissions var1) {
      if (var0 == null) {
         return var1;
      } else {
         return var1 == null ? var0 : add(var0, var1.allowed, var1.denied);
      }
   }

   public static Permissions remove(Permissions var0, EnumSet<Permission> var1, EnumSet<Permission> var2) {
      EnumSet var3 = null;
      if (var0.allowed != null) {
         var3 = var0.allowed.clone();
         if (var1 != null) {
            var3.removeAll(var1);
         }
      }

      EnumSet var4 = null;
      if (var0.denied != null) {
         var4 = var0.denied.clone();
         if (var2 != null) {
            var4.removeAll(var2);
         }
      }

      return new Permissions(var3, var4);
   }

   public static Permissions deny(Permissions var0, Permission var1) {
      if (var0 == null) {
         return new Permissions(null, EnumSet.of(var1));
      }

      EnumSet var2 = var0.getDeniedPermissions();
      var2.add(var1);
      return new Permissions(var0.allowed, var2);
   }

   public static Permissions grantRead(Permissions var0) {
      if (var0 == null) {
         return new Permissions(READ_SET, null);
      }

      EnumSet var1 = var0.getAllowedPermissions();
      if (var1 == null) {
         var1 = READ_SET;
      } else {
         var1.add(Permission.READ);
      }

      EnumSet var2 = var0.getDeniedPermissions();
      if (var2 != null) {
         var2.remove(Permission.READ);
      }

      return new Permissions(var1, var2, true);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Allowed: ");
      if (this.allowed != null) {
         var1.append(this.allowed);
      }

      var1.append("\nDenied: ");
      if (this.denied != null) {
         var1.append(this.denied);
      }

      return var1.toString();
   }
}
