package org.etlforge.integration.client.ui.interchange.core.action;

import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.operation.runner.AbstractRunOperationAction;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.Operation;

public final class RunOperationAction extends AbstractRunOperationAction {
   public static final String ID = "action.ic.RunOperation";

   public RunOperationAction(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("action.ic.RunOperation", lookupName("action.ic.RunOperation"), var1, var2);
      this.putValue("ShortDescription", InterchangeActionDictionary.DICTIONARY.getToolTip("action.ic.RunOperation"));
   }

   private static String lookupName(String var0) {
      return InterchangeActionDictionary.DICTIONARY.getName(var0).getRaw();
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof Operation;
   }

   @Override
   protected Operation getOperationToRun() {
      List var1 = this.getActiveObjects(Operation.class);
      return var1.size() == 1 ? (Operation)var1.get(0) : null;
   }

   @Override
   protected Editor getPageForObjectToRun() {
      Operation var1 = this.getOperationToRun();
      return var1 == null ? null : this.getEditorFor(var1);
   }
}
