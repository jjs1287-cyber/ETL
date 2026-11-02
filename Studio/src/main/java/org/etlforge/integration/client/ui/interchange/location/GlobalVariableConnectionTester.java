package org.etlforge.integration.client.ui.interchange.location;
import org.etlforge.integration.data.location.GlobalVariableLocation;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.test.*;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.*;
public final class GlobalVariableConnectionTester extends LocationConnectionTester<GlobalVariableLocation> {
   public GlobalVariableConnectionTester(GlobalVariableLocation location) { super(location); }
   private final class Result extends ResultProducingServerCallback<TestConnectionResult> {
      Result(IntegrationEntity entity) {
         if (GlobalVariableLocation.isValidVariableName(location.getVariableName())) set(TestConnectionResult.success(entity));
         else caught(new IllegalArgumentException("Invalid global variable name. Runtime values can only be checked inside an operation."));
      }
   }
   @Override protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source entity, TestSourceConnectionApi api) { return new Result(entity); }
   @Override protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target entity, TestTargetConnectionApi api) { return new Result(entity); }
}
