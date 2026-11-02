package org.etlforge.integration.client.ui.script.text;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.script.builder.FunctionArgumentSuggestions;
import org.etlforge.integration.client.ui.script.builder.FunctionInserterLookup;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.client.ui.script.partition.Tokens;
import org.etlforge.integration.client.ui.script.prefs.UseReferencedDataElementsOnlyInAutoCompletion;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.integration.data.script.de.BuiltInDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.partition.TextPartitioner;

public final class ScriptCompletionContext implements CompletionContext {
   private final ScriptPartitioner partitioner;
   private final JTextComponent inputComponent;
   private final ImmutableList<String> displayNames;
   private final boolean withInstanceFunctions;
   private GlobalDataElements globalDEs = GlobalDataElements.EMPTY;
   private final ScriptCompletionContext.FunctionArgumentCompleter funcArgCompleter;
   private FunctionInserterLookup functionInserterLookup = FunctionInserterLookup.DEFAULT;
   private final List<CompletionContext> additionalContexts;

   public ScriptCompletionContext(JTextComponent var1, ScriptPartitioner var2, FunctionArgumentSuggestions var3, boolean var4) {
      this.inputComponent = var1;
      this.partitioner = var2;
      java.util.LinkedHashSet<String> names = new java.util.LinkedHashSet<>(FunctionRegistry.getRegistry().getDisplayNames());
      for (org.etlforge.integration.data.script.FunctionCategory category : org.etlforge.integration.data.script.JavaScriptFunctions.categories())
         for (Function function : category.getFunctions()) names.add(function.getName());
      this.displayNames = ImmutableList.copyOf(names);
      this.withInstanceFunctions = var4;
      if (var3 != null) {
         this.funcArgCompleter = new ScriptCompletionContext.FunctionArgumentCompleter(var2, var3);
      } else {
         this.funcArgCompleter = null;
      }

      this.additionalContexts = Lists.newArrayList();
   }

   public void addAdditionalContext(CompletionContext var1) {
      this.additionalContexts.add(var1);
   }

   public void removeAdditionalContext(CompletionContext var1) {
      this.additionalContexts.remove(var1);
   }

   public void setFunctionInserterLookup(FunctionInserterLookup var1) {
      this.functionInserterLookup = var1;
   }

   public void setGlobalDataElements(GlobalDataElements var1) {
      this.globalDEs = Preconditions.checkNotNull(var1);
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      Preconditions.checkArgument(var1 == this.inputComponent.getDocument(), "doc is not the document for which this context was created.");
      if (!this.partitioner.isAutoCompletionAvailable(var2)) {
         return new CompletionToken[0];
      }

      if (this.funcArgCompleter != null) {
         CompletionToken[] var3 = this.funcArgCompleter.getTokens(var1, var2);
         if (var3 != null) {
            return var3;
         }
      }

      List var8 = null;
      String var4 = this.getPreContext(var1, var2);
      if (var4.length() > 0) {
         int var5 = this.getLeftWordBoundary(var4, var2);
         if (var5 < var2) {
            var8 = this.getMatchingTokens(var4, var2, var5);
         } else if (StringUtils.endsWith(var4, ".")) {
            var8 = Lists.newArrayList();
         }
      }

      if (var8 == null) {
         var8 = this.getAllExistingTokens(var2, var2);
      }

      for (CompletionContext var6 : this.additionalContexts) {
         CompletionToken[] var7 = var6.getAvailableTokens(var1, var2);
         Collections.addAll(var8, var7);
      }

      return (CompletionToken[]) var8.toArray(new CompletionToken[var8.size()]);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }

   private int getLeftWordBoundary(String var1, int var2) {
      int var3 = var2 - 1;
      boolean var4 = this.partitioner.isGlobalDataElement(Math.max(0, var3));

      while (var3 >= 0) {
         char var5 = var1.charAt(var3);
         if (GlobalDE.isMarker(var5)) {
            return var3;
         }

         if (!var4 && !Character.isDigit(var5) && !Character.isLetter(var5)) {
            return var3 + 1;
         }

         var3--;
      }

      return var3 < 0 ? 0 : var2;
   }

