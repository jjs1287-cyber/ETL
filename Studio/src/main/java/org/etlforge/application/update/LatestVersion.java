package org.etlforge.application.update;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.util.Date;
import java.util.Set;
import org.etlforge.util.version.Version;

public final class LatestVersion implements Comparable<LatestVersion> {
   static final LatestVersion UNKNOWN = new LatestVersion(Version.UNKNOWN, Optional.absent(), Optional.absent(), Optional.absent(), ImmutableSet.of());
   private final Version version;
   private final Optional<String> downloadUrl;
   private final Optional<Date> releaseDate;
   private final Optional<Long> fileSize;
   private final ImmutableSet<Branch> branchesRecommendedFor;

   public LatestVersion(Version var1, String var2, Date var3, long var4, Set<Branch> var6) {
      this(var1, optionalString(var2), optionalDate(var3), optionalSize(var4), var6);
   }

   private static Optional<String> optionalString(String var0) {
      return Strings.isNullOrEmpty(var0) ? Optional.absent() : Optional.of(var0);
   }

   private static Optional<Date> optionalDate(Date var0) {
      return var0 == null ? Optional.absent() : Optional.of(new Date(var0.getTime()));
   }

   private static Optional<Long> optionalSize(long var0) {
      return var0 <= 0L ? Optional.absent() : Optional.of(var0);
   }

   public LatestVersion(Version var1, String var2) {
      this(var1, optionalString(var2), Optional.absent(), Optional.absent(), ImmutableSet.of());
   }

   public LatestVersion(Version var1, Optional<String> var2, Optional<Date> var3, Optional<Long> var4, Set<Branch> var5) {
      this.version = Preconditions.checkNotNull(var1);
      this.downloadUrl = Preconditions.checkNotNull(var2);
      this.releaseDate = Preconditions.checkNotNull(var3);
      this.fileSize = Preconditions.checkNotNull(var4);
      this.branchesRecommendedFor = ImmutableSet.copyOf(var5);
   }

   public boolean isRecommendedFor(Version var1) {
      Branch var2 = Branch.forVersion(var1);
      return this.isRecommendedFor(var2);
   }

   public boolean isRecommendedFor(Branch var1) {
      return this.branchesRecommendedFor.contains(var1);
   }

   public boolean isLaterThan(LatestVersion var1) {
      return this.version.isLaterThan(var1.version);
   }

   public boolean isSameAsOrEarlierThan(Version var1) {
      return this.version.isSameAsOrEarlierThan(var1);
   }

   public boolean isLaterThan(Version var1) {
      return this.version.isLaterThan(var1);
   }

   public Version getVersionNumber() {
      return this.version;
   }

   public boolean hasDownloadUrl() {
      return this.downloadUrl.isPresent();
   }

   public String getDownloadUrl() {
      return this.downloadUrl.get();
   }

   public boolean hasReleaseDate() {
      return this.releaseDate.isPresent();
   }

   public Date getReleaseDate() {
      return new Date(this.releaseDate.get().getTime());
   }

   public boolean hasFileSize() {
      return this.fileSize.isPresent();
   }

   public long getFileSizeInBytes() {
      return this.fileSize.get();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.version, this.downloadUrl, this.releaseDate, this.fileSize);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof LatestVersion)) {
         return false;
      }

      LatestVersion var2 = (LatestVersion)var1;
      return this.version.equals(var2.version)
         && this.downloadUrl.equals(var2.downloadUrl)
         && this.releaseDate.equals(var2.releaseDate)
         && this.fileSize.equals(var2.fileSize);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("version-number", this.version)
         .add("download-url", toString(this.downloadUrl))
         .add("release-date", toString(this.releaseDate))
         .add("file-size", toString(this.fileSize))
         .toString();
   }

   private static String toString(Optional<?> var0) {
      return var0.isPresent() ? var0.get().toString() : "Unknown";
   }

   public static void main(String[] var0) {
      System.out.println(UNKNOWN);
   }

   public int compareTo(LatestVersion var1) {
      return this.version.compareTo(var1.version);
   }
}
