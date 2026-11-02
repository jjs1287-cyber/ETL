package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Size;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.preference.PreferencesKey;

final class SampleFilePage extends AbstractWizardPage {
   private final KongaTextField fileField = new KongaTextField(30);
   private final KongaButton browseButton;
   private final KongaTextArea filePreview;
   private volatile SampleFile sampleFile;

   public SampleFilePage(Wizard var1) {
      super(var1, "Select a Sample File");
      this.fileField.setEditable(false);
      this.browseButton = new KongaButton(new SampleFilePage.BrowseAction());
      this.filePreview = KongaTextArea.viewer(Size.rows(10), Size.columns(60));
      TextStyles.DefaultMonoSpaced.makeOver(this.filePreview);
      this.filePreview.setLabel("Top of file:");
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Sample File", "Select a sample file for field mappings");
      var1.setInsets(0, 0, 0, 0);
      var1.add(this.layoutFileSelector(), true, false);
      var1.add(this.filePreview.asUiProvider(), true, false);
      return var1.asNorthBorderLayout();
   }

   private JComponent layoutFileSelector() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.addAllWithSpace(5, this.fileField, this.browseButton);
      return var1.container();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SampleFilePage.this.browseButton.requestFocusInWindow();
         }
      });
   }

   public SampleFile getSampleFile() {
      return this.sampleFile;
   }

   private void blankOutUi() {
      this.sampleFile = null;
      this.fileField.setText("");
      this.filePreview.setText("");
      this.setCanContinue(false);
   }

   private class BrowseAction extends AbstractAction {
      private FileChooser fc;

      public BrowseAction() {
         super(ApplicationResources.getString("Browse..."));
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               BrowseAction.this.browseForFile(this.getParent(var1));
            }

            private Component getParent(ActionEvent var1x) {
               return var1x == null ? null : (Component)var1x.getSource();
            }
         });
      }

      private void browseForFile(Component var1) {
         if (this.fc == null) {
            this.fc = this.createFileChooser();
         }

         SuffixFileChooserFilter var2 = new SuffixFileChooserFilter(".csv", ".txt");
         File var3 = this.fc.getFileToOpen(var1, var2, this.getRecentFileStore(), "Select a source file");
         if (var3 != null) {
            this.processFile(var3);
         }
      }

      private FileChooser createFileChooser() {
         FileChooser var1 = ApplicationFileChooser.getInstance(null);
         var1.setSelectionMode(FileChooser.SelectionMode.FILES_ONLY);
         return var1;
      }

      private RecentFileStore getRecentFileStore() {
         PreferencesKey var1 = new PreferencesKey(BulkLoadWizard.class, "sample_file");
         return new PreferencesFileStore(var1, 1);
      }

      private void processFile(File var1) {
         SampleFilePage.this.browseButton.setEnabled(false);
         SampleFilePage.this.blankOutUi();
         Computation var2 = this.createJob(var1);
         this.runJobInBackgroundThread(var2);
      }

      private Computation<SampleFile> createJob(File var1) {
         SampleFilePage.SampleFileProducer var2 = new SampleFilePage.SampleFileProducer(var1);
         SampleFilePage.SampleFileConsumer var3 = SampleFilePage.this.new SampleFileConsumer();
         return Computation.create(var2, var3);
      }

      private void runJobInBackgroundThread(Computation<SampleFile> var1) {
         Runnable var2 = new Runnable() {
            @Override
            public void run() {
               SampleFilePage.this.browseButton.setEnabled(true);
            }
         };
         var1.swingWorkerBuilder()
            .withErrorLog(ErrorLevel.ATTENTION, SampleFilePage.class)
            .withWaitService(SampleFilePage.this.getWaitService())
            .runWhenFinished(var2)
            .buildAndExecute();
      }
   }

   private class SampleFileConsumer implements Receiver<SampleFile> {
      private SampleFileConsumer() {
      }

      public void handle(SampleFile var1) {
         if (var1.isEmpty()) {
            SampleFilePage.this.blankOutUi();
            NonBlockingAlert.error(SampleFilePage.this.getMainPanel(), "The selected file is empty. Please select another file.", "Invalid file");
         } else {
            SampleFilePage.this.sampleFile = var1;
            SampleFilePage.this.fileField.setText(var1.getPath());
            SampleFilePage.this.filePreview.setText(var1.getTopOfFile());
            SampleFilePage.this.filePreview.setCaretPosition(0);
            SampleFilePage.this.setCanContinue(true);
         }
      }
   }

   private static class SampleFileProducer implements Callable<SampleFile> {
      private final File file;

      public SampleFileProducer(File var1) {
         this.file = var1;
      }

      public SampleFile call() throws Exception {
         Charset var1 = Charset.defaultCharset();
         return SampleFile.create(this.file, var1);
      }
   }
}
