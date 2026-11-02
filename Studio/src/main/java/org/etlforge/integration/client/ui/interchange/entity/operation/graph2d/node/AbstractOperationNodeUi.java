package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.ValidationListener;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.validation.ValidationMessage;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.node.AbstractGraphNodeUi2D;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.html.HtmlListGenerator;

public abstract class AbstractOperationNodeUi extends AbstractGraphNodeUi2D {
   private static final TextStyle TITLE_STYLE = createTitleStyle();
   private final OperationWrapperNode node;
   private final JPanel titlePane;
   private final JLabel iconLabel;
   private final JLabel nameLabel;
   private final ImmutableList<OperationNodeToolBarContribution> toolbarContribs;
   private final OperationWrapper dataObject;
   private final AbstractOperationNodeUi.WrapperListener wrapperListener;

   public AbstractOperationNodeUi(Graph2D var1, OperationWrapperNode var2) {
      super(var1);
      this.node = var2;
      this.dataObject = var2.getDataObject();
      this.iconLabel = this.createIconLabel();
      this.nameLabel = this.createNameLabel();
      this.toolbarContribs = this.createToolbarContribs(var2);
      this.titlePane = this.createTitlePane();
      this.buildTitlePane();
      this.wrapperListener = new AbstractOperationNodeUi.WrapperListener();
      var2.getDataObject().addPropertyChangeListener(this.wrapperListener);
      this.getOperation().addValidationListener(this.wrapperListener);
      this.setOpaque(false);
      this.setSelected(false);
   }

   private ImmutableList<OperationNodeToolBarContribution> createToolbarContribs(OperationWrapperNode var1) {
      Builder var2 = ImmutableList.builder();
      var2.add(new ScheduleIndicator(this));
      if (var1.getDataObject().getActivity().isImplementingSalesforcePartnerWizard()) {
         var2.add(new SfdcWizardIndicator(this));
      }

      var2.addAll(Arrays.asList(new HttpEndpointIndicator(this), new OptionsButton(this)));
      var2.add(this.createToggleButton());
      return var2.build();
   }

   public final OperationWrapperNode getNodeObject() {
      return this.node;
   }

   @Override
   public void dispose() {
      try {
         this.node.getDataObject().removePropertyChangeListener(this.wrapperListener);
         this.getOperation().removeValidationListener(this.wrapperListener);
      } finally {
         super.dispose();
      }
   }

