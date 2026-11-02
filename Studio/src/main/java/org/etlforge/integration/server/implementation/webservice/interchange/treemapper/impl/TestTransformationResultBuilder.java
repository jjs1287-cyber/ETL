package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.util.Collection;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataFile;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestResult;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestTransformationResult;
import org.etlforge.util.id.KongaGuid;

final class TestTransformationResultBuilder {
   private TestTransformationResultBuilder() {
   }

   public static TestTransformationResult build(
      WsTestTransformationResult var0, DebugSession var1, KongaGuid var2, ServerGuid var3, String var4, IntegrationEntityId var5, String var6
   ) {
      WsTestResult var7 = new WsTestResult();
      var7.setCallStack(var0.getCallStack());
      var7.setDataElements(var0.getDataElements());
      var7.setDebuggingSessionId(var0.getDebuggingSessionId());
      var7.setErrors(var0.getErrors());
      var7.setGeneralKeyValues(var0.getGeneralKeyValues());
      var7.setResult(var0.getIntermediateResult());
      var7.setWarnings(var0.getWarnings());
      TestResult var8 = TestResultBuilder.build(var7, var3, var4, var1, var6);
      SourceDataIdentifier var9 = createSourceDataIdentifier(var2, var3, var4);
      return new TestTransformationResultBuilder.ResultImpl((TransformationId)var1.getDebuggeeId(), var9, var8, var0);
   }

   private static SourceDataIdentifier createSourceDataIdentifier(KongaGuid var0, ServerGuid var1, String var2) {
      ServerInfo var3 = new ServerInfo(var1, var2);
      return var0 == null ? SourceDataIdentifier.noSource(var3) : new SourceDataIdentifier(var3, var0);
   }

   private static class ResultImpl implements TestTransformationResult {
      private final TestResult baseImpl;
      private final TransformationId txId;
      private final KongaGuid dataFilesGuid;
      private final SourceDataIdentifier sourceDataId;
      private final DataFile linkFile;
      private final DataFile targetFile;

      public ResultImpl(TransformationId var1, SourceDataIdentifier var2, TestResult var3, WsTestTransformationResult var4) {
         this.txId = var1;
         this.sourceDataId = var2;
         this.baseImpl = var3;
         this.dataFilesGuid = createDataFilesGuid(var4);
         this.linkFile = createDataFile(var4.getLinkFile());
         this.targetFile = createDataFile(var4.getTargetFile());
      }

      private static KongaGuid createDataFilesGuid(WsTestTransformationResult var0) {
         String var1 = var0.getDataFileGuid();
         return var1 == null ? null : KongaGuid.createGuid(var1);
      }

      private static DataFile createDataFile(WsDataFile var0) {
         return var0 == null ? null : new DataFile(var0.getLocalPath(), var0.getCompressedData());
      }

      @Override
      public DebugSession getSession() {
         return this.baseImpl.getSession();
      }

      @Override
      public TransformationId getDebuggeeId() {
         return this.txId;
      }

      @Override
      public IntegrationEntityId getIdOfEntityToDisplay() {
         return this.baseImpl.getIdOfEntityToDisplay();
      }

      @Override
      public SourceDataIdentifier getSourceDataIdentifier() {
         return this.sourceDataId;
      }

      @Override
      public KongaGuid getDataFilesGuid() {
         return this.dataFilesGuid;
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
      public DataFile getLinkFile() {
         return this.linkFile;
      }

      @Override
      public DataFile getTargetFile() {
         return this.targetFile;
      }
   }
}
