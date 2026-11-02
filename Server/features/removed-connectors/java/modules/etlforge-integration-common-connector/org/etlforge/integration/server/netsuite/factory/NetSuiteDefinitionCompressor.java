package org.etlforge.integration.server.netsuite.factory;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.definition.NetSuiteConstants;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteOperationRootElements;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaGlobalElement;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaXsd;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdl;

public class NetSuiteDefinitionCompressor {
   final NetSuiteWsdl wsdl;
   final NetSuiteDefinition definition = new NetSuiteDefinition();
   Queue<XsdGlobalElementName> processingElements;
   Set<String> targetNamespaces;

   NetSuiteDefinitionCompressor(NetSuiteWsdl var1) {
      this.wsdl = var1;
   }

   public NetSuiteDefinition compress() {
      this.definition.setServiceUrl(this.wsdl.getServiceUrl());
      this.compressOperation();
      this.compressTypes();

      for (String var2 : this.targetNamespaces) {
         NetSuiteSchemaXsd var3 = this.wsdl.getDefinitions().getNetSuiteSchemaXsdExists(var2);
         this.definition.putNetSuiteSchemaXsdWithPrefix(var2, var3);
         var3.compress(this.targetNamespaces);
      }

      return this.definition;
   }

   private void compressOperation() {
      for (String var4 : NetSuiteConstants.instance.supportedOperations) {
         XsdGlobalElementName var5 = this.getOperationElement(var4 + "Request");
         XsdGlobalElementName var6 = this.getOperationElement(var4 + "Response");
         if (NetSuiteConstants.instance.isNetSuiteSearchOperation(var4)) {
            this.definition.getOperationRoots().put(var4, new NetSuiteOperationRootElements(null, var6));
         } else {
            this.definition.getOperationRoots().put(var4, new NetSuiteOperationRootElements(var5, var6));
         }
      }
   }

   private XsdGlobalElementName getOperationElement(String var1) {
      XsdGlobalElementName var2 = this.wsdl.getMessages().get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDefinitionError("operation " + var1 + " not found");
      } else {
         return var2;
      }
   }

   private void compressTypes() {
      this.processingElements = Queues.newArrayDeque();
      this.targetNamespaces = Sets.newHashSet();

      for (Entry var2 : this.wsdl.getHeaders().entrySet()) {
         this.processingElements.add((XsdGlobalElementName)var2.getValue());
      }

      for (Entry var6 : this.definition.getOperationRoots().entrySet()) {
         NetSuiteOperationRootElements var3 = (NetSuiteOperationRootElements)var6.getValue();
         if (var3.getRequestRoot() != null) {
            this.processingElements.add(var3.getRequestRoot());
         }

         this.processingElements.add(var3.getResponseRoot());
      }

      HashSet var5 = Sets.newHashSet();

      while (!this.processingElements.isEmpty()) {
         XsdGlobalElementName var7 = this.processingElements.poll();
         if (!var5.contains(var7)) {
            this.compressTypes(var7);
            var5.add(var7);
         }
      }
   }

   private void compressTypes(XsdGlobalElementName var1) {
      NetSuiteSchemaGlobalElement var2 = this.wsdl.getDefinitions().getNetSuiteSchemaGlobalElementExists(var1);
      this.processingElements.addAll(var2.getDependsGlobalElements());
      Set var3 = this.wsdl.getDefinitions().getDerivedTypes().getDeriveTypes(var1);
      if (var3 != null) {
         this.processingElements.addAll(var3);
         this.definition.getDerivedTypes().put(var1, var3);
      }

      this.targetNamespaces.add(var1.getNamespace());
      this.definition.putGlobalELement(var1, var2);
   }
}
