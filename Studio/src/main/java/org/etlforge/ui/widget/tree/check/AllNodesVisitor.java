package org.etlforge.ui.widget.tree.check;

public abstract class AllNodesVisitor implements CheckBoxNodeVisitor {
   @Override
   public final boolean isDone() {
      return false;
   }
}
