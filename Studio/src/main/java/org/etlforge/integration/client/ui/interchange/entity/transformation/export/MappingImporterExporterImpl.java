package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import com.google.common.base.Supplier;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.filechooser.FileFilter;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.structure.StructureNature;
import org.etlforge.integration.salesforce.mapping.SdlToSoapMappingConverter;
import org.etlforge.io.FileUtils;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.util.file.FileProducer;
import org.etlforge.util.preference.PreferencesKey;

final class MappingImporterExporterImpl implements MappingImporter, MappingExporter {
   private static final String PREFERENCE_KEY = "MappingFile";
   private static final FileFilter XML_FILE_FILTER = new SuffixFileChooserFilter(".xml");
   private static final FileFilter XML_AND_DL_FILE_FILTER = new SuffixFileChooserFilter(Arrays.asList(".xml", ".jdl", ".sdl"));
   private final TransformationPage page;
   private final RecentFileStore recentFiles;

   public MappingImporterExporterImpl(TransformationPage var1) {
      this.page = var1;
      PreferencesKey var2 = new PreferencesKey(this.getClass(), "MappingFile");
      this.recentFiles = new PreferencesFileStore(var2, 1);
   }

   @Override
   public void exportMappings(final Transformation var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            MappingImporterExporterImpl.this.exportMappingsImpl(var1);
         }
      });
   }

   @Override
   public void exportMultipleFolders(final Transformation var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            MappingImporterExporterImpl.this.exportMultipleFoldersImpl(var1);
         }
      });
   }

   private void exportMappingsImpl(Transformation var1) {
      try {
         List var2 = var1.getMapList();
         Map var3 = var1.getRenamedNodes();
         MappingImporterExporterImpl.ExportFileProducer var4 = new MappingImporterExporterImpl.ExportFileProducer(var1, "_mappings.xml");
         MappingsXmlWriter var5 = new MappingsXmlWriter(var2, var3, var4);
         var5.write();
      } catch (IOException var6) {
         ErrorLog.log(MappingImporterExporterImpl.class, "Failed to export the mappings: " + var6.getMessage(), var6);
      }
   }

   private void exportMultipleFoldersImpl(Transformation var1) {
      try {
         Map var2 = var1.getRenamedNodes();
         MappingImporterExporterImpl.ExportFileProducer var3 = new MappingImporterExporterImpl.ExportFileProducer(var1, "_multiple_folders.xml");
         MultipleFoldersXmlWriter var4 = new MultipleFoldersXmlWriter(var2, var3);
         var4.write();
      } catch (IOException var5) {
         ErrorLog.log(MappingImporterExporterImpl.class, "Failed to export the mappings: " + var5.getMessage(), var5);
      }
   }

   @Override
   public void importMappings(final Transformation var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            MappingImporterExporterImpl.this.importMappingsImpl(var1);
         }
      });
   }

   @Override
   public void importMultipleFolders(final Transformation var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            MappingImporterExporterImpl.this.importMultipleFoldersImpl(var1);
         }
      });
   }

   private void importMappingsImpl(Transformation var1) {
      try {
         FileFilter var2 = this.getSupportedMappingFormatsForImport(var1);
         ImportFileProducer var3 = new ImportFileProducer(this.page.getWindow(), var2, this.recentFiles);
         File var4 = var3.getFile();
         if (var4 == null) {
            return;
         }

         if (".xml".equals(FileUtils.getExtension(var4, true))) {
            this.importMappingsFromXml(var1, var4);
         } else {
            this.importMappingsFromSDL(var1, var4);
         }
      } catch (Exception var5) {
         this.handleImportMappingsError(var5);
      }
   }

   private void importMappingsFromXml(Transformation var1, File var2) throws Exception {
      MappingsXmlReader var3 = new MappingsXmlReader();
      var3.read(var2);
      List var4 = var3.getMappings();
      Map var5 = var3.getMultipleFolders();
      this.applyImportedData(var1, var4, var5);
   }

   private void importMappingsFromSDL(Transformation var1, File var2) throws IOException, TreeMapperException {
      SdlMappingsImporter var3 = new SdlMappingsImporter(var1, this.page.getTreeMapper());
      List var4 = var3.getMappings(var2);
      this.applyImportedData(var1, var4, null);
   }

   private void importMultipleFoldersImpl(Transformation var1) {
      try {
         ImportFileProducer var2 = new ImportFileProducer(this.page.getWindow(), XML_FILE_FILTER, this.recentFiles);
         MultipleFolderXmlReader var3 = new MultipleFolderXmlReader(var2);
         Map var4 = var3.read();
         this.applyImportedData(var1, null, var4);
      } catch (Exception var5) {
         this.handleImportFoldersError(var5);
      }
   }

   private FileFilter getSupportedMappingFormatsForImport(Transformation var1) {
      return this.isSdlImportSupported(var1) ? XML_AND_DL_FILE_FILTER : XML_FILE_FILTER;
   }

   private boolean isSdlImportSupported(Transformation var1) {
      if (!var1.isPartOfSalesforcePartnerWizard()) {
         return false;
      }

      if (var1.getInputStructure().getStructureNature() == StructureNature.Tree) {
         return false;
      }

      SalesforceWizardRole var2 = var1.getSalesforceWizardRole();
      return var2.isResponse() ? false : SdlToSoapMappingConverter.isSupportedForRequest(var2.method);
   }

   private void applyImportedData(Transformation var1, List<String> var2, Map<String, String> var3) throws TreeMapperException {
      TreeMapper var4 = this.page.getTreeMapper();
      if (var2 != null) {
         List var5 = ImportedMappingsMerger.merge(var1, var2);
         var4.setMapList(var5, true);
         var4.updateHiddenNodesOnMapChange();
      }

      if (var3 != null) {
         var4.setRenamedTargetNodes(var3);
         var4.setDirty(true, false);
      }
   }

   private void handleImportMappingsError(Exception var1) {
      ErrorLog.attention(MappingImporterExporterImpl.class, "Failed to import the mappings: " + var1.getMessage(), var1);
   }

   private void handleImportFoldersError(Exception var1) {
      ErrorLog.attention(MappingImporterExporterImpl.class, "Failed to import the multiple folders: " + var1.getMessage(), var1);
   }

   private class ExportFileProducer implements FileProducer {
      private final Transformation transformation;
      private final String postfix;

      public ExportFileProducer(Transformation var2, String var3) {
         this.transformation = var2;
         this.postfix = var3;
      }

      @Override
      public File getFile() {
         return UiThreadSupplier.call(
            new Supplier<File>() {
               public File get() {
                  FileChooser var1 = ExportFileProducer.this.getFileChooser();
                  File var2 = ExportFileProducer.this.getSuggestedFile(var1);
                  return var1.getFileToSave(
                     null, MappingImporterExporterImpl.XML_FILE_FILTER, MappingImporterExporterImpl.this.recentFiles, "Export mappings to file:", var2
                  );
               }
            }
         );
      }

      private FileChooser getFileChooser() {
         ApplicationWindow var1 = MappingImporterExporterImpl.this.page.getWindow();
         return ApplicationFileChooser.getInstance(var1);
      }

      private File getSuggestedFile(FileChooser var1) {
         File var2 = var1.getCurrentDirectory();
         return var2 != null ? new File(var2, this.getPreSelectedFileName()) : null;
      }

      private String getPreSelectedFileName() {
         return FileUtils.getSafeFileName(this.transformation.getName() + this.postfix, '_');
      }
   }
}
