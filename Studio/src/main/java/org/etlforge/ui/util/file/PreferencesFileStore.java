package org.etlforge.ui.util.file;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.io.File;
import java.util.LinkedHashSet;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.preference.PreferencesKey;

public final class PreferencesFileStore implements RecentFileStore {
   private final PreferencesKey key;
   private final int historySize;
   private final KongaPreferences preferences;
   private LinkedHashSet<FileUi> storedFiles;

   public PreferencesFileStore(PreferencesKey var1, int var2) {
      Preconditions.checkNotNull(var1, "key");
      Preconditions.checkArgument(var2 > 0, "size must be > 0 (was " + var2 + ")");
      this.key = var1;
      this.historySize = var2;
      this.preferences = KongaPreferences.getPreferences(var1);
      this.storedFiles = new LinkedHashSet<>();
      this.init();
   }

   private void init() {
      String var1 = this.key.keyName();

      for (int var2 = 0; var2 < this.historySize; var2++) {
         File var3 = this.preferences.getFile(getIndexedKeyName(var1, var2), null);
         if (var3 == null) {
            break;
         }

         this.storedFiles.add(new FileUi(var3));
      }
   }

   @Override
   public FileUi latestFile() {
      return this.storedFiles.isEmpty() ? null : Iterables.get(this.storedFiles, 0);
   }

   @Override
   public FileUi[] recentFiles() {
      return this.storedFiles.toArray(new FileUi[this.storedFiles.size()]);
   }

   @Override
   public boolean remove(File var1) {
      boolean var2 = false;

      for (FileUi var4 : this.storedFiles) {
         if (var4.getFile().equals(var1)) {
            this.storedFiles.remove(var4);
            var2 = true;
            break;
         }
      }

      if (var2) {
         this.updatePreferenceStore();
      }

      return var2;
   }

   @Override
   public void update(File var1) {
      if (var1 != null) {
         this.updateInternalStore(var1);
         this.updatePreferenceStore();
      }
   }

   private void updateInternalStore(File var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      var2.add(new FileUi(var1));

      for (FileUi var4 : this.storedFiles) {
         if (!var2.contains(var4)) {
            var2.add(var4);
         }
      }

      this.storedFiles = var2;
   }

   private void updatePreferenceStore() {
      String var1 = this.key.keyName();
      int var2 = 0;

      for (FileUi var4 : this.storedFiles) {
         this.preferences.putFile(getIndexedKeyName(var1, var2), var4.getFile());
         if (++var2 == this.historySize) {
            break;
         }
      }

      while (var2 < this.historySize) {
         this.preferences.putFile(getIndexedKeyName(var1, var2), null);
         var2++;
      }
   }

   private static String getIndexedKeyName(String var0, int var1) {
      return var1 < 1 ? var0 : var0 + "_" + (var1 + 1);
   }
}
