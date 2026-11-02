package org.etlforge.integration.salesforce.soap.header;

public final class PackageVersion {
   private final int majorNumber;
   private final int minorNumber;
   private final String namespace;

   public PackageVersion(int var1, int var2, String var3) {
      this.majorNumber = var1;
      this.minorNumber = var2;
      this.namespace = var3;
   }

   public int getMajorNumber() {
      return this.majorNumber;
   }

   public int getMinorNumber() {
      return this.minorNumber;
   }

   public String getNamespace() {
      return this.namespace;
   }
}
