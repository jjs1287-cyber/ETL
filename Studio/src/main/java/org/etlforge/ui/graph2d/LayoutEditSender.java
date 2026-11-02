package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Timer;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.graph.layout.GraphLayout;
import org.etlforge.ui.graph.layout.GraphLayoutEdit;
import org.etlforge.ui.graph.layout.GraphLayoutMemento;

public class LayoutEditSender {
   private final GraphLayout layout;
   private GraphLayoutMemento previous;
   private final List<GraphControllerListener> listeners;
   private final Timer timer;

   public LayoutEditSender(GraphLayout var1) {
      this(var1, 0);
   }

   public LayoutEditSender(GraphLayout var1, int var2) {
      this.layout = var1;
      this.previous = var1.getMemento();
      this.listeners = new CopyOnWriteArrayList<>();
      this.timer = var2 > 0 ? this.createTimer(var2) : null;
   }

   public void newEdit() {
      if (this.timer != null) {
         this.timer.restart();
      } else {
         this.createAndSendEdit();
      }
   }

   public void addListener(GraphControllerListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(GraphControllerListener var1) {
      this.listeners.remove(var1);
   }

   private void createAndSendEdit() {
      GraphLayoutMemento var1 = this.layout.getMemento();
      LayoutEditSender.EditImpl var2 = new LayoutEditSender.EditImpl(this.previous, var1);

      for (GraphControllerListener var4 : this.listeners) {
         var4.layoutWasChanged(var2);
      }

      this.previous = var1;
   }

   private Timer createTimer(int var1) {
      Timer var2 = new Timer(var1, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            LayoutEditSender.this.createAndSendEdit();
         }
      });
      var2.setRepeats(false);
      return var2;
   }

   private class EditImpl extends GraphLayoutEdit {
      public EditImpl(GraphLayoutMemento var2, GraphLayoutMemento var3) {
         super(var2, var3);
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         LayoutEditSender.this.previous = this.getNewState();
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         LayoutEditSender.this.previous = this.getOldState();
      }
   }
}
