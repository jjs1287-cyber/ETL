package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadBatchInfo;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadJobInfo;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.system.SystemUtils;
import org.etlforge.util.csv.CSVParser;
import org.etlforge.util.csv.FileContents;

public class BulkLoadResultReport {
   public static final String NL = SystemUtils.LINE_SEPARATOR;
   private final char delimiter = ',';
   private final char qualifier = '"';
   private final BulkLoadResultReport.BulkLoadResultReportHelper resultHelper;
   private final String salesforceAction;

   public BulkLoadResultReport(BulkLoadResultReport.BulkLoadResultReportHelper var1, String var2) {
      this.resultHelper = var1;
      this.salesforceAction = var2;
   }

   private static BufferedWriter obtainWriter(String var0, Charset var1) throws FileNotFoundException {
      FileOutputStream var2 = new FileOutputStream(var0);
      return new BufferedWriter(new OutputStreamWriter(var2, var1));
   }

   public boolean produceBatchResultReport(BulkLoadBatchInfo var1, BulkLoadJobInfo var2) throws IOException, Exception {
      BufferedWriter var3 = null;
      BufferedWriter var4 = null;
      FileContents var5 = null;
      FileContents var6 = null;
      long var7 = 0L;
      long var9 = 0L;

      try {
         Charset var11 = var2.getSourceFileCharEncode();
         String var12 = this.resultHelper.getBatchSuccessFileName(var1.getBatchId());
         var3 = obtainWriter(var12, var11);
         String var13 = this.resultHelper.getBatchFailureFileName(var1.getBatchId());
         var4 = obtainWriter(var13, var11);
         if (var3 == null || var4 == null) {
            return false;
         }

         var5 = new FileContents(this.resultHelper.datafile, var11);
         long var14 = var1.getStartPos();
         long var16 = var1.getEndPos();
         if (var14 < 0L || var14 > var16) {
            var14 = 0L;
         }

         if (var16 > var5.size()) {
            var16 = var5.size();
         }

         CSVParser var18 = new CSVParser(var5);
         String var19 = var2.getHeader();
         int var20 = var18.getColumnNumber(var19);
         String var21 = this.resultHelper.getBatchFileName(var1.getBatchId());
         var6 = new FileContents(var21, var11);
         long var22 = var6.size();
         CSVParser var24 = new CSVParser(var6);
         long var33 = 0L;
         long var31 = 0L;
         long var29 = 0L;
         if (var22 > 0L) {
            var31 = var24.readRecord(var29, false);
            var31 = var24.skipLineBreak(var31);
         }

         var29 = var31;
         boolean var35 = var29 < var22;
         SalesforceResultStatusReport var38 = null;
         boolean var39 = var20 > 0;
         long var25 = var18.skipLineBreak(var14);

         while (var25 < var16) {
            long var27 = var18.readRecord(var25, var39);
            if (var27 <= 0L) {
               break;
            }

            String var36;
            if (var39) {
               List var40 = var18.getValuesForLastRecord();
               if (BulkLoadUtils.isBlankRecord(var40)) {
                  var36 = "";
               } else {
                  var36 = var18.converToLine(var40, var20);
               }
            } else {
               var36 = var18.read(var25, var27);
            }

            var27 = var18.skipLineBreak(var27);
            if (!Strings.isNullOrEmpty(var36)) {
               if (var35 && var31 >= 0L) {
                  var29 = var31;
                  var35 = var29 < var22;
                  if (var35) {
                     var31 = var24.readRecord(var29, true);
                     var35 = var31 > var29;
                     if (var35) {
                        var38 = SalesforceResultStatusReport.parse(var33, var24.getDecodedValuesForLastRecord());
                        var31 = var24.skipLineBreak(var31);
                     }
                  }
               }

               if (!var35) {
                  String var37;
                  if (var1.getStatusEnum() == BatchStatus.FAILED) {
                     var37 = "Failed batch : ";
                  } else {
                     var37 = "Not processed batch : ";
                  }

                  var37 = var37 + var1.getBatchId() + " : " + var1.getstatusMessage();
                  var38 = new SalesforceResultStatusReport(var33, false, false, var37);
               }

               if (var38.success) {
                  String var49 = var38.Message + "," + var36 + "," + var38.getSuccessStatusString(this.salesforceAction) + NL;
                  var7++;
                  var3.write(var49);
               } else {
                  String var50 = var36 + "," + CSVParser.CSVEncodeOptional(var38.Message, '"', ',') + NL;
                  var9++;
                  var4.write(var50);
               }

               var33++;
            }

            var25 = var27;
         }
      } finally {
         KongaIoUtils.close(var5);
         KongaIoUtils.close(var6);
         KongaIoUtils.close(var3);
         KongaIoUtils.close(var4);
      }

      var1.setFailureRecords(var9);
      var1.setSuccessRecords(var7);
      return true;
   }

