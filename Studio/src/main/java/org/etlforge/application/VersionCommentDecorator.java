package org.etlforge.application;

import com.google.common.base.Preconditions;
import org.etlforge.util.version.Version;
import org.etlforge.util.version.VersionDecorator;

public class VersionCommentDecorator implements VersionDecorator {
   private final String comment;

   public VersionCommentDecorator(String var1) {
      Preconditions.checkNotNull(var1, "comment");
      this.comment = var1;
   }

   public final Version apply(Version var1) {
      String var2 = this.comment;
      if (!var1.getComment().isEmpty()) {
         var2 = var2 + "-" + var1.getComment();
      }

      return VersionFactory.newVersion(var1.getMajorVersion(), var1.getMinorVersion(), var1.getPatchLevel(), var1.getBuildNumber(), var2);
   }
}
