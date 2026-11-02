package org.etlforge.application.ui.viewer.log;

import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.viewer.log.tree.LogFileRepositoryTree;
import org.etlforge.system.Disposable;

final class FileStatusMonitor implements Disposable {
   private final LogFileRepositoryTree tree;
   private final Map<File, Long> sizes;
   private final Timer timer;
   private final Object lock;

   public FileStatusMonitor(LogFileRepositoryTree var1, LogFileRepository.Folder var2) {
      this.tree = var1;
      this.sizes = Maps.newHashMap();
      this.timer = new Timer();
      this.lock = new Object();
   }

   public void start() {
      long var1 = TimeUnit.SECONDS.toMillis(1L);
      this.timer.schedule(new FileStatusMonitor.SizeChecker(), var1, var1);
   }

   public void fileOpened(File var1) {
      synchronized (this.lock) {
         this.storeSize(var1);
      }

      this.tree.setLogFileDirty(var1, false);
   }

   private void storeSize(File var1) {
      this.sizes.put(var1, var1.length());
   }

   @Override
   public void dispose() {
      this.timer.cancel();
   }

   private class SizeChecker extends TimerTask {
      private SizeChecker() {
      }

      @Override
      public void run() {
         HashMap var1 = Maps.newHashMap();
         synchronized (FileStatusMonitor.this.lock) {
            for (Entry var4 : FileStatusMonitor.this.sizes.entrySet()) {
               File var5 = (File)var4.getKey();
               long var6 = var5.length();
               if (var6 != (Long)var4.getValue()) {
                  var1.put(var5, var6);
               }
            }

            FileStatusMonitor.this.sizes.putAll(var1);
         }

         if (!var1.isEmpty()) {
            this.updateTree(var1.keySet());
         }
      }

      private void updateTree(final Iterable<File> var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (File var2 : var1) {
                  FileStatusMonitor.this.tree.setLogFileDirty(var2, true);
               }
            }
         });
      }
   }
}
