package org.etlforge.integration.client.ui.script.builder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.integration.data.entity.script.GuidPathCacheListener;

final class PathChangeHandler implements GuidPathCacheListener {
   private final ExpressionArea expressionArea;
   private Runnable reaction;

   public PathChangeHandler(ExpressionArea var1) {
      this.expressionArea = var1;
   }

   public void setReaction(Runnable var1) {
      this.reaction = var1;
   }

   @Override
   public void pathsHaveChanged(List<GuidPathCacheListener.PathChange> var1) {
      String var2 = this.expressionArea.getText();
      String var3 = var2;

      for (GuidPathCacheListener.PathChange var5 : var1) {
         var3 = this.applyChange(var3, var5);
      }

      if (!var3.equals(var2)) {
         this.expressionArea.setText(var3);
         if (this.reaction != null) {
            this.reaction.run();
         }
      }
   }

   private String applyChange(String var1, GuidPathCacheListener.PathChange var2) {
      String var3 = this.wrapInTags(var2.oldPath);
      if (!var1.contains(var3)) {
         return var1;
      }

      String var4 = this.wrapInTags(var2.newPath);
      return var1.replaceAll(Pattern.quote(var3), Matcher.quoteReplacement(var4));
   }

   private String wrapInTags(String var1) {
      return "\"<TAG>" + var1 + "</TAG>\"";
   }
}
