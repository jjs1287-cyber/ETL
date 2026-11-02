package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import java.util.LinkedList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.lang.ArrayUtils;

public final class PreferencesTreeModel implements TreeModel {
   private final ApplicationPreferences.Node root;

   public PreferencesTreeModel(ApplicationPreferences.Node var1) {
      Preconditions.checkNotNull(var1, "root");
      this.root = var1;
   }

   public ApplicationPreferences.Node getChild(Object var1, int var2) {
      if (!(var1 instanceof ApplicationPreferences.Node)) {
         throw new IllegalArgumentException("Unkown parent: " + var1);
      }

      ApplicationPreferences.Node[] var3 = ((ApplicationPreferences.Node)var1).getChildren();
      return var2 >= 0 && var2 < var3.length ? var3[var2] : null;
   }

   @Override
   public int getChildCount(Object var1) {
      if (var1 instanceof ApplicationPreferences.Node) {
         return ((ApplicationPreferences.Node)var1).getChildren().length;
      } else {
         throw new IllegalArgumentException("Unkown parent: " + var1);
      }
   }

   @Override
   public int getIndexOfChild(Object var1, Object var2) {
      if (var1 == null || var2 == null) {
         return -1;
      } else if (var1 instanceof ApplicationPreferences.Node && var2 instanceof ApplicationPreferences.Node) {
         ApplicationPreferences.Node[] var3 = ((ApplicationPreferences.Node)var1).getChildren();
         return ArrayUtils.indexOf(var3, var2);
      } else {
         return -1;
      }
   }

   public ApplicationPreferences.Node getRoot() {
      return this.root;
   }

   @Override
   public boolean isLeaf(Object var1) {
      return this.getChildCount(var1) == 0;
   }

   @Override
   public void addTreeModelListener(TreeModelListener var1) {
   }

   @Override
   public void removeTreeModelListener(TreeModelListener var1) {
   }

   @Override
   public void valueForPathChanged(TreePath var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public TreePath getPathFromRoot(ApplicationPreferences.Node var1) {
      LinkedList var2 = new LinkedList();

      while (var1 != null) {
         var2.addFirst(var1);
         var1 = var1.getParent();
      }

      return new TreePath(var2.toArray());
   }
}
