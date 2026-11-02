package com.etlforge.integration.client.ui.sfdc.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;

public final class SoqlCompletionContext implements CompletionContext {
   private static final String SELECT_REGEX = "\\s*select\\s+";
   private static final String FIELD_LIST_REGEX = "([^\\s]+(\\s*,\\s*)*?)";
   private static final String AT_LEAST_ONE_FIELD_REGEX = "([^\\s]+(\\s*,\\s*)*?)+?";
   private static final String NOT_ENDING_WITH_COMMA_REGEX = "(?<!,)";
   private static final String FROM_REGEX = "\\s+from\\s+";
   private static final String OBJECT_PARTICLE_REGEX = "([^\\s])*";
   private final Pattern nextTokenShouldBeAnObject = Pattern.compile("\\s*select\\s+([^\\s]+(\\s*,\\s*)*?)+?\\s+from\\s+([^\\s])*", 2);
   private final Pattern nextTokenShouldBeFrom = Pattern.compile("\\s*select\\s+([^\\s]+(\\s*,\\s*)*?)+?(?<!,)\\s+", 2);
   private final Pattern nextTokenShouldBeAField = Pattern.compile("\\s*select\\s+([^\\s]+(\\s*,\\s*)*?)*?", 2);
   private final TokenListCompletionContext fields;
   private final TokenListCompletionContext objects;
   private SObject currentObject;

   public SoqlCompletionContext(SalesforceConfig var1) {
      this.fields = this.createTokenList(var1.getFieldsInheritedFromSObject());
      this.objects = this.createTokenList(new String[0]);
   }

   private TokenListCompletionContext createTokenList(String[] var1) {
      TokenListCompletionContext var2 = new TokenListCompletionContext(var1);
      var2.setCaseSensitive(false);
      var2.setUseWordBoundaries(true);
      return var2;
   }

   public void setWsdlInfo(SalesforceWsdlInfo var1) {
      ArrayList var2 = Lists.newArrayList();

      for (SObject var4 : var1) {
         var2.add(var4.getLocalName());
      }

      this.objects.setTokens((String[]) var2.toArray(new String[var2.size()]));
   }

   public void setObject(SObject var1) {
      if (var1 != this.currentObject) {
         this.fields.setTokens(this.getFieldNames(var1));
         this.currentObject = var1;
      }
   }

   private String[] getFieldNames(SObject var1) {
      if (var1 == null) {
         return new String[0];
      }

      HashSet var2 = Sets.newHashSet();

      for (SObject.Field var4 : var1.getFields()) {
         var2.add(var4.getName());
      }

      return (String[]) (String[]) var2.toArray(new String[var2.size()]);
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      String var3 = DocumentUtils.getText(var1, 0, var2);
      if (StringUtils.isBlank(var3)) {
         String var5 = "SELECT ";
         return this.singleToken(var2, var5);
      } else if (this.nextTokenShouldBeAnObject.matcher(var3).matches()) {
         return this.objects.getAvailableTokens(var1, var2);
      } else if (this.nextTokenShouldBeFrom.matcher(var3).matches()) {
         String var4 = "FROM ";
         return this.singleToken(var2, var4);
      } else {
         return this.nextTokenShouldBeAField.matcher(var3).matches() ? this.fields.getAvailableTokens(var1, var2) : new CompletionToken[0];
      }
   }

   private CompletionToken[] singleToken(int var1, String var2) {
      return new CompletionToken[]{new DefaultCompletionToken(var2, var1, var1)};
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }
}
