package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFileConfirmer;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFileTabularViewer;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFileTextViewer;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFileViewer;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.ReadFilesAsUTF8Preference;
import java.io.IOException;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.util.csv.CSVParams;

public final class SourceLocationPage extends LocationPage<Source> {
   private final SourceLocationPage.SampleFileConfirmerWrapper confirmer = new SourceLocationPage.SampleFileConfirmerWrapper();
   private Optional<SampleFile> sampleFile = Optional.absent();
   private Optional<RequestSourceType> deducedFileType = Optional.absent();

   public SourceLocationPage(SalesforceWizard<?> var1, Iterable<DataLocationType> var2, EditorPanel var3) {
      super(var1, "Select Where Your Source Data Comes From", createModel(var2), var3);
   }

   private static LocationPageModel<Source> createModel(Iterable<DataLocationType> var0) {
      LocationPageModel var1 = LocationPageModel.forSources(var0);
      boolean var2 = ReadFilesAsUTF8Preference.INSTANCE.get();
      var1.setUTF8(var2);
      return var1;
   }

   public Optional<SampleFile> getSampleFile() {
      return this.sampleFile;
   }

   public Optional<RequestSourceType> getDeducedFileType() {
      return this.deducedFileType;
   }

   @Override
   protected boolean doAdditionalForwardNavigationValidation(LocationPageModel<Source> var1) {
      if (var1.getSelectedLocationType() == DataLocationType.None) {
         this.deducedFileType = Optional.of(RequestSourceType.NONE);
         return true;
      }

      this.sampleFile = null;
      this.deducedFileType = Optional.absent();

      try {
         SampleFile var2 = var1.getSampleFile();
         boolean var3 = this.validateSampleFile(var1, var2);
         if (var3) {
            this.sampleFile = Optional.fromNullable(var2);
            this.deduceFileType();
         }

         return var3;
      } catch (IOException var4) {
         return this.askUserOnReadError();
      }
   }

   private boolean validateSampleFile(LocationPageModel<Source> var1, SampleFile var2) {
      return var2 == null ? this.isNoSampleFileOk(var1) : this.confirmer.confirm(var2);
   }

   private boolean isNoSampleFileOk(LocationPageModel<Source> var1) {
      return var1.isLocalFileSelected() ? this.askUserIfLocalFileMissingIsOk() : true;
   }

   private boolean askUserIfLocalFileMissingIsOk() {
      Answer var1 = Ask.yesOrNo(
         this.getMainPanel(), "The selected file does not exist. Do you want to continue?", "No such file", Alert.Option.NO, Alert.Type.WARNING
      );
      return var1.isYes();
   }

   private void deduceFileType() {
      RequestSourceTypeDeducer var1 = new RequestSourceTypeDeducer(this.getLocation(), this.sampleFile);
      this.deducedFileType = var1.deduceType();
   }

   private boolean askUserOnReadError() {
      Answer var1 = Ask.yesOrNo(
         this.getMainPanel(),
         "The selected file exists, but could not be read. Do you still want to use it?",
         "Could not read file",
         Alert.Option.NO,
         Alert.Type.ERROR
      );
      return var1.isYes();
   }

   private class SampleFileConfirmerWrapper {
      private SampleFile lastConfirmedFile;

      private SampleFileConfirmerWrapper() {
      }

      public boolean confirm(SampleFile var1) {
         if (this.lastConfirmedFile != null && var1.isLikelySameAs(this.lastConfirmedFile)) {
            return true;
         }

         SampleFileViewer var2 = this.createSampleFileViewer(var1);
         SampleFileConfirmer var3 = new SampleFileConfirmer(SourceLocationPage.this.getMainPanel(), var2);
         boolean var4 = var3.confirm(var1);
         if (var4) {
            this.lastConfirmedFile = var1;
         }

         return var4;
      }

      private SampleFileViewer createSampleFileViewer(SampleFile var1) {
         if (SourceLocationPage.this.getModel().hasCsvParams()) {
            CSVParams var2 = SourceLocationPage.this.getModel().getCsvParams();
            return new SampleFileTabularViewer(SourceLocationPage.this.getMainPanel(), var1, var2);
         } else {
            return new SampleFileTextViewer(var1);
         }
      }
   }
}
