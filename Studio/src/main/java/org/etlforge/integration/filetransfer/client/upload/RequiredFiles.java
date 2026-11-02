package org.etlforge.integration.filetransfer.client.upload;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.List;
import org.etlforge.integration.filetransfer.client.ServerFile;

public final class RequiredFiles {
   private final ServerFile parentFile;
   private final ImmutableList<File> requiredFiles;

   public RequiredFiles(ServerFile var1, List<File> var2) {
      this.parentFile = Preconditions.checkNotNull(var1, "parentFile");
      this.requiredFiles = ImmutableList.copyOf(var2);
   }

   public ServerFile getParentFile() {
      return this.parentFile;
   }

   public ImmutableList<File> getRequiredFiles() {
      return this.requiredFiles;
   }
}
