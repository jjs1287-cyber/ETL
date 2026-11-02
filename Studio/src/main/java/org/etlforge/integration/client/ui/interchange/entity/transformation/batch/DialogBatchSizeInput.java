package org.etlforge.integration.client.ui.interchange.entity.transformation.batch;

import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.TextFieldInput;

public class DialogBatchSizeInput implements BatchSizeInput {
   @Override
   public Integer getBatchSize(String var1, int var2) {
      Object var3 = null;
      Object var4 = null;
      return TextFieldInput.askForIntegerInDialog(
         ApplicationUi.getAppWinFrame(),
         "Define batch node",
         "<html>Batch node: " + var1 + "<br><br>Maximum number of records per batch:<br>(set to 0 to disable)</html>",
         (Integer)var3,
         (Integer)var4,
         var2
      );
   }
}
