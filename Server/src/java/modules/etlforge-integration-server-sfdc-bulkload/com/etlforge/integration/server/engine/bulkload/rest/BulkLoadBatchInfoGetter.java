package com.etlforge.integration.server.engine.bulkload.rest;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadBatchInfo;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadJobInfo;
import org.etlforge.lang.LongParser;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class BulkLoadBatchInfoGetter {
   private final String jobBaseUrl;
   private final String SessionID;
   private final HttpClient client;
   private static final String ResultHeader = "\"Id\",\"Success\",\"Created\",\"Error\"";

   public BulkLoadBatchInfoGetter(String var1, String var2, HttpClient var3) {
      this.jobBaseUrl = var1;
      this.SessionID = var2;
      this.client = var3;
   }

   public boolean getBulkLoadBatchResult(BulkLoadBatchInfo var1, String var2, Charset var3) throws Exception {
      if (var1.getStatusEnum() == BatchStatus.NOTPROCESSED) {
         Files.write("", new File(var2), var3);
         return true;
      }

      String var4 = this.getJobBaseUrl() + "/batch/" + var1.getBatchId() + "/result";
      String var5 = this.getResponse(var4);
      if (var5.isEmpty()) {
         return false;
      }

      if (var5.startsWith("\"Id\",\"Success\",\"Created\",\"Error\"")) {
         Files.write(var5, new File(var2), var3);
      } else {
         if (var1.getStatusEnum() == BatchStatus.COMPLETED) {
            return false;
         }

         Files.write("", new File(var2), var3);
         Files.write(var5, new File(var2 + "_res.xml"), Charsets.UTF_8);
      }

      return true;
   }

   public Map<String, BulkLoadBatchInfo> getBulkLoadBatchInfoListMap() throws Exception {
      HashMap var1 = Maps.newHashMap();

      for (BulkLoadBatchInfo var3 : this.getBulkLoadBatchInfoList()) {
         var1.put(var3.getBatchId(), var3);
      }

      return var1;
   }

   public List<BulkLoadBatchInfo> getBulkLoadBatchInfoList() throws Exception {
      String var1 = this.getJobBaseUrl() + "/batch";
      String var2 = this.getResponse(var1);
      return parseBulkLoadBatchInfo(var2);
   }

   public BulkLoadJobInfo getBulkLoadJobInfo() throws Exception {
      BulkLoadJobInfo var1 = null;
      String var2 = this.getJobBaseUrl();
      String var3 = this.getResponse(var2).trim();
      if (!var3.isEmpty()) {
         Document var4 = null;
         var4 = DomParser.parseXmlString(var3, true);
         Element var5 = var4.getDocumentElement();
         if (var5 != null && var5.getLocalName().equals("jobInfo")) {
            String var6 = KongaXmlUtils.getChildElementValue(var5, "id");
            String var7 = KongaXmlUtils.getChildElementValue(var5, "state");
            var1 = new BulkLoadJobInfo(var6, var7);
         }
      }

      return var1;
   }

   public List<String> getBulkQueryResultIDList(String var1) throws Exception {
      ArrayList var2 = null;
      String var3 = this.getJobBaseUrl() + "/batch/" + var1 + "/result";
      String var4 = this.getResponse(var3).trim();
      if (!var4.isEmpty()) {
         Document var5 = null;
         var5 = DomParser.parseXmlString(var4, true);
         Element var6 = var5.getDocumentElement();
         if (var6 != null && var6.getLocalName().equals("result-list")) {
            var2 = Lists.newArrayList();
            Elements var7 = Elements.childElementsWithName(var6, "result");

            for (int var8 = 0; var8 < var7.getLength(); var8++) {
               Element var9 = var7.item(var8);
               String var10 = KongaXmlUtils.getElementValue(var9);
               var2.add(var10);
            }
         }
      }

      return var2;
   }

   private String getResponse(String var1) throws Exception {
      Getter var2 = new Getter(var1, this.getSessionID());
      return var2.get(this.client);
   }

   private static List<BulkLoadBatchInfo> parseBulkLoadBatchInfo(String var0) throws SAXException {
      ArrayList var1 = Lists.newArrayList();
      if (var0.isEmpty()) {
         return var1;
      }

      String var2 = var0.trim();
      Document var3 = DomParser.parseXmlString(var2, true);
      Node var4 = var3.getFirstChild();
      if (var4 != null && var4.getLocalName().equals("batchInfoList")) {
         for (Element var6 : Elements.under(var4)) {
            String var7 = KongaXmlUtils.getChildElementValue(var6, "id");
            String var8 = KongaXmlUtils.getChildElementValue(var6, "jobId");
            String var9 = KongaXmlUtils.getChildElementValue(var6, "numberRecordsProcessed");
            long var10 = LongParser.parse(var9, 0L);
            String var12 = KongaXmlUtils.getChildElementValue(var6, "numberRecordsFailed");
            long var13 = LongParser.parse(var12, 0L);
            String var15 = KongaXmlUtils.getChildElementValue(var6, "state");
            String var16 = KongaXmlUtils.getChildElementValue(var6, "stateMessage");
            BulkLoadBatchInfo var17 = new BulkLoadBatchInfo(var8, var7, var15, var10, 0L, 0L);
            var17.setFailureRecords(var13);
            var17.setSuccessRecords(var10 - var13);
            if (var16 != null) {
               var17.setStatusMessage(var16);
            }

            var1.add(var17);
         }
      }

      return var1;
   }

   public String getJobBaseUrl() {
      return this.jobBaseUrl;
   }

   public String getSessionID() {
      return this.SessionID;
   }
}
