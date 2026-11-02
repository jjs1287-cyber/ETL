package com.etlforge.integration.client.ui.interchange.project.share;

import com.etlforge.integration.client.project.share.IdConflict;
import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.focus.StaticFocusTraversalPolicy;
import org.etlforge.ui.widget.button.BigButton;
import org.etlforge.ui.widget.button.KongaCheckBox;

class IdConflictResolverUi implements UiProvider {
   private final BigButton replace;
   private final BigButton keep;
   private final JCheckBox applyToAllCheck;
   private int numberOfRemainingConflicts;
   private OneColumnPanel ui;

   public IdConflictResolverUi(IdConflict var1, Receiver<IdConflictResolver.Choice> var2) {
      IntegrationEntity var3 = getNewerEntity(var1);
      this.replace = this.createButton(
         var2,
         IdConflictResolver.Choice.REPLACE,
         "Move and Replace",
         "Replace the item in the destination project with the item you are exporting:",
         var1.getCopiedEntity(),
         var1.getCopiedEntity() == var3
      );
      this.keep = this.createButton(
         var2,
         IdConflictResolver.Choice.KEEP,
         "Do Not Move",
         "Keep the existing definition of the item in the destination project:",
         var1.getExistingEntity(),
         var1.getExistingEntity() == var3
      );
      this.applyToAllCheck = new KongaCheckBox();
   }

   public void setNumberOfRemainingConflicts(int var1) {
      this.numberOfRemainingConflicts = var1;
      this.applyToAllCheck.setText("&Do this for next " + var1 + " conflicts");
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui = new OneColumnPanel()
         .withBackground(javax.swing.UIManager.getColor("Panel.background"))
         .withBorder(new BorderBuilder().line(Color.DARK_GRAY).empty(10))
         .asFocusCycleRoot()
         .withFocusTraversalPolicy(this.getTraversalPolicy());
      this.ui.setWeightX(1.0);
      this.ui.withInsets(0, 0, 5, 0);
      this.ui.add(TextStyles.BigAdminHintBold.makeLabel("There is already an item with the same ID in the destination project"));
      this.ui.add(new JLabel("Click the item you want to keep"));
      this.ui.withInsets(10, 20, 10, 0);
      this.ui.addAll(this.replace, this.keep);
      if (this.numberOfRemainingConflicts > 0) {
         this.applyToAllCheck.setOpaque(false);
         this.ui.withInsets(10, 0, 0, 0);
         this.ui.add(this.applyToAllCheck);
      }

      this.ui.decorate(var1);
   }

   private FocusTraversalPolicy getTraversalPolicy() {
      Component[] var1 = this.numberOfRemainingConflicts > 0
         ? new Component[]{this.replace.getUi(), this.keep.getUi(), this.applyToAllCheck}
         : new Component[]{this.replace.getUi(), this.keep.getUi()};
      return new StaticFocusTraversalPolicy(var1);
   }

   @Override
   public void requestFocus() {
      if (this.ui != null) {
         this.ui.requestFocus();
      }
   }

   private static IntegrationEntity getNewerEntity(IdConflict var0) {
      Date var1 = var0.getCopiedEntity().getModifiedAt();
      Date var2 = var0.getExistingEntity().getModifiedAt();
      if (var1.after(var2)) {
         return var0.getCopiedEntity();
      } else {
         return var2.after(var1) ? var0.getExistingEntity() : null;
      }
   }

   public boolean isApplyToAllSelected() {
      return this.applyToAllCheck.isSelected();
   }

   private BigButton createButton(
      final Receiver<IdConflictResolver.Choice> var1, final IdConflictResolver.Choice var2, String var3, String var4, IntegrationEntity var5, boolean var6
   ) {
      BigButton var7 = new BigButton(this.createButtonUi(var3, var4, var5, var6));
      var7.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.handle(var2);
         }
      });
      var7.setUnselectedBackground(Color.WHITE);
      return var7;
   }

   private JComponent createButtonUi(String var1, String var2, IntegrationEntity var3, boolean var4) {
      OneColumnPanel var5 = new OneColumnPanel();
      var5.withInsets(0, 0, 5, 0);
      var5.setWeightX(1.0);
      var5.add(this.createTop(var1, var2));
      var5.add(this.createBottom(var3, var4));
      return ComponentCanvasFactory.northBorder(var5).getUi();
   }

   private UiProvider createTop(String var1, String var2) {
      OneColumnPanel var3 = new OneColumnPanel();
      var3.setWeightX(1.0);
      var3.withInsets(0, 0, 2, 0);
      var3.add(TextStyles.BigAdminHintBold.makeLabel(var1));
      var3.add(TextStyles.AdminHint.makeLabel(var2));
      return var3;
   }

   private UiProvider createBottom(IntegrationEntity var1, boolean var2) {
      BoxBuilder var3 = BoxBuilder.horizontal();
      JLabel var4 = new JLabel(IconStore.getIcons(var1).getDefaultIcon(EntityIcons.Size.LARGE));
      var4.setVerticalAlignment(1);
      var4.setBorder(Empty.border(0, 10, 0, 20));
      OneColumnPanel var5 = this.createEntityInfo(var1, var2);
      var3.add(var4).add(var5);
      return var3;
   }

   private OneColumnPanel createEntityInfo(IntegrationEntity var1, boolean var2) {
      OneColumnPanel var3 = new OneColumnPanel();
      var3.setWeightX(1.0);
      var3.withInsets(0, 0, 5, 0);
      var3.add(TextStyles.DefaultBold.makeLabel(var1.getName()));
      JLabel var4 = new JLabel(EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL));
      var4.setText("Project: " + ProjectUtils.getProject(var1).getName());
      var4.setHorizontalAlignment(10);
      var3.add(var4);
      String var5 = "Date modified: " + var1.getModifiedAt();
      if (var2) {
         var5 = var5 + " (newer)";
      }

      var3.add(new JLabel(var5));
      return var3;
   }
}

