package org.etlforge.util.version;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

                  
public final class SimpleVersion implements Version {
   public static final long MAJOR_CONVERSION = 10000000L;
   public static final long MINOR_CONVERSION = 10000L;
   public static final long PATCH_CONVERSION = 1L;
   private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#");
   private static final String STRING_FORMAT = "{0}.{1}.{2}{3}";
   private final long versionNumber;
   private final String comment;

   public SimpleVersion(int var1, int var2, int var3) {
      this(var1, var2, var3, null);
   }

   public SimpleVersion(int var1, int var2, int var3, String var4) {
      if (var1 < 0 || var1 > 400) {
         throw new IllegalArgumentException("Invalid major version number: " + var1 + ". Restriction: 0 <= major <= " + 400 + ".");
      }

      if (var2 < 0 || var2 > 999) {
         throw new IllegalArgumentException("Invalid minor version number: " + var2 + ". Restriction: 0 <= minor <= " + 999 + ".");
      }

      if (var3 >= 0 && var3 <= 9999) {
         this.versionNumber = getVersionNumber(var1, var2, var3);
         this.comment = var4 != null ? var4.trim() : "";
      } else {
         throw new IllegalArgumentException("Invalid patch level number: " + var3 + ". Restriction: 0 <= patch <= " + 9999 + ".");
      }
   }

   public static SimpleVersion fromString(String var0) throws IllegalVersionException {
      try {
         int var1 = var0.indexOf(32);
         String var2 = var1 == -1 ? var0 : var0.substring(0, var1);
         int[] var3 = parseVersionString(var2);
         String var4 = var1 == -1 ? "" : var0.substring(var1 + 1);
         return new SimpleVersion(var3[0], var3[1], var3[2], var4);
      } catch (NumberFormatException var5) {
         throw new IllegalVersionException("The string " + var0 + " cannot be parsed into a valid version.", var5);
      }
   }

   public static SimpleVersion fromNumber(long var0) throws IllegalVersionException {
      return fromNumberAndComment(var0, null);
   }

   public static SimpleVersion fromNumberAndComment(long var0, String var2) throws IllegalVersionException {
      try {
         return new SimpleVersion(getMajorVersionImpl(var0), getMinorVersionImpl(var0), getPatchLevelImpl(var0), var2);
      } catch (IllegalArgumentException var4) {
         throw new IllegalVersionException(
            "The value " + var0 + " cannot be split into valid values of the major version, minor version, and patch level.", var4
         );
      }
   }

   @Override
   public int getMajorVersion() {
      return getMajorVersionImpl(this.versionNumber);
   }

   @Override
   public int getMinorVersion() {
      return getMinorVersionImpl(this.versionNumber);
   }

   @Override
   public int getPatchLevel() {
      return getPatchLevelImpl(this.versionNumber);
   }

   @Override
   public int getBuildNumber() {
      return 0;
   }

   @Override
   public long getVersionNumber() {
      return this.versionNumber;
   }

   @Override
   public String getComment() {
      return this.comment;
   }

   private static int getMajorVersionImpl(long var0) {
      return (int)(var0 / 10000000L);
   }

   private static int getMinorVersionImpl(long var0) {
      int var2 = (int)(var0 % 10000000L);
      return (int)(var2 / 10000L);
   }

   private static int getPatchLevelImpl(long var0) {
      int var2 = (int)(var0 % 10000L);
      return (int)(var2 / 1L);
   }

   @Override
   public String toString() {
      Object[] var1 = new Object[]{
         NUMBER_FORMAT.format(this.getMajorVersion()),
         NUMBER_FORMAT.format(this.getMinorVersion()),
         NUMBER_FORMAT.format(this.getPatchLevel()),
         this.comment.length() == 0 ? "" : " " + this.comment
      };
      return MessageFormat.format("{0}.{1}.{2}{3}", var1);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof Version ? this.versionNumber == ((Version)var1).getVersionNumber() : false;
   }

   @Override
   public int hashCode() {
      return (int)(this.versionNumber ^ this.versionNumber >>> 32);
   }

   @Override
   public boolean isLaterThan(Version var1) {
      return this.versionNumber > var1.getVersionNumber();
   }

   @Override
   public boolean isSameAsOrLaterThan(Version var1) {
      return !this.isEarlierThan(var1);
   }

   @Override
   public boolean isEarlierThan(Version var1) {
      return this.versionNumber < var1.getVersionNumber();
   }

   @Override
   public boolean isSameAsOrEarlierThan(Version var1) {
      return !this.isLaterThan(var1);
   }

   @Override
   public int compareTo(Version var1) {
      if (this.isEarlierThan(var1)) {
         return -1;
      } else {
         return this.isLaterThan(var1) ? 1 : 0;
      }
   }

   @Override
   public String getShortString() {
      return this.getMajorVersion() + "." + this.getMinorVersion();
   }

   @Override
   public String getPatchLevelString() {
      return this.getMajorVersion() + "." + this.getMinorVersion() + "." + this.getPatchLevel();
   }

   private static int[] parseVersionString(String var0) throws NumberFormatException {
      String[] var1 = var0.split("\\.");
      int var2 = Integer.parseInt(var1[0]);
      int var3 = 0;
      int var4 = 0;
      if (var1.length > 1) {
         var3 = Integer.parseInt(var1[1]);
         if (var1.length > 2) {
            var4 = Integer.parseInt(var1[2]);
         }
      }

      return new int[]{var2, var3, var4};
   }

   private static long getVersionNumber(int var0, int var1, int var2) {
      return 10000000L * var0 + 10000L * var1 + 1L * var2;
   }
}
