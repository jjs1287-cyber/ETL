package org.etlforge.util.version;

import java.io.Serializable;

public interface Version extends Comparable<Version>, Serializable {
   int MAJOR_MAX = 400;
   int MINOR_MAX = 999;
   int PATCH_MAX = 9999;
   int BUILD_MAX = 999999;
   Version UNKNOWN = new Version() {
      @Override
      public int compareTo(Version var1) {
         return 1;
      }

      @Override
      public int getBuildNumber() {
         return -1;
      }

      @Override
      public int getMajorVersion() {
         return -1;
      }

      @Override
      public int getMinorVersion() {
         return -1;
      }

      @Override
      public int getPatchLevel() {
         return -1;
      }

      @Override
      public String getPatchLevelString() {
         return "Unknown";
      }

      @Override
      public String getShortString() {
         return "Unknown";
      }

      @Override
      public long getVersionNumber() {
         return -1L;
      }

      @Override
      public boolean isEarlierThan(Version var1) {
         return false;
      }

      @Override
      public boolean isLaterThan(Version var1) {
         return false;
      }

      @Override
      public boolean isSameAsOrEarlierThan(Version var1) {
         return false;
      }

      @Override
      public boolean isSameAsOrLaterThan(Version var1) {
         return false;
      }

      @Override
      public String getComment() {
         return "";
      }

      @Override
      public String toString() {
         return "Unknown";
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this;
      }

      @Override
      public int hashCode() {
         return System.identityHashCode(this);
      }
   };

   int getMajorVersion();

   int getMinorVersion();

   int getPatchLevel();

   int getBuildNumber();

   long getVersionNumber();

   @Override
   boolean equals(Object var1);

   boolean isLaterThan(Version var1);

   boolean isSameAsOrLaterThan(Version var1);

   boolean isEarlierThan(Version var1);

   boolean isSameAsOrEarlierThan(Version var1);

   int compareTo(Version var1);

   String getShortString();

   String getPatchLevelString();

   String getComment();
}