   private void appendFile(File var1, String var2) throws IOException {
      File var3 = new File(var2);
      Files.copy(var3, Files.newOutputStreamSupplier(var1, true));
   }

   public void combineResultFiles(long var1, long var3, BulkLoadJobInfo var5, List<BulkLoadBatchInfo> var6) throws IOException {
      Charset var7 = var5.getSourceFileCharEncode();
      String var8 = var5.getHeader();
      File var9 = this.resultHelper.getSuccessFile();
      File var10 = this.resultHelper.getFailureFile();
      Files.write("SalesforceID," + var8 + ",Status" + NL, var9, var7);
      Files.write(var8 + ",Errors" + NL, var10, var7);

      for (BulkLoadBatchInfo var12 : var6) {
         String var13 = this.resultHelper.getBatchSuccessFileName(var12.getBatchId());
         String var14 = this.resultHelper.getBatchFailureFileName(var12.getBatchId());
         this.appendFile(var9, var13);
         this.appendFile(var10, var14);
      }
   }

   public static class BulkLoadResultReportHelper {
      private final File datafile;
      private static File SuccessFailureFileDir;
      private Folder operationSuccessFailureFileDir;

      public BulkLoadResultReportHelper(String var1) {
         this.datafile = new File(var1);
      }

      public void initSuccessFailureFileDir(int var1, long var2) throws ServerConfigurationException {
         SuccessFailureFileDir = ServerConfig.getSuccessFileDir();
         this.operationSuccessFailureFileDir = new Folder(SuccessFailureFileDir, "opid_" + var1, "inid_" + var2 + "_" + UUID.randomUUID().toString());
         this.operationSuccessFailureFileDir.createOnDisk();
      }

      public String getRootFolder() {
         return FileUtils.getFileDir(this.datafile.getParentFile());
      }

      public String getRelativePath(File var1) {
         String var2 = var1.getAbsolutePath();
         String var3 = FileUtils.getFileDir(SuccessFailureFileDir);
         return var2.startsWith(var3) ? var2.substring(var3.length()) : var2;
      }

      public String getdatafilename() {
         return this.datafile.getName();
      }

      public String getBatchFileName(String var1) {
         String var2 = this.getRootFolder() + this.getdatafilename();
         return this.getFileNameUtils(var2, var1);
      }

      private String getFileNameUtils(String var1, String var2) {
         return FileUtils.getPostfixFilename(var1, var2, ".csv");
      }

      private String getBatchSuccessFailureFileName(String var1, String var2) {
         String var3 = this.getBatchFileName(var1);
         return this.getFileNameUtils(var3, var2);
      }

      public String getBatchSuccessFileName(String var1) {
         return this.getBatchSuccessFailureFileName(var1, "_s");
      }

      public String getBatchFailureFileName(String var1) {
         return this.getBatchSuccessFailureFileName(var1, "_f");
      }

      public File getSuccessFile() {
         return this.getSuccessFailureFile("_s");
      }

      public File getFailureFile() {
         return this.getSuccessFailureFile("_f");
      }

      private File getSuccessFailureFile(String var1) {
         String var2 = FileUtils.getPostfixFilename(this.getdatafilename(), var1, ".csv");
         File var3 = this.operationSuccessFailureFileDir.getFile(var2);
         if (!var3.setReadable(true, false)) {
            String var4 = var3.getAbsolutePath() + " Can not be set readable for everyone ";
            System.out.print(var4);
         }

         return var3;
      }
   }
}
