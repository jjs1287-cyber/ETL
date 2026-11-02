package org.etlforge.integration.activity.client.files;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.common.server.file.OperationFileOwner;

public final class SingleRunResult implements OperationFileOwner {
   private final OperationLogEntry logEntry;
   private final EnumMap<FileCategory, OperationFile> files = Maps.newEnumMap(FileCategory.class);

   public SingleRunResult(OperationLogEntry var1, OperationFile var2, OperationFile var3) {
      this.logEntry = var1;
      if (var2 != null) {
         this.files.put(FileCategory.SFDC_SUCCESS, var2);
      }

      if (var3 != null) {
         this.files.put(FileCategory.SFDC_FAILURE, var3);
      }
   }

   public OperationLogEntry getLogEntry() {
      return this.logEntry;
   }

   @Override
   public boolean hasFile(FileCategory var1) {
      return this.files.containsKey(var1);
   }

   @Override
   public OperationFile getFile(FileCategory var1) {
      return this.files.get(var1);
   }
}
