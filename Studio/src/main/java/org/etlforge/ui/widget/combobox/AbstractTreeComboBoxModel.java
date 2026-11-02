package org.etlforge.ui.widget.combobox;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.tree.TreeModel;

public abstract class AbstractTreeComboBoxModel implements TreeComboBoxModel {
   private final DefaultComboBoxModel impl;
   private final Map<Object, Integer> indentLevel = Maps.newHashMap();
   private int lastSelectedIndex = -1;
   private AbstractTreeComboBoxModel.UnselectableItemMode unselectableItemMode = AbstractTreeComboBoxModel.UnselectableItemMode.SELECT_NEXT;

   public AbstractTreeComboBoxModel(TreeModel var1, boolean var2) {
      Preconditions.checkNotNull(var1, "treeModel");
      this.impl = new DefaultComboBoxModel();
      this.process(var1, var1.getRoot(), var2 ? 0 : -1, var2);
   }

   private void process(TreeModel var1, Object var2, int var3, boolean var4) {
      if (var4 || var2 != var1.getRoot()) {
         this.addItem(var2, var3);
      }

      int var5 = 0;

      for (int var6 = var1.getChildCount(var2); var5 < var6; var5++) {
         Object var7 = var1.getChild(var2, var5);
         this.process(var1, var7, var3 + 1, true);
      }
   }

   private void addItem(Object var1, int var2) {
      Object var3 = this.getModelObject(var1);
      this.indentLevel.put(var3, var2);
      this.impl.addElement(var3);
   }

   protected abstract Object getModelObject(Object var1);

   public final void setUnselectableItemMode(AbstractTreeComboBoxModel.UnselectableItemMode var1) {
      this.unselectableItemMode = Preconditions.checkNotNull(var1);
   }

   @Override
   public final Object getSelectedItem() {
      return this.impl.getSelectedItem();
   }

   public final void selectFirstAvailableItem() {
      for (int var1 = 0; var1 < this.getSize(); var1++) {
         Object var2 = this.getElementAt(var1);
         if (this.isSelectable(var2)) {
            this.setSelectedItem(var2);
            return;
         }
      }
   }

   protected final void insertItemAt(Object var1, int var2) {
      this.impl.insertElementAt(var1, var2);
   }

   @Override
   public final void setSelectedItem(Object var1) {
      int var2 = this.impl.getIndexOf(var1);
      if (this.isSelectable(var1)) {
         this.impl.setSelectedItem(var1);
         this.lastSelectedIndex = var2;
      } else if (this.unselectableItemMode != AbstractTreeComboBoxModel.UnselectableItemMode.IGNORE) {
         if (var2 > this.lastSelectedIndex) {
            for (int var3 = var2 + 1; var3 < this.getSize(); var3++) {
               if (this.selectIfSelectable(var3)) {
                  return;
               }
            }
         } else if (var2 < this.lastSelectedIndex) {
            for (int var4 = var2 - 1; var4 >= 0; var4--) {
               if (this.selectIfSelectable(var4)) {
                  return;
               }
            }

            this.selectFirstAvailableItem();
         }
      }
   }

   private boolean selectIfSelectable(int var1) {
      Object var2 = this.getElementAt(var1);
      if (this.isSelectable(var2)) {
         this.impl.setSelectedItem(var2);
         this.lastSelectedIndex = var1;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public final Object getElementAt(int var1) {
      return this.impl.getElementAt(var1);
   }

   @Override
   public final int getSize() {
      return this.impl.getSize();
   }

   @Override
   public final void addListDataListener(ListDataListener var1) {
      this.impl.addListDataListener(var1);
   }

   @Override
   public final void removeListDataListener(ListDataListener var1) {
      this.impl.removeListDataListener(var1);
   }

   @Override
   public final int getIndentLevel(Object var1) {
      Integer var2 = this.indentLevel.get(var1);
      return var2 != null ? var2 : 0;
   }

   @Override
   public boolean isSelectable(Object var1) {
      return true;
   }

   public enum UnselectableItemMode {
      IGNORE,
      SELECT_NEXT;
   }
}
