package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.panel;

import com.google.common.base.Optional;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeVisibilityOptionsMenuBuilder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.OpenFormulaBuilderAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeExpansionAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.ZoomAction;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.PopupMenuButton;

public class ButtonPanel extends JPanel {
   public static final int g_buttonPanelHeight = ClientInfo.isDataLoader() ? 0 : 36;
   public static final int g_buttonWidth = ClientInfo.isDataLoader() ? 0 : 62;
   private final TreeMapper treeMapper;
   private final JButton mapButton;
   private final JButton autoMapperButton;
   private final JButton formulaButton;
   private final Optional<KongaButton> loadSourceButton;
   private final Optional<KongaButton> testTransformationButton;
   private final Optional<KongaButton> testOperationButton;
   private final JButton sourceTreeExpansionButton;
   private final JButton targetTreeExpansionButton;
   private final PopupMenuButton sourceTreeNodeVisibilityOptionsButton;
   private final PopupMenuButton targetTreeNodeVisibilityOptionsButton;
   private final KongaButton zoomInButton;
   private final KongaButton zoomOutButton;

   public ButtonPanel(TreeMapper var1, JButton var2, Action var3, Action var4, Action var5, Action var6) {
      super(null);
      this.treeMapper = var1;
      this.mapButton = var2;
      this.autoMapperButton = createButtonFor(var3);
      this.loadSourceButton = createButtonForOptionalAction(var4);
      this.testTransformationButton = createButtonForOptionalAction(var5);
      this.testOperationButton = createButtonForOptionalAction(var6);
      this.formulaButton = this.createFormulaButton();
      this.sourceTreeExpansionButton = KongaButton.forToolbar(new TreeExpansionAction(this.treeMapper.m_sourceTreeComponent));
      this.targetTreeExpansionButton = KongaButton.forToolbar(new TreeExpansionAction(this.treeMapper.m_targetTreeComponent));
      NodeVisibilityOptionsMenuBuilder var7 = new NodeVisibilityOptionsMenuBuilder(this.treeMapper);
      this.sourceTreeNodeVisibilityOptionsButton = var7.buttonForSourceTree();
      this.targetTreeNodeVisibilityOptionsButton = var7.buttonForTargetTree();
      this.zoomInButton = createButtonFor(ZoomAction.zoomIn(this.treeMapper));
      this.zoomOutButton = createButtonFor(ZoomAction.zoomOut(this.treeMapper));
      this.add(var2);
      this.add(this.formulaButton);
      this.add(this.sourceTreeExpansionButton);
      this.add(this.targetTreeExpansionButton);
      this.add(this.sourceTreeNodeVisibilityOptionsButton);
      this.add(this.targetTreeNodeVisibilityOptionsButton);
      this.add(this.zoomInButton);
      this.add(this.zoomOutButton);
      this.add(this.autoMapperButton);
      if (this.loadSourceButton.isPresent()) {
         this.add(this.loadSourceButton.get());
      }

      if (this.testTransformationButton.isPresent()) {
         this.add(this.testTransformationButton.get());
      }

      if (this.testOperationButton.isPresent()) {
         this.add(this.testOperationButton.get());
      }
   }

   private KongaButton createFormulaButton() {
      KongaButton var1 = new KongaButton(new OpenFormulaBuilderAction(this.treeMapper));
      ButtonUtils.configureButtonForToolBar(var1);
      return var1.withMediumActionIcon();
   }

   public void adjustComponentSize(int var1) {
      byte var2 = 4;
      byte var3 = 25;
      byte var4 = 25;
      int var5 = 3;
      int var6 = var2 + 2;
      byte var7 = 5;
      this.sourceTreeExpansionButton.setBounds(var5, var6, var3, var4);
      var5 += var3 + var7;
      this.sourceTreeNodeVisibilityOptionsButton.setBounds(var5, var6, var3, var4);
      int var8 = var1 / 5 - 90;
      byte var9 = 48;
      byte var10 = 2;
      if (this.loadSourceButton.isPresent()) {
         this.loadSourceButton.get().setBounds(var8, var2, var9, 32);
         var8 += var9 + var10;
      }

      if (this.testTransformationButton.isPresent()) {
         this.testTransformationButton.get().setBounds(var8, var2, var9, 32);
         var8 += var9 + var10;
      }

      if (this.testOperationButton.isPresent()) {
         this.testOperationButton.get().setBounds(var8, var2, var9, 32);
      }

      this.zoomInButton.setBounds(var1 / 2 - g_buttonWidth + 2, var2, var9, 32);
      this.zoomOutButton.setBounds(var1 / 2 - 2, var2, var9, 32);
      var8 = 4 * var1 / 5 - 75;
      this.mapButton.setBounds(var8, var2, var9, 32);
      var8 += var9 + var10;
      this.formulaButton.setBounds(var8, var2, var9, 32);
      var8 += var9 + var10;
      this.autoMapperButton.setBounds(var8, var2, var9, 32);
      var5 = var1 - 25 - 3;
      this.targetTreeExpansionButton.setBounds(var5, var6, var3, var4);
      var5 -= var3 + var7;
      this.targetTreeNodeVisibilityOptionsButton.setBounds(var5, var6, var3, var4);
      this.validate();
   }

   @Override
   public void paintComponent(Graphics var1) {
      var1.setColor(Color.lightGray);
      verticalLine(var1, 67);
      verticalLine(var1, this.treeMapper.m_appletWidth - 69);
      verticalLine(var1, this.treeMapper.m_appletWidth / 2 - g_buttonWidth - 7);
      verticalLine(var1, this.treeMapper.m_appletWidth / 2 + g_buttonWidth - 5);
      var1.drawLine(0, 0, this.treeMapper.m_appletWidth, 0);
      var1.setColor(Color.white);
      var1.drawLine(0, 1, this.treeMapper.m_appletWidth, 1);
   }

   private static void verticalLine(Graphics var0, int var1) {
      var0.drawLine(var1, 2, var1, g_buttonPanelHeight);
   }

   private static Optional<KongaButton> createButtonForOptionalAction(Action var0) {
      return var0 == null ? Optional.absent() : Optional.of(createButtonFor(var0));
   }

   private static KongaButton createButtonFor(Action var0) {
      KongaButton var1 = KongaButton.forToolbar(var0);
      var1.setUseMediumActionIcon(true);
      return var1;
   }
}
