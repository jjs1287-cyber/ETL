package org.etlforge.integration.design.netsuite.schema;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjects;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionConnector;
import org.etlforge.integration.design.netsuite.definition.NetSuiteOperationRootElements;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaGlobalElement;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class ConnectorNetSuiteSchemaCollector {
   Map<String, ConnectorNetSuiteSchemaGlobalELements> schemaData = Maps.newHashMap();
   final Queue<XsdGlobalElementName> ProcessingItems = Queues.newArrayDeque();
   final Set<XsdGlobalElementName> ProcessedItems = Sets.newHashSet();
   final List<XsdGlobalElementName> recordObjects;
   final boolean isSearchRow;
   final NetSuiteDefinition instance;

   public ConnectorNetSuiteSchemaCollector(NetSuiteDefinition var1, NetSuiteOperationRootElements var2, NetSuiteObjects var3) {
      this.instance = var1;
      if (var2.getRequestRoot() != null) {
         this.ProcessingItems.add(var2.getRequestRoot());
      }

      this.ProcessingItems.add(var2.getResponseRoot());
      this.recordObjects = this.convert(var3);
      this.isSearchRow = var3.isSearchRow();
   }

   private List<XsdGlobalElementName> convert(NetSuiteObjects var1) {
      ArrayList var2 = Lists.newArrayList();

      for (NetSuiteObject var4 : var1.getRecordObjects()) {
         var2.add(var4.getTypeXsdGlobalElementName());
      }

      return var2;
   }

   public Map<String, ConnectorNetSuiteSchemaGlobalELements> collect() {
      while (!this.ProcessingItems.isEmpty()) {
         XsdGlobalElementName var1 = this.ProcessingItems.poll();
         if (!this.ProcessedItems.contains(var1)) {
            this.process(var1);
            this.ProcessedItems.add(var1);
         }
      }

      return this.schemaData;
   }

   void process(XsdGlobalElementName var1) {
      this.processDependNamespaces(var1);
      String var2 = var1.getNamespace();
      ConnectorNetSuiteSchemaGlobalELements var3 = this.getConnectorNetSuiteSchemaGlobalELements(var2);
      var3.globalElements.add(var1);
   }

   private void processDependNamespaces(XsdGlobalElementName var1) {
      String var2 = var1.getNamespace();
      ConnectorNetSuiteSchemaGlobalELements var3 = this.getConnectorNetSuiteSchemaGlobalELements(var2);
      NetSuiteDefinitionConnector var4 = this.instance.getDefinitionConnector();
      boolean var5 = var4.isReplacedBaseType(this.isSearchRow, var1);
      if (var5) {
         this.processDepend(var1, this.recordObjects, var3);
      } else {
         NetSuiteSchemaGlobalElement var6 = this.instance.getNetSuiteSchemaGlobalElementExists(var1);
         this.processDepend(var1, var6.getDependsGlobalElements(), var3);
         Set var7 = this.instance.getDerivedTypes().getDeriveTypes(var1);
         if (var7 != null) {
            this.processDepend(var1, var7, var3);
         }
      }
   }

   private ConnectorNetSuiteSchemaGlobalELements getConnectorNetSuiteSchemaGlobalELements(String var1) {
      ConnectorNetSuiteSchemaGlobalELements var2 = this.schemaData.get(var1);
      if (var2 == null) {
         var2 = new ConnectorNetSuiteSchemaGlobalELements();
         this.schemaData.put(var1, var2);
      }

      return var2;
   }

   void processDepend(XsdGlobalElementName var1, Set<XsdGlobalElementName> var2, ConnectorNetSuiteSchemaGlobalELements var3) {
      if (var2 != null) {
         for (XsdGlobalElementName var5 : var2) {
            this.processDepend(var1, var5, var3);
         }
      }
   }

   void processDepend(XsdGlobalElementName var1, List<XsdGlobalElementName> var2, ConnectorNetSuiteSchemaGlobalELements var3) {
      if (var2 != null) {
         for (XsdGlobalElementName var5 : var2) {
            this.processDepend(var1, var5, var3);
         }
      }
   }

   boolean processDepend(XsdGlobalElementName var1, XsdGlobalElementName var2, ConnectorNetSuiteSchemaGlobalELements var3) {
      this.ProcessingItems.add(var2);
      return var2.getNamespace().equals(var1.getNamespace()) ? false : var3.dependNamespaces.add(var2.getNamespace());
   }
}
