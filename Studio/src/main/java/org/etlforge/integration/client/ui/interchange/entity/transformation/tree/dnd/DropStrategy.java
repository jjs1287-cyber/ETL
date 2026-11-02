package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.dnd;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

public interface DropStrategy {
   DropZone[] NO_ZONES = new DropZone[0];
   DropStrategy NONE = new DropStrategy() {
      @Override
      public boolean handleDrop(DropTargetDropEvent var1) {
         return false;
      }

      @Override
      public DropZone[] getDropZones(DropTargetDragEvent var1) {
         return NO_ZONES;
      }
   };

   DropZone[] getDropZones(DropTargetDragEvent var1);

   boolean handleDrop(DropTargetDropEvent var1);
}
