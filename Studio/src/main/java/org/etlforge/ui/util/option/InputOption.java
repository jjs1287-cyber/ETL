package org.etlforge.ui.util.option;

import org.etlforge.ui.UiProvider;

public interface InputOption<T> extends Option, UiProvider {
   T getValue();

   boolean isActive();

   void setActive(boolean var1);
}
