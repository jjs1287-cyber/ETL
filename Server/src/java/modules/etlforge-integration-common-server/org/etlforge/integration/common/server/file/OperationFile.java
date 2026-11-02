package org.etlforge.integration.common.server.file;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.File;
import java.nio.charset.Charset;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.id.KongaGuid;

public final class OperationFile {
   private final OperationId opId;
   private final long opInstanceId;
   private final FileCategory category;
   private final KongaGuid fileGuid;
   private final String path;
   private final long size;
   private final Charset encoding;
   private final long numberOfRecords;
   private final String statusMessage;

   public OperationFile(OperationId var1, long var2, FileCategory var4, KongaGuid var5, String var6, long var7, Charset var9, long var10, String var12) {
      this.opId = Preconditions.checkNotNull(var1, "opId");
      this.opInstanceId = var2;
      this.category = Preconditions.checkNotNull(var4, "category");
      this.fileGuid = Preconditions.checkNotNull(var5, "fileGuid");
      this.path = ArgCheck.checkNotEmpty(var6, "path");
      this.size = ArgCheck.checkNotNegative(var7, "size");
      this.encoding = var9;
      this.numberOfRecords = Math.max(-1L, var10);
      this.statusMessage = Strings.nullToEmpty(var12);
   }

   public OperationId getOperationId() {
      return this.opId;
   }

   public long getOpInstanceId() {
      return this.opInstanceId;
   }

   public FileCategory getCategory() {
      return this.category;
   }

   public KongaGuid getGuid() {
      return this.fileGuid;
   }

   public String getStatusMessage() {
      return this.statusMessage;
   }

   public long sizeInBytes() {
      return this.size;
   }

   public Charset getEncoding() {
      return this.encoding;
   }

   public long getNumberOfRecords() {
      return this.numberOfRecords;
   }

   public boolean existsLocally() {
      File var1 = this.asLocalFile();
      return var1.canRead();
   }

   public File asLocalFile() {
      return new File(this.path);
   }

   @Override
   public String toString() {
      return this.path;
   }
}
