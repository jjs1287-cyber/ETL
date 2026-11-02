package org.etlforge.integration.client.ui.interchange.entity.script.search;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.entity.search.ProjectItemOrder;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.ui.util.find.SearchAndReplaceTarget;
import org.etlforge.ui.util.find.SearchAndReplaceTextTarget;
import org.etlforge.ui.util.find.SearchDirection;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.TextSearchMatch;
import org.etlforge.ui.util.find.TextSearchParameters;
import org.etlforge.util.Interval;

public final class ScriptSearchAndReplaceTarget implements SearchAndReplaceTarget {
   private final ProjectItemOrder<Script> scripts;
   private final ExpressionTranslator translator;
   private final ScriptReplaceStrategy replaceStrategy;
   private ScriptSearchAndReplaceTarget.ScriptMatch currentMatch;
   private SearchAndReplaceTextTarget currentText;

   public ScriptSearchAndReplaceTarget(ProjectItemOrder<Script> var1, ExpressionTranslator var2, ScriptReplaceStrategy var3) {
      this.scripts = var1;
      this.translator = var2;
      this.replaceStrategy = var3;
   }

   public ScriptSearchAndReplaceTarget.ScriptMatch nextMatch(TextSearchParameters var1) {
      ScriptSearchAndReplaceTarget.ScriptMatch var2 = null;
      if (this.currentMatch != null) {
         var2 = this.searchInCurrentScript(var1);
      }

      if (var2 == null) {
         var2 = this.searchInNextScript(var1);
      }

      if (var2 != null) {
         this.currentMatch = var2;
      }

      return var2;
   }

   private ScriptSearchAndReplaceTarget.ScriptMatch searchInCurrentScript(TextSearchParameters var1) {
      TextSearchMatch var2 = this.findNextMatchInCurrentScript(var1);
      return this.isNewTextMatch(var2) ? new ScriptSearchAndReplaceTarget.ScriptMatch(this.currentMatch.script, this.scripts, var2) : null;
   }

   private TextSearchMatch findNextMatchInCurrentScript(TextSearchParameters var1) {
      TextSearchParameters var2 = var1.newWrap(false);
      return this.currentText.nextMatch(var2);
   }

   private boolean isNewTextMatch(TextSearchMatch var1) {
      return var1 != null && !this.currentMatch.textLocation.equals(var1);
   }

   private ScriptSearchAndReplaceTarget.ScriptMatch searchInNextScript(TextSearchParameters var1) {
      for (Script var2 = this.getNextScriptToSearch(var1); var2 != null; var2 = this.getNextScriptToSearch(var1)) {
         this.currentText = null;
         ScriptSearchAndReplaceTarget.ScriptMatch var3 = this.searchInScript(var2, var1);
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   private Script getNextScriptToSearch(TextSearchParameters var1) {
      Script var2 = null;
      if (var1.getDirection() == SearchDirection.DOWN) {
         var2 = this.scripts.next();
         if (var2 == null && var1.isWrap()) {
            var2 = this.scripts.getFirst();
         }
      } else {
         var2 = this.scripts.previous();
         if (var2 == null && var1.isWrap()) {
            var2 = this.scripts.getLast();
         }
      }

      return var2;
   }

   private ScriptSearchAndReplaceTarget.ScriptMatch searchInScript(Script var1, TextSearchParameters var2) {
      SearchAndReplaceTextTarget var3 = this.createTextTargetForScript(var1);
      TextSearchMatch var4 = var3.nextMatch(var2);
      if (var4 != null) {
         this.currentText = var3;
         return new ScriptSearchAndReplaceTarget.ScriptMatch(var1, this.scripts, var4);
      } else {
         return null;
      }
   }

   private SearchAndReplaceTextTarget createTextTargetForScript(Script var1) {
      String var2 = this.getExpression(var1);
      return new SearchAndReplaceTextTarget(var2);
   }

   private String getExpression(Script var1) {
      String var2 = var1.getScript();
      return this.translator.toDisplay(var2);
   }

   @Override
   public void replace(SearchTarget.Match var1, String var2) {
      ScriptSearchAndReplaceTarget.ScriptMatch var3 = (ScriptSearchAndReplaceTarget.ScriptMatch)var1;
      SearchAndReplaceTextTarget var4 = this.getTextToReplace(var3);
      var4.replace(var3.textLocation, var2);
      this.handleReplace(var3.script, var4);
   }

   private SearchAndReplaceTextTarget getTextToReplace(ScriptSearchAndReplaceTarget.ScriptMatch var1) {
      return var1 == this.currentMatch && this.currentText != null ? this.currentText : this.createTextTargetForScript(var1.script);
   }

   private void handleReplace(Script var1, SearchAndReplaceTextTarget var2) {
      String var3 = var2.getText();
      this.replaceStrategy.handleReplace(var1, var3);
   }

   @Override
   public List<ScriptSearchAndReplaceTarget.ScriptMatch> replaceAll(TextSearchParameters var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Script var3 = this.scripts.getFirst(); var3 != null; var3 = this.scripts.next()) {
         this.replaceAllInScript(var3, var1, var2);
      }

      return var2;
   }

   private void replaceAllInScript(Script var1, TextSearchParameters var2, List<ScriptSearchAndReplaceTarget.ScriptMatch> var3) {
      SearchAndReplaceTextTarget var4 = this.createTextTargetForScript(var1);
      List var5 = var4.replaceAll(var2);
      if (!var5.isEmpty()) {
         for (Object __etlItem143 : var5) {
            TextSearchMatch var7 = (TextSearchMatch) __etlItem143;
            var3.add(new ScriptSearchAndReplaceTarget.ScriptMatch(var1, this.scripts, var7));
         }

         this.handleReplace(var1, var4);
      }
   }

   public static final class ScriptMatch implements SearchTarget.Match {
      private final Script script;
      private final ProjectItemOrder<Script> scriptOrder;
      private final TextSearchMatch textLocation;

      public ScriptMatch(Script var1, ProjectItemOrder<Script> var2, TextSearchMatch var3) {
         this.script = var1;
         this.scriptOrder = var2;
         this.textLocation = var3;
      }

      @Override
      public int compareLocation(SearchTarget.Match var1) {
         return this.compareLocation((ScriptSearchAndReplaceTarget.ScriptMatch)var1);
      }

      private int compareLocation(ScriptSearchAndReplaceTarget.ScriptMatch var1) {
         if (this.script == var1.script) {
            return this.textLocation.compareLocation(var1.textLocation);
         } else {
            return this.scriptOrder.isBefore(this.script, var1.script) ? -1 : 1;
         }
      }

      public Script getScript() {
         return this.script;
      }

      public Interval getTextLocation() {
         return this.textLocation;
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + System.identityHashCode(this.script);
         return 31 * var1 + this.textLocation.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof ScriptSearchAndReplaceTarget.ScriptMatch)) {
            return false;
         }

         ScriptSearchAndReplaceTarget.ScriptMatch var2 = (ScriptSearchAndReplaceTarget.ScriptMatch)var1;
         return this.script == var2.script && this.textLocation.equals(var2.textLocation);
      }
   }
}
