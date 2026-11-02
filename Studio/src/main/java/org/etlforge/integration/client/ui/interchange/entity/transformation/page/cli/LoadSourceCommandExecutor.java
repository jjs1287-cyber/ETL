package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.db.LoadDatabaseSourceDataInput;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.loadsource.client.LoadLdapSourceDataInput;
import org.etlforge.integration.loadsource.client.LoadSourceDataFileInput;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.system.callback.CallbackResultAdapter;

public final class LoadSourceCommandExecutor implements CommandExecutor {
   private final TransformationPage page;

   public LoadSourceCommandExecutor(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      DataStructureType var2 = this.page.getObject().getInputStructure().getStructureType();
      if (var2 == DataStructureType.None) {
         throw new CommandException("This transformation does not use source data.");
      }

      if (var1.length == 0) {
         this.loadSourceWithoutArgument();
      } else {
         this.loadSourceWithArgument(var2, StringUtils.join(var1, " "));
      }
   }

   private void loadSourceWithoutArgument() {
      this.page.getTreeDataService().getSourceDataService().loadSourceData(new CallbackResultAdapter() {});
   }

   private void loadSourceWithArgument(DataStructureType var1, String var2) {
      LoadSourceDataInput var3;
      switch (var1) {
         case Database:
            var3 = new LoadDatabaseSourceDataInput(var2);
            break;
         case LDAP:
            var3 = new LoadLdapSourceDataInput();
            break;
         default:
            var3 = new LoadSourceDataFileInput(new File(var2));
      }

      this.page.getTreeDataService().getSourceDataService().loadSourceData(var3, null);
   }
}
