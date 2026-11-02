package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.LabelDecorator;

public class SourceAndTargetList implements UiProvider {
   private static final Icon LEFT_ARROW = UiResourceBundle.getIcon("LeftArrow");
   private static final Icon RIGHT_ARROW = UiResourceBundle.getIcon("RightArrow");
   private final KongaListFactory sourceListFactory;
   private KongaList sourceList;
   private final KongaListFactory targetListFactory;
   private KongaList targetList;
   private ListCellRenderer cellRenderer;
   private final Set<Object> targetItems;
   private final MutableFilteredListModel sourceModel;
   private final MutableListModel targetModel;
   private ListTransferPolicy transferPolicy;
   private final SelectionAction sourceToTargetAction;
   private final SelectionAction targetToSourceAction;
   private JComponent ui;
   private String sourceTitle;
   private String targetTitle;
   private LabelDecorator titleDecorator;
   private boolean opaque;
   private final List<SourceAndTargetListListener> listeners;

   public SourceAndTargetList() {
      this(new DefaultKongaListFactory(), new DefaultKongaListFactory());
   }

   public SourceAndTargetList(KongaListFactory var1, KongaListFactory var2) {
      Preconditions.checkNotNull(var1, "sourceListFactory");
      Preconditions.checkNotNull(var2, "targetListFactory");
      this.sourceListFactory = var1;
      this.targetListFactory = var2;
      this.transferPolicy = ListTransferPolicy.TRIVIAL;
      this.cellRenderer = new DefaultListCellRenderer();
      this.sourceModel = new MutableFilteredListModel(new KongaListModel());
      this.targetModel = new KongaListModel();
      this.targetItems = new LinkedHashSet<>();
      this.sourceToTargetAction = new SourceAndTargetList.SourceToTargetAction();
      this.targetToSourceAction = new SourceAndTargetList.TargetToSourceAction();
      this.listeners = new CopyOnWriteArrayList<>();
   }

   public void setTransferPolicy(ListTransferPolicy var1) {
      this.transferPolicy = var1 != null ? var1 : ListTransferPolicy.TRIVIAL;
   }

   public final void setSourceItems(final Object[] var1, final boolean var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var2) {
               SourceAndTargetList.this.clearTarget(false);
            }

            SourceAndTargetList.this.sourceModel.clear();

            for (Object var4 : var1) {
               SourceAndTargetList.this.sourceModel.addElement(var4);
            }

