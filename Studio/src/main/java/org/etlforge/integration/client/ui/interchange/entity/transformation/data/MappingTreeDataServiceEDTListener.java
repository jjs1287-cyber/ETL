package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.ui.util.UiUtils;

public final class MappingTreeDataServiceEDTListener implements MappingTreeDataServiceListener {
   private final MappingTreeDataServiceListener decoratee;

   public MappingTreeDataServiceEDTListener(MappingTreeDataServiceListener var1) {
      this.decoratee = var1;
   }

   @Override
   public void allDataRemoved() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MappingTreeDataServiceEDTListener.this.decoratee.allDataRemoved();
         }
      });
   }

   @Override
   public void targetDataRemoved() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MappingTreeDataServiceEDTListener.this.decoratee.targetDataRemoved();
         }
      });
   }

   @Override
   public void loadSourceDataAvailable(final DataFilesInfo var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MappingTreeDataServiceEDTListener.this.decoratee.loadSourceDataAvailable(var1);
         }
      });
   }

   @Override
   public void testTransformationDataAvailable(final DataFilesInfo var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MappingTreeDataServiceEDTListener.this.decoratee.testTransformationDataAvailable(var1);
         }
      });
   }

   @Override
   public void testOperationDataAvailable(final DataFilesInfo var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MappingTreeDataServiceEDTListener.this.decoratee.testOperationDataAvailable(var1);
         }
      });
   }
}
