package com.etlforge.integration.client.ui.script.fcn.db;

import com.etlforge.integration.client.ui.script.fcn.Expression;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ScriptConstants;

final class ExpressionBuilder {
   private final Function function;
   private final DbWizardPageChain pages;

   public ExpressionBuilder(Function var1, DbWizardPageChain var2) {
      this.function = var1;
      this.pages = var2;
   }

   public Expression buildExpression() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.function.getName() + "(");
      IntegrationEntity var2 = (IntegrationEntity)this.pages.getSelectedDatabase();
      String var3 = ScriptConstants.getProjectItemTagString(var2);
      var1.append(var3);
      String var4 = this.pages.getSql();
      var1.append(", ").append(var4).append(")");
      return new Expression(var1.toString());
   }
}
