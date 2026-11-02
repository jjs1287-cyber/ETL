package org.etlforge.integration.client.ui.entity.search;

import javax.swing.Action;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.ui.UiProvider;

public interface ReplaceField extends UiProvider {
   ReplaceableProperty getProperty();

   Object getValue();

   void setValue(Object var1);

   void setReplaceAction(Action var1);
}
