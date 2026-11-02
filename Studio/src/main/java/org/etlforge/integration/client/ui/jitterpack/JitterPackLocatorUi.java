package org.etlforge.integration.client.ui.jitterpack;

import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.ui.UiProvider;

public interface JitterPackLocatorUi extends UiProvider {
   JitterPack getJitterPack();

   void addJitterPackSelectionListener(JitterPackSelectionListener var1);

   void removeJitterPackSelectionListener(JitterPackSelectionListener var1);

   boolean isEnabled();

   void setEnabled(boolean var1);
}

