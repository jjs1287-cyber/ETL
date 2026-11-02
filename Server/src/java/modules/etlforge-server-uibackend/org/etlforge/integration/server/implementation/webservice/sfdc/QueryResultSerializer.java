package org.etlforge.integration.server.implementation.webservice.sfdc;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.salesforce.query.Field;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;

final class QueryResultSerializer {
   public static WsQueryResult serialize(QueryResult var0) {
      WsQueryResult var1 = new WsQueryResult();
      var1.setDone(var0.isDone());
      WsQueryRecord[] var2 = serializeRecords(var0.getRecords());
      var1.setRecords(var2);
      WsSalesforceError var3 = new WsSalesforceError();
      var1.setError(var3);
      return var1;
   }

   private static WsQueryRecord[] serializeRecords(Iterable<QueryRecord> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (QueryRecord var3 : var0) {
         WsQueryRecord var4 = serializeRecord(var3);
         var1.add(var4);
      }

      return (WsQueryRecord[]) var1.toArray(new WsQueryRecord[var1.size()]);
   }

   private static WsQueryRecord serializeRecord(QueryRecord var0) {
      WsQueryRecord var1 = new WsQueryRecord();
      var1.setName(var0.getName());
      WsQueryField[] var2 = serializeFields(var0);
      var1.setFields(var2);
      WsQueryRecord[] var3 = serializeRecords(var0.getChildren());
      var1.setChildren(var3);
      return var1;
   }

   private static WsQueryField[] serializeFields(QueryRecord var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Field var3 : var0.getFields()) {
         WsQueryField var4 = new WsQueryField(var3.getName(), var3.getValueAsString());
         var1.add(var4);
      }

      return (WsQueryField[]) var1.toArray(new WsQueryField[var1.size()]);
   }

   private QueryResultSerializer() {
   }
}
