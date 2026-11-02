package org.etlforge.integration.client.deploy.serverapi;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.io.FileUtils;

public class JitterPackServerBackup {
   private final IntegrationProjectId projectId;
   private final File jitterPack;
   private final String tag;

   public JitterPackServerBackup(IntegrationProjectId var1, File var2, String var3) {
      Preconditions.checkNotNull(var1, "projectId");
      Preconditions.checkNotNull(var2, "jitterPack");
      this.projectId = var1;
      this.jitterPack = var2;
      this.tag = var3;
   }

   public IntegrationProjectId getProjectId() {
      return this.projectId;
   }

   public String getBase64EncodedData() throws IOException {
      return FileUtils.base64EncodeFile(this.jitterPack);
   }

   public String getTag() {
      return this.tag;
   }
}
