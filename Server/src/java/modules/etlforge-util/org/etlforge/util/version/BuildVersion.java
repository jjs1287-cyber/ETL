package org.etlforge.util.version;

import com.google.common.base.Preconditions;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.text.ThreadSafeNumberFormat;

public final class BuildVersion implements Version {
   private static final long serialVersionUID = 6358915333974023146L;
   private static final long MAJOR_CONVERSION = 10000000000000L;
   private static final long MINOR_CONVERSION = 10000000000L;
   private static final long PATCH_CONVERSION = 1000000L;
   private static final long BUILD_CONVERSION = 1L;
   private static final ThreadSafeNumberFormat NUMBER_FORMAT = new ThreadSafeNumberFormat(new DecimalFormat("#"));
   private static final String STRING_FORMAT_NO_COMMENT = "{0}.{1}.{2}.{3}";
   private static final String STRING_FORMAT_WITH_COMMENT = "{0}.{1}.{2}.{3}-{4}";
   private final long versionNumber;
   private final String comment;

   public BuildVersion(int var1, int var2, int var3, int var4) {
      this(var1, var2, var3, var4, null);
   }

   public BuildVersion(int var1, int var2, int var3, int var4, String var5) {
      checkNumber(var1, 400, "major");
      checkNumber(var2, 999, "minor");
      checkNumber(var3, 9999, "patch");
      checkNumber(var4, 999999, "build");
      this.versionNumber = getVersionNumber(var1, var2, var3, var4);
      this.comment = getComment(var5);
   }

   private static void checkNumber(int var0, int var1, String var2) {
      Preconditions.checkArgument(var0 >= 0 && var0 <= var1, String.format("Invalid %s number: %d. Restriction: 0 <= %s <= %d", var2, var0, var2, var1));
   }

   private static String getComment(String var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = var0.trim();
      return StringUtils.removeStart(var1, "-");
   }

   public static BuildVersion fromString(String var0) throws IllegalVersionException {
      String var1 = guardForBackwardsCompability(var0);

      try {
         String[] var2 = KongaStringUtils.splitInTwo(var1, "-");
         int[] var3 = parseVersionString(var2[0]);
         String var4 = getComment(var2[1]);
         return new BuildVersion(var3[0], var3[1], var3[2], var3[3], var4);
      } catch (NumberFormatException var5) {
         throw new IllegalVersionException("The string " + var0 + " cannot be parsed into a valid version.", var5);
      }
   }

   private static String guardForBackwardsCompability(String var0) {
      var0 = var0.trim();
      int var1 = var0.indexOf("-");
      if (var1 >= 0) {
         return var0;
      }

      var1 = var0.indexOf(" ");
      return var1 < 0 ? var0 : KongaStringUtils.insertAt(var0, "-", " ");
   }

   public static BuildVersion fromNumber(long var0) throws IllegalVersionException {
      return fromNumberAndComment(var0, null);
   }

   public static BuildVersion fromNumberAndComment(long var0, String var2) throws IllegalVersionException {
      try {
         return new BuildVersion(getMajorVersionImpl(var0), getMinorVersionImpl(var0), getPatchLevelImpl(var0), getBuildLevelImpl(var0), var2);
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
      return getBuildLevelImpl(this.versionNumber);
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
      return (int)(var0 / 10000000000000L);
   }

   private static int getMinorVersionImpl(long var0) {
      long var2 = var0 % 10000000000000L;
      return (int)(var2 / 10000000000L);
   }

   private static int getPatchLevelImpl(long var0) {
      long var2 = var0 % 10000000000L;
      return (int)(var2 / 1000000L);
   }

   private static int getBuildLevelImpl(long var0) {
      long var2 = var0 % 1000000L;
      return (int)(var2 / 1L);
   }

   @Override
   public String toString() {
      return this.comment.length() == 0 ? this.toStringWithoutComment() : this.toStringWithComment();
   }

   private String toStringWithComment() {
      NumberFormat var1 = NUMBER_FORMAT.get();
      Object[] var2 = new Object[]{
         var1.format(this.getMajorVersion()),
         var1.format(this.getMinorVersion()),
         var1.format(this.getPatchLevel()),
         var1.format(this.getBuildNumber()),
         this.comment
      };
      return MessageFormat.format("{0}.{1}.{2}.{3}-{4}", var2);
   }

   private String toStringWithoutComment() {
      NumberFormat var1 = NUMBER_FORMAT.get();
      Object[] var2 = new Object[]{
         var1.format(this.getMajorVersion()), var1.format(this.getMinorVersion()), var1.format(this.getPatchLevel()), var1.format(this.getBuildNumber())
      };
      return MessageFormat.format("{0}.{1}.{2}.{3}", var2);
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
      String[] var1 = var0.trim().split("\\.");
      int var2 = Integer.parseInt(var1[0]);
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      if (var1.length > 1) {
         var3 = Integer.parseInt(var1[1]);
         if (var1.length > 2) {
            var4 = Integer.parseInt(var1[2]);
         }

         if (var1.length > 3) {
            var5 = Integer.parseInt(var1[3]);
         }
      }

      return new int[]{var2, var3, var4, var5};
   }

   private static long getVersionNumber(int var0, int var1, int var2, int var3) {
      return 10000000000000L * var0 + 10000000000L * var1 + 1000000L * var2 + 1L * var3;
   }
}
