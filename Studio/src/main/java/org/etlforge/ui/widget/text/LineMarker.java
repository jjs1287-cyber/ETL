package org.etlforge.ui.widget.text;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.text.Position;

public interface LineMarker {
   Position getPosition();

   boolean isAlive();

   Icon getIcon();

   String getToolTip();

   void handleEvent(LineNumberBar var1, MouseEvent var2);
}
