package com.etlforge.integration.license.client.ui.actions;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.etlforge.integration.license.client.ui.CaptionArea;
import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.option.AbstractInputOption;
import org.etlforge.ui.util.option.InputOption;
import org.etlforge.ui.util.option.InputOptionPanel;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.util.preference.PreferencesKey;

final class LicenseKeyEntryUi implements UiProvider, Disposable {
   private final InputOptionPanel<String> options = new InputOptionPanel<>(new LicenseKeyEntryUi.FileOption(), new LicenseKeyEntryUi.PasteInKeyOption());

   public LicenseKeyEntryUi() {
   }

   @Override
   public void dispose() {
      this.options.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.withInsets(0, 0, 10, 0);
      var2.setWeightX(1.0);
      var2.add(this.createCaption());
      var2.setWeightY(1.0);
      var2.add(this.options);
      var2.decorate(var1);
   }

   private Object createCaption() {
      String var1 = "To upload the license key, either select the license file you received from ETLForge, or paste in the license key directly in the text box below.";
      CaptionArea var2 = new CaptionArea(var1);
      var2.setBackgroundColor(Colors.NEUTRAL_BACKGROUND);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.options.requestFocus();
   }

   public String getLicenseKey() {
      return this.options.getValue();
   }

   private static class FileOption extends AbstractInputOption<String> implements LicenseKeyEntryUi.KeyOption {
      private final FileSelectorPanel fileSelector = this.createFileSelector();

      public FileOption() {
      }

      private FileSelectorPanel createFileSelector() {
         FileSelectorPanelFactory var1 = FileSelectorPanelFactory.newInstance();
         FileSelectorPanel var2 = var1.singleFilePanel(null, FileSelectorPanel.Mode.OPEN, null, this.getRecentFileStore());
         var2.setUseTitleLabel(false);
         return var2;
      }

      private RecentFileStore getRecentFileStore() {
         PreferencesKey var1 = new PreferencesKey(LicenseKeyEntryUi.class, "last-file-selection");
         return new PreferencesFileStore(var1, 10);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.fileSelector.decorate(var1);
      }

      @Override
      public void requestFocus() {
         new DelayedJob(250, new Runnable() {
            @Override
            public void run() {
               FileOption.this.fileSelector.requestFocus();
            }
         }).schedule();
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText("&Upload license file");
         var1.setSelected(true);
      }

      public String getValue() {
         File var1 = this.fileSelector.getSelectedFile();
         if (var1 != null) {
            try {
               String var2 = Files.toString(var1, Charsets.UTF_8);
               if (StringUtils.isBlank(var2)) {
                  throw new Exception("The file \"" + var1.getAbsolutePath() + "\" is empty.");
               }

               return var2;
            } catch (Exception var3) {
               ErrorLog.attention(LicenseKeyEntryUi.class, "I'm sorry, I could not read the selected license file.", var3);
            }
         }

         return null;
      }

      @Override
      public void setActive(boolean var1) {
         super.setActive(var1);
         this.fileSelector.setActive(var1);
      }
   }

   private interface KeyOption extends InputOption<String> {
   }

   private static class PasteInKeyOption extends AbstractInputOption<String> implements LicenseKeyEntryUi.KeyOption {
      private final KongaTextArea textArea = new KongaTextArea(5, 60);

      public PasteInKeyOption() {
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText("&Paste in the license key");
      }

      public String getValue() {
         return this.textArea.getText().trim();
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.textArea.asUiProvider().decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.textArea.requestFocusInWindow();
      }
   }
}
