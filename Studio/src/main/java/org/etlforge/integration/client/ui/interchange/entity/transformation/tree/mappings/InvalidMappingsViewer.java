package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.awt.Point;
import java.awt.Window;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;

public class InvalidMappingsViewer {
   private static boolean OVERLAY_ENABLED = false;
   private final TreeMapper treeMapper;
   private final InvalidMappingsTable table;
   private JComponent trigger;

   public InvalidMappingsViewer(TreeMapper var1) {
      this.table = new InvalidMappingsTable(var1);
      this.treeMapper = var1;
   }

   public void show() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            InvalidMappingsViewer.this.getDisplayer().display();
         }
      });
   }

   public void setTrigger(JComponent var1) {
      this.trigger = var1;
   }

   private InvalidMappingsViewer.Displayer getDisplayer() {
      if (OVERLAY_ENABLED) {
         OverlayContainer var1 = WidgetUtils.getOverlayContainerAncestor(this.treeMapper);
         if (var1 != null) {
            return new InvalidMappingsViewer.OverlayDisplayer(var1);
         }
      }

      return new InvalidMappingsViewer.DialogDisplayer();
   }

   private void applyChanges() {
      List var1 = this.table.getDeletedMappings();
      if (!var1.isEmpty()) {
         this.treeMapper.deleteMappings(var1);
      }
   }

   private class DialogDisplayer extends InvalidMappingsViewer.Displayer {
      private DialogDisplayer() {
      }

      @Override
      public void display() {
         Window var1 = InvalidMappingsViewer.this.trigger != null ? SwingUtilities.getWindowAncestor(InvalidMappingsViewer.this.trigger) : null;
         if (var1 == null) {
            var1 = UiUtils.getActiveWindow();
         }

         KongaDialog var2 = this.createDialog(var1, InvalidMappingsViewer.this.table);
         var2.pack();
         var2.manageLocation(InvalidMappingsViewer.class);
         var2.setOkButtonAsDefault();
         var2.setVisible(true);
         if (var2.wasOkPressed()) {
            InvalidMappingsViewer.this.applyChanges();
         }
      }

      private KongaDialog createDialog(Window var1, final InvalidMappingsTable var2) {
         KongaDialog var3 = new KongaDialog(var1, TreeStrings.get("InvalidMappings.Dialog.Title")) {
            @Override
            protected void onDialogOpened() {
               super.onDialogOpened();
               var2.requestFocus();
               var2.setInitialSelection();
            }
         };
         BorderLayoutBuilder var4 = new BorderLayoutBuilder(5, 0);
         this.fillContentPane(var3, var4, var2);
         var4.withBorder(Empty.border(10)).asContentPaneFor(var3);
         return var3;
      }

      private void fillContentPane(KongaDialog var1, BorderLayoutBuilder var2, InvalidMappingsTable var3) {
         this.layoutTransformationLabel(var2);
         this.layoutTable(var2, var3);
         this.layoutDialogButtons(var1, var2);
      }

      private void layoutTransformationLabel(BorderLayoutBuilder var1) {
         KongaLabel var2 = TextStyles.AdminHint.makeLabel("");
         RendererUtils.decorateRenderer((javax.swing.JLabel)var2, InvalidMappingsViewer.this.treeMapper.getTransformation(), false);
         var1.north(var2);
      }

      private void layoutDialogButtons(KongaDialog var1, BorderLayoutBuilder var2) {
         JPanel var3 = var1.getButtonPanel(KongaDialog.OK_CANCEL, 10);
         var3.setBorder(Empty.border(10, 0, 10, 0));
         var2.south(var3);
      }
   }

   private abstract static class Displayer {
      private Displayer() {
      }

      public abstract void display();

      protected final void layoutTable(BorderLayoutBuilder var1, InvalidMappingsTable var2) {
         OneColumnPanel var3 = new OneColumnPanel();
         this.layoutTableButtons(var3.withInsets(10, 0, 5, 0), var2);
         this.layoutTable(var3.withInsets(0, 0, 5, 0), var2);
         var1.center(var3.deeplyNonOpaque());
      }

      private void layoutTableButtons(OneColumnPanel var1, InvalidMappingsTable var2) {
         final BoxBuilder var3 = BoxBuilder.horizontal();
         JLabel var4 = new JLabel(TreeStrings.get("InvalidMappings.Dialog.Caption"));
         var3.add(var4).glue();
         var2.populate(new ActionContainer() {
            private boolean first = true;

            @Override
            public void add(Action var1) {
               if (!this.first) {
                  var3.strut(10);
               }

               var3.add(new KongaButton(var1));
               this.first = false;
            }

            @Override
            public void remove(Action var1) {
            }
         });
         var1.setAnchor(OneColumnPanel.EAST);
         var1.add(var3);
      }

      private void layoutTable(OneColumnPanel var1, InvalidMappingsTable var2) {
         var1.setAnchor(OneColumnPanel.WEST);
         var1.fillOut(var2);
      }
   }

   private class OverlayDisplayer extends InvalidMappingsViewer.Displayer {
      private final OverlayContainer container;

      public OverlayDisplayer(OverlayContainer var2) {
         this.container = var2;
      }

      @Override
      public void display() {
         OverlayDecorator var1 = this.createOverlay();
         var1.show(this.container, this.getPositioner());
      }

      private OverlayDecorator createOverlay() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10).withBorder(Empty.border(0, 10, 0, 10));
         this.layoutTable(var1, InvalidMappingsViewer.this.table);
         OverlayDecorator var2 = new OverlayDecorator(var1, "Invalid Mappings") {
            @Override
            protected void onOk() {
               InvalidMappingsViewer.this.applyChanges();
            }
         };
         var2.setBlocking(true);
         var2.setFillHorizontally(true);
         var2.useOkAndCancel();
         return var2;
      }

      private OverlayPositioner getPositioner() {
         return InvalidMappingsViewer.this.trigger != null
            ? new OverlayRelativePositioner(InvalidMappingsViewer.this.trigger, new Point(8, 8))
            : new OverlayCenterPositioner();
      }
   }
}

