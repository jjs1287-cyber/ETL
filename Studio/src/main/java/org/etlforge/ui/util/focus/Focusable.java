package org.etlforge.ui.util.focus;

import java.awt.event.FocusListener;

public interface Focusable {
   void requestFocus();

   void addFocusListener(FocusListener var1);

   void removeFocusListener(FocusListener var1);
}
