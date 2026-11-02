package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.MapListTextViewer;
import org.etlforge.integration.data.entity.Transformation;

public final class ViewMapListExecutor implements CommandExecutor {
   private final Transformation tx;

   public ViewMapListExecutor(Transformation var1) {
      this.tx = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      MapListTextViewer.open(this.tx);
   }
}
