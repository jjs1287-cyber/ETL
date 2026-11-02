package org.etlforge.ui.canvas;

import javax.swing.JComponent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.ComponentCustomizer;

public interface ComponentCanvas<T extends JComponent> extends UiCanvas {
   JComponent getUi();

   void customize(ComponentCustomizer<T> var1);
}
