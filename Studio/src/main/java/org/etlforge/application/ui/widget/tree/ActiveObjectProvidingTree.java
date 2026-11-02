package org.etlforge.application.ui.widget.tree;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import java.util.List;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderHelper;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.event.KongaTreeAdapter;
import org.etlforge.ui.widget.tree.event.KongaTreeEvent;

public final class ActiveObjectProvidingTree implements ActiveObjectProvider {
   private final String id;
   private final KongaTree tree;
   private Function<? super KongaTreeNode, ?> activeObjectConverter;
   private final ActiveObjectProviderHelper activeObjectHelper;
   private boolean active;

   public ActiveObjectProvidingTree(String var1, KongaTree var2) {
      this.id = var1;
      this.tree = var2;
      this.activeObjectConverter = Functions.identity();
      this.activeObjectHelper = new ActiveObjectProviderHelper(this);
      var2.addKongaTreeListener(new ActiveObjectProvidingTree.TreeSelectionListener());
   }

   @Override
   public String getProviderId() {
      return this.id;
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public List<?> getActiveObjects() {
      return KongaListUtils.transform(this.tree.getSelectedNodes(), this.activeObjectConverter, Predicates.notNull());
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.add(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.remove(var1);
   }

   public void setActiveObjectConverter(Function<? super KongaTreeNode, ?> var1) {
      if (var1 == null) {
         this.activeObjectConverter = Functions.identity();
      } else {
         this.activeObjectConverter = var1;
      }
   }

   public void setActive(boolean var1) {
      if (var1 != this.active) {
         this.active = var1;
         if (var1) {
            this.fireActiveObjectChange();
         }
      }
   }

   private void fireActiveObjectChange() {
      this.activeObjectHelper.notifyListeners(this.getActiveObjects());
   }

   private class TreeSelectionListener extends KongaTreeAdapter {
      private TreeSelectionListener() {
      }

      @Override
      public void selectionChanged(KongaTreeEvent var1) {
         if (ActiveObjectProvidingTree.this.active) {
            ActiveObjectProvidingTree.this.fireActiveObjectChange();
         }
      }
   }
}
