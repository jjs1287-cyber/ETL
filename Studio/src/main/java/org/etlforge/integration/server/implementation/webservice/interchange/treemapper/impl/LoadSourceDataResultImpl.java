package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataFile;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsLoadSourceResult;
import org.etlforge.util.id.KongaGuid;

final class LoadSourceDataResultImpl implements LoadSourceDataResult {
   private final SourceDataIdentifier id;
   private final TransformationId txId;
   private final DataFile data;

   public LoadSourceDataResultImpl(WsLoadSourceResult var1, ServerGuid var2, String var3, TransformationId var4) {
      KongaGuid var5 = KongaGuid.createGuid(var1.getGuid());
      ServerInfo var6 = new ServerInfo(var2, var3);
      this.id = new SourceDataIdentifier(var6, var5);
      this.txId = var4;
      this.data = this.getDataFile(var1);
   }

   private DataFile getDataFile(WsLoadSourceResult var1) {
      WsDataFile var2 = var1.getFile();
      String var3 = var2.getLocalPath();
      String var4 = var2.getCompressedData();
      return new DataFile(var3, var4);
   }

   @Override
   public SourceDataIdentifier getIdentifier() {
      return this.id;
   }

   @Override
   public TransformationId getTransformationId() {
      return this.txId;
   }

   @Override
   public DataFile getSourceData() {
      return this.data;
   }
}