   private List<CompletionToken> getMatchingTokens(String var1, int var2, int var3) {
      assert var1.length() == var2;
      assert var3 < var2;
      if (GlobalDE.isMarker(var1.charAt(var3))) {
         if (var3 > 0) {
            char var4 = var1.charAt(var3 - 1);
            if (!isAllowedBeforeFunctionOrDataElement(var4)) {
               return Lists.newArrayList();
            }
         }

         return this.getMatchingGlobalDataElements(var1, var2, var3);
      } else {
         return this.getMatchingFunctions(var1, var2, var3);
      }
   }

   private static boolean isAllowedBeforeFunctionOrDataElement(char var0) {
      return Character.isWhitespace(var0)
         || var0 == '='
         || var0 == '+'
         || var0 == '-'
         || var0 == '*'
         || var0 == '/'
         || var0 == ';'
         || var0 == ','
         || var0 == '<'
         || var0 == '>'
         || var0 == '&'
         || var0 == '|'
         || var0 == '^'
         || var0 == '('
         || var0 == '['
         || var0 == '{'
         || var0 == '!';
   }

   private List<CompletionToken> getMatchingFunctions(String var1, int var2, int var3) {
      if (var3 > 0) {
         char var4 = var1.charAt(var3 - 1);
         if (!isAllowedBeforeFunctionOrDataElement(var4)) {
            return Lists.newArrayList();
         }
      }

      return this.toTokenList(this.displayNames, new ScriptCompletionContext.FunctionTokenFactory(var1, var3, var2));
   }

   private List<CompletionToken> getMatchingGlobalDataElements(String var1, int var2, int var3) {
      return this.globalDEs != null
         ? this.toTokenList(this.getGlobalDataElements(), new ScriptCompletionContext.DataElementTokenFactory(var1, var3, var2))
         : null;
   }

   private List<CompletionToken> toTokenList(Collection<String> var1, ScriptCompletionContext.TokenFactory var2) {
      return var2.transform(var1);
   }

   private List<CompletionToken> getAllExistingTokens(int var1, int var2) {
      List var3 = this.toTokenList(this.displayNames, new ScriptCompletionContext.FunctionTokenFactory(var1, var2));
      var3.addAll(this.getAllGlobalDataElements(var1, var2));
      return var3;
   }

   private List<CompletionToken> getAllGlobalDataElements(int var1, int var2) {
      return this.toTokenList(this.getGlobalDataElements(), new ScriptCompletionContext.DataElementTokenFactory(var1, var2));
   }

   private Set<String> getGlobalDataElements() {
      SortedSet var1 = this.globalDEs.getAllDataElementsWithPrefix();
      if (UseReferencedDataElementsOnlyInAutoCompletion.includeAll()) {
         var1.addAll(BuiltInDataElements.getBuiltInDataElementsWithPrefix());
      }

      return var1;
   }

   private String getPreContext(Document var1, int var2) {
      try {
         return var1.getText(0, var2);
      } catch (BadLocationException var4) {
         return "";
      }
   }

   private class DataElementTokenFactory extends ScriptCompletionContext.TokenFactory {
      public DataElementTokenFactory(int var2, int var3) {
         super(var2, var3);
      }

      public DataElementTokenFactory(String var2, int var3, int var4) {
         super(var2, var3, var4);
      }

      @Override
      protected CompletionToken getToken(String var1, int var2, int var3) {
         return new GlobalDataElementCompletionToken(var1, var2, var3, ScriptCompletionContext.this.inputComponent);
      }
   }

   private static final class FunctionArgumentCompleter {
      private final ScriptPartitioner partitioner;
      private final FunctionArgumentSuggestions functionArgumentSuggestions;
      private Function function;

