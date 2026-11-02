package org.etlforge.ui.text.highlight;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.etlforge.ui.text.partition.Token;

public abstract class AbstractTokenStyles implements TokenStyles {
   private final Map<Token, AttributeSet> map = Maps.newIdentityHashMap();
   private final AttributeSet defaultStyle;

   public AbstractTokenStyles() {
      this(coloredForeground(Color.BLACK));
   }

   public AbstractTokenStyles(AttributeSet var1) {
      this.defaultStyle = var1;
   }

   @Override
   public AttributeSet getStyle(Token var1) {
      AttributeSet var2 = this.map.get(var1);
      return var2 != null ? var2 : this.getDefaultStyle();
   }

   protected AttributeSet getDefaultStyle() {
      return this.defaultStyle;
   }

   protected final void addStyle(Token var1, Color var2) {
      this.addStyle(var1, coloredForeground(var2));
   }

   protected final void addStyle(Token var1, AttributeSet var2) {
      this.map.put(var1, var2);
   }

   protected static SimpleAttributeSet coloredForeground(Color var0) {
      SimpleAttributeSet var1 = new SimpleAttributeSet();
      StyleConstants.setForeground(var1, var0);
      return var1;
   }
}
