package org.etlforge.integration.client.project.jitterpack;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public final class ImportParameters extends JitterPackParameters {
   private final boolean createNewGUIDsWhenImporting;
   private final String newProjectName;
   public static final ImportParameters DEFAULT = new ImportParameters(null, false);

   public ImportParameters(char[] var1) {
      this(var1, false, null);
   }

   public ImportParameters(char[] var1, boolean var2) {
      this(var1, var2, null);
   }

   public ImportParameters(char[] var1, boolean var2, String var3) {
      super(var1);
      this.createNewGUIDsWhenImporting = var2;
      this.newProjectName = StringUtils.isBlank(var3) ? null : var3.trim();
      Preconditions.checkArgument(this.newProjectName == null || var2, "The project can be given a new name only if it is also given new GUIDs");
   }

   public boolean createNewGUIDsWhenImporting() {
      return this.createNewGUIDsWhenImporting;
   }

   public String getNewProjectName() {
      return this.newProjectName;
   }
}
