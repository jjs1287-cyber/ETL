package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.GeneratorInstance;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.BreakLocation;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataElement;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestResult;

public final class TestResultBuilder {
   public static TestResult build(WsTestResult var0, ServerGuid var1, String var2, DebugSession var3, String var4) {
      return new TestResultBuilder.TestResultImpl(var0, var1, var2, var3, var4);
   }

   private static final class TestResultImpl implements TestResult {
      private final BreakLocationImpl[] callStack;
      private final DebugSession session;
      private final boolean done;
      private final String result;
      private final String errors;
      private final String warnings;
      private final Collection<DataElement> dataElements;

      public TestResultImpl(WsTestResult var1, ServerGuid var2, String var3, DebugSession var4, String var5) {
         this.session = var4;
         this.done = checkDone(var1);
         this.callStack = this.buildCallStack(var1, var4.getDebuggeeId(), var5);
         this.dataElements = this.buildDataElements(var1);
         this.warnings = var1.getWarnings();
         this.result = var1.getResult();
         this.errors = var1.getErrors();
      }

      private static boolean checkDone(WsTestResult var0) {
         return StringUtils.isEmpty(var0.getDebuggingSessionId());
      }

      private Collection<DataElement> buildDataElements(WsTestResult var1) {
         int var2 = 0;
         if (var1.getDataElements() != null) {
            var2 = var1.getDataElements().length;
         }

         ArrayList var3 = Lists.newArrayList();

         for (int var4 = 0; var4 < var2; var4++) {
            WsDataElement var5 = var1.getDataElements()[var4];
            var3.add(DataElementBuilder.build(var5));
         }

         return Collections.unmodifiableCollection(var3);
      }

      private BreakLocationImpl[] buildCallStack(WsTestResult var1, IntegrationEntityId var2, String var3) {
         int var4 = var1.getCallStack() != null ? var1.getCallStack().length : 0;
         BreakLocationImpl[] var5 = new BreakLocationImpl[var4];

         for (int var6 = 0; var6 < var4; var6++) {
            BreakLocation var7 = var1.getCallStack()[var6];
            GeneratorInstance var8 = this.createGeneratorInstance(var1);
            var5[var6] = new BreakLocationImpl(var7, var3, var8);
         }

         IntegrationEntityId var9 = var2;

         for (int var10 = var4 - 1; var10 >= 0; var10--) {
            BreakLocationImpl var11 = var5[var10];
            if (var11.getIntegrationEntityId() == null) {
               var11.fillInIntegrationEntityId(var9);
            }

            var9 = var11.getIntegrationEntityId();
         }

         return var5;
      }

      private GeneratorInstance createGeneratorInstance(WsTestResult var1) {
         WsKeyValuePairs var2 = var1.getGeneralKeyValues();
         if (var2 == null) {
            return null;
         }

         String var3 = null;
         String var4 = null;

         for (WsKeyValue var8 : var2.getKeyValues()) {
            if ("generator_path".equals(var8.getKey())) {
               var3 = var8.getValue();
            } else if ("instance_string".equals(var8.getKey())) {
               var4 = var8.getValue();
            }

            if (var3 != null && var4 != null) {
               return new GeneratorInstance(var3, var4);
            }
         }

         return null;
      }

      @Override
      public DebugSession getSession() {
         return this.session;
      }

      @Override
      public IntegrationEntityId getDebuggeeId() {
         return this.session.getDebuggeeId();
      }

      @Override
      public IntegrationEntityId getIdOfEntityToDisplay() {
         org.etlforge.integration.debug.client.BreakLocation var1 = this.getCurrentLocation();
         return var1 != null ? var1.getIntegrationEntityId() : this.getDebuggeeId();
      }

      @Override
      public org.etlforge.integration.debug.client.BreakLocation getCurrentLocation() {
         return this.callStack.length > 0 ? this.callStack[0] : null;
      }

      @Override
      public org.etlforge.integration.debug.client.BreakLocation[] getCallStack() {
         return this.callStack;
      }

      @Override
      public Collection<DataElement> getDataElements() {
         return this.dataElements;
      }

      @Override
      public String getErrors() {
         return this.errors;
      }

      @Override
      public String getWarnings() {
         return this.warnings;
      }

      @Override
      public boolean isDone() {
         return this.done;
      }

      @Override
      public String getResult() {
         return this.result;
      }
   }
}
