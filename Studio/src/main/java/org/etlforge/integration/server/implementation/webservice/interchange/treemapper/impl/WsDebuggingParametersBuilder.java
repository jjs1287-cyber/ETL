package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.Breakpoint;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataElement;

final class WsDebuggingParametersBuilder {
   public static DebuggingParameters build(org.etlforge.integration.debug.client.DebuggingParameters var0) {
      DebuggingParameters var1 = new DebuggingParameters();
      var1.setDebuggingSessionId(var0.getSessionId().toString());
      var1.setCommand(var0.getCommand());
      var1.setObserveBreakpoints(var0.isObservingBreakpoints());
      var1.setBreakOnError(var0.isBreakingOnErrors());
      var1.setBreakOnWarning(var0.isBreakingOnWarnings());
      WsDataElement[] var2 = buildDataElements(var0);
      var1.setGlobalVariables(var2);
      Breakpoint[] var3 = buildBreakpoints(var0);
      var1.setBreakpoints(var3);
      return var1;
   }

   private static WsDataElement[] buildDataElements(org.etlforge.integration.debug.client.DebuggingParameters var0) {
      DataElement[] var1 = var0.getGlobalDataElements();
      WsDataElement[] var2 = new WsDataElement[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = WsDataElementBuilder.build(var1[var3]);
      }

      return var2;
   }

   private static Breakpoint[] buildBreakpoints(org.etlforge.integration.debug.client.DebuggingParameters var0) {
      org.etlforge.integration.debug.client.Breakpoint[] var1 = var0.getBreakpoints();
      Breakpoint[] var2 = new Breakpoint[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = WsBreakpointBuilder.build(var1[var3]);
      }

      return var2;
   }

   private WsDebuggingParametersBuilder() {
   }
}
