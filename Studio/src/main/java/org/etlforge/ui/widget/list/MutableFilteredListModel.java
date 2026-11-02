package org.etlforge.ui.widget.list;

public class MutableFilteredListModel extends FilteredListModel implements MutableListModel {
   public MutableFilteredListModel(MutableListModel var1) {
      super(var1);
   }

   @Override
   public void addElement(Object var1) {
      this.getUnfilteredModel().addElement(var1);
   }

   @Override
   public void addElementAt(Object var1, int var2) {
      this.getUnfilteredModel().addElementAt(var1, var2);
   }

   @Override
   public void clear() {
      this.getUnfilteredModel().clear();
   }

   @Override
   public boolean removeElement(Object var1) {
      return this.getUnfilteredModel().removeElement(var1);
   }

   @Override
   public Object removeItemAt(int var1) {
      return this.getUnfilteredModel().removeItemAt(var1);
   }

   @Override
   public int indexOf(Object var1) {
      return this.getUnfilteredModel().indexOf(var1);
   }

   @Override
   public void removeRange(int var1, int var2) {
      this.getUnfilteredModel().removeRange(var1, var2);
   }

   public MutableListModel getUnfilteredModel() {
      return (MutableListModel)super.getUnfilteredModel();
   }
}
