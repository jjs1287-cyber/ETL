package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.toolbar;

import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import org.etlforge.ui.graph.GraphSelectionEvent;
import org.etlforge.ui.graph.GraphSelectionListener;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.KongaButton;

public final class OperationGraphToolbar {
   private final List<OperationGraphToolbarContribution> contributions = Lists.newArrayList();
   private final JMenuBar menuBar = new JMenuBar();
   private final JToolBar toolbar = new JToolBar();

   public void addContribution(OperationGraphToolbarContribution var1) {
      var1.contributeTo(this);
      this.contributions.add(var1);
   }

   public void addMenu(JMenu var1) {
      this.menuBar.add(var1);
   }

   public void addButton(Action var1) {
      this.addButton(KongaButton.forToolbar(var1).setUseSmallActionIcon(true));
   }

   public void addButton(JButton var1) {
      this.toolbar.add(var1);
   }

   public void addComponent(JComponent var1) {
   }

   public void register(Graph2D var1) {
      var1.getSelectionModel().addSelectionListener(new GraphSelectionListener() {
         @Override
         public void selectionChanged(GraphSelectionEvent var1) {
            OperationGraphToolbar.this.updateContributions(var1.getNewSelection());
         }
      });
      this.updateContributions(var1.getSelectionModel().getSelection().toArray());
   }

   public JComponent getUi() {
      return Horizontal.box().withBorder(new EtchedBorder()).addAllWithSpace(10, this.menuBar, this.toolbar).container();
   }

   private void updateContributions(Object[] var1) {
      for (OperationGraphToolbarContribution var3 : this.contributions) {
         var3.selectionChanged(var1);
      }
   }
}
