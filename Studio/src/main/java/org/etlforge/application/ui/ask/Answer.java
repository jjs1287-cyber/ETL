package org.etlforge.application.ui.ask;

public interface Answer {
   Answer AFFIRMATIVE = new Answer() {
      @Override
      public boolean isCancel() {
         return false;
      }

      @Override
      public boolean isContinue() {
         return true;
      }

      @Override
      public boolean isNo() {
         return false;
      }

      @Override
      public boolean isOk() {
         return true;
      }

      @Override
      public boolean isYes() {
         return true;
      }
   };
   Answer NEGATIVE = new Answer() {
      @Override
      public boolean isCancel() {
         return true;
      }

      @Override
      public boolean isContinue() {
         return false;
      }

      @Override
      public boolean isNo() {
         return true;
      }

      @Override
      public boolean isOk() {
         return false;
      }

      @Override
      public boolean isYes() {
         return false;
      }
   };

   boolean isYes();

   boolean isOk();

   boolean isContinue();

   boolean isNo();

   boolean isCancel();
}
