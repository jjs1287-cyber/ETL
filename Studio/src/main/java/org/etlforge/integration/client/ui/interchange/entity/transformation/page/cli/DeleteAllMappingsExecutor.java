package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class DeleteAllMappingsExecutor implements CommandExecutor {
   private final TransformationPage page;

   public DeleteAllMappingsExecutor(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      this.page.getTreeMapper().deleteAllMappings();
   }
}
