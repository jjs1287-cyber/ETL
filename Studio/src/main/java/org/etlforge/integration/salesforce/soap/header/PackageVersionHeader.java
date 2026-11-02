package org.etlforge.integration.salesforce.soap.header;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class PackageVersionHeader extends SoapHeader {
   private final ImmutableList<PackageVersion> packageVersions;

   public PackageVersionHeader() {
      super("PackageVersionHeader");
      this.packageVersions = ImmutableList.of();
   }

   public PackageVersionHeader(List<PackageVersion> var1) {
      super("PackageVersionHeader");
      this.packageVersions = ImmutableList.copyOf(var1);
   }

   public ImmutableList<PackageVersion> getPackageVersions() {
      return this.packageVersions;
   }

   @Override
   public String getStringRepresentation() {
      return "";
   }

   public static PackageVersionHeader fromString(String var0) {
      return new PackageVersionHeader();
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
   }

   @Override
   public int hashCode() {
      return this.packageVersions.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof PackageVersionHeader && this.packageVersions.equals(((PackageVersionHeader)var1).packageVersions);
   }
}
