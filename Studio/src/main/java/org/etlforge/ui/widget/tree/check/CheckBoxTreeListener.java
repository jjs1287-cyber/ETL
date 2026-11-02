package org.etlforge.ui.widget.tree.check;

import java.util.EventListener;

public interface CheckBoxTreeListener extends EventListener {
   void nodeToggled(CheckBoxTreeEvent var1);

   void overallCheckedStateChanged(CheckBoxTreeEvent var1);
}
