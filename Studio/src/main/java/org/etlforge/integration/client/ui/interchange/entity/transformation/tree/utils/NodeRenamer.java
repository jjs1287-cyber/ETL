package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelDialog;
import org.etlforge.ui.input.InputPanelOverlay;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

final class NodeRenamer {
   private final TreeComponent tree;
   private final Node node;

   public NodeRenamer(TreeComponent var1, Node var2) {
      this.tree = var1;
      this.node = var2;
   }

   public void rename() {
      NodeRenamer.Editor var1 = this.getEditor();
      var1.show(new NodeRenamerPanel(this.tree, this.node));
   }

   private NodeRenamer.Editor getEditor() {
      TreeMapper var1 = this.node.getTreeMapper();
      OverlayContainer var2 = WidgetUtils.getOverlayContainerAncestor(var1);
      return var2 != null ? new NodeRenamer.OverlayEditor(var2) : new NodeRenamer.DialogEditor();
   }

   private void applyNewName(String var1) {
      if (var1 != null) {
         NodeNameUtils.setUserDefinedName(this.tree, this.node, var1);
         this.node.getTreeMapper().targetNodeWasRenamed(this.node, var1);
         this.node.getTreeComponent().repaint();
      }
   }

   private class DialogEditor implements NodeRenamer.Editor {
      private DialogEditor() {
      }

      @Override
      public void show(NodeRenamerPanel var1) {
         KongaDialog var2 = this.createDialog(var1);
         var2.setVisible(true);
         if (var2.wasOkPressed()) {
            NodeRenamer.this.applyNewName(var1.getNewName());
         }
      }

      private KongaDialog createDialog(final NodeRenamerPanel var1) {
         InputPanelDialog var2 = new InputPanelDialog(NodeRenamer.this.node.getTreeMapper().getOwner(), var1, "Rename Node") {
            @Override
            protected void onDialogOpened() {
               super.onDialogOpened();
               var1.requestFocus();
            }

            @Override
            protected boolean useScroll() {
               return false;
            }
         };
         this.customizeDialog(var2);
         this.bindOkEnabler(var2, var1);
         return var2;
      }

      private void customizeDialog(KongaDialog var1) {
         var1.setResizable(false);
         var1.pack();
         var1.manageLocation(NodeRenamer.class);
         var1.setOkButtonAsDefault();
      }

      private void bindOkEnabler(final KongaDialog var1, final InputPanel var2) {
         var2.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1x) {
               DialogEditor.this.updateOkButton(var1, var2);
            }
         });
         this.updateOkButton(var1, var2);
      }

      private void updateOkButton(KongaDialog var1, InputPanel var2) {
         var1.getOKButton().setEnabled(var2.isInputValid());
      }
   }

   private interface Editor {
      void show(NodeRenamerPanel var1);
   }

   private class OverlayEditor implements NodeRenamer.Editor {
      private final OverlayContainer container;

      public OverlayEditor(OverlayContainer var2) {
         this.container = var2;
      }

      @Override
      public void show(NodeRenamerPanel var1) {
         OverlayDecorator var2 = this.createOverlay(var1);
         var2.show(this.container, new OverlayCenterPositioner());
         var1.requestFocus();
      }

      private OverlayDecorator createOverlay(final NodeRenamerPanel var1) {
         InputPanelOverlay var2 = new InputPanelOverlay(var1, "Rename Node") {
            @Override
            protected void onOk() {
               NodeRenamer.this.applyNewName(var1.getNewName());
            }
         };
         var2.setFocusReceiverWhenClosed(NodeRenamer.this.node.getTreeMapper());
         var2.setFadeIn(AnimationPreference.get());
         var2.setAlpha(0.8F);
         var2.useOkAndCancel();
         var2.setBlocking(true);
         this.bindOkAction(var2, var1);
         return var2;
      }

      private void bindOkAction(final OverlayDecorator var1, final NodeRenamerPanel var2) {
         var2.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1x) {
               OverlayEditor.this.updateOkAction(var1, var2);
            }
         });
      }

      private void updateOkAction(OverlayDecorator var1, NodeRenamerPanel var2) {
         var1.getOkAction().setEnabled(var2.isInputValid());
      }
   }
}
