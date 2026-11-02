package org.etlforge.integration.server.implementation.webservice.sfdc.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.salesforce.query.Field;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsQueryField;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsQueryRecord;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsQueryResult;

final class QueryResultDeserializer {
   public static QueryResult deserialize(WsQueryResult var0) {
      List var1 = deserializeRecords(var0.getRecords());
      boolean var2 = var0.isDone();
      return new QueryResult(var1, var2, null);
   }

   private static List<QueryRecord> deserializeRecords(WsQueryRecord[] var0) {
      ArrayList var1 = Lists.newArrayList();
      if (var0 != null) {
         for (WsQueryRecord var5 : var0) {
            QueryRecord var6 = deserializeRecord(var5);
            var1.add(var6);
         }
      }

      return var1;
   }

   private static QueryRecord deserializeRecord(WsQueryRecord var0) {
      String var1 = var0.getName();
      List var2 = deserializeFields(var0);
      List var3 = deserializeRecords(var0.getChildren());
      return new QueryRecord(var1, var2, var3);
   }

   private static List<Field> deserializeFields(WsQueryRecord var0) {
      ArrayList var1 = Lists.newArrayList();
      WsQueryField[] var2 = var0.getFields();
      if (var2 != null) {
         for (WsQueryField var6 : var2) {
            Field var7 = new Field(var6.getName(), var6.getValue());
            var1.add(var7);
         }
      }

      return var1;
   }

   private QueryResultDeserializer() {
   }
}
