package org.etlforge.integration.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang.StringUtils;

class ProcessRunner {
   public final String[] cmds;

   public ProcessRunner(String[] var1) {
      this.cmds = (String[])var1.clone();
   }

   public void run() throws Exception {
      StringWriter var1 = new StringWriter();
      StringWriter var2 = new StringWriter();
      Runtime var3 = Runtime.getRuntime();
      Process var4 = var3.exec(this.cmds);
      ProcessRunner.StreamGobbler var5 = new ProcessRunner.StreamGobbler(var4.getErrorStream(), new PrintWriter(var1));
      ProcessRunner.StreamGobbler var6 = new ProcessRunner.StreamGobbler(var4.getInputStream(), new PrintWriter(var2));
      new Thread(var5).start();
      new Thread(var6).start();
      int var7 = var4.waitFor();
      if (var7 != 0) {
         throw new Exception(
            "The process " + StringUtils.join(this.cmds, " ") + " returned a non-zero exit value: " + var7 + ". Reported errors: " + var1.toString()
         );
      }
   }

   private static class StreamGobbler implements Runnable {
      private final InputStream is;
      private final PrintWriter out;

      public StreamGobbler(InputStream var1, PrintWriter var2) {
         this.is = var1;
         this.out = var2;
      }

      @Override
      public void run() {
         try {
            InputStreamReader var1 = new InputStreamReader(this.is);
            BufferedReader var2 = new BufferedReader(var1);
            String var3 = null;

            while ((var3 = var2.readLine()) != null) {
               this.out.println(var3);
            }
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }
   }
}
