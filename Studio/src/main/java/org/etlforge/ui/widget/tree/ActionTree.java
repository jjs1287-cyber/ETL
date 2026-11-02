package org.etlforge.ui.widget.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UIManager2;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

public class ActionTree implements UiProvider {
   private final LinkedHashMap<String, ActionTree.Category> categories = Maps.newLinkedHashMap();
   private UiProvider tree;
   private boolean useScroll = true;

   public void addAction(String var1, Action var2) {
      ActionTree.Category var3 = this.categories.get(var1);
      if (var3 == null) {
         var3 = new ActionTree.Category(var1);
         this.categories.put(var1, var3);
      }

      var3.actions.add(var2);
   }

   public void setCategoryIcon(String var1, Icon var2) {
      ActionTree.Category var3 = this.categories.get(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("Unknown category: " + var1);
      }

      var3.icon = var2;
   }

   public void setUseScroll(boolean var1) {
      this.useScroll = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.tree == null) {
         this.tree = new ActionTree.TreeLikeUi();
      }

      if (this.useScroll) {
         var1.display(new KongaScrollPane(this.tree));
      } else {
         this.tree.decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      if (this.tree != null) {
         this.tree.requestFocus();
      }
   }

   private static class Category {
      public final String name;
      public Icon icon;
      public final List<Action> actions;

      public Category(String var1) {
         this.name = var1;
         this.actions = Lists.newArrayList();
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof ActionTree.Category && ((ActionTree.Category)var1).name.equals(this.name);
      }

      @Override
      public int hashCode() {
         return this.name.hashCode();
      }

      @Override
      public String toString() {
         return this.name;
      }
   }

   private class TreeLikeUi implements UiProvider {
      private final OneColumnPanel ui;

      public TreeLikeUi() {
         Icon var2 = UIManager2.getIcon("Tree.expandedIcon", UiResourceBundle.getIcon("DropDownExpanded.16"));
         Icon var3 = UIManager2.getIcon("Tree.collapsedIcon", UiResourceBundle.getIcon("DropDownCollapsedRight.16"));
         TextStyle var4 = this.getCategoryStyle();
         this.ui = new OneColumnPanel();
         this.ui.withInsets(0, 0, 10, 0);
         this.ui.setWeightX(1.0);

         for (ActionTree.Category var6 : ActionTree.this.categories.values()) {
            this.ui.add(this.createCategoryUi(var6, var2, var3, var4).getDisplayer());
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         new BorderLayoutBuilder().north(this.ui).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.ui.requestFocus();
      }

      private TextStyle getCategoryStyle() {
         Font var1 = Fonts.bold(new JButton());
         var1 = var1.deriveFont(var1.getSize2D() + 1.0F);
         return new TextStyle(var1, Color.BLACK);
      }

      private DropDownPanel createCategoryUi(ActionTree.Category var1, Icon var2, Icon var3, TextStyle var4) {
         JComponent var5 = this.layoutButtons(var1);
         DropDownPanel var6 = new DropDownPanel(var5, var1.name, DropDownPanel.ButtonOrientation.LEADING, true);
         var6.setTitleIcon(var1.icon);
         var6.decorateTitle(var4);
         var6.setExpandedIcon(var2);
         var6.setCollapsedIcon(var3);
         return var6;
      }

      private JComponent layoutButtons(ActionTree.Category var1) {
         BoxBuilder var2 = BoxBuilder.vertical().leftAligned().withBorder(Empty.border(0, 16, 0, 0));

         for (Action var4 : var1.actions) {
            KongaButton var5 = new KongaButton(var4).setUseSmallActionIcon(true);
            var2.add(ButtonUtils.configureButtonForToolBar(var5));
         }

         return var2.container();
      }
   }
}
