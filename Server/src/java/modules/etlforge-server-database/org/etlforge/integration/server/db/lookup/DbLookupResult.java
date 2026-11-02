package org.etlforge.integration.server.db.lookup;

public final class DbLookupResult {
   private final String value;
   private final int updateCount;

   public DbLookupResult(String var1) {
      this.value = var1;
      this.updateCount = -1;
   }

   public DbLookupResult(int var1) {
      this.updateCount = var1;
      this.value = null;
   }

   public String getValue() {
      return this.value;
   }

   public int getUpdateCount() {
      return this.updateCount;
   }

   @Override
   public String toString() {
      return "Value: " + this.value + " - Update Count: " + this.updateCount;
   }
}
