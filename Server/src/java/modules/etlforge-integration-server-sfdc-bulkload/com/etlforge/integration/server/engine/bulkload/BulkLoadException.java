package com.etlforge.integration.server.engine.bulkload;

public final class BulkLoadException extends Exception {
   public static final int BACKEND_READ_ERROR = 100;
   public static final int PERSIST_ERROR = 101;
   public static final int JOB_NOT_CREATED = 102;
   public static final int JOB_NOT_CLOSED = 103;
   public static final int SCANNING_ERROR = 104;
   public static final int GET_BATCH_RESULT_FAILED = 105;
   public static final int SEND_QUERY_FAILED = 201;
   public static final int BACKEND_CONNECTION_ERROR = 105;
   private final int errorCode;

   public BulkLoadException(PersistorException var1) {
      this(101, var1);
   }

   public BulkLoadException(int var1, String var2, Throwable var3) {
      super(var2, var3);
      this.errorCode = var1;
   }

   public BulkLoadException(int var1, String var2) {
      super(var2);
      this.errorCode = var1;
   }

   public BulkLoadException(int var1, Throwable var2) {
      super(var2);
      this.errorCode = var1;
   }

   public int getErrorCode() {
      return this.errorCode;
   }
}
