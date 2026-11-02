package com.etlforge.integration.client.ui.sfdc.page.soql;

import javax.swing.JComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.salesforce.parser.SoqlErrorReporter;
import org.etlforge.lang.exception.ExceptionHandler;

public final class ErrorAlert implements SoqlErrorReporter, ExceptionHandler<SaveEditorException> {
   private final JComponent owner;

   public ErrorAlert(JComponent var1) {
      this.owner = var1;
   }

   @Override
   public void reportError(String var1, String var2) {
      Alert.error(this.owner, var2, var1);
   }

   public void caught(SaveEditorException var1) {
      Alert.error(this.owner, var1.getMessage(), "Cannot Save");
   }
}
