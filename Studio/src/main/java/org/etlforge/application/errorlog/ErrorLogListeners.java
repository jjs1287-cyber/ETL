package org.etlforge.application.errorlog;

import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

class ErrorLogListeners {
   private final List<ErrorLogListener> globalListeners = new CopyOnWriteArrayList<>();
   private final EnumMap<ErrorLevel, List<ErrorLogListener>> levelListeners = new EnumMap<>(ErrorLevel.class);

   ErrorLogListeners() {
      for (ErrorLevel var4 : ErrorLevel.values()) {
         this.levelListeners.put(var4, new CopyOnWriteArrayList<>());
      }
   }

   void add(ErrorLogListener var1) {
      if (var1 == null) {
         throw new NullPointerException("lst");
      }

      this.globalListeners.add(var1);
   }

   void remove(ErrorLogListener var1) {
      this.globalListeners.remove(var1);
   }

   void add(ErrorLevel var1, ErrorLogListener var2) {
      if (var1 == null) {
         throw new NullPointerException("level");
      }

      if (var2 == null) {
         throw new NullPointerException("lst");
      }

      this.levelListeners.get(var1).add(var2);
   }

   void remove(ErrorLevel var1, ErrorLogListener var2) {
      if (var1 == null) {
         throw new NullPointerException("level");
      }

      this.levelListeners.get(var1).remove(var2);
   }

   void notifyListeners(ErrorLogEntry var1) {
      notifyListeners(var1, this.globalListeners);
      ErrorLevel var2 = var1.level();

      for (Entry var4 : this.levelListeners.entrySet()) {
         if (var2.compareTo((ErrorLevel)var4.getKey()) >= 0) {
            notifyListeners(var1, (List<ErrorLogListener>)var4.getValue());
         }
      }
   }

   private static void notifyListeners(ErrorLogEntry var0, List<ErrorLogListener> var1) {
      for (ErrorLogListener var3 : var1) {
         notifyListener(var0, var3);
      }
   }

   private static void notifyListener(ErrorLogEntry var0, ErrorLogListener var1) {
      try {
         var1.errorOccurred(var0);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
