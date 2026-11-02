package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.bind.XmlObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.etlforge.integration.salesforce.query.Field;
import org.etlforge.integration.salesforce.query.FieldList;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;

final class QueryResultBuilder {
   private final List<QueryResultBuilder.RecordBuilder> recordBuilders = Lists.newArrayList();
   private final Stack<QueryResultBuilder.RecordBuilder> recordStack = new Stack<>();
   private final Set<String> childRelationships = Sets.newHashSet();

   public QueryResult translate(com.sforce.soap.partner.QueryResult var1) {
      for (SObject var5 : var1.getRecords()) {
         this.processRecord(var5);
      }

      this.correctEmptyChildRecords();
      boolean var6 = var1.isDone();
      return new QueryResult(buildRecords(this.recordBuilders), var6, var1.getQueryLocator());
   }

   private void processRecord(SObject var1) {
      String var2 = var1.getType();
      this.processRecord(var1, var2);
   }

   private void processRecord(XmlObject var1, String var2) {
      QueryResultBuilder.RecordBuilder var3 = new QueryResultBuilder.RecordBuilder(var2);
      this.recordStack.push(var3);
      Iterator var4 = var1.getChildren();

      while (var4.hasNext()) {
         XmlObject var5 = (XmlObject)var4.next();
         String var6 = var5.getName().getLocalPart();
         if (var5.hasChildren()) {
            this.processNestedRecord(var5, var6);
         } else {
            Object var7 = var5.getValue();
            var3.addField(var6, var7);
         }
      }

      this.recordStack.pop();
      if (this.recordStack.isEmpty()) {
         this.recordBuilders.add(var3);
      } else {
         this.recordStack.peek().addChild(var3);
      }
   }

   private void processNestedRecord(XmlObject var1, String var2) {
      QueryResultBuilder.NestedRecordType var3 = null;
      Iterator var4 = var1.getChildren();

      while (var4.hasNext()) {
         XmlObject var5 = (XmlObject)var4.next();
         if (var3 == null) {
            var3 = QueryResultBuilder.NestedRecordType.fromXmlObject(var5);
         }

         switch (var3) {
            case PARENT:
               this.processRecord(var1, var2);
               break;
            case CHILD:
               this.processChildRecord(var2, var4);
         }

         if (!var5.hasChildren()) {
         }
      }
   }

   private void processChildRecord(String var1, Iterator<XmlObject> var2) {
      this.childRelationships.add(var1);

      while (var2.hasNext()) {
         XmlObject var3 = (XmlObject)var2.next();
         if (var3.hasChildren()) {
            this.processRecord(var3, var1);
            break;
         }
      }
   }

   private void correctEmptyChildRecords() {
      if (!this.childRelationships.isEmpty()) {
         for (QueryResultBuilder.RecordBuilder var2 : this.recordBuilders) {
            var2.correctEmptyChildRecords(this.childRelationships);
         }
      }
   }

   private static Iterable<QueryRecord> buildRecords(Iterable<QueryResultBuilder.RecordBuilder> var0) {
      return Iterables.transform(var0, new Function<QueryResultBuilder.RecordBuilder, QueryRecord>() {
         public QueryRecord apply(QueryResultBuilder.RecordBuilder var1) {
            return var1.buildRecord();
         }
      });
   }

   private enum NestedRecordType {
      PARENT,
      CHILD;

      public static QueryResultBuilder.NestedRecordType fromXmlObject(XmlObject var0) {
         String var1 = var0.getName().getLocalPart();
         if ("type".equals(var1)) {
            return PARENT;
         } else if ("done".equals(var1)) {
            return CHILD;
         } else {
            throw new RuntimeException("Unexpected XML object in response: " + var0);
         }
      }
   }

   private static class RecordBuilder {
      private final String objectType;
      private final FieldList fields = new FieldList();
      private final LinkedHashMap<String, QueryResultBuilder.RecordBuilder> children = Maps.newLinkedHashMap();

      public RecordBuilder(String var1) {
         this.objectType = var1;
      }

      public void addField(String var1, Object var2) {
         this.fields.add(new Field(var1, var2));
      }

      public void addChild(QueryResultBuilder.RecordBuilder var1) {
         this.children.put(var1.objectType, var1);
      }

      public void correctEmptyChildRecords(Iterable<String> var1) {
         this.fields.remove(var1);

         for (String var3 : var1) {
            if (!this.children.containsKey(var3)) {
               this.addChild(new QueryResultBuilder.RecordBuilder(var3));
            }
         }
      }

      public QueryRecord buildRecord() {
         return new QueryRecord(this.objectType, this.fields, QueryResultBuilder.buildRecords(this.children.values()));
      }
   }
}
