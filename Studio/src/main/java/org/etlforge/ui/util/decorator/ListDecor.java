package org.etlforge.ui.util.decorator;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;

public interface ListDecor {
   void setIcon(Icon var1);

   void setText(String var1);

   void setToolTip(String var1);

   void setFont(Font var1);

   void setTextColor(Color var1);

   Font getListFont();

   Color getTextColor();
}
