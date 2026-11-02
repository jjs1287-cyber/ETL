package org.etlforge.integration.client.ui;

import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.log.ApplicationLogHistoryPreference;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.integration.client.runtime.appdir.ApplicationDirectoryLocationPreference;
import org.etlforge.integration.client.runtime.appdir.DefaultLocation;
import org.etlforge.integration.client.server.prefs.ServerCallTimeoutPreference;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;

public final class GeneralPreferencesPage extends AbstractPreferencesPage {
   public GeneralPreferencesPage() {
      this(true, true);
   }

   public GeneralPreferencesPage(boolean var1, boolean var2) {
      super(new String[]{"General"});
      if (var1) {
         this.addControl(new GeneralPreferencesPage.ApplicationDirectoryPreference());
      }

      this.addControl(new GeneralPreferencesPage.LogHistoryPreference());


      this.addControl(this.createServerCallTimeoutPrefUi());
   }

   private PreferenceUi createServerCallTimeoutPrefUi() {
      IntPreferenceUi var1 = new IntPreferenceUi(ServerCallTimeoutPreference.INSTANCE, "ETLForge Server call timeout (in minutes):", 4);
      var1.setSelectAllWhenFocused(true);
      return var1;
   }

   private static class ApplicationDirectoryPreference implements PreferenceUi {
      private final FileSelectorPanel fileBrowser = FileSelectorFactory.getFactory()
         .singleFilePanel(null, FileSelectorPanel.OPEN, "Select an Application Directory", null);

      public ApplicationDirectoryPreference() {
         this.fileBrowser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES_ONLY);
         this.restore();
      }

      @Override
      public void decorate(UiCanvas var1) {
         new GeneralPreferencesPage.ApplicationDirectoryPreference.Layout().decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.fileBrowser.requestFocus();
      }

      @Override
      public void apply() {
         File var1 = this.fileBrowser.getSelectedFile();
         if (var1 != null) {
            ApplicationDirectoryLocationPreference.set(var1);
         }
      }

      @Override
      public void restore() {
         this.fileBrowser.setSelectedFile(ApplicationDirectoryLocationPreference.get());
      }

      @Override
      public void restoreDefault() {
         this.fileBrowser.setSelectedFile(DefaultLocation.LOCATION);
      }

      @Override
      public boolean isDirty() {
         return !ApplicationDirectoryLocationPreference.get().equals(this.fileBrowser.getSelectedFile());
      }

      @Override
      public void addChangeListener(final ChangeListener var1) {
         this.fileBrowser.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1x) {
               var1.stateChanged(new ChangeEvent(var1x.getSource()));
            }
         });
      }

      @Override
      public void share() {
         throw new UnsupportedOperationException();
      }

      private class Layout {
         private final OneColumnPanel layout;

         public Layout() {
            BorderBuilder var2 = new BorderBuilder(StyleSheet.getDefaultTitleBorder("Application Directory")).empty(5);
            this.layout = new OneColumnPanel().withInsets(0, 0, 5, 0).withBorder(var2);
            this.addBrowser();
            this.addRestartNotice();
            this.layout.deeplyNonOpaque();
         }

         public void decorate(UiCanvas var1) {
            this.layout.decorate(var1);
         }

         public void addBrowser() {
            this.layout.setWeightX(1.0);
            this.layout.add(ApplicationDirectoryPreference.this.fileBrowser);
         }

         public void addRestartNotice() {
            this.layout.noFill(AbstractPreferencesPage.getRequiresRestartNote());
         }
      }
   }

   private static class LogHistoryPreference extends IntPreferenceUi {
      public LogHistoryPreference() {
         super(ApplicationLogHistoryPreference.INSTANCE, "Delete log files older than (in days):", 4);
         this.setSelectAllWhenFocused(true);
      }

      @Override
      public void decorate(UiCanvas var1) {
         Grid var2 = Grid.oneColumn();
         super.decorate(var2);
         var2.add(AbstractPreferencesPage.getRequiresRestartNote());
         var2.withBorder(StyleSheet.getDefaultTitleBorder("Application Log History"));
         var2.decorate(var1);
      }
   }
}
