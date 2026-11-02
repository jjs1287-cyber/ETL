package org.etlforge.integration.debug.client.file;

import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.util.id.KongaGuid;

public final class DataFilesInfoBuilder {
   private final ServerInfo serverInfo;

   public DataFilesInfoBuilder(ServerInfo var1) {
      this.serverInfo = var1;
   }

   public DataFilesInfo buildForLoadSource(KongaGuid var1, TransformationId var2) {
      DefaultDataFilesInfo var3 = new DefaultDataFilesInfo(var1, this.serverInfo, null, var2);
      var3.addFileInfo(new DataFileInfo(DataFileType.SOURCE, 1));
      return var3;
   }

   public static DataFilesInfo buildForTestTransformation(TestTransformationResult var0) {
      KongaGuid var1 = var0.getDataFilesGuid();
      SourceDataIdentifier var2 = var0.getSourceDataIdentifier();
      DefaultDataFilesInfo var3 = new DefaultDataFilesInfo(var1, var2.getServerInfo(), null, var0.getDebuggeeId());
      if (!var2.isNoSource()) {
         var3.addFileInfo(new DataFileInfo(DataFileType.SOURCE, 1));
      }

      var3.addFileInfo(new DataFileInfo(DataFileType.LINK, 1));
      var3.addFileInfo(new DataFileInfo(DataFileType.TARGET, 1));
      return var3;
   }
}
