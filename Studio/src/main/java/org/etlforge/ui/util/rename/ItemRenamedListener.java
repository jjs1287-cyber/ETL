package org.etlforge.ui.util.rename;

import java.util.EventListener;

public interface ItemRenamedListener extends EventListener {
   void itemRenamed(ItemRenamedEvent var1);

   void itemRenameCancelled();

   String getItemName(Object var1);
}
