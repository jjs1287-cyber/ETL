package org.etlforge.util.progress;

import java.util.EventObject;

public class ProgressEvent extends EventObject {
   public static final int NORMAL = 0;
   public static final int WARNING = 1;
   public static final int ERROR = 2;
   public static final float UNKNOWN_COMPLETION = -1.0F;
   private float m_fPercentageCompleted = -1.0F;
   private String m_sMessage;
   private int m_iProgressState;
   private Throwable m_errorCause;

   public ProgressEvent(Object var1, float var2) {
      super(var1);
      this.m_fPercentageCompleted = var2 >= 0.0 ? Math.min(var2, 1.0F) : -1.0F;
      this.m_iProgressState = 0;
   }

   public ProgressEvent(Object var1, String var2) {
      super(var1);
      this.m_sMessage = var2;
      this.m_iProgressState = 0;
   }

   public ProgressEvent(Object var1, String var2, int var3) {
      super(var1);
      this.m_sMessage = var2;
      this.m_iProgressState = getProgressState(var3);
   }

   public ProgressEvent(Object var1, float var2, String var3, int var4) {
      super(var1);
      this.m_fPercentageCompleted = Math.min(var2, 1.0F);
      this.m_sMessage = var3;
      this.m_iProgressState = getProgressState(var4);
   }

   public ProgressEvent(Object var1, String var2, Throwable var3) {
      super(var1);
      this.m_sMessage = var2;
      this.m_iProgressState = 2;
      this.m_errorCause = var3;
      this.m_fPercentageCompleted = -1.0F;
   }

   public float getPercentageCompleted() {
      return this.m_fPercentageCompleted;
   }

   public String getMessage() {
      return this.m_sMessage;
   }

   public int getProgressState() {
      return this.m_iProgressState;
   }

   public Throwable getErrorCause() {
      return this.m_errorCause;
   }

   private static int getProgressState(int var0) {
      switch (var0) {
         case 1:
            return 1;
         case 2:
            return 2;
         default:
            return 0;
      }
   }
}
