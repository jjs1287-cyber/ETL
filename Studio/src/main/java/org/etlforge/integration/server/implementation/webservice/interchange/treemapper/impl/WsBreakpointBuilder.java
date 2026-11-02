package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.Breakpoint;

final class WsBreakpointBuilder {
   public static Breakpoint build(org.etlforge.integration.debug.client.Breakpoint var0) {
      Breakpoint var1 = new Breakpoint();
      var1.setLocation(WsBreakLocationBuilder.build(var0.getLocation()));
      var1.setConditionScript(var0.getConditionScript());
      var1.setConditionVariables(WsKeyValuePairsBuilder.build(var0.getConditionVariables()));
      return var1;
   }

   private WsBreakpointBuilder() {
   }
}
