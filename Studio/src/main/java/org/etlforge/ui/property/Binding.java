package org.etlforge.ui.property;

import org.etlforge.system.Disposable;

public interface Binding extends Disposable {
   void syncUi();

   void syncModel();

   void setUiToModelEnabled(boolean var1);

   void setModelToUiEnabled(boolean var1);

   @Override
   void dispose();
}
