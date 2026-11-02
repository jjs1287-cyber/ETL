package org.etlforge.ui.widget.menu;

import com.google.common.base.Function;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.button.ButtonUtils;

public class KongaMenuItem extends JMenuItem implements KongaWidget {
   public static final Function<Action, KongaMenuItem> FROM_ACTION = new Function<Action, KongaMenuItem>() {
      public KongaMenuItem apply(Action var1) {
         return new KongaMenuItem(var1);
      }
   };

   public KongaMenuItem() {
   }

   public KongaMenuItem(Action var1) {
      super(var1);
   }

   public KongaMenuItem(Icon var1) {
      super(var1);
   }

   public KongaMenuItem(String var1, Icon var2) {
      super(var1, var2);
   }

   public KongaMenuItem(String var1, int var2) {
      super(var1, var2);
   }

   public KongaMenuItem(String var1) {
      super(var1);
   }

   public KongaMenuItem(MnemonicString var1) {
      this.setText(var1.getText());
      ButtonUtils.applyMnemonics(this, var1);
   }

   @Override
   public void setText(String var1) {
      MnemonicString var2 = MNEMONIC_FACTORY.fromString(var1 != null ? var1 : "");
      super.setText(var2.getText());
      ButtonUtils.applyMnemonics(this, var2);
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
   }
}
