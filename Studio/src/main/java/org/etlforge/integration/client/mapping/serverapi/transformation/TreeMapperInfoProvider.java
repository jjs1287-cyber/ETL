package org.etlforge.integration.client.mapping.serverapi.transformation;

import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.entity.webservicecall.WebServiceOperationIdentifier;

public interface TreeMapperInfoProvider extends ServerCall {
   void getJtrForStructure(String var1, String var2, boolean var3, GetMappingStructureCallback var4);

   void getJtrForCombinedRequestAndResponse(String var1, String var2, String var3, String var4, boolean var5, GetMappingStructureCallback var6);

   void getJtrForWS(
      WebServiceCallId var1,
      String var2,
      String var3,
      WebServiceOperationIdentifier var4,
      String var5,
      String var6,
      boolean var7,
      boolean var8,
      GetMappingStructureCallback var9
   );
}
