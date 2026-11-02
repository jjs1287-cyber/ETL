package org.etlforge.integration.server.backend.webservice.mscrm;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEngineMsCrm;
import org.etlforge.util.zip.ZipUtils;

final class GetObjectDescriptionsTask extends DesignTask<WsMetaDataResponse> {
   public GetObjectDescriptionsTask(String var1, String var2, WsEndpointParameters var3) {
      super("getObjectDescriptions", var1, var2, var3);
   }

   protected WsMetaDataResponse runTask(ConnectorEngineMsCrm var1) throws Exception {
      ArrayList var2 = var1.describeAllMSCrmObjects();
      byte[] var3 = SerializationUtils.serialize(var2);
      return new WsMetaDataResponse(successStatus(), ZipUtils.deflateAndBase64EncodeInputStream(new ByteArrayInputStream(var3)));
   }
}
