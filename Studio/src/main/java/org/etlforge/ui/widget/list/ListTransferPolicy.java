package org.etlforge.ui.widget.list;

public interface ListTransferPolicy {
   ListTransferPolicy TRIVIAL = new ListTransferPolicy() {
      @Override
      public Object[] filterTargetTransfer(Object[] var1) {
         return var1;
      }

      @Override
      public boolean isSelectable(Object var1) {
         return true;
      }
   };

   boolean isSelectable(Object var1);

   Object[] filterTargetTransfer(Object[] var1);
}
