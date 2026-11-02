package org.etlforge.application.prefs;

import java.io.File;

public class FilePreference extends Preference<File> {
   public FilePreference(Class<?> var1, String[] var2, String var3, File var4) {
      super(var1, var2, var3, var4);
   }

   public File get() {
      String var1 = this.getNode().get(this.getKey(), null);
      return var1 != null ? new File(var1) : this.getDefaultValue();
   }

   public void set(File var1) {
      File var2 = this.get();
      if (var1 != null) {
         this.getNode().put(this.getKey(), var1.getAbsolutePath());
      } else {
         this.clear();
      }

      this.fireValueChanged(var2, var1);
   }
}