   private Operation getOperation() {
      return this.node.getDataObject().getActivity();
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public boolean handleMouseEvent(MouseEvent var1) {
      if (var1.getID() == 500) {
         for (OperationNodeToolBarContribution var3 : this.toolbarContribs) {
            if (var3.handleMouseEvent(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public String getToolTipText(MouseEvent var1) {
      Point var2 = var1.getPoint();
      var2.translate(-this.iconLabel.getX(), -this.iconLabel.getY());
      if (this.iconLabel.contains(var2)) {
         return this.getIconToolTip();
      }

      var2.translate(-this.nameLabel.getX(), -this.nameLabel.getY());
      if (this.nameLabel.contains(var2)) {
         return ToolTipStringFactory.getToolTip(this.getOperation());
      }

      for (OperationNodeToolBarContribution var4 : this.toolbarContribs) {
         if (var4.isToolTipContributor(var1.getPoint())) {
            return var4.getToolTipText();
         }
      }

      return null;
   }

   private String getIconToolTip() {
      ValidationResult var1 = this.getOperation().getCurrentValidationResult();
      if (var1.status() == ValidationStatus.VALID) {
         return "This " + EntityType.Operation.toString() + " is valid.";
      }

      StringBuilder var2 = new StringBuilder("<html>");
      var2.append("<b>Validation Problems:</b><br>");
      HtmlListGenerator var3 = HtmlListGenerator.unordered();

      for (ValidationMessage var5 : var1.allMessages()) {
         var3.newItem().escapeAndAdd(var5.message());
      }

      return var2.append(var3.toHtml()).append("</html>").toString();
   }

   @Override
   public void doLayout() {
      super.doLayout();
      this.titlePane.doLayout();
   }

   @Override
   public GraphNodeEditor getEditor() {
      return new AbstractOperationNodeUi.NameEditor();
   }

   protected final JLabel getNameLabel() {
      return this.nameLabel;
   }

   protected final JComponent getTitlePane() {
      return this.titlePane;
   }

   protected abstract Icon getToggleButtonIcon();

   protected abstract String getToggleButtonToolTip();

   protected abstract void handleToggleButtonClick();

   private void nameChanged(String var1) {
      this.nameLabel.setText(var1);
      this.revalidate();
      this.doLayout();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractOperationNodeUi.this.getGraph().repaint();
         }
      });
   }

   private JLabel createNameLabel() {
      KongaLabel var1 = TITLE_STYLE.makeLabel(this.dataObject.getName());
      var1.setMinimumSize(new Dimension());
      return var1;
   }

   private OperationNodeToolBarContribution createToggleButton() {
      return new NodeExpansionToggleButton(this) {
         @Override
         protected Icon getToggleButtonIcon() {
            return AbstractOperationNodeUi.this.getToggleButtonIcon();
         }

         @Override
         protected void doClick() {
            AbstractOperationNodeUi.this.handleToggleButtonClick();
         }
      };
   }

   private JLabel createIconLabel() {
      return new JLabel(this.getIcon()) {
         @Override
         public Dimension getMinimumSize() {
            Icon var1 = this.getIcon();
            return new Dimension(var1.getIconWidth(), var1.getIconHeight());
         }
      };
   }

   private Icon getIcon() {
      EntityIcons var1 = EntityIcons.forType(EntityType.Operation);
      ValidationStatus var2 = this.getOperation().getValidationStatus();
      return var1.getIcon(EntityIcons.Category.fromValidationStatus(var2), EntityIcons.Size.LARGE);
   }

   private JPanel createTitlePane() {
      JPanel var1 = new JPanel(false);
      var1.setOpaque(false);
      return var1;
   }

   private static TextStyle createTitleStyle() {
      TextStyle var0 = TextStyle.adjustSize(TextStyles.InfoText, 2);
      return TextStyle.newStyle(var0, 1);
   }

   private void buildTitlePane() {
      BoxBuilder var1 = BoxBuilder.horizontal(this.titlePane);
      ArrayList var2 = Lists.newArrayList();

      for (OperationNodeToolBarContribution var4 : this.toolbarContribs) {
         var2.add(var4.getUi());
      }

      var1.addAllWithSpace(10, this.iconLabel, this.nameLabel).rigid(20).glue().addAllWithSpace(5, var2);
   }

   private class NameEditor extends EntityNodeNameEditor {
      public NameEditor() {
         super(AbstractOperationNodeUi.this.node, AbstractOperationNodeUi.this.getOperation());
         this.setOffset(SwingUtilities.convertPoint(AbstractOperationNodeUi.this.nameLabel, new Point(), AbstractOperationNodeUi.this));
      }

      @Override
      protected void prepareField(JTextField var1) {
         super.prepareField(var1);
         var1.setText(AbstractOperationNodeUi.this.node.getDataObject().getName());
         Dimension var2 = AbstractOperationNodeUi.this.nameLabel.getSize();
         Dimension var3 = var1.getPreferredSize();
         var3.width = Math.max(var2.width, 75);
         var3.height = Math.max(var2.height, var3.height);
         var1.setSize(var3);
      }
   }

   private class WrapperListener implements PropertyChangeListener, ValidationListener {
      private WrapperListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("name".equals(var2)) {
            AbstractOperationNodeUi.this.nameChanged((String)var1.getNewValue());
            this.rebuildTitlePane();
         } else {
            for (OperationNodeToolBarContribution var4 : AbstractOperationNodeUi.this.toolbarContribs) {
               if (var4.isAffectedByProperty(var2)) {
                  this.rebuildTitlePane();
                  AbstractOperationNodeUi.this.getGraph().repaint();
                  break;
               }
            }
         }
      }

      @Override
      public void validationComplete(IntegrationData var1, ValidationResult var2) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               AbstractOperationNodeUi.this.iconLabel.setIcon(AbstractOperationNodeUi.this.getIcon());
               AbstractOperationNodeUi.this.getGraph().repaint();
            }
         });
      }

      private void rebuildTitlePane() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               AbstractOperationNodeUi.this.titlePane.removeAll();
               AbstractOperationNodeUi.this.buildTitlePane();
            }
         });
      }
   }
}
