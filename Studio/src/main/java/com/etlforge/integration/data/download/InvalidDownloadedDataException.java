package com.etlforge.integration.data.download;

public class InvalidDownloadedDataException extends RuntimeException {
   public InvalidDownloadedDataException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidDownloadedDataException(String var1) {
      super(var1);
   }
}
