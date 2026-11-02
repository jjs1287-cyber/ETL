package org.etlforge.integration.data.connector;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.copy.AdditionalCopyInstructions;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.structure.CallStructureSchema;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.io.Folder;
import org.etlforge.string.RegexUtils;
import org.etlforge.xml.xsd.XsdReferenceReplacer;

public final class ConnectorCallCopyInstructions extends AdditionalCopyInstructions {
   @ExtensionObjectConstructor
   public ConnectorCallCopyInstructions(IntegrationEntity var1) {
      super(var1);
   }

   @Override
   public void handle(IntegrationEntity var1, ManagedProject var2) {
      this.copyJtrFiles(var1, var2);
      this.processXsds((ConnectorCall)var1, var2);
   }

   private void copyJtrFiles(IntegrationEntity var1, ManagedProject var2) {
      InputAndOutputMappingTreeStructureCache var3 = var2.getConnectorCallMappingStructureCache();
      this.copyCachedStructures(var3, var1);
   }

   private void processXsds(ConnectorCall var1, ManagedProject var2) {
      ConnectorCall var3 = (ConnectorCall)this.getOriginal();
      Folder var4 = createWorkDir(var2);

      try {
         ConnectorCallCopyInstructions.Xsds var5 = new ConnectorCallCopyInstructions.Xsds(var3, var2.getFileStore());
         ConnectorCallCopyInstructions.NewNameFunction var6 = new ConnectorCallCopyInstructions.NewNameFunction(var1);
         var5.rename(var6);
         updateConnectorCallProperties(var1, var6);
      } catch (Exception var10) {
         Throwables.propagate(var10);
      } finally {
         var4.delete();
      }
   }

   private static Folder createWorkDir(ManagedProject var0) {
      Folder var1 = new Folder(ApplicationTempFolder.newLikelyUniqueFolder("cc-copy-" + var0.getProject().getID(), false));
      var1.createOnDisk();
      return var1;
   }

   private static void updateConnectorCallProperties(ConnectorCall var0, ConnectorCallCopyInstructions.NewNameFunction var1) {
      AbstractConnectorCall var2 = (AbstractConnectorCall)var0;
      if (var2.hasRequestTransformation()) {
         CallStructureSchema var3 = var0.getInputStructure().getSchema();
         CallStructureSchema var4 = renameXmlStructureFile(var3, var1);
         var2.setInputSchema(var4);
      }

      CallStructureSchema var5 = var0.getOutputStructure().getSchema();
      CallStructureSchema var6 = renameXmlStructureFile(var5, var1);
      var2.setOutputSchema(var6);
   }

   private static CallStructureSchema renameXmlStructureFile(CallStructureSchema var0, ConnectorCallCopyInstructions.NewNameFunction var1) {
      String var2 = var0.getSchemaName();
      String var3 = var1.apply(var2);
      return new CallStructureSchema(var3, var0.getRootElement());
   }

   @VisibleForTesting
   static class NewNameFunction implements Function<String, String> {
      private final String newGuid;
      private final Map<String, String> renamedFiles = Maps.newHashMap();
      private final Pattern pattern;

      public NewNameFunction(ConnectorCall var1) {
         this.newGuid = var1.getFunctionID().toString();
         String var2 = "etlforge\\.[^\\.]+\\.(" + RegexUtils.guidRegex() + ")\\..+";
         this.pattern = Pattern.compile(var2, 2);
      }

      public String apply(String var1) {
         String var2 = this.renamedFiles.get(var1);
         if (var2 != null) {
            return var2;
         }

         Matcher var3 = this.pattern.matcher(var1);
         if (!var3.matches()) {
            return var1;
         }

         String var4 = var3.group(1);
         String var5 = var1.replace(var4, this.newGuid);
         this.renamedFiles.put(var1, var5);
         return var5;
      }
   }

   private static class Xsds {
      private final ConnectorCall original;
      private final FileStore fileStore;

      public Xsds(ConnectorCall var1, FileStore var2) {
         this.original = var1;
         this.fileStore = var2;
      }

      public void rename(Function<String, String> var1) throws Exception {
         HashMap var2 = Maps.newHashMap();
         Set var3 = this.collectFiles(this.original);
         ArrayList var4 = Lists.newArrayList();

         for (Object __etlItem137 : var3) {
            FileStoreFile var6 = (FileStoreFile) __etlItem137;
            String var7 = var6.getPath();
            String var8 = (String)var1.apply(var7);
            if (!var7.equals(var8)) {
               var2.put(var7, var8);
               File var9 = var6.getPhysicalRepresentation();
               File var10 = new File(var9.getParentFile(), var8);
               Files.copy(var9, var10);
               var4.add(var10);
            }
         }

         this.fileStore.copyDependencies(var2);
         this.updateReferencesInFiles(var4, var2);
      }

      private Set<FileStoreFile> collectFiles(ConnectorCall var1) throws FileStoreException {
         HashSet var2 = Sets.newHashSet();
         if (var1.hasRequestTransformation()) {
            this.collectFiles(var1.getInputStructure(), var2);
         }

         this.collectFiles(var1.getOutputStructure(), var2);
         return var2;
      }

      private void collectFiles(XsdBasedStructure var1, Set<FileStoreFile> var2) throws FileStoreException {
         String var3 = var1.getXsdPath();
         FileStoreFile var4 = this.fileStore.getFile(var3);
         if (var4 != null) {
            this.collectFiles(var4, var2);
         }
      }

      private void collectFiles(FileStoreFile var1, Set<FileStoreFile> var2) throws FileStoreException {
         if (var2.add(var1)) {
            for (FileStoreFile var4 : this.fileStore.getRequiredFiles(var1.getPath())) {
               this.collectFiles(var4, var2);
            }
         }
      }

      private void updateReferencesInFiles(List<File> var1, Map<String, String> var2) throws Exception {
         Function var3 = Functions.forMap(var2);
         XsdReferenceReplacer var4 = new XsdReferenceReplacer(var3);

         for (File var6 : var1) {
            var4.replaceReferences(var6);
         }
      }
   }
}
