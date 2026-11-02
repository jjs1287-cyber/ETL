package org.etlforge.integration.client.ui.entity.move;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.label.HtmlLabel;
import org.etlforge.ui.widget.label.KongaLabel;

public final class DialogConflictResolver implements ConflictResolver {
   private final DeleteRequestHandler deleter;

   public DialogConflictResolver(DeleteRequestHandler var1) {
      Preconditions.checkNotNull(var1, "deleter");
      this.deleter = var1;
   }

   @Override
   public boolean resolve(IntegrationEntity var1, IntegrationEntity var2, IntegrationEntity var3) {
      ConflictResolver var4 = UiThreadSupplier.call(new DialogConflictResolver.Resolution(var1, var2));
      return var4.resolve(var1, var2, var3);
   }

   private class Resolution implements Supplier<ConflictResolver>, Receiver<Boolean> {
      private final IntegrationEntity itemToMove;
      private final IntegrationEntity newOwner;
      private final ConflictResolverSelector selector;
      private final KongaDialog dialog;

      public Resolution(IntegrationEntity var2, IntegrationEntity var3) {
         this.itemToMove = var2;
         this.newOwner = var3;
         this.selector = new ConflictResolverSelector(var2, var3, DialogConflictResolver.this.deleter);
         this.dialog = this.createDialog();
         this.selector.setValidStateMonitor(this);
      }

      public ConflictResolver get() {
         this.dialog.setVisible(true);
         this.selector.dispose();
         return this.dialog.wasOkPressed() ? this.selector.getResolver() : ConflictResolver.DO_NOT_MOVE;
      }

      public void handle(Boolean var1) {
         this.dialog.getOKButton().setEnabled(var1);
      }

      private KongaDialog createDialog() {
         KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), EntityResources.getString("Move.ConflictDialog.Title"));
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.northToSouth(this.createCaption(), this.selector, this.getButtons(var1));
         var2.withBorder(Empty.border(10));
         var2.asContentPaneFor(var1);
         var1.pack();
         var1.setResizable(false);
         var1.manageLocation(DialogConflictResolver.class);
         var1.setOkButtonAsDefault();
         return var1;
      }

      private Object createCaption() {
         KongaLabel var1 = TextStyles.InfoTextHeader.makeLabel(EntityResources.getString("Move.ConflictDialog.Header"));
         var1.setIcon(CommonIcons.WARNING_16);
         HtmlLabel var2 = new HtmlLabel(EntityResources.format("Move.ConflictDialog.Message", this.newOwner.getName(), this.itemToMove.getName()), 400);
         JLabel var3 = new JLabel(EntityResources.getString("Move.ConflictDialog.Question"));
         return Vertical.box().addAllWithSpace(10, var1, var2, var3).leftAligned().withBorder(new BorderBuilder().etched().empty(5));
      }

      private JComponent getButtons(KongaDialog var1) {
         JPanel var2 = var1.getButtonPanel(KongaDialog.OK_CANCEL, 10);
         var2.setBorder(Empty.border(10, 0, 10, 0));
         return var2;
      }
   }
}
