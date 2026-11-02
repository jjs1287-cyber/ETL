package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.system.callback.Receiver;

final class TokenInserter implements Receiver<InsertionToken> {
   private final ExpressionArea expressionArea;

   public TokenInserter(ExpressionArea var1) {
      this.expressionArea = var1;
   }

   public void handle(InsertionToken var1) {
      Preconditions.checkNotNull(var1);
      if (var1 instanceof InsertionToken.Function) {
         this.insertFunction((InsertionToken.Function)var1);
      } else if (var1 instanceof InsertionToken.ProjectItem) {
         this.insertProjectItem((InsertionToken.ProjectItem)var1);
      } else if (var1 instanceof InsertionToken.DataElement) {
         this.insertDataElement((InsertionToken.DataElement)var1);
      } else if (var1 instanceof InsertionToken.Plugin) {
         this.insertPlugin((InsertionToken.Plugin)var1);
      } else {
         ErrorLog.log(this.getClass(), null, new Exception("Unknown token type: " + var1.getClass()));
      }
   }

   private void insertFunction(InsertionToken.Function var1) {
      this.expressionArea.insertFunction(var1.getFunction());
   }

   private void insertProjectItem(InsertionToken.ProjectItem var1) {
      String var2 = ScriptConstants.getProjectItemTagString(var1.getItem());
      this.expressionArea.insertText(var2);
   }

   private void insertDataElement(InsertionToken.DataElement var1) {
      this.expressionArea.insertGlobalDataElement(var1.getName());
   }

   private void insertPlugin(InsertionToken.Plugin var1) {
      String var2 = ScriptConstants.getPluginTagString(var1.getManifest());
      this.expressionArea.insertText(var2);
   }
}
