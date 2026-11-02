package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.util.Date;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.data.entity.id.OperationId;

final class SourceHistoryEntryBuilder {
   private long entryId;
   private OperationId operationId;
   private String operationName;
   private String sourceFiles;
   private int fileSizeInBytes;
   private Date timestamp;

   public void setEntryId(long var1) {
      this.entryId = var1;
   }

   public void setOperationId(OperationId var1) {
      this.operationId = var1;
   }

   public void setOperationName(String var1) {
      this.operationName = var1;
   }

   public void setSourceFiles(String var1) {
      this.sourceFiles = var1;
   }

   public void setFileSizeInBytes(int var1) {
      this.fileSizeInBytes = var1;
   }

   public void setTimeStamp(Date var1) {
      this.timestamp = var1;
   }

   public SourceHistoryEntry getEntry() {
      return new SourceHistoryEntry(this.entryId, this.operationId, this.operationName, this.sourceFiles, this.fileSizeInBytes, this.timestamp);
   }
}
