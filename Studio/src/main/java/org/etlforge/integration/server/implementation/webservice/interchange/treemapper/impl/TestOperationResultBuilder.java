package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestOperationResult;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.file.DataFileInfo;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.integration.debug.client.file.DefaultDataFilesInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataFileInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataFilesInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestOperationResult;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestResult;
import org.etlforge.util.id.KongaGuid;

final class TestOperationResultBuilder {
   private TestOperationResultBuilder() {
   }

   public static TestOperationResult build(WsTestOperationResult var0, ServerGuid var1, String var2, DebugSession var3, String var4) {
      WsTestResult var5 = new WsTestResult();
      var5.setCallStack(var0.getCallStack());
      var5.setDataElements(var0.getDataElements());
      var5.setDebuggingSessionId(var0.getDebuggingSessionId());
      var5.setErrors(var0.getErrors());
      var5.setGeneralKeyValues(var0.getGeneralKeyValues());
      var5.setResult(var0.getIntermediateResult());
      var5.setWarnings(var0.getWarnings());
      TestResult var6 = TestResultBuilder.build(var5, var1, var2, var3, var4);
      return new TestOperationResultBuilder.ResultImpl(var6, var0, new ServerInfo(var1, var2));
   }

   private static class Key {
      public final Pair<OperationId, TransformationId> data;

      public Key(OperationId var1, TransformationId var2) {
         this.data = Pair.of(var1, var2);
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof TestOperationResultBuilder.Key && this.data.equals(((TestOperationResultBuilder.Key)var1).data);
      }
   }

   private static class ResultImpl implements TestOperationResult {
      private final TestResult baseImpl;
      private final Map<TransformationId, DataFilesInfo> files;

      public ResultImpl(TestResult var1, WsTestOperationResult var2, ServerInfo var3) {
         this.baseImpl = var1;
         if (var2.getDataFileInfo() != null) {
            this.files = this.buildFileInfo(var2, var3);
         } else {
            this.files = Maps.newHashMap();
         }
      }

      private final Map<TransformationId, DataFilesInfo> buildFileInfo(WsTestOperationResult var1, ServerInfo var2) {
         Map var3 = this.collectAllOperationTransformationPairs(var1, var2);
         HashMap var4 = Maps.newHashMap();

         for (Object __etlItem81 : var3.entrySet()) {
            Entry var6 = (Entry) __etlItem81;
            TransformationId var7 = ((TestOperationResultBuilder.Key)var6.getKey()).data.second;
            if (!var4.containsKey(var7)) {
               var4.put(var7, var6.getValue());
            }
         }

         return var4;
      }

      private Map<TestOperationResultBuilder.Key, DefaultDataFilesInfo> collectAllOperationTransformationPairs(WsTestOperationResult var1, ServerInfo var2) {
         HashMap var3 = Maps.newHashMap();
         WsDataFilesInfo var4 = var1.getDataFileInfo();
         KongaGuid var5 = KongaGuid.createGuid(var4.getGuid());

         for (WsDataFileInfo var9 : var4.getDataFiles()) {
            OperationId var10 = new OperationId(var9.getOperationGuid());
            TransformationId var11 = new TransformationId(var9.getTransformationGuid());
            TestOperationResultBuilder.Key var12 = new TestOperationResultBuilder.Key(var10, var11);
            DefaultDataFilesInfo var13 = (DefaultDataFilesInfo)var3.get(var12);
            if (var13 == null) {
               var13 = new DefaultDataFilesInfo(var5, var2, var10, var11);
               var3.put(var12, var13);
            }

            DataFileType var14 = DataFileType.values()[var9.getType()];
            DataFileInfo var15 = new DataFileInfo(var14, var9.getNumberOfChunks());
            var13.addFileInfo(var15);
         }

         return var3;
      }

      @Override
      public DebugSession getSession() {
         return this.baseImpl.getSession();
      }

      @Override
      public IntegrationEntityId getDebuggeeId() {
         return this.baseImpl.getDebuggeeId();
      }

      @Override
      public IntegrationEntityId getIdOfEntityToDisplay() {
         return this.baseImpl.getIdOfEntityToDisplay();
      }

      @Override
      public boolean isDone() {
         return this.baseImpl.isDone();
      }

      @Override
      public Collection<DataElement> getDataElements() {
         return this.baseImpl.getDataElements();
      }

      @Override
      public BreakLocation getCurrentLocation() {
         return this.baseImpl.getCurrentLocation();
      }

      @Override
      public BreakLocation[] getCallStack() {
         return this.baseImpl.getCallStack();
      }

      @Override
      public String getResult() {
         return this.baseImpl.getResult();
      }

      @Override
      public String getWarnings() {
         return this.baseImpl.getWarnings();
      }

      @Override
      public String getErrors() {
         return this.baseImpl.getErrors();
      }

      @Override
      public String getIntermediateResult() {
         return this.baseImpl.getResult();
      }

      @Override
      public Map<TransformationId, DataFilesInfo> getDataFilesInfo() {
         return Maps.newHashMap(this.files);
      }
   }
}
