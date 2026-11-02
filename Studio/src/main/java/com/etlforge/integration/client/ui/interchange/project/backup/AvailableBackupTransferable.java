package com.etlforge.integration.client.ui.interchange.project.backup;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.etlforge.integration.client.project.backup.AvailableBackup;

public class AvailableBackupTransferable implements Transferable {
   public static final DataFlavor FLAVOR = createFlavor();
   private static final DataFlavor[] supportedFlavors = new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
   private final AvailableBackup backup;

   public AvailableBackupTransferable(AvailableBackup var1) {
      this.backup = var1;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
      if (var1.equals(FLAVOR)) {
         return this.backup;
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return RendererUtils.display(this.backup);
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return (DataFlavor[])supportedFlavors.clone();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor var1) {
      for (DataFlavor var5 : supportedFlavors) {
         if (var5.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   private static DataFlavor createFlavor() {
      try {
         DataFlavor var0 = new DataFlavor("application/x-java-jvm-local-objectref;class=" + AvailableBackup.class.getName());
         var0.setHumanPresentableName("Project Backup");
         return var0;
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException(var1);
      }
   }
}
