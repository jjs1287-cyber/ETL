package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

public class SavedSearch {
   private final String id;
   private final String name;

   public SavedSearch(String var1, String var2) {
      this.id = var1;
      this.name = var2;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      return this.getName();
   }

   private String key() {
      return this.id + this.name;
   }

   @Override
   public int hashCode() {
      return this.key().hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof SavedSearch) {
         SavedSearch var2 = (SavedSearch)var1;
         return this.key().equals(var2.key());
      } else {
         return false;
      }
   }
}
