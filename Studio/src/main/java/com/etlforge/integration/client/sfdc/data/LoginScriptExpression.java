package com.etlforge.integration.client.sfdc.data;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.sfdc.ScriptModificationWarning;
import org.etlforge.integration.data.script.GrammarUtils;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.salesforce.LoginCredentials;

public class LoginScriptExpression {
   private static final String RUN_OPERATION = "RunOperation";

   public static String createLoginScript(Operation var0, LoginCredentials var1) {
      return createLoginScript(var0.getID(), var1);
   }

   private static String createLoginScript(OperationId var0, LoginCredentials var1) {
      LoginScriptExpression.Builder var2 = new LoginScriptExpression.Builder(var0, var1);
      return var2.getExpression();
   }

   public static LoginCredentials getCredentials(String var0) {
      LoginScriptExpression.Parser var1 = new LoginScriptExpression.Parser(var0);
      return var1.getCredentials();
   }

   public static OperationId getLoginOperationId(String var0) {
      LoginScriptExpression.Parser var1 = new LoginScriptExpression.Parser(var0);
      return var1.getLoginOperationId();
   }

   public static String replaceCredentials(String var0, LoginCredentials var1) {
      LoginScriptExpression.Parser var2 = new LoginScriptExpression.Parser(var0);
      OperationId var3 = var2.getLoginOperationId();
      return var3 != null ? createLoginScript(var3, var1) : var0;
   }

   private LoginScriptExpression() {
   }

   private static class Builder {
      private final StringBuilder expression = new StringBuilder();

      public Builder(OperationId var1, LoginCredentials var2) {
         this.insertModificationWarning();
         this.implementCredentials(var2);
         this.callOperation(var1);
      }

      private void insertModificationWarning() {
         String var1 = ScriptModificationWarning.forScript();
         this.expression.append(var1);
      }

      private void implementCredentials(LoginCredentials var1) {
         this.assignVariable("salesforce.username", var1.username);
         this.assignVariable("salesforce.password", var1.password);
         this.assignVariable("salesforce.securitytoken", var1.securityToken);
      }

      private void assignVariable(String var1, String var2) {
         this.expression.append(GlobalDE.forScript(var1)).append(" = \"").append(var2).append("\";\n");
      }

      private void callOperation(OperationId var1) {
         String var2 = GrammarUtils.idToPrefix(OperationId.class) + var1;
         this.expression
            .append("if(!")
            .append("RunOperation")
            .append("(\"")
            .append(var2)
            .append("\"),\n\tCancelOperationChain(\"SFDC login failed.\");\n\tRaiseError(GetLastError())\n);\n");
      }

      public String getExpression() {
         return ScriptConstants.wrapInTags(this.expression.toString().trim());
      }
   }

   public static class Parser {
      private final List<Transform.Token> tokens;
      private LoginCredentials creds;
      private OperationId loginOperationId;
      private String username;
      private String password;
      private String securityToken;
      private int pos;

      public Parser(String var1) {
         Transform var2 = new Transform();
         this.tokens = var2.tokenize(var1);
         if (this.tokens != null) {
            this.parse();
         }
      }

      private void parse() {
         for (this.pos = 0; this.pos < this.tokens.size(); this.pos++) {
            Transform.Token var1 = this.tokens.get(this.pos);
            switch (var1.m_id) {
               case 101:
                  this.parseFunction(var1, this.pos);
                  break;
               case 406:
                  this.parseDataElement(var1, this.pos);
            }
         }

         this.creds = this.recreateCredentials(this.username, this.password, this.securityToken);
      }

      private void parseDataElement(Transform.Token var1, int var2) {
         String var3 = GlobalDE.nameOf(var1.m_str);
         if (var3.equalsIgnoreCase("salesforce.username")) {
            this.username = this.getValueOfVariable(var2);
         } else if (var3.equalsIgnoreCase("salesforce.password")) {
            this.password = this.getValueOfVariable(var2);
         } else if (var3.equalsIgnoreCase("salesforce.securitytoken")) {
            this.securityToken = this.getValueOfVariable(var2);
         }
      }

      private String getValueOfVariable(int var1) {
         boolean var2 = false;
         boolean var3 = false;

         for (this.pos = var1 + 1; !var2 && this.pos < this.tokens.size(); this.pos++) {
            Transform.Token var4 = this.tokens.get(this.pos);
            switch (var4.m_id) {
               case 310:
                  var2 = true;
                  break;
               case 320:
                  var3 = true;
                  break;
               case 401:
                  if (var3) {
                     return StringUtils.strip(var4.m_str, "\"");
                  }
                  break;
               case 402:
                  if (var3) {
                     return var4.m_str;
                  }
            }
         }

         return null;
      }

      private void parseFunction(Transform.Token var1, int var2) {
         if (var1.m_str.equalsIgnoreCase("RunOperation(")) {
            for (this.pos = var2 + 1; this.pos < this.tokens.size(); this.pos++) {
               Transform.Token var3 = this.tokens.get(this.pos);
               switch (var3.m_id) {
                  case 401:
                     this.parseOperationId(var3);
                     return;
                  case 1000:
                     return;
               }
            }
         }
      }

      private void parseOperationId(Transform.Token var1) {
         String var2 = GrammarUtils.idToPrefix(OperationId.class);
         String var3 = StringUtils.strip(var1.m_str, "\"");
         if (var3.startsWith(var2)) {
            try {
               String var4 = StringUtils.removeStart(var3, var2);
               this.loginOperationId = new OperationId(var4);
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }
      }

      private LoginCredentials recreateCredentials(String var1, String var2, String var3) {
         return var1 != null && var2 != null ? new LoginCredentials(var1, var2, var3) : null;
      }

      public LoginCredentials getCredentials() {
         return this.creds;
      }

      public OperationId getLoginOperationId() {
         return this.loginOperationId;
      }
   }
}
