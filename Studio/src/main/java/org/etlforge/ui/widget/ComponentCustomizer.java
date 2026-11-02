package org.etlforge.ui.widget;

import java.awt.Component;

public interface ComponentCustomizer<T extends Component> {
   T customize(T var1);
}
