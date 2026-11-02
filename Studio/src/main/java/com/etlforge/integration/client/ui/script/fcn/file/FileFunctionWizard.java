package com.etlforge.integration.client.ui.script.fcn.file;

import com.etlforge.integration.client.ui.script.fcn.AbstractSimpleFunctionWizard;
import com.etlforge.integration.client.ui.script.fcn.Expression;
import com.etlforge.integration.client.ui.script.fcn.SimpleFunctionWizardPage;
import com.etlforge.integration.client.ui.script.fcn.VariableSelector;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.script.text.VariableField;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.ui.input.TextInputField2;

final class FileFunctionWizard<T extends IntegrationEntity> extends AbstractSimpleFunctionWizard {
   private final IntegrationProject project;
   private final Class<T> typeClass;

   public FileFunctionWizard(Function var1, Class<T> var2, IntegrationProject var3) {
      super(var1);
      this.project = var3;
      this.typeClass = var2;
   }

   @Override
   protected SimpleFunctionWizardPage createPage(VariableSelector var1) {
      TextInputField2 var2 = this.createFileNameField();
      return new FileFunctionWizardPage<>(this.getFunction(), this.typeClass, this.project, var2, var1, this.getTestConsole());
   }

   private TextInputField2 createFileNameField() {
      VariableField var1 = this.getFieldFactory().newField();
      TextInputField2 var2 = var1.asInputField("File &name:", false);
      var2.setAutoTrim(true);
      var2.setSendInputChangeEvents(true);
      var2.addInputChangeListener(this.getScanner());
      return var2;
   }

   @Override
   public Expression buildExpression() {
      String var1 = this.getFunction().getName() + "(";
      FileFunctionWizardPage var2 = (FileFunctionWizardPage)this.getPage();
      IntegrationEntity var3 = var2.getFileLocation();
      String var4 = ScriptConstants.getProjectItemTagString(var3);
      var1 = var1 + var4;
      String var5 = var2.getFileName();
      if (StringUtils.isNotBlank(var5)) {
         var1 = var1 + ", " + var5;
      }

      var1 = var1 + ")";
      return new Expression(var1);
   }
}