            if (SourceAndTargetList.this.ui != null) {
               SourceAndTargetList.this.ui.repaint();
            }
         }
      });
   }

   public final void setTargetItems(final Object[] var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SourceAndTargetList.this.clearTarget(false);

            for (Object var4 : var1) {
               SourceAndTargetList.this.targetModel.addElement(var4);
               SourceAndTargetList.this.targetItems.add(var4);
            }

            if (SourceAndTargetList.this.ui != null) {
               SourceAndTargetList.this.ui.repaint();
            }
         }
      });
   }

   public final void clearTarget() {
      this.clearTarget(true);
   }

   public final Object[] getTargetItems() {
      return this.targetItems.toArray();
   }

   public final boolean isTargetEmpty() {
      return this.targetItems.isEmpty();
   }

   public void repaint() {
      if (this.ui != null) {
         this.ui.repaint();
      }
   }

   public final void setCellRenderer(ListCellRenderer var1) {
      this.cellRenderer = Preconditions.checkNotNull(var1);
   }

   public final void setVisibleRowCount(int var1) {
      this.sourceListFactory.setVisibleRowCount(var1);
      this.targetListFactory.setVisibleRowCount(var1);
   }

   public final void setPrototypeCellValue(Object var1) {
      this.sourceListFactory.setPrototypeValue(var1);
      this.targetListFactory.setPrototypeValue(var1);
   }

   public final void setSourceListFilter(ListFilter var1, boolean var2) {
      this.sourceModel.setFilter(var1);
      if (var2 && this.sourceList != null) {
         this.sourceList.selectFirst();
      }
   }

   public void setTitles(String var1, String var2) {
      this.sourceTitle = var1;
      this.targetTitle = var2;
   }

   public final void setTitleDecorator(LabelDecorator var1) {
      this.titleDecorator = var1;
   }

   public final void setOpaque(boolean var1) {
      this.opaque = var1;
   }

   public void addListener(SourceAndTargetListListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(SourceAndTargetListListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.ui = this.createUi();
      var1.display(this.ui);
   }

   @Override
   public final void requestFocus() {
      if (this.sourceList != null) {
         this.sourceList.requestFocus();
      }
   }

   public boolean transferSelectedItems() {
      if (this.sourceToTargetAction.isEnabled()) {
         ActionUtils.run(this.sourceToTargetAction, this, "transfer");
         return true;
      } else {
         return false;
      }
   }

   public Disposable installSourceListKeyTraversalBindings(JComponent var1) {
      return this.sourceList.installKeyTraversalBindings(var1);
   }

   public final void selectFirst() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (SourceAndTargetList.this.sourceList != null) {
               SourceAndTargetList.this.sourceList.requestFocus();
               SourceAndTargetList.this.sourceList.selectFirst();
            }
         }
      });
   }

   public void enableTargetListReordering() {
      Preconditions.checkState(this.ui == null, "This method must be called before this SourceAndTargetList has been added to a parent");
      this.targetListFactory.setDragSource(true);
      this.targetListFactory.setTransferHandler(new SourceAndTargetReorderHandler(this));
   }

   private JComponent createUi() {
      this.sourceList = this.createSourceList();
      this.targetList = this.createTargetList();
      OneRowPanel var1 = new OneRowPanel();
      this.layoutList(this.sourceList, this.sourceTitle, var1);
      this.layoutButtons(var1);
      this.layoutList(this.targetList, this.targetTitle, var1);
      var1.setOpaque(false, false);
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      var2.setOpaque(this.opaque);
      return var2;
   }

   private void layoutList(KongaList var1, String var2, OneRowPanel var3) {
      var3.setAnchor(OneRowPanel.SOUTHWEST);
      var3.setFillOut();
      OneColumnPanel var4 = new OneColumnPanel();
      if (var2 != null) {
         ListTitlePanel var5 = new ListTitlePanel(var2);
         var5.bindLabel(var1);
         if (this.titleDecorator != null) {
            var5.decorateTitle(this.titleDecorator);
         }

         var4.add(var5);
      }

      var4.fillOut(var1);
      var3.add(var4);
   }

   private void layoutButtons(OneRowPanel var1) {
      var1.setAnchor(OneRowPanel.CENTER);
      var1.setFillNone();
      OneColumnPanel var2 = new OneColumnPanel()
         .setAnchor(OneColumnPanel.CENTER)
         .withInsets(5, 10, 5, 10)
         .of(createButton(this.sourceToTargetAction), createButton(this.targetToSourceAction));
      var1.add(var2.deeplyNonOpaque());
   }

   private KongaList createSourceList() {
      DisabledListCellRenderer var1 = new DisabledListCellRenderer(this.cellRenderer) {
         @Override
         protected boolean disableFor(Object var1) {
            return SourceAndTargetList.this.targetItems.contains(var1);
         }
      };
      this.sourceListFactory.setCellRenderer(var1);
      KongaList var2 = this.sourceListFactory.newList(this.sourceModel);
      var2.setDefaultAction(this.sourceToTargetAction);
      return var2;
   }

   private KongaList createTargetList() {
      this.targetListFactory.setCellRenderer(this.cellRenderer);
      KongaList var1 = this.targetListFactory.newList(this.targetModel);
      var1.setDefaultAction(this.targetToSourceAction);
      return var1;
   }

   private static JButton createButton(Action var0) {
      JButton var1 = new JButton(var0);
      var1.setBorder(null);
      var1.setContentAreaFilled(false);
      return var1;
   }

   private void clearTarget(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SourceAndTargetList.this.targetItems.clear();
            SourceAndTargetList.this.targetModel.clear();
            if (var1 && SourceAndTargetList.this.ui != null) {
               SourceAndTargetList.this.ui.repaint();
            }
         }
      });
   }

   private void fireTargetItemsChanged() {
      if (!this.listeners.isEmpty()) {
         Object[] var1 = this.getTargetItems();

         for (SourceAndTargetListListener var3 : this.listeners) {
            var3.targetItemsChanged(this, var1);
         }
      }
   }

   void targetListWasReordered() {
      this.targetItems.clear();

      for (int var1 = 0; var1 < this.targetModel.getSize(); var1++) {
         this.targetItems.add(this.targetModel.getElementAt(var1));
      }

      this.fireTargetItemsChanged();
   }

   private class SourceToTargetAction extends AbstractSelectionAction {
      public SourceToTargetAction() {
         super("", SourceAndTargetList.RIGHT_ARROW);
         this.putValue("ShortDescription", "Move the selected items from the source list to the target list");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.updateTargetModel()) {
            SourceAndTargetList.this.fireTargetItemsChanged();
            SourceAndTargetList.this.ui.repaint();
         }

         SourceAndTargetList.this.sourceList.requestFocus();
      }

      private boolean updateTargetModel() {
         List var1 = this.getMovableObjects();
         if (var1.isEmpty()) {
            return false;
         }

         Object[] var2 = SourceAndTargetList.this.transferPolicy.filterTargetTransfer(var1.toArray());
         if (var2.length == 0) {
            return false;
         }

         for (Object var6 : var2) {
            SourceAndTargetList.this.targetModel.addElement(var6);
            SourceAndTargetList.this.targetItems.add(var6);
         }

         return true;
      }

      private List<Object> getMovableObjects() {
         OrderedSelection var1 = (OrderedSelection)this.getSelection();
         ArrayList var2 = new ArrayList();

         for (Object var6 : var1.toArray()) {
            if (this.canItemBeMovedToTarget(var6)) {
               var2.add(var6);
            }
         }

         return var2;
      }

      @Override
      protected void handleSelectionChange() {
         OrderedSelection var1 = (OrderedSelection)this.getSelection();

         for (Object var5 : var1.toArray()) {
            if (this.canItemBeMovedToTarget(var5)) {
               this.setEnabled(true);
               return;
            }
         }

         this.setEnabled(false);
      }

      private boolean canItemBeMovedToTarget(Object var1) {
         return !SourceAndTargetList.this.targetItems.contains(var1) && SourceAndTargetList.this.transferPolicy.isSelectable(var1);
      }
   }

   private class TargetToSourceAction extends AbstractSelectionAction {
      public TargetToSourceAction() {
         super("", SourceAndTargetList.LEFT_ARROW);
         this.putValue("ShortDescription", "Move the selected items from the target list to the source list");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.updateTargetModel()) {
            SourceAndTargetList.this.fireTargetItemsChanged();
            ((MutableSelectionSource)this.getSelectionSource()).setSelection(null);
            SourceAndTargetList.this.ui.repaint();
         }

         SourceAndTargetList.this.targetList.requestFocus();
      }

      private boolean updateTargetModel() {
         OrderedSelection var1 = (OrderedSelection)this.getSelection();
         boolean var2 = false;

         for (Object var6 : var1.toArray()) {
            if (SourceAndTargetList.this.targetModel.removeElement(var6)) {
               var2 = true;
            }

            SourceAndTargetList.this.targetItems.remove(var6);
         }

         return var2;
      }
   }
}
