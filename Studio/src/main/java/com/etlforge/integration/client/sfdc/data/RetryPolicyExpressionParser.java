package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.dataelement.GlobalDE;

public final class RetryPolicyExpressionParser {
   private final String expression;
   private final List<Transform.Token> tokens;
   private int numberOfRetries;
   private int retryInterval;
   private int pos;
   private boolean replacedNumberOfRetries;
   private boolean replacedInterval;
   private List<Transform.Token> tokensToRemove;

   public RetryPolicyExpressionParser(String var1) {
      this.expression = var1;
      if (StringUtils.isEmpty(var1)) {
         this.tokens = Lists.newArrayList();
      } else {
         Transform var2 = new Transform();
         this.tokens = var2.tokenize(var1);
      }
   }

   public RetryPolicy parse() {
      if (this.tokens == null) {
         return RetryPolicy.NONE;
      }

      this.numberOfRetries = -1;
      this.retryInterval = -1;

      for (this.pos = 0; this.pos < this.tokens.size(); this.pos++) {
         Transform.Token var1 = this.tokens.get(this.pos);
         switch (var1.m_id) {
            case 406:
               this.parseDataElement(var1, this.pos);
               break;
         }
      }

      return this.numberOfRetries >= 0 && this.retryInterval >= 0 ? new RetryPolicy(this.numberOfRetries, this.retryInterval) : RetryPolicy.NONE;
   }

   private void parseDataElement(Transform.Token var1, int var2) {
      String var3 = GlobalDE.nameOf(var1.m_str);
      if (var3.equalsIgnoreCase("etlforge.web_service_call.number_of_retries")) {
         this.numberOfRetries = this.getIntValue(var2);
      } else if (var3.equalsIgnoreCase("etlforge.web_service_call.retry_wait_seconds")) {
         this.retryInterval = this.getIntValue(var2);
      }
   }

   private int getIntValue(int var1) {
      Transform.Token var2 = this.getValueOfVariable(var1);
      if (var2 == null) {
         return -1;
      }

      assert var2.m_id == 402;
      return Integer.parseInt(var2.m_str);
   }

   private Transform.Token getValueOfVariable(int var1) {
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
            case 402:
               if (var3) {
                  return var4;
               }
         }
      }

      return null;
   }

   public String replace(RetryPolicy var1) {
      if (this.tokens == null) {
         return null;
      }

      if (var1 == RetryPolicy.NONE) {
         return this.removeRetryPolicy();
      }

      this.replacedNumberOfRetries = false;
      this.replacedInterval = false;

      for (this.pos = 0; this.pos < this.tokens.size(); this.pos++) {
         Transform.Token var2 = this.tokens.get(this.pos);
         switch (var2.m_id) {
            case 406:
               this.replaceDataElementValue(var2, this.pos, var1);
               break;
         }
      }

      return this.replacedNumberOfRetries && this.replacedInterval ? this.createNewExpression() : this.insertRetryPolicyAfterOpeningTag(var1);
   }

   private void replaceDataElementValue(Transform.Token var1, int var2, RetryPolicy var3) {
      String var4 = GlobalDE.nameOf(var1.m_str);
      if (var4.equalsIgnoreCase("etlforge.web_service_call.number_of_retries")) {
         Transform.Token var5 = this.getValueOfVariable(var2);
         if (var5 != null) {
            var5.m_str = String.valueOf(var3.numberOfRetries);
            this.replacedNumberOfRetries = true;
         }
      } else if (var4.equalsIgnoreCase("etlforge.web_service_call.retry_wait_seconds")) {
         Transform.Token var6 = this.getValueOfVariable(var2);
         if (var6 != null) {
            var6.m_str = String.valueOf(var3.retryIntervalInSeconds);
            this.replacedInterval = true;
         }
      }
   }

   private String createNewExpression() {
      if (this.isTrivialCondition()) {
         return null;
      }

      StringBuilder var1 = new StringBuilder();

      for (Transform.Token var3 : this.tokens) {
         var1.append(var3.m_str);
      }

      return var1.toString();
   }

   private boolean isTrivialCondition() {
      for (Transform.Token var2 : this.tokens) {
         switch (var2.m_id) {
            case -2:
            case -1:
            case 1100:
            case 1101:
               break;
            case 404:
               if ("true".equalsIgnoreCase(var2.m_str)) {
                  break;
               }

               return false;
            default:
               return false;
         }
      }

      return true;
   }

   private String insertRetryPolicyAfterOpeningTag(RetryPolicy var1) {
      String var2 = RetryPolicyExpression.getVariableAssignment(var1);
      int var3 = this.expression.indexOf("<trans>");
      if (var3 == -1) {
         String var7 = ScriptConstants.wrapInTags(var2 + "\n");
         return this.expression + "\n" + var7;
      } else {
         int var4 = var3 + ScriptConstants.BEGIN_TRANS_LENGTH;
         String var5 = this.expression.substring(0, var4);
         String var6 = this.expression.substring(var4 + 1);
         return var5 + "\n" + var2 + "\n" + var6;
      }
   }

   public String removeRetryPolicy() {
      if (this.tokens == null) {
         return null;
      }

      this.tokensToRemove = Lists.newArrayList();

      for (this.pos = 0; this.pos < this.tokens.size(); this.pos++) {
         Transform.Token var1 = this.tokens.get(this.pos);
         switch (var1.m_id) {
            case 406:
               this.removeIfPolicyDataElement(var1, this.pos);
               break;
         }
      }

      for (Transform.Token var2 : this.tokensToRemove) {
         this.tokens.remove(var2);
      }

      return this.createNewExpression();
   }

   private void removeIfPolicyDataElement(Transform.Token var1, int var2) {
      String var3 = GlobalDE.nameOf(var1.m_str);
      if (var3.equalsIgnoreCase("etlforge.web_service_call.number_of_retries") || var3.equalsIgnoreCase("etlforge.web_service_call.retry_wait_seconds")) {
         this.removePolicyDataElement(var2);
      }
   }

   private void removePolicyDataElement(int var1) {
      for (this.pos = var1; this.pos < this.tokens.size(); this.pos++) {
         Transform.Token var2 = this.tokens.get(this.pos);
         if (var2.m_id == 200 || var2.m_id == 310 || var2.m_id == -2) {
            if (var2.m_id == 310) {
               this.tokensToRemove.add(var2);
               this.consumeTrailingWhitespace(this.pos + 1);
            }
            break;
         }

         this.tokensToRemove.add(var2);
      }
   }

   private void consumeTrailingWhitespace(int var1) {
      for (this.pos = var1; this.pos < this.tokens.size(); this.pos++) {
         Transform.Token var2 = this.tokens.get(this.pos);
         if (var2.m_id != 1100) {
            this.pos--;
            break;
         }

         this.tokensToRemove.add(var2);
      }
   }
}
