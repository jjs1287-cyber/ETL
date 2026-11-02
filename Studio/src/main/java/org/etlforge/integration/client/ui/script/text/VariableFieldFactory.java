package org.etlforge.integration.client.ui.script.text;

import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.Disposable;
import org.etlforge.ui.text.contentassist.CompletionContext;

public final class VariableFieldFactory implements Disposable {
   private final VariableCompletionContext completionContext = new VariableCompletionContext();
   private ExpressionParser parser;

   @Override
   public void dispose() {
      this.completionContext.dispose();
   }

   public void setGlobalDataElements(GlobalDataElements var1) {
      this.completionContext.setGlobalDataElements(var1);
   }

   public void setSourceDataElements(CompletionContext var1) {
      this.completionContext.setSourceDataElements(var1);
   }

   public void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   public VariableField newField() {
      VariableField var1 = new VariableField(this.completionContext);
      var1.setParser(this.parser);
      return var1;
   }
}
