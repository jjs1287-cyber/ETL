package org.etlforge.integration.client.ui.core.window;

import java.awt.event.KeyEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import javax.swing.KeyStroke;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.ui.util.KeyUtils;

final class GarbageCollectionTrigger implements WindowKeyDispatcher {
   private static final KeyStroke KEY_STROKE = KeyUtils.getCtrlShiftDown(77);

   private GarbageCollectionTrigger() {
   }

   static boolean install(ApplicationWindow var0) {
      KeyStroke var1 = ApplicationUi.requestGlobalKeyStroke(KEY_STROKE, GarbageCollectionTrigger.class);
      if (var1 != null) {
         var0.addGlobalKeyDispatcher(var1, new GarbageCollectionTrigger());
         return true;
      } else {
         ApplicationLog.getMainLogger().warning("Failed to install the garbage collection key trigger");
         return false;
      }
   }

   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      MemoryMXBean var3 = ManagementFactory.getMemoryMXBean();
      System.out.println("*** Before GC ***");
      this.reportStatus(var3);
      var3.gc();
      System.out.println();
      System.out.println("*** After GC ***");
      this.reportStatus(var3);
      return false;
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }

   private void reportStatus(MemoryMXBean var1) {
      System.out.println("Heap memory usage: ");
      System.out.println(var1.getHeapMemoryUsage());
      System.out.println("Non-heap memory usage: ");
      System.out.println(var1.getNonHeapMemoryUsage());
      System.out.println("Number of objects pending finalization: " + var1.getObjectPendingFinalizationCount());
   }
}
