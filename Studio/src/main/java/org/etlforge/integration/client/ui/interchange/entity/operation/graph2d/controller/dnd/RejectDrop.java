package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;

public final class RejectDrop extends DropBehavior {
   @Override
   public boolean isDropAllowed(DropTargetDragEvent var1) {
      return false;
   }

   @Override
   public boolean handleDrop(DropTargetDropEvent var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public Collection<?> getNewSelection() {
      throw new UnsupportedOperationException();
   }
}
