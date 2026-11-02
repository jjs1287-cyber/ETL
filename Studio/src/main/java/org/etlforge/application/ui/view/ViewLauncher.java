package org.etlforge.application.ui.view;

import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.Label;

public interface ViewLauncher {
   void decorate(Label var1);

   void activate(UiCanvas var1);

   void dismiss();
}
