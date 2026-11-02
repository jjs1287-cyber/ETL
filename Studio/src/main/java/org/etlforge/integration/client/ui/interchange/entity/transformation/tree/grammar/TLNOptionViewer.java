package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar;

import java.awt.Dialog.ModalityType;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public class TLNOptionViewer {
   private final TreeMapper treeMapper;
   private final TreeComponent tree;
   private final Node node;

   public TLNOptionViewer(TreeComponent var1, Node var2) {
      this.tree = var1;
      this.treeMapper = var1.getTreeMapper();
      this.node = var2;
   }

   public void open() {
      this.getDisplayer().open();
   }

   private TLNOptionViewer.Displayer getDisplayer() {
      OverlayContainer var1 = WidgetUtils.getOverlayContainerAncestor(this.treeMapper);
      return var1 != null ? new TLNOptionViewer.OverlayDisplayer(var1) : new TLNOptionViewer.DialogDisplayer();
   }

   private class DialogDisplayer implements TLNOptionViewer.Displayer {
      private final KongaDialog dialog;
      private final TLNOptionPanel panel = new TLNOptionPanel(TLNOptionViewer.this.tree, TLNOptionViewer.this.node) {
         @Override
         public void setValid(boolean var1) {
            super.setValid(var1);
            DialogDisplayer.this.updateOkState();
         }
      };

      public DialogDisplayer() {
         this.dialog = UiProviderDialogDisplayer.createDialog(
            UiUtils.getActiveWindow(), this.panel, "Mapping Options", ModalityType.APPLICATION_MODAL, KongaDialog.OK_CANCEL, TLNOptionViewer.class
         );
         this.dialog.setOkButtonAsDefault();
         this.updateOkState();
      }

      @Override
      public void open() {
         this.dialog.setVisible(true);
         if (this.dialog.wasOkPressed()) {
            this.panel.apply();
         }
      }

      private void updateOkState() {
         this.dialog.getOKButton().setEnabled(this.panel.isValid());
      }
   }

   private interface Displayer {
      void open();
   }

   private class OverlayDisplayer implements TLNOptionViewer.Displayer {
      private final OverlayContainer container;
      private TLNOptionPanel panel;
      private OverlayDecorator overlay;

      public OverlayDisplayer(OverlayContainer var2) {
         this.container = var2;
      }

      @Override
      public void open() {
         this.panel = new TLNOptionPanel(TLNOptionViewer.this.tree, TLNOptionViewer.this.node) {
            @Override
            public void setValid(boolean var1) {
               super.setValid(var1);
               OverlayDisplayer.this.updateOkState();
            }
         };
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.panel).withBorder(Empty.border(10));
         this.overlay = new OverlayDecorator(var1, "Mapping Options") {
            @Override
            protected void onOk() {
               OverlayDisplayer.this.panel.apply();
            }
         };
         this.overlay.setFadeIn(AnimationPreference.get());
         this.overlay.setAlpha(0.8F);
         this.overlay.useOkAndCancel();
         this.overlay.setBlocking(true);
         this.overlay.show(this.container, new OverlayCenterPositioner());
         this.updateOkState();
      }

      private void updateOkState() {
         this.overlay.getOkAction().setEnabled(this.panel.isValid());
      }
   }
}
