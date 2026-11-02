package org.etlforge.integration.client.applications.deployer;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocator;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocatorCallback;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocatorFactory;
import org.etlforge.util.progress.ProgressEvent;

final class DirectoryScanner implements JitterPackCollector {
   private final File directory;
   private final boolean recursive;
   private final ConsoleWriter consoleWriter;

   public DirectoryScanner(File var1, boolean var2, ConsoleWriter var3) {
      assert var1 != null && var1.exists() && var1.isDirectory();
      this.directory = var1;
      this.recursive = var2;
      this.consoleWriter = var3;
   }

   @Override
   public JitterPack[] collect() {
      try {
         DirectoryScanner.Locator var1 = new DirectoryScanner.Locator();
         var1.start();
         return var1.getResult();
      } catch (InterruptedException var2) {
         this.consoleWriter.log(var2);
         System.exit(1);
         return null;
      }
   }

   private class Locator implements JitterPackLocatorCallback {
      private final List<JitterPack> jitterPacks = Lists.newArrayList();
      private final CountDownLatch latch = new CountDownLatch(1);

      public Locator() {
      }

      public void start() {
         JitterPackLocator var1 = JitterPackLocatorFactory.newLocator();
         var1.search(DirectoryScanner.this.directory, DirectoryScanner.this.recursive, ImportParameters.DEFAULT, true, this);
      }

      public JitterPack[] getResult() throws InterruptedException {
         this.latch.await();
         return this.jitterPacks.toArray(new JitterPack[this.jitterPacks.size()]);
      }

      @Override
      public void locatedJitterPack(JitterPack var1) {
         this.jitterPacks.add(var1);
      }

      @Override
      public void progressEnded(ProgressEvent var1) {
         DirectoryScanner.this.consoleWriter.log("Done searching for Jitterpaks");
         this.latch.countDown();
      }

      @Override
      public void progressStarted(ProgressEvent var1) {
         DirectoryScanner.this.consoleWriter.log("Searching for Jitterpaks...");
      }

      @Override
      public void progressUpdated(ProgressEvent var1) {
         DirectoryScanner.this.consoleWriter.log(var1.getMessage());
      }
   }
}
