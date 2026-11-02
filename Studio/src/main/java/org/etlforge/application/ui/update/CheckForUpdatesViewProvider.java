package org.etlforge.application.ui.update;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.application.update.BranchId;
import org.etlforge.application.update.LatestVersion;
import org.etlforge.application.update.UpdateService;

public final class CheckForUpdatesViewProvider {
   private final UpdateService updateService;
   private final Supplier<BranchId> branchIdSupplier;
   private CheckForUpdatesView view;

   public CheckForUpdatesViewProvider(UpdateService var1, Supplier<BranchId> var2) {
      this.updateService = Preconditions.checkNotNull(var1);
      this.branchIdSupplier = Preconditions.checkNotNull(var2);
   }

   public void openAndCheck(ApplicationWindow var1) {
      this.open(var1, new CheckForUpdatesViewProvider.Starter());
   }

   public void openAndShowLatestVersion(ApplicationWindow var1, LatestVersion var2) {
      this.open(var1, new CheckForUpdatesViewProvider.VersionDisplayer(var2));
   }

   private void open(ApplicationWindow var1, WindowSectionContentUpdater var2) {
      var1.displayContent(new CheckForUpdatesViewProvider.ContentProvider(), ApplicationWindow.SOUTH, var2, null);
   }

   private class ContentProvider implements WindowSectionContentProvider {
      private ContentProvider() {
      }

      @Override
      public String getContentId() {
         return CheckForUpdatesView.ID;
      }

      @Override
      public WindowSectionContent getContent(WindowSection var1) {
         return this.getView(var1);
      }

      private CheckForUpdatesView getView(WindowSection var1) {
         if (CheckForUpdatesViewProvider.this.view == null) {
            BranchId var2 = CheckForUpdatesViewProvider.this.branchIdSupplier.get();
            CheckForUpdatesViewProvider.this.view = new CheckForUpdatesView(var1, CheckForUpdatesViewProvider.this.updateService, var2);
         }

         return CheckForUpdatesViewProvider.this.view;
      }
   }

   private class Starter implements WindowSectionContentUpdater {
      private Starter() {
      }

      @Override
      public void update(WindowSectionContent var1) {
         ((CheckForUpdatesView)var1).checkForUpdates();
      }
   }

   private class VersionDisplayer implements WindowSectionContentUpdater {
      private final LatestVersion version;

      public VersionDisplayer(LatestVersion var2) {
         this.version = var2;
      }

      @Override
      public void update(WindowSectionContent var1) {
         ((CheckForUpdatesView)var1).showLatestVersion(this.version);
      }
   }
}
