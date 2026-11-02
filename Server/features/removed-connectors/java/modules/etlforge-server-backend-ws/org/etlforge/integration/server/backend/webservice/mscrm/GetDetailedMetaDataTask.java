package org.etlforge.integration.server.backend.webservice.mscrm;

import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEngineMsCrm;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.zip.ZipUtils;

final class GetDetailedMetaDataTask extends DesignTask<WsMetaDataResponse> {
   private final String objectName;

   public GetDetailedMetaDataTask(String var1, String var2, WsEndpointParameters var3, String var4) {
      super("Get Specific Metadata", var1, var2, var3);
      this.objectName = ArgCheck.checkNotEmpty(var4, "objectName");
   }

   protected WsMetaDataResponse runTask(ConnectorEngineMsCrm var1) throws Exception {
      MSCrmObject var2 = var1.getDetailedMetaData(this.objectName);
      return createResponse(var2);
   }

   private static WsMetaDataResponse createResponse(MSCrmObject var0) throws IOException {
      ArrayList var1 = Lists.newArrayList(var0);
      byte[] var2 = SerializationUtils.serialize(var1);
      return new WsMetaDataResponse(successStatus(), ZipUtils.deflateAndBase64EncodeInputStream(new ByteArrayInputStream(var2)));
   }
}
