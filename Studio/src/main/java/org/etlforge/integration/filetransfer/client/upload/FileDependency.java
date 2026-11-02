package org.etlforge.integration.filetransfer.client.upload;

import com.google.common.base.Preconditions;
import org.etlforge.integration.filetransfer.client.ServerFile;

public final class FileDependency {
   private final ServerFile mainFile;
   private final ServerFile requiredFile;

   public FileDependency(ServerFile var1, ServerFile var2) {
      Preconditions.checkNotNull(var1, "mainFile");
      Preconditions.checkNotNull(var2, "requiredFile");
      this.mainFile = var1;
      this.requiredFile = var2;
   }

   public ServerFile getMainFile() {
      return this.mainFile;
   }

   public ServerFile getRequiredFile() {
      return this.requiredFile;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof FileDependency ? this.equals((FileDependency)var1) : false;
      }
   }

   private boolean equals(FileDependency var1) {
      return this.mainFile.equals(var1.mainFile) && this.requiredFile.equals(var1.requiredFile);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.mainFile.hashCode();
      return 31 * var1 + this.requiredFile.hashCode();
   }
}
