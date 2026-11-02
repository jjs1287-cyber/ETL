package org.etlforge.application.ui.update;

import com.google.common.base.Preconditions;
import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.update.prefs.UpdateDownloadLocationPreference;
import org.etlforge.application.update.LatestVersion;

final class DownloadLocationFactory {
   public static File getDestination(LatestVersion var0) {
      Preconditions.checkArgument(var0.hasDownloadUrl());
      File var1 = UpdateDownloadLocationPreference.INSTANCE.get();
      var1.mkdirs();
      String var2 = suggestFileName(var0);
      return new File(var1, var2);
   }

   private static String suggestFileName(LatestVersion var0) {
      return StringUtils.substringAfterLast(var0.getDownloadUrl(), "/");
   }

   private DownloadLocationFactory() {
   }
}
