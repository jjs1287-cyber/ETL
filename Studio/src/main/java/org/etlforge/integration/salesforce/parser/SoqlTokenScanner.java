package org.etlforge.integration.salesforce.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.text.partition.AbstractTokenScanner;
import org.etlforge.util.string.StringLimiter;

final class SoqlTokenScanner extends AbstractTokenScanner {
   private String errorMessage;
   private int errorPos;

   public SoqlTokenScanner(String var1) {
      super(var1);
   }

   public SoqlTokenScanner.Result doScan() {
      this.errorMessage = null;
      this.errorPos = -1;
      String var1 = null;
      boolean var2 = false;
      LinkedHashMap var3 = Maps.newLinkedHashMap();
      TreeMap var4 = Maps.newTreeMap();

      for (SoqlToken var5 = this.next(); var5 != null; var5 = this.next()) {
         if (var5.getId() == SoqlTokenId.OBJECT) {
            var1 = var5.lexeme();
         } else if (var5.getId() == SoqlTokenId.AGGREGATE_FUNCTION) {
            var2 = true;
         } else if (var5.getId() == SoqlTokenId.SUB_QUERY) {
            TokenizingSoqlParser var6 = new TokenizingSoqlParser();
            SubQueryToken var7 = (SubQueryToken)var5;
            ParsedQuery var8 = var7.parse(var6);
            String var9 = var8.getObjectName();
            if (var3.containsKey(var9)) {
               this.errorMessage = "Cannot follow the same aggregate relationship twice: " + var9;
               this.errorPos = this.getPosition();
               break;
            }

            var3.put(var9, var8);
         }

         var4.put(var5.offset(), var5);
      }

      if (var2) {
         var1 = "AggregateResult";
      }

      this.checkForError(var1);
      return new SoqlTokenScanner.Result(var1, var4, var3.values(), this.errorMessage, this.errorPos);
   }

   private void checkForError(String var1) {
      if (this.errorMessage == null) {
         this.errorMessage = null;
         this.errorPos = -1;
         if (this.getPosition() < this.getInput().length()) {
            this.errorMessage = this.constructUnexpectedInputErrorMessage();
            this.errorPos = this.getPosition();
         } else if (var1 == null) {
            this.errorMessage = "No object name defined.";
            this.errorPos = this.getInput().length();
         }
      }
   }

   private String constructUnexpectedInputErrorMessage() {
      assert this.getPosition() < this.getInput().length();
      String var1 = "The query string is invalid. Unexpected input starting at: \"";
      String var2 = this.getInput().substring(this.getPosition());
      StringLimiter var3 = new StringLimiter(var2);
      String var4 = var3.getLimitedString(30);
      return var1 + var4 + "\"";
   }

   public SoqlToken next() {
      return (SoqlToken)super.next();
   }

   protected SoqlToken[] getPossibleTokens() {
      SoqlToken var1 = (SoqlToken)this.getLastToken();
      if (var1 == null) {
         return new SoqlToken[]{SoqlTokenId.SELECT.createToken()};
      }

      switch (var1.getId()) {
         case SELECT:
         case COMMA:
            return createTokens(SoqlTokenId.SUB_QUERY, SoqlTokenId.AGGREGATE_FUNCTION, SoqlTokenId.FIELD);
         case FIELD:
         case AGGREGATE_FUNCTION:
         case SUB_QUERY:
            return createTokens(SoqlTokenId.FROM, SoqlTokenId.SUB_QUERY, SoqlTokenId.AGGREGATE_FUNCTION, SoqlTokenId.FIELD, SoqlTokenId.COMMA);
         case FROM:
            return createTokens(SoqlTokenId.OBJECT);
         case OBJECT:
            return createTokens(SoqlTokenId.OBJECT_ALIAS, SoqlTokenId.EVERYTHING_AFTER_OBJECT);
         default:
            return new SoqlToken[0];
      }
   }

   private static SoqlToken[] createTokens(SoqlTokenId... var0) {
      SoqlToken[] var1 = new SoqlToken[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2] = var0[var2].createToken();
      }

      return var1;
   }

   public static final class Result {
      private final String objectName;
      private final TreeMap<Integer, SoqlToken> tokens;
      private final ImmutableList<ParsedQuery> nestedQueries;
      private final String errorMessage;
      private final int locationOfError;

      public Result(String var1, TreeMap<Integer, SoqlToken> var2, Collection<ParsedQuery> var3, String var4, int var5) {
         this.objectName = getSimpleName(var1);
         this.tokens = var2;
         this.nestedQueries = ImmutableList.copyOf(var3);
         this.errorMessage = var4;
         this.locationOfError = var5;
      }

      private static String getSimpleName(String var0) {
         return var0 == null ? null : KongaStringUtils.substringAfterLastIfPresent(var0, ".");
      }

      public boolean isValid() {
         return this.errorMessage == null;
      }

      public String getObjectName() {
         return this.objectName;
      }

      public String getErrorMessage() {
         return this.errorMessage;
      }

      public int getLocationOfError() {
         return this.locationOfError;
      }

      public TreeMap<Integer, SoqlToken> getTokenTree() {
         return Maps.newTreeMap(this.tokens);
      }

      public List<SoqlToken> getTokenList() {
         return Lists.newArrayList(this.tokens.values());
      }

      public List<String> getFieldNameList() {
         List var1 = Lists.newArrayList();
         String var2 = null;

         for (SoqlToken var4 : this.tokens.values()) {
            switch (var4.getId()) {
               case FIELD:
                  String var5 = ((FieldToken)var4).getFieldName();
                  var1.add(var5);
                  break;
               case AGGREGATE_FUNCTION:
                  return null;
               case SUB_QUERY:
               case FROM:
               default:
                  break;
               case OBJECT:
                  var2 = var4.lexeme();
                  break;
               case OBJECT_ALIAS:
                  ObjectAlias var6 = new ObjectAlias(var4.lexeme(), var2);
                  var1 = var6.replaceInFieldNames(var1);
            }
         }

         return var1;
      }

      public ImmutableList<ParsedQuery> getNestedQueries() {
         return this.nestedQueries;
      }
   }
}
