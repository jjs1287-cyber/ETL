package org.etlforge.ui.event;

import java.awt.ActiveEvent;

public class ApplicationMessageEvent extends KongaAWTEvent implements ActiveEvent {
   public static final ApplicationMessageEvent.LifeTime LIFETIME_INDEFINITE = new ApplicationMessageEvent.LifeTime(Long.MAX_VALUE);
   public static final ApplicationMessageEvent.LifeTime LIFETIME_HANDLER_DEPENDANT = new ApplicationMessageEvent.LifeTime(-1L);
   public static final int INFORMATION_MESSAGE = 0;
   public static final int WARNING_MESSAGE = 1;
   public static final int ATTENTION_MESSAGE = 2;
   public static final int ERROR_MESSAGE = 3;
   private static ApplicationMessagePoster s_msgPoster = new EventQueueApplicationMessagePoster();
   private static final Object s_postLock = new Object();
   private String m_message;
   private int m_messageType;
   private ApplicationMessageEvent.LifeTime m_messageLifeTime;

   public ApplicationMessageEvent(Object var1, String var2) {
      this(var1, var2, 0);
   }

   public ApplicationMessageEvent(Object var1, String var2, int var3) {
      this(var1, var2, var3, LIFETIME_HANDLER_DEPENDANT);
   }

   public ApplicationMessageEvent(Object var1, String var2, int var3, ApplicationMessageEvent.LifeTime var4) {
      super(var1, 2000);
      if (var2 == null) {
         var2 = "";
      }

      this.m_message = var2;
      switch (var3) {
         case 0:
         case 1:
         case 2:
         case 3:
            this.m_messageType = var3;
            break;
         default:
            this.m_messageType = 0;
      }

      this.setLifeTime(var4);
   }

   public String getMessage() {
      return this.m_message;
   }

   public int getMessageType() {
      return this.m_messageType;
   }

   public ApplicationMessageEvent.LifeTime getLifeTime() {
      return this.m_messageLifeTime;
   }

   public void setLifeTime(ApplicationMessageEvent.LifeTime var1) {
      if (var1 == null) {
         var1 = LIFETIME_HANDLER_DEPENDANT;
      }

      this.m_messageLifeTime = var1;
   }

   @Override
   public void dispatch() {
   }

   public static void postMessage(ApplicationMessageEvent var0) {
      if (s_msgPoster != null && var0 != null) {
         synchronized (s_postLock) {
            s_msgPoster.postMessage(var0);
         }
      }
   }

   public static void setMessagePoster(ApplicationMessagePoster var0) {
      synchronized (s_postLock) {
         s_msgPoster = var0;
      }
   }

   public static void postInformationMessage(Object var0, String var1, ApplicationMessageEvent.LifeTime var2) {
      postMessage(new ApplicationMessageEvent(var0, var1, 0, var2));
   }

   public static void postInformationMessage(Object var0, String var1) {
      postInformationMessage(var0, var1, LIFETIME_HANDLER_DEPENDANT);
   }

   public static void postWarningMessage(Object var0, String var1, ApplicationMessageEvent.LifeTime var2) {
      postMessage(new ApplicationMessageEvent(var0, var1, 1, var2));
   }

   public static void postWarningMessage(Object var0, String var1) {
      postWarningMessage(var0, var1, LIFETIME_HANDLER_DEPENDANT);
   }

   public static void postErrorMessage(Object var0, String var1, ApplicationMessageEvent.LifeTime var2) {
      postMessage(new ApplicationMessageEvent(var0, var1, 3, var2));
   }

   public static void postErrorMessage(Object var0, String var1) {
      postErrorMessage(var0, var1, LIFETIME_HANDLER_DEPENDANT);
   }

   public static void postAttentionMessage(Object var0, String var1, ApplicationMessageEvent.LifeTime var2) {
      postMessage(new ApplicationMessageEvent(var0, var1, 2, var2));
   }

   public static void postAttentionMessage(Object var0, String var1) {
      postAttentionMessage(var0, var1, LIFETIME_HANDLER_DEPENDANT);
   }

   public static class LifeTime {
      private long m_lMilliSeconds;

      public LifeTime(long var1) {
         this.m_lMilliSeconds = var1;
      }

      public long getMilliSeconds() {
         return this.m_lMilliSeconds;
      }
   }
}
