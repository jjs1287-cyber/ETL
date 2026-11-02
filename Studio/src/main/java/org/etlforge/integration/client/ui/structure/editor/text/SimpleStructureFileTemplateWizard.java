package org.etlforge.integration.client.ui.structure.editor.text;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.EnumMap;
import javax.swing.JFrame;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.InvalidTemplateException;
import org.etlforge.integration.data.structure.text.Prototype;
import org.etlforge.integration.data.structure.text.SegmentType;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.dialog.KongaDialog;

public final class SimpleStructureFileTemplateWizard {
   private final ApplicationWindow appWin;
   private final TextDocument document;
   private final String name;
   private final SegmentType segmentType;
   private final CallbackResult callback;
   private final FileTemplateWizardUi ui;
   private final EnumMap<Prototype, SimpleStructureFileTemplateWizard.Factory> factories;

   public SimpleStructureFileTemplateWizard(ApplicationWindow var1, TextDocument var2, String var3, SegmentType var4, CallbackResult var5) {
      this.appWin = var1;
      this.document = var2;
      this.name = var3;
      this.segmentType = var4;
      this.callback = var5;
      this.ui = new FileTemplateWizardUi(var1, var4);
      this.factories = this.createFactories();
   }

   public void start() {
      final KongaDialog var1 = this.createDialog();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.setLocationRelativeTo(var1.getParent());
            var1.setVisible(true);
            if (var1.wasOkPressed()) {
               SimpleStructureFileTemplateWizard.this.createStructure();
            } else {
               SimpleStructureFileTemplateWizard.this.callback.cancelled(null);
            }
         }
      });
   }

   private KongaDialog createDialog() {
      JFrame var1 = this.appWin.getFrame();
      final FileTemplateWizardDialog var2 = new FileTemplateWizardDialog(var1, this.ui);
      var2.getOKButton().setEnabled(false);
      this.ui.setValidationListener(new Enablable() {
         @Override
         public boolean isEnabled() {
            return var2.getOKButton().isEnabled();
         }

         @Override
         public void setEnabled(boolean var1) {
            var2.getOKButton().setEnabled(var1);
         }
      });
      return var2;
   }

   private void createStructure() {
      SimpleStructureFileTemplateWizard.CreateStructureJob var1 = new SimpleStructureFileTemplateWizard.CreateStructureJob();
      KongaSwingWorker.runWhileWaiting(var1, this.appWin);
   }

   private EnumMap<Prototype, SimpleStructureFileTemplateWizard.Factory> createFactories() {
      EnumMap var1 = new EnumMap<>(Prototype.class);
      SimpleStructureFileTemplateWizard.SampleDataFactory var2 = new SimpleStructureFileTemplateWizard.SampleDataFactory(this.ui);
      var1.put(Prototype.SampleFlatFile, var2);
      var1.put(Prototype.SampleFlatFileWithHeader, var2);
      var1.put(Prototype.SimpleTextDef, new SimpleStructureFileTemplateWizard.DefinitionFileFactory(this.segmentType));
      return var1;
   }

   private class CreateStructureJob implements ApplicationWorker.SafeRunnable {
      private CreateStructureJob() {
      }

      @Override
      public void run() {
         try {
            SimpleStructureFileTemplateWizard.Factory var1 = SimpleStructureFileTemplateWizard.this.factories
               .get(SimpleStructureFileTemplateWizard.this.ui.getPrototype());
            File var2 = SimpleStructureFileTemplateWizard.this.ui.getFile();
            SimpleTextStructure var3 = var1.createStructure(SimpleStructureFileTemplateWizard.this.name, var2);
            SimpleStructureFileTemplateWizard.this.document.setDelimiter(SimpleStructureFileTemplateWizard.this.ui.getDelimiter());
            SimpleStructureFileTemplateWizard.this.callback.succeeded(var3);
         } catch (Exception var4) {
            SimpleStructureFileTemplateWizard.this.callback.failed(var4);
         }
      }
   }

   private static class DefinitionFileFactory implements SimpleStructureFileTemplateWizard.Factory {
      private final boolean fixedField;

      public DefinitionFileFactory(SegmentType var1) {
         this.fixedField = var1 == SegmentType.FIXED_FIELD;
      }

      @Override
      public SimpleTextStructure createStructure(String var1, File var2) throws IOException, InvalidTemplateException {
         BufferedReader var3 = new BufferedReader(new FileReader(var2));

         try {
            return SimpleTextStructure.fromDefinitionFile(var1, this.fixedField, var3);
         } finally {
            KongaIoUtils.close(var3);
         }
      }
   }

   private interface Factory {
      SimpleTextStructure createStructure(String var1, File var2) throws IOException, InvalidTemplateException;
   }

   private static class SampleDataFactory implements SimpleStructureFileTemplateWizard.Factory {
      private final FileTemplateWizardUi ui;

      public SampleDataFactory(FileTemplateWizardUi var1) {
         this.ui = var1;
      }

      @Override
      public SimpleTextStructure createStructure(String var1, File var2) throws IOException, InvalidTemplateException {
         Prototype var3 = this.ui.getPrototype();
         assert var3 != Prototype.SimpleTextDef : "SampleDataFactory cannot be used with " + var3;
         return SimpleTextStructure.fromSampleDataFile(var1, var3, this.ui.getDelimiter(), var2, Charset.defaultCharset());
      }
   }
}
