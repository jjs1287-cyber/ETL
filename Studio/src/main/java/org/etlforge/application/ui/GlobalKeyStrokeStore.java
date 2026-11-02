package org.etlforge.application.ui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.KeyStroke;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.log.ApplicationLog;

final class GlobalKeyStrokeStore {
   private final Map<KeyStroke, Object> assignedKeyStrokes = Maps.newHashMap();
   private final Object lock = new Object();

   public GlobalKeyStrokeStore() {
   }

   public KeyStroke request(KeyStroke var1, Object var2) {
      Preconditions.checkNotNull(var1, "ks");
      Preconditions.checkNotNull(var2, "requestor");
      Object var3 = null;
      synchronized (this.lock) {
         var3 = this.assignedKeyStrokes.get(var1);
         if (var3 == null) {
            this.assignedKeyStrokes.put(var1, var2);
            this.logGrantedRequest(var1, var2);
            return var1;
         }
      }

      if (var3.equals(var2)) {
         return var1;
      }

      this.logDeniedRequest(var1, var2);
      return null;
   }

   private void logGrantedRequest(KeyStroke var1, Object var2) {
      StringBuilder var3 = new StringBuilder("Granted request for global keystroke: ");
      this.appendRequestDetailsForLogging(var1, var2, var3);
      ApplicationLog.getMainLogger().info(var3.toString());
   }

   private void logDeniedRequest(KeyStroke var1, Object var2) {
      StringBuilder var3 = new StringBuilder("Denied request for global keystroke: ");
      this.appendRequestDetailsForLogging(var1, var2, var3);
      ApplicationLog.getMainLogger().warning(var3.toString());
   }

   private void appendRequestDetailsForLogging(KeyStroke var1, Object var2, StringBuilder var3) {
      var3.append(var1).append(SystemUtils.LINE_SEPARATOR);
      var3.append("The request was made from " + var2);
   }

   public void release(KeyStroke var1, Object var2) {
      Preconditions.checkNotNull(var1, "ks");
      Preconditions.checkNotNull(var2, "requestor");
      synchronized (this.lock) {
         if (this.assignedKeyStrokes.get(var1) == var2) {
            this.assignedKeyStrokes.remove(var1);
         }
      }
   }
}
