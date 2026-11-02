package org.etlforge.integration.data.entity.script;

import java.util.regex.Pattern;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.script.GrammarUtils;

public final class ExpressionReferenceReplacer {
   private final IntegrationEntity client;

   public ExpressionReferenceReplacer(IntegrationEntity var1) {
      this.client = var1;
   }

   public String replace(String var1, IntegrationEntity var2, IntegrationEntity var3) {
      String var4 = GrammarUtils.idToPrefix((Class<? extends IntegrationEntityId>)var2.getID().getClass());
      if (var4 == null) {
         String var7 = "Unexpected project item encountered in the "
            + this.getClientStringForErrorMessage()
            + ": \""
            + var2.getName()
            + "\" ["
            + var2.getEntityType().getDisplayName()
            + "]";
         ErrorLog.log(this.getClass(), var7, new Exception(var7));
         return var1;
      } else {
         String var5 = this.getTag(var2, var4);
         String var6 = Pattern.quote(var5);
         String replaced=var1.replaceAll(var6, java.util.regex.Matcher.quoteReplacement(this.getTag(var3, var4)));
         if(var1.contains("<javascript>"))replaced=replaced.replace("'"+var4+var2.getID()+"'","'"+var4+var3.getID()+"'");
         return replaced;
      }
   }

   private String getClientStringForErrorMessage() {
      return this.client.getEntityType().getDisplayName() + " \"" + this.client.getName() + "\"";
   }

   private String getTag(IntegrationEntity var1, String var2) {
      return "\"" + var2 + var1.getID() + "\"";
   }
}
