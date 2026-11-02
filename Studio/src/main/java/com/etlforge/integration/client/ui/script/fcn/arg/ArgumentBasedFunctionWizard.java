package com.etlforge.integration.client.ui.script.fcn.arg;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.script.fcn.AbstractSimpleFunctionWizard;
import com.etlforge.integration.client.ui.script.fcn.Expression;
import com.etlforge.integration.client.ui.script.fcn.SimpleFunctionWizardPage;
import com.etlforge.integration.client.ui.script.fcn.TestAction;
import com.etlforge.integration.client.ui.script.fcn.VariableSelector;
import java.util.List;
import org.etlforge.integration.client.ui.script.text.VariableField;
import org.etlforge.integration.data.script.ArgumentDescriptor;
import org.etlforge.integration.data.script.Function;

public final class ArgumentBasedFunctionWizard extends AbstractSimpleFunctionWizard {
   private final ImmutableList<ArgumentDescriptor> argDescriptors;
   private List<ArgumentField> fields;
   private boolean questionLayout;

   public ArgumentBasedFunctionWizard(Function var1, List<ArgumentDescriptor> var2) {
      super(var1);
      Preconditions.checkNotNull(var1, "function");
      Preconditions.checkArgument(!var2.isEmpty(), "argDescriptors cannot be empty");
      this.argDescriptors = ImmutableList.copyOf(var2);
   }

   public void setQuestionLayout(boolean var1) {
      this.questionLayout = var1;
   }

   @Override
   protected SimpleFunctionWizardPage createPage(VariableSelector var1) {
      this.createFields();
      ArgumentFieldsPanel var2 = this.createFieldsPanel();
      var2.addChangeListener(this.getScanner());
      return new ArgumentBasedWizardsPage(this.getFunction(), var2, var1, this.getTestConsole());
   }

   private ArgumentFieldsPanel createFieldsPanel() {
      ArgumentFieldLayout var1 = this.questionLayout ? new QuestionLayout() : new LabelLayout();
      TestAction var2 = this.createTestAction(true);
      return new ArgumentFieldsPanel(var1, this.fields, var2);
   }

   private void createFields() {
      this.fields = Lists.newArrayList();

      for (ArgumentDescriptor var2 : this.argDescriptors) {
         VariableField var3 = this.getFieldFactory().newField();
         var3.setText(var2.getDefaultValue());
         this.fields.add(new ArgumentField(var3, var2));
      }
   }

   @Override
   public Expression buildExpression() {
      int var1 = -1;
      StringBuilder var2 = new StringBuilder(this.getFunction().getName());
      var2.append("(");
      boolean var3 = true;

      for (ArgumentField var5 : this.fields) {
         String var6 = var5.getValue();
         if (!var6.isEmpty() || !var5.isOptional()) {
            if (!var3) {
               var2.append(", ");
            }

            if (var6.isEmpty() && var1 == -1 && !var5.isOptional()) {
               var1 = var2.length();
            }

            var2.append(var6);
            var3 = false;
         }
      }

      var2.append(")");
      if (var1 == -1) {
         var1 = var2.length();
      }

      return new Expression(var2.toString(), var1);
   }
}
