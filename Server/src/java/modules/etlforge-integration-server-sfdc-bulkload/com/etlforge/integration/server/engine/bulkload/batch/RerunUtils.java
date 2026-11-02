package com.etlforge.integration.server.engine.bulkload.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.io.KongaIoUtils;

class RerunUtils {
   public static void main(String[] var0) throws Exception {
      SectionBlock[] var1 = readOldLogFile(new File("Z:/tmp/dev/id.txt"));
      System.out.print("[");
      boolean var2 = true;

      for (SectionBlock var6 : var1) {
         if (var2) {
            var2 = false;
         } else {
            System.out.println(",");
         }

         System.out.print(var6);
      }

      System.out.println("]");
   }

   public static SectionBlock[] readOldLogFile(File var0) throws Exception {
      FileReader var1 = new FileReader(var0);
      BufferedReader var2 = new BufferedReader(var1);

      try {
         ArrayList var3 = new ArrayList();
         String var4 = var2.readLine();

         String var5;
         while ((var5 = var2.readLine()) != null) {
            Interval var6 = new Interval(var5);
            if (!var6.isError()) {
               Interval[] var7 = (Interval[]) var3.toArray(new Interval[var3.size()]);
               int var8 = 0;
               int var9 = 0;

               for (int var10 = var3.size(); var8 < var10; var8++) {
                  Interval var11 = var7[var8];
                  if (var6.start == var11.end) {
                     var11.end = var6.end;
                     var6 = var11;
                     var9++;
                  }

                  if (var6.end == var11.start) {
                     var11.start = var6.start;
                     if (++var9 == 2) {
                        var3.remove(var6);
                     }
                     break;
                  }

                  if (var11.start > var6.end) {
                     break;
                  }
               }

               if (var9 == 0) {
                  var3.add(var8, var6);
               }
            }
         }

         var2.close();
         var2 = null;
         String[] var19 = var4.split(",");
         long[] var20 = new long[var19.length];
         int var21 = var20.length - 1;
         SectionBlock[] var22 = new SectionBlock[var21];
         long var23 = Long.parseLong(var19[0]);
         int var14 = 0;

         while (var14 < var21) {
            long var12 = Long.parseLong(var19[var14 + 1]);
            ArrayList var15 = new ArrayList();
            var15.add(new Interval("Section_" + var14, var23, var12));
            exclude(var15, var3);
            var22[var14] = new SectionBlock(var15);
            var14++;
            var23 = var12;
         }

         return var22;
      } finally {
         KongaIoUtils.close(var2);
      }
   }

   private static void exclude(List<Interval> var0, List<Interval> var1) {
      assert var0.size() == 1;

      for (Interval var3 : var1) {
         Interval[] var4 = var0.toArray(new Interval[var0.size()]);
         Interval var5 = null;

         for (int var6 = 0; var6 < var4.length; var6++) {
            var5 = var4[var6];
            if (var5.overlap(var3)) {
               if (var5.start >= var3.start) {
                  if (var5.end > var3.end) {
                     var5.start = var3.end;
                  } else {
                     var0.remove(var5);
                  }
               } else if (var5.end <= var3.end) {
                  if (var5.start < var3.start) {
                     var5.end = var3.start;
                  } else {
                     var0.remove(var5);
                  }
               } else {
                  var0.add(++var6, new Interval(var5.id, var3.end, var5.end));
                  var5.end = var3.start;
               }
               break;
            }
         }
      }
   }
}
