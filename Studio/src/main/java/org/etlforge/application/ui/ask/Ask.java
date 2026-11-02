package org.etlforge.application.ui.ask;

import java.awt.Component;
import org.etlforge.application.ui.Alert;
import org.etlforge.ui.util.UiUtils;

public final class Ask {
   public static Answer yesOrNo(Object var0, String var1) {
      return yesOrNo(null, var0, var1, Alert.Option.YES, Alert.Type.QUESTION);
   }

   public static Answer yesOrNo(Component var0, Object var1, String var2) {
      return yesOrNo(var0, var1, var2, Alert.Option.YES, Alert.Type.QUESTION);
   }

   public static Answer yesOrNo(Component var0, Object var1, String var2, Alert.Option var3, Alert.Type var4) {
      Alert.Option var5 = Alert.ask(var0 != null ? var0 : UiUtils.getActiveFrame(), var1, var2, Alert.Option.YES_NO, var3, var4);
      return new Ask.AnswerImpl(var5);
   }

   public static Answer yesNoOrCancel(Object var0, String var1) {
      return yesNoOrCancel(null, var0, var1, Alert.Option.YES, Alert.Type.QUESTION);
   }

   public static Answer yesNoOrCancel(Component var0, Object var1, String var2) {
      return yesNoOrCancel(var0, var1, var2, Alert.Option.YES, Alert.Type.QUESTION);
   }

   public static Answer yesNoOrCancel(Component var0, Object var1, String var2, Alert.Option var3, Alert.Type var4) {
      Alert.Option var5 = Alert.ask(var0 != null ? var0 : UiUtils.getActiveFrame(), var1, var2, Alert.Option.YES_NO_CANCEL, var3, var4);
      return new Ask.AnswerImpl(var5);
   }

   public static Answer okOrCancel(Object var0, String var1) {
      return okOrCancel(null, var0, var1, Alert.Option.OK, Alert.Type.QUESTION);
   }

   public static Answer okOrCancel(Component var0, Object var1, String var2) {
      return okOrCancel(var0, var1, var2, Alert.Option.OK, Alert.Type.QUESTION);
   }

   public static Answer okOrCancel(Component var0, Object var1, String var2, Alert.Option var3, Alert.Type var4) {
      Alert.Option var5 = Alert.ask(var0 != null ? var0 : UiUtils.getActiveFrame(), var1, var2, Alert.Option.OK_CANCEL, var3, var4);
      return new Ask.AnswerImpl(var5);
   }

   public static Answer continueOrCancel(Object var0, String var1) {
      return continueOrCancel(null, var0, var1, Alert.Option.CONTINUE, Alert.Type.QUESTION);
   }

   public static Answer continueOrCancel(Component var0, Object var1, String var2) {
      return continueOrCancel(var0, var1, var2, Alert.Option.CONTINUE, Alert.Type.QUESTION);
   }

   public static Answer continueOrCancel(Component var0, Object var1, String var2, Alert.Option var3, Alert.Type var4) {
      Alert.Option var5 = Alert.ask(var0, var1, var2, Alert.Option.CONTINUE_CANCEL, var3, var4);
      return new Ask.AnswerImpl(var5);
   }

   private Ask() {
   }

   private static final class AnswerImpl implements Answer {
      private final Alert.Option option;

      public AnswerImpl(Alert.Option var1) {
         this.option = var1;
      }

      @Override
      public boolean isCancel() {
         return this.option == Alert.Option.CANCEL;
      }

      @Override
      public boolean isContinue() {
         return this.option == Alert.Option.CONTINUE;
      }

      @Override
      public boolean isNo() {
         return this.option == Alert.Option.NO;
      }

      @Override
      public boolean isOk() {
         return this.option == Alert.Option.OK;
      }

      @Override
      public boolean isYes() {
         return this.option == Alert.Option.YES;
      }

      @Override
      public String toString() {
         return this.option.name();
      }
   }
}
