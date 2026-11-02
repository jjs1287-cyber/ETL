package com.etlforge.integration.client.ui.script.fcn;

import org.etlforge.application.ui.widget.tree.ExpandCollapseToggleAction;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunction;
import org.etlforge.ui.widget.tree.TreeFilterField;

public final class TreeSelectorLayout implements UiProvider {
   private final EntityTreeSelector<?> selector;
   private final TreeFilterField filterField;
   private final boolean useRootChildren;

   public TreeSelectorLayout(EntityTreeSelector<?> var1, boolean var2) {
      this.selector = var1;
      var1.expandAll();
      this.filterField = var1.createFilterField(new LeafDisplayNameFilterFunction(StringMatchers.CONTAINS_IGNORE_CASE, true), "&Filter:");
      this.useRootChildren = var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      ExpandCollapseToggleAction var2 = this.selector.getExpansionToggle();
      var2.setUseRootChildren(this.useRootChildren);
      BoxBuilder var3 = this.createToolbar(var2);
      BorderLayoutBuilder var4 = new BorderLayoutBuilder(0, 2);
      var4.north(var3).center(this.selector);
      ActionKeyBinding.install(var2, var4.container(), 1);
      var4.decorate(var1);
   }

   private BoxBuilder createToolbar(ExpandCollapseToggleAction var1) {
      return Horizontal.box().add(this.filterField).glue().rigid(10).add(KongaButton.forToolbar(var1)).withBorder(Empty.border(0, 2, 0, 0));
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocus();
   }
}
