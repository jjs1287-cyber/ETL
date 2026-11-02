package org.etlforge.integration.client.ui.script.partition;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.etlforge.application.ui.look.ApplicationPropertyColor;
import org.etlforge.ui.text.highlight.AbstractTokenStyles;
import org.etlforge.ui.text.highlight.TokenStyles;
import org.etlforge.ui.text.partition.Token;

public final class ScriptTokenStyles extends AbstractTokenStyles {
   private static final AttributeSet TRANS_TAG_ATTRIBUTES = getTransTagAttributes();
   public static final TokenStyles STYLES = new ScriptTokenStyles();

   private ScriptTokenStyles() {
      super(coloredForeground("FormulaBuilder.Color.Default", org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND));
      this.addStyle(Tokens.RAISE_ERROR, getRaiseErrorAttributes());
      this.addColoredForegroundToken(Tokens.FUNCTION, "FormulaBuilder.Color.Function", new Color(220, 160, 230));
      this.addColoredForegroundToken(Tokens.STRING, "FormulaBuilder.Color.String", new Color(142, 193, 238));
      Color var1 = new Color(143, 174, 140);
      this.addColoredForegroundToken(Tokens.SINGLE_LINE_COMMENT, "FormulaBuilder.Color.Comment", var1);
      this.addColoredForegroundToken(Tokens.BLOCK_COMMENT, "FormulaBuilder.Color.Comment", var1);
      this.addColoredForegroundToken(Tokens.BOOLEAN, "FormulaBuilder.Color.Constant", new Color(108, 209, 144));
      this.addColoredForegroundToken(Tokens.GLOBAL_DATA_ELEMENT, "FormulaBuilder.Color.GlobalDE", new Color(108, 209, 144));
      this.addStyle(Tokens.BEGIN_TRANS, TRANS_TAG_ATTRIBUTES);
      this.addStyle(Tokens.END_TRANS, TRANS_TAG_ATTRIBUTES);
      this.addColoredForegroundToken(Tokens.JAVASCRIPT_KEYWORD, "FormulaBuilder.Color.Function", new Color(220,160,230));
   }

   private void addColoredForegroundToken(Token var1, String var2, Color var3) {
      this.addStyle(var1, ApplicationPropertyColor.load(var2, var3));
   }

   private static SimpleAttributeSet coloredForeground(String var0, Color var1) {
      return coloredForeground(ApplicationPropertyColor.load(var0, var1));
   }

   private static AttributeSet getTransTagAttributes() {
      SimpleAttributeSet var0 = coloredForeground(ApplicationPropertyColor.load("FormulaBuilder.Color.TransTags", org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND));
      StyleConstants.setBold(var0, true);
      return var0;
   }

   private static AttributeSet getRaiseErrorAttributes() {
      SimpleAttributeSet var0 = coloredForeground(new Color(255, 125, 125));
      StyleConstants.setItalic(var0, true);
      return var0;
   }
}

