package org.etlforge.integration.client.ui.interchange.entity.script.page;

import org.etlforge.integration.data.binding.BoundProperty;

final class ExpressionProperty implements BoundProperty {
   private final ScriptPage page;

   public ExpressionProperty(ScriptPage var1) {
      this.page = var1;
   }

   @Override
   public String getName() {
      return "script";
   }

   @Override
   public void newValue(Object var1) {
      boolean var2 = this.page.isDirty();
      this.page.resetExpression((String)var1);
      if (var2) {
         this.showMessage();
      }
   }

   private void showMessage() {
      String var1 = "Automatic Change";
      String var2 = "This script has been modified indirectly through another object in the project.";
      this.page.showOverlayMessage(var1, var2, false);
   }
}
