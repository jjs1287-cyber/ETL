package org.etlforge.integration.client.ui.script.builder;

import java.util.EnumSet;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.ui.script.text.WhitespaceDecorator;
import org.etlforge.ui.text.BracketMatcher;
import org.etlforge.ui.text.BracketType;

final class ScriptBuilderAddOns {
   private final ExpressionArea inputArea;

   public ScriptBuilderAddOns(ExpressionArea var1) {
      this.inputArea = var1;
   }

   public void installCharacterMatching() {
      BracketMatcher var1 = new BracketMatcher(this.inputArea.getTextPane(), EnumSet.of(BracketType.PARENTHESES)) {
         @Override
         protected boolean isCharacterMatchingAvailable(int var1) {
            return ScriptBuilderAddOns.this.inputArea.getPartitioner().isCharacterMatchingAvailable(var1);
         }
      };
      var1.setHighlightColors(Colors.CHARACTER_PAIR_MATCHING_HIGHLIGHT, Colors.CHARACTER_PAIR_MISMATCH_HIGHLIGHT);
   }

   public void installWhitespaceDecorator() {
      new WhitespaceDecorator().install(this.inputArea);
   }
}
