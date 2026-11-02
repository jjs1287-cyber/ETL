package org.etlforge.ui.widget.list;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public final class NoSelectionModel implements ListSelectionModel {
   @Override
   public void addListSelectionListener(ListSelectionListener var1) {
   }

   @Override
   public void addSelectionInterval(int var1, int var2) {
   }

   @Override
   public void clearSelection() {
   }

   @Override
   public int getAnchorSelectionIndex() {
      return -1;
   }

   @Override
   public int getLeadSelectionIndex() {
      return -1;
   }

   @Override
   public int getMaxSelectionIndex() {
      return -1;
   }

   @Override
   public int getMinSelectionIndex() {
      return -1;
   }

   @Override
   public int getSelectionMode() {
      return -1;
   }

   @Override
   public boolean getValueIsAdjusting() {
      return false;
   }

   @Override
   public void insertIndexInterval(int var1, int var2, boolean var3) {
   }

   @Override
   public boolean isSelectedIndex(int var1) {
      return false;
   }

   @Override
   public boolean isSelectionEmpty() {
      return false;
   }

   @Override
   public void removeIndexInterval(int var1, int var2) {
   }

   @Override
   public void removeListSelectionListener(ListSelectionListener var1) {
   }

   @Override
   public void removeSelectionInterval(int var1, int var2) {
   }

   @Override
   public void setAnchorSelectionIndex(int var1) {
   }

   @Override
   public void setLeadSelectionIndex(int var1) {
   }

   @Override
   public void setSelectionInterval(int var1, int var2) {
   }

   @Override
   public void setSelectionMode(int var1) {
   }

   @Override
   public void setValueIsAdjusting(boolean var1) {
   }
}
