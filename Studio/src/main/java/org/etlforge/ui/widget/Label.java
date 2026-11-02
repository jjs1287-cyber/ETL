package org.etlforge.ui.widget;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.border.Border;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;

public interface Label {
   void setText(String var1);

   void setText(MnemonicString var1);

   void setIcon(Icon var1);

   void setFont(Font var1);

   void setTextColor(Color var1);

   void setBackgroundColor(Color var1);

   void setToolTip(String var1);

   void setBorder(Border var1);

   UiProvider asUiProvider();
}
