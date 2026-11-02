package org.etlforge.integration.activity.serverapi.files;

import org.etlforge.integration.activity.serverapi.ActivitySubjectId;

public final class OperationFileRequest {
   private final ActivitySubjectId subjectId;
   private volatile int maxEntriesToGet = 100;
   private final long[] opInstanceIds;

   public static OperationFileRequest forSubject(ActivitySubjectId var0) {
      return new OperationFileRequest(var0, new long[0]);
   }

   public static OperationFileRequest forOpInstance(long var0) {
      return new OperationFileRequest(null, new long[]{var0});
   }

   public static OperationFileRequest forOpInstances(long[] var0) {
      return new OperationFileRequest(null, (long[])var0.clone());
   }

   private OperationFileRequest(ActivitySubjectId var1, long[] var2) {
      this.subjectId = var1;
      this.opInstanceIds = var2;
   }

   public ActivitySubjectId getSubjectId() {
      return this.subjectId;
   }

   public int getMaxEntriesToGet() {
      return this.maxEntriesToGet;
   }

   public void setMaxEntriesToGet(int var1) {
      this.maxEntriesToGet = var1;
   }

   public long[] getOpInstanceIds() {
      return (long[])this.opInstanceIds.clone();
   }
}
