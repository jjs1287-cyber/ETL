package org.etlforge.integration.client.project;

import java.io.File;
import org.etlforge.util.version.Version;

public interface UpgradeStrategy {
   UpgradeStrategy AUTOMATIC = new UpgradeStrategy() {
      @Override
      public UpgradeStrategy.Instruction getInstruction(String var1) {
         return UpgradeStrategy.Instruction.UPGRADE;
      }

      @Override
      public boolean doBackup(String var1, File var2, Version var3) {
         throw new UnsupportedOperationException();
      }
   };

   UpgradeStrategy.Instruction getInstruction(String var1);

   boolean doBackup(String var1, File var2, Version var3);

   enum Instruction {
      BACKUP_AND_UPGRADE,
      UPGRADE,
      CANCEL;
   }
}