      public FunctionArgumentCompleter(ScriptPartitioner var1, FunctionArgumentSuggestions var2) {
         this.partitioner = var1;
         this.functionArgumentSuggestions = var2;
      }

      public CompletionToken[] getTokens(Document var1, int var2) {
         if (this.isFirstArgumentOfFunction(var1, var2)) {
            FunctionArgumentSuggestion var3 = this.functionArgumentSuggestions.getSuggestion(this.function);
            if (var3 != FunctionArgumentSuggestion.NONE) {
               return var3.getCompletionTokens(this.function, var2);
            }
         }

         return null;
      }

      private boolean isFirstArgumentOfFunction(Document var1, int var2) {
         TextPartitioner.Partition var3 = this.findFunction(var2 - 1);
         if (var3 != null) {
            String var4 = DocumentUtils.getText(var1, var3);
            this.function = FunctionRegistry.getRegistry().getFunctionByName(var4);
            return this.function != null;
         } else {
            return false;
         }
      }

      private TextPartitioner.Partition findFunction(int var1) {
         TextPartitioner.Partition var2 = this.consumeCommentsAndWhitespace(this.partitioner.getPartitionAt(var1));
         if (var2 != null && var2.token == Tokens.GROUPING) {
            var2 = var2.getPrevious();
            if (var2 != null && var2.token == Tokens.FUNCTION) {
               return var2;
            }
         }

         return null;
      }

      private TextPartitioner.Partition consumeCommentsAndWhitespace(TextPartitioner.Partition var1) {
         while (this.isCommentOrWhitespace(var1)) {
            var1 = var1.getPrevious();
         }

         return var1;
      }

      private boolean isCommentOrWhitespace(TextPartitioner.Partition var1) {
         return var1 == null ? false : var1.token.isWhitespace() || var1.token == Tokens.SINGLE_LINE_COMMENT || var1.token == Tokens.BLOCK_COMMENT;
      }
   }

   private class FunctionTokenFactory extends ScriptCompletionContext.TokenFactory {
      public FunctionTokenFactory(int var2, int var3) {
         super(var2, var3);
      }

      public FunctionTokenFactory(String var2, int var3, int var4) {
         super(var2, var3, var4);
      }

      @Override
      protected CompletionToken getToken(String var1, int var2, int var3) {
         Function var4 = FunctionRegistry.getRegistry().getFunctionByName(var1);
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(ScriptCompletionContext.this.inputComponent.getText(), var3))
            var4 = org.etlforge.integration.data.script.JavaScriptFunctions.find(var1);
         if (var4 == null) return null;
         if (this.isHidden(var4)) {
            return null;
         }

         FunctionInserter var5 = ScriptCompletionContext.this.functionInserterLookup.getInserter(var4);
         return var5.getCompletionToken(var2, var3, ScriptCompletionContext.this.inputComponent);
      }

      private boolean isHidden(Function var1) {
         return var1.isHidden() ? true : var1.isInstanceFunction() && !ScriptCompletionContext.this.withInstanceFunctions;
      }
   }

   private abstract class TokenFactory implements com.google.common.base.Function<String, CompletionToken> {
      private final String preamble;
      private final int leftBoundary;
      private final int cursor;

      public TokenFactory(int var2, int var3) {
         this(null, var2, var3);
      }

      public TokenFactory(String var2, int var3, int var4) {
         this.preamble = var2 != null ? var2.substring(var3).toLowerCase() : null;
         this.leftBoundary = var3;
         this.cursor = var4;
      }

      public CompletionToken apply(String var1) {
         return this.preamble != null && !var1.toLowerCase().startsWith(this.preamble) ? null : this.getToken(var1, this.leftBoundary, this.cursor);
      }

      protected abstract CompletionToken getToken(String var1, int var2, int var3);

      public final List<CompletionToken> transform(Collection<String> var1) {
         return KongaListUtils.transform(var1, this, Predicates.notNull());
      }
   }
}
