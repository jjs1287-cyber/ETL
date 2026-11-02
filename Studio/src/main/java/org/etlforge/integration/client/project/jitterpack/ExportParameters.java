package org.etlforge.integration.client.project.jitterpack;

import org.etlforge.integration.client.info.KeepPasswordsOnClientProperty;

public final class ExportParameters extends JitterPackParameters {
   private final PackingFileFilter fileFilter;
   private final boolean exportPasswords;
   private final boolean readOnly;
   public static final ExportParameters DEFAULT = new ExportParameters();

   public ExportParameters() {
      this(PackingFileFilter.DEFAULT, null, KeepPasswordsOnClientProperty.getValue(), false);
   }

   public ExportParameters(PackingFileFilter var1, char[] var2, boolean var3, boolean var4) {
      super(var2);
      this.fileFilter = var1;
      this.exportPasswords = var3;
      this.readOnly = var4;
   }

   public PackingFileFilter getFileFilter() {
      return this.fileFilter;
   }

   public boolean arePasswordsExported() {
      return this.exportPasswords;
   }

   public boolean isPackedProjectReadOnly() {
      return this.readOnly;
   }
}
