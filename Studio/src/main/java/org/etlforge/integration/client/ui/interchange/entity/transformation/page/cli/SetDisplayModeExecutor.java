package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;

public final class SetDisplayModeExecutor implements CommandExecutor {
   private final TransformationPage page;
   private final TreeMapperMode mode;

   public SetDisplayModeExecutor(TransformationPage var1, TreeMapperMode var2) {
      this.page = var1;
      this.mode = var2;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      this.page.setDisplayMode(this.mode);
   }
}
