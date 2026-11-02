package org.etlforge.integration.debug.client.file;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.util.id.KongaGuid;

public final class DefaultDataFilesInfo implements DataFilesInfo {
   private static final long serialVersionUID = 5477417612347484529L;
   private final KongaGuid guid;
   private final OperationId opId;
   private final TransformationId txId;
   private final ServerInfo serverInfo;
   private final EnumMap<DataFileType, List<DataFileInfo>> files;

   public DefaultDataFilesInfo(KongaGuid var1, ServerInfo var2, OperationId var3, TransformationId var4) {
      Preconditions.checkNotNull(var1, "guid");
      Preconditions.checkNotNull(var2, "serverInfo");
      Preconditions.checkNotNull(var4, "txId");
      this.guid = var1;
      this.serverInfo = var2;
      this.opId = var3;
      this.txId = var4;
      this.files = new EnumMap<>(DataFileType.class);

      for (DataFileType var8 : DataFileType.values()) {
         this.files.put(var8, new ArrayList<>());
      }
   }

   @Override
   public KongaGuid getGuid() {
      return this.guid;
   }

   @Override
   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   @Override
   public OperationId getOperationId() {
      return this.opId;
   }

   @Override
   public TransformationId getTransformationId() {
      return this.txId;
   }

   @Override
   public int getNumberOfOriginalFiles(DataFileType var1) {
      List var2 = this.files.get(var1);
      return var2.size();
   }

   @Override
   public DataFileInfo getFileInfo(DataFileType var1, int var2) {
      List var3 = this.files.get(var1);
      return (DataFileInfo)var3.get(var2);
   }

   @Override
   public boolean contains(DataFileIdentifier var1) {
      if (Objects.equal(this.opId, var1.operationId) && this.txId.equals(var1.transformationId)) {
         List var2 = this.files.get(var1.type);
         if (var2.size() >= var1.instanceNumber) {
            DataFileInfo var3 = (DataFileInfo)var2.get(var1.instanceNumber - 1);
            return var1.chunkNumber <= var3.getNumberOfChunks();
         }
      }

      return false;
   }

   @Override
   public boolean containsFileOfType(DataFileType var1) {
      List var2 = this.files.get(var1);
      return !var2.isEmpty();
   }

   public DefaultDataFilesInfo addFileInfo(DataFileInfo var1) {
      List var2 = this.files.get(var1.getType());
      var2.add(var1);
      return this;
   }
}
