package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import java.io.File;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.LoadSourceFileStoreImpl;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.loadsource.client.LoadSourceDataFileInput;
import org.etlforge.integration.loadsource.client.RecentSourceDataFiles;
import org.etlforge.integration.loadsource.client.SourceDataService;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.RecentFileStore;

public final class LoadLastSourceFileExecutor implements CommandExecutor {
   private final TransformationPage page;

   public LoadLastSourceFileExecutor(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      DataStructureType var2 = this.page.getObject().getInputStructure().getStructureType();
      switch (var2) {
         case None:
         case Database:
         case LDAP:
            throw new CommandException("This command is not supported for transformations with input type " + var2);
         default:
            this.loadSourceFile();
      }
   }

   private void loadSourceFile() throws CommandException {
      RecentFileStore var1 = this.createProjectFileStore();
      FileUi var2 = var1.latestFile();
      if (var2 == null) {
         throw new CommandException("No known source file to use.");
      }

      SourceDataService var3 = this.page.getTreeDataService().getSourceDataService();
      File var4 = var2.getFile();
      LoadSourceDataFileInput var5 = new LoadSourceDataFileInput(var4);
      var3.loadSourceData(var5, null);
   }

   private RecentFileStore createProjectFileStore() {
      try {
         return new RecentSourceDataFiles(new LoadSourceFileStoreImpl(this.page.getObject()));
      } catch (FileStoreException var2) {
         var2.printStackTrace();
         return RecentFileStore.EMPTY;
      }
   }
}
