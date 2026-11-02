package org.etlforge.integration.client.ui.interchange.entity.script.page;

import javax.swing.Action;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.ui.action.ActionUtils;

final class TestScriptExecutor implements CommandExecutor {
   private final ScriptPage page;

   public TestScriptExecutor(ScriptPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      Action var2 = this.page.getTestAction();
      if (var2 != null && var2.isEnabled()) {
         ActionUtils.run(var2, this, "test script");
      }
   }
}
