package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;

public class KongaListModel implements MutableListModel {
   private final DefaultListModel impl;

   public KongaListModel() {
      this.impl = new DefaultListModel();
   }

   public KongaListModel(DefaultListModel var1) {
      this.impl = Preconditions.checkNotNull(var1, "impl");
   }

   public KongaListModel(Collection<?> var1) {
      this.impl = ListUtils.createModel(var1);
   }

   public KongaListModel(Object... var1) {
      this(Arrays.asList(var1));
   }

   @Override
   public Object getElementAt(int var1) {
      return this.impl.getElementAt(var1);
   }

   @Override
   public int getSize() {
      return this.impl.getSize();
   }

   @Override
   public void addElement(Object var1) {
      this.impl.addElement(var1);
   }

   @Override
   public void addElementAt(Object var1, int var2) {
      this.impl.insertElementAt(var1, var2);
   }

   @Override
   public void clear() {
      this.impl.clear();
   }

   @Override
   public boolean removeElement(Object var1) {
      return this.impl.removeElement(var1);
   }

   @Override
   public Object removeItemAt(int var1) {
      return this.impl.remove(var1);
   }

   @Override
   public int indexOf(Object var1) {
      return this.impl.indexOf(var1);
   }

   @Override
   public void removeRange(int var1, int var2) {
      this.impl.removeRange(var1, var2);
   }

   @Override
   public void addListDataListener(ListDataListener var1) {
      this.impl.addListDataListener(var1);
   }

   @Override
   public void removeListDataListener(ListDataListener var1) {
      this.impl.removeListDataListener(var1);
   }
}
