package org.etlforge.integration.common.server.file;

public interface OperationFileOwner {
   boolean hasFile(FileCategory var1);

   OperationFile getFile(FileCategory var1);
}
