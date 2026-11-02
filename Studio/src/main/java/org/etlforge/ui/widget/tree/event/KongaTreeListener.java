package org.etlforge.ui.widget.tree.event;

import java.util.EventListener;

public interface KongaTreeListener extends EventListener {
   void selectionChanged(KongaTreeEvent var1);

   void nodePropertyChanged(KongaTreeEvent var1);

   void nodeWillExpand(KongaTreeEvent var1);

   void nodeWillCollapse(KongaTreeEvent var1);
}
