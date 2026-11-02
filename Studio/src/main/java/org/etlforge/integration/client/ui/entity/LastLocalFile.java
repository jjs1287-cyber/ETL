package org.etlforge.integration.client.ui.entity;

import java.io.File;
import javax.swing.filechooser.FileSystemView;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.util.preference.PreferencesKey;

public final class LastLocalFile {
   private final RecentFileStore recentFileStore;

   public static LastLocalFile forType(SourceTarget var0) {
      RecentFileStore var1 = getRecentFileStoreForType(var0 == SourceTarget.Source ? EntityType.Source : EntityType.Target);
      return new LastLocalFile(var1);
   }

   public static LastLocalFile forSource() {
      return forType(SourceTarget.Source);
   }

   public static LastLocalFile forTarget() {
      return forType(SourceTarget.Source);
   }

   public static <T extends Locatable> LastLocalFile forType(Class<T> var0) {
      return var0.equals(Source.class) ? forSource() : forTarget();
   }

   private static RecentFileStore getRecentFileStoreForType(EntityType var0) {
      PreferencesKey var1 = new PreferencesKey(LocalFileLocation.class, String.format("latest_local_%s_file", var0.name().toLowerCase()));
      return new PreferencesFileStore(var1, 1);
   }

   private LastLocalFile(RecentFileStore var1) {
      this.recentFileStore = var1;
   }

   public RecentFileStore getRecentFileStore() {
      return this.recentFileStore;
   }

   public File getSuggestedParent() {
      FileUi var1 = this.recentFileStore.latestFile();
      if (var1 != null) {
         File var2 = var1.getFile();
         if (var2.isDirectory()) {
            return var2;
         }

         File var3 = var2.getParentFile();
         if (var3 != null) {
            return var3;
         }
      }

      FileSystemView var4 = FileSystemView.getFileSystemView();
      return var4.getDefaultDirectory();
   }
}
