package org.etlforge.application.ui.widget.tree;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.PopupMenuActionPopulators;
import org.etlforge.application.ui.widget.TitlePanelFactory;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.ui.widget.panel.KongaPanel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;
import org.etlforge.ui.widget.tree.TreeFilterField;

public class TreePanel implements UiProvider {
   private final KongaTree tree;
   private final PopupMenuButton dropdownMenuButton;
   private boolean active;
   private KongaPanel ui;
   private final TitlePanelFactory titlePanelFactory;
   private TreeFilterField filterField;
   private boolean removeFilterWhenFilterFieldClosed;
   private boolean layoutFilterFieldInSouthPosition;
   private boolean opaque;
   private Receiver<KongaTreeFilter> filterInterceptor;
   private ExpandCollapseToggleAction expansionToggle;
   private UiProvider additionalTitleComponent;

   public TreePanel(String var1, KongaTree var2) {
      this.tree = var2;
      this.titlePanelFactory = new TreePanel.TitlePanelFactoryImpl();
      this.titlePanelFactory.setTitle(var1);
      this.dropdownMenuButton = this.createPopupMenuButton();
      this.removeFilterWhenFilterFieldClosed = true;
      this.opaque = true;
      this.customizeTree();
   }

   private PopupMenuButton createPopupMenuButton() {
      PopupMenuButton var1 = new PopupMenuButton("", ApplicationResources.ICONS.getIcon("ArrowDown.16"), new Action[0], PopupMenuActionPopulators.DEFAULT);
      var1.setToolTipText(ApplicationResources.STRINGS.getString("Menu"));
      ButtonUtils.configureButtonForToolBar(var1);
      return var1;
   }

   private void customizeTree() {
      this.tree.setRootVisible(false);
      this.tree.setShowsRootHandles(true);
   }

   public KongaTree getTree() {
      return this.tree;
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaPanel var2 = this.getUi();
      var1.display(var2);
   }

   public KongaPanel getUi() {
      if (this.ui == null) {
         this.ui = this.createUi();
      }

      return this.ui;
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public String getTitle() {
      return this.titlePanelFactory.getTitle();
   }

   public void setTitle(String var1) {
      this.titlePanelFactory.setTitle(var1);
   }

   public TreePanel setEnabled(boolean var1) {
      this.tree.setEnabled(var1);
      this.titlePanelFactory.setEnabled(var1);
      if (var1) {
         if (this.active) {
            this.activated();
         }
      } else {
         this.deactivated();
      }

      return this;
   }

   public TreePanel setRootVisible(boolean var1) {
      this.tree.setRootVisible(var1);
      return this;
   }

   public TreePanel setTitlePanelBorder(Border var1) {
      this.titlePanelFactory.setBorder(var1);
      return this;
   }

   public TreePanel setTitlePanelBackground(Color var1) {
      this.titlePanelFactory.setBackground(var1);
      return this;
   }

   public TreePanel setTitleStyle(TextStyle var1) {
      this.titlePanelFactory.setTitleStyle(var1);
      return this;
   }

   public TreePanel setIncludeToolBar(boolean var1) {
      this.titlePanelFactory.setIncludeToolBar(var1);
      return this;
   }

   public void activated() {
      if (!this.active) {
         this.active = true;
         this.titlePanelFactory.setActive(true);
      }
   }

   public void deactivated() {
      if (this.active) {
         this.active = false;
         this.titlePanelFactory.setActive(false);
      }
   }

   public void setLayoutFilterFieldBelowTheTree(boolean var1) {
      if (var1 != this.layoutFilterFieldInSouthPosition) {
         this.layoutFilterFieldInSouthPosition = var1;
         if (this.ui != null) {
            this.rebuildUi();
         }
      }
   }

   public void showFilterField(Function<String, KongaTreeFilter> var1) {
      this.showFilterField(var1, true);
   }

   public void showFilterField(Function<String, KongaTreeFilter> var1, boolean var2) {
      this.filterField = this.createFilterField(this.tree, var1);
      this.filterField.setRemoveFilterOnClose(this.removeFilterWhenFilterFieldClosed);
      this.filterField.setInterceptor(this.filterInterceptor);
      if (var2) {
         this.filterField.setCloseable(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               TreePanel.this.hideFilterField();
            }
         });
      }

