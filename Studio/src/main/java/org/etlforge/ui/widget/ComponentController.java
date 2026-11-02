package org.etlforge.ui.widget;

import java.awt.Component;

public interface ComponentController<T extends Component> {
   void install(T var1);

   void uninstall(T var1);
}
