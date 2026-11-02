package org.etlforge.io;

public interface ByteTransferCallback {
   ByteTransferCallback NONE = new ByteTransferCallback() {
      @Override
      public void update(long var1) {
      }

      @Override
      public void started() {
      }
   };

   void started();

   void update(long var1);
}
