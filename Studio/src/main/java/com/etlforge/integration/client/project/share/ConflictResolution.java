package com.etlforge.integration.client.project.share;

public interface ConflictResolution {
   ConflictResolution CONTINUE = new ConflictResolution() {
      @Override
      public boolean skipItem() {
         return false;
      }
   };
   ConflictResolution SKIP = new ConflictResolution() {
      @Override
      public boolean skipItem() {
         return true;
      }
   };

   boolean skipItem();
}
