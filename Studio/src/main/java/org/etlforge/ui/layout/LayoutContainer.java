package org.etlforge.ui.layout;

import java.awt.Component;
import org.etlforge.ui.UiProvider;

public interface LayoutContainer {
   LayoutContainer add(Component var1);

   LayoutContainer add(UiProvider var1);
}
