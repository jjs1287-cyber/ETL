package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.lang.KongaStringUtils;

public final class WscPartnerConnectionQuery {
   private final PartnerConnection connection;

   public WscPartnerConnectionQuery(PartnerConnection var1) {
      this.connection = Preconditions.checkNotNull(var1, "connection");
   }

   public QueryResult query(String var1) throws ConnectionException {
      com.sforce.soap.partner.QueryResult var2 = this.connection.query(var1);
      QueryResult var3 = this.buildQueryResult(var2);

      while (!var3.isDone()) {
         String var4 = var3.getQueryLocator();
         com.sforce.soap.partner.QueryResult var5 = this.connection.queryMore(var4);
         QueryResult var6 = this.buildQueryResult(var5);
         var3 = var3.addMore(var6);
      }

      return var3;
   }

   public int queryAllToFile(String var1, String var2) throws ConnectionException {
      File var3 = new File(var2);
      BufferedWriter var4 = null;
      int var5 = 0;
      ExecutorService var6 = Executors.newSingleThreadExecutor();

      try {
         var4 = Files.newWriter(var3, Charsets.UTF_8);
         com.sforce.soap.partner.QueryResult var7 = this.connection.query(var1);
         QueryResult var53 = this.buildQueryResult(var7);
         Future var54 = var6.submit(new WscPartnerConnectionQuery.Write_to_file(var53.getRecords(), var4));
         boolean var10 = var53.isDone();
         String var11 = var53.getQueryLocator();

         while (!var10) {
            com.sforce.soap.partner.QueryResult var12 = this.connection.queryMore(var11);
            QueryResult var13 = this.buildQueryResult(var12);
            var10 = var13.isDone();
            var11 = var13.getQueryLocator();
            var5 += (Integer)var54.get();
            var54 = var6.submit(new WscPartnerConnectionQuery.Write_to_file(var13.getRecords(), var4));
         }

         return var5 + (Integer)var54.get();
      } catch (FileNotFoundException var47) {
         throw new ConnectionException("queryAllToFile failed; File not found, fileName=\"" + var2 + "\"");
      } catch (ConnectionException var48) {
         String var52 = var48.getMessage();
         if (Strings.isNullOrEmpty(var52)) {
            var52 = var48.toString();
         }

         throw new ConnectionException("queryAllToFile failed. " + var52);
      } catch (InterruptedException var49) {
         throw new ConnectionException("queryAllToFile failed. The thread was interrupted. ", var49);
      } catch (ExecutionException var50) {
         Throwable var8 = var50.getCause();
         String var9 = var8.getMessage();
         throw new ConnectionException("queryAllToFile failed. " + var9, var50);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var45) {
            throw new ConnectionException("queryAllToFile failed. Close file exception. " + var45.getMessage());
         } finally {
            var6.shutdown();
         }
      }
   }

   private QueryResult buildQueryResult(com.sforce.soap.partner.QueryResult var1) {
      QueryResultBuilder var2 = new QueryResultBuilder();
      return var2.translate(var1);
   }

   public QueryResult queryAll(String var1) throws ConnectionException {
      com.sforce.soap.partner.QueryResult var2 = this.connection.queryAll(var1);
      return this.buildQueryResult(var2);
   }

   static class Write_to_file implements Callable<Integer> {
      ImmutableList<QueryRecord> records;
      BufferedWriter out;

      public Write_to_file(ImmutableList<QueryRecord> var1, BufferedWriter var2) {
         this.records = var1;
         this.out = var2;
      }

      private static void writeRecord(Iterable<String> var0, BufferedWriter var1) throws Exception {
         boolean var2 = true;

         for (String var4 : var0) {
            if (!var2) {
               var1.write(44);
            }

            if (KongaStringUtils.CSV_RequireQualifier(var4, ",")) {
               var1.write(34);
               var4 = KongaStringUtils.CSV_EscapeDoubleQuote(var4);
               var1.write(var4);
               var1.write(34);
            } else {
               var1.write(var4);
            }

            var2 = false;
         }

         var1.write(10);
      }

      public Integer call() throws Exception {
         int var1 = 0;

         for (QueryRecord var3 : this.records) {
            writeRecord(var3.getFieldValues(), this.out);
            var1++;
         }

         return var1;
      }
   }
}