      if (this.ui != null) {
         this.rebuildUi();
         this.filterField.requestFocus();
      }
   }

   protected TreeFilterField createFilterField(KongaTree var1, Function<String, KongaTreeFilter> var2) {
      return new SimpleTreeFilterField(var1, var2);
   }

   public boolean isShowingFilterField() {
      return this.filterField != null;
   }

   public void giveFocusToFilterField() {
      if (this.filterField != null) {
         this.filterField.requestFocus();
      }
   }

   public void setRemoveFilterWhenFilterFieldClosed(boolean var1) {
      this.removeFilterWhenFilterFieldClosed = var1;
      if (this.filterField != null) {
         this.filterField.setRemoveFilterOnClose(var1);
      }
   }

   public void setFilterInterceptor(Receiver<KongaTreeFilter> var1) {
      this.filterInterceptor = var1;
      if (this.filterField != null) {
         this.filterField.setInterceptor(var1);
      }
   }

   public void setFilterFieldDescription(String var1) {
      if (this.filterField != null) {
         this.filterField.setDescription(var1);
      }
   }

   public void clearFilter() {
      if (this.filterField != null) {
         this.filterField.clear();
      }
   }

   public void hideFilterField() {
      if (this.filterField != null) {
         this.filterField = null;
         if (this.ui != null) {
            this.rebuildUi();
         }
      }
   }

   private void rebuildUi() {
      this.ui.removeAll();
      this.layoutUi(this.ui);
      this.ui.revalidate();
      this.ui.repaint();
   }

   public void setAdditionalTitleComponent(UiProvider var1) {
      this.additionalTitleComponent = var1;
      if (this.ui != null) {
         this.rebuildUi();
      }
   }

   public final TreePanel setDropdownActions(Action[] var1) {
      this.dropdownMenuButton.setActions(var1);
      return this;
   }

   protected JButton[] getToolBarButtons() {
      ArrayList var1 = Lists.newArrayList();
      if (this.dropdownMenuButton.isEnabled()) {
         var1.add(this.dropdownMenuButton);
      }

      this.expansionToggle = new ExpandCollapseToggleAction(this.tree);
      KongaButton var2 = new KongaButton(this.expansionToggle).makeToolbarButton();
      var1.add(var2);
      return (JButton[]) var1.toArray(new JButton[var1.size()]);
   }

   protected JComponent[] getAdditionalTitlePanelComponents() {
      return new JComponent[0];
   }

   public final void setOpaque(boolean var1) {
      this.opaque = var1;
      if (this.ui != null) {
         this.ui.setOpaque(var1);
      }
   }

   private KongaPanel createUi() {
      KongaPanel var1 = new KongaPanel(new BorderLayout());
      var1.setOpaque(this.opaque);
      this.layoutUi(var1);
      if (this.expansionToggle != null) {
         ActionKeyBinding.install(this.expansionToggle, var1, 1);
      }

      return var1;
   }

   private void layoutUi(KongaPanel var1) {
      TitlePanelFactory.TitlePanel var2 = this.titlePanelFactory.createPanel();
      if (this.hasNoAdditionalTitleDecoration()) {
         ContainerCanvas.decorate(var2, var1, "North");
      } else {
         OneColumnPanel var3 = new OneColumnPanel();
         var3.setWeightX(1.0);
         var3.add(var2);
         if (this.additionalTitleComponent != null) {
            var3.add(this.additionalTitleComponent);
         }

         if (this.filterField != null) {
            this.filterField.setBorder(getFilterFieldBorder());
            var3.add(this.filterField);
         }

         ContainerCanvas.decorate(var3, var1, "North");
      }

      JComponent var4 = this.layoutTree(this.tree);
      var1.add(var4, "Center");
      if (this.layoutFilterFieldInSouthPosition && this.filterField != null) {
         this.filterField.setBorder(getFilterFieldBorder());
         ContainerCanvas.decorate(this.filterField, var1, "South");
      }

      FocusUtils.makeComponentMouseFocusable(var1);
   }

   private boolean hasNoAdditionalTitleDecoration() {
      return this.additionalTitleComponent == null && (this.filterField == null || this.layoutFilterFieldInSouthPosition);
   }

   private static Border getFilterFieldBorder() {
      int var0 = getFilterFieldGap();
      return Empty.border(var0, 2, var0, 0);
   }

   private static int getFilterFieldGap() {
      return SupportedLookAndFeel.SYSTEM.isInstalled() && SystemUtils.IS_OS_WINDOWS ? 2 : 0;
   }

   protected JComponent layoutTree(KongaTree var1) {
      return new JScrollPane(var1);
   }

   protected static JButton createToolBarButton(Icon var0) {
      return new KongaButton(var0).makeToolbarButton();
   }

   protected static JButton createToolBarButton(Action var0) {
      return KongaButton.forToolbar(var0);
   }

   private class TitlePanelFactoryImpl extends TitlePanelFactory {
      private TitlePanelFactoryImpl() {
      }

      @Override
      protected JButton[] getToolBarButtons() {
         return TreePanel.this.getToolBarButtons();
      }

      @Override
      protected JComponent[] getAdditionalToolbarComponents() {
         return TreePanel.this.getAdditionalTitlePanelComponents();
      }
   }
}
