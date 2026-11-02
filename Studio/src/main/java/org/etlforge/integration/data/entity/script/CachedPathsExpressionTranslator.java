package org.etlforge.integration.data.entity.script;

public final class CachedPathsExpressionTranslator implements ExpressionTranslator {
   private final GuidPathCache paths;

   public CachedPathsExpressionTranslator(GuidPathCache var1) {
      this.paths = var1;
   }

   @Override
   public String fromDisplay(String var1) {
      return GuidPathTranslator.translateToGuid(var1, this.paths, null);
   }

   @Override
   public String toDisplay(String var1) {
      return GuidPathTranslator.translateToPath(var1, this.paths);
   }
}
