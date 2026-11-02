package org.etlforge.ui.text.partition;

import java.awt.event.MouseEvent;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.text.TextComponentMouseHoverer;

public abstract class PartitionWordHoverer extends TextComponentMouseHoverer {
   private TextPartitioner.Partition latestPartition;

   public PartitionWordHoverer(JTextComponent var1, int var2) {
      super(var1, var2);
   }

   @Override
   protected final boolean didMouseMove(MouseEvent var1, MouseEvent var2) {
      TextPartitioner.Partition var3 = this.getPartitionAt(var1);
      TextPartitioner.Partition var4 = this.getPartitionAt(var2);
      this.latestPartition = var4;
      return var3 != var4;
   }

   protected abstract TextPartitioner.Partition getPartitionAt(MouseEvent var1);

   protected final JTextComponent getTarget() {
      return (JTextComponent)super.getTarget();
   }

   @Override
   protected void mouseMoved(MouseEvent var1) {
      if (this.latestPartition != null) {
         this.startTimer(var1);
      }
   }

   @Override
   protected final void hovering(MouseEvent var1) {
      if (this.latestPartition != null) {
         this.handlePartition(this.latestPartition, var1);
      }
   }

   protected abstract void handlePartition(TextPartitioner.Partition var1, MouseEvent var2);
}
