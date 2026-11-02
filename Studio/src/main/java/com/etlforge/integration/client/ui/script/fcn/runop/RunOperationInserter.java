package com.etlforge.integration.client.ui.script.fcn.runop;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.script.WizardBasedFunctionInserter;
import com.etlforge.integration.client.ui.script.fcn.Expression;
import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ProjectItemFunction;
import org.etlforge.integration.data.script.ProjectItemFunctionInserter;

public final class RunOperationInserter extends WizardBasedFunctionInserter implements ProjectItemFunctionInserter {
   private IntegrationProject project;

   public RunOperationInserter(Function var1) {
      super(var1);
      Preconditions.checkArgument(var1.getName().toLowerCase().equals("runoperation"), "Unexpected function: " + var1.getName());
   }

   @Override
   public void setProject(IntegrationProject var1) {
      this.project = var1;
   }

   @Override
   protected Wizard<Expression> createWizard() {
      List var1 = this.getApplicableOperations();
      if (var1.isEmpty()) {
         this.showNoOperationsMessage();
         return null;
      } else {
         return new RunOperationWizard(this.function, var1);
      }
   }

   private List<Operation> getApplicableOperations() {
      if (this.project != null) {
         List var1 = ((ProjectItemFunction)this.function).getApplicableProjectItems(this.project);
         return translate(var1);
      } else {
         return Lists.newArrayList();
      }
   }

   private void showNoOperationsMessage() {
      Alert.info("The project has no operations that can be started.", "No Operations Available");
   }

   private static List<Operation> translate(List<IntegrationEntity> var0) {
      return Lists.newArrayList(Iterables.filter(var0, Operation.class));
   }
}
