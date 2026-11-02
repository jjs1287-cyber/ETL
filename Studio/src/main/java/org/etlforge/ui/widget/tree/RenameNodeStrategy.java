package org.etlforge.ui.widget.tree;

public interface RenameNodeStrategy {
   boolean isRenamable(KongaTreeNode var1);

   boolean rename(KongaTree var1, KongaTreeNode var2, String var3);
}
