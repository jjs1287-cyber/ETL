package org.etlforge.integration.data.project;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public final class DownloadInfo {
   private final String serverName;
   private final long when;

   public DownloadInfo(String var1, Date var2) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "serverName may not be null or empty");
      this.serverName = var1;
      this.when = var2.getTime();
   }

   public String getServerName() {
      return this.serverName;
   }

   public Date getWhen() {
      return new Date(this.when);
   }
}
