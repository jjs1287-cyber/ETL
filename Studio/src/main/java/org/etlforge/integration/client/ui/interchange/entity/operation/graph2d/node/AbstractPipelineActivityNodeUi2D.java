package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityEvent;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityListener;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.node.AbstractIconLabelNodeUi2D;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;

public abstract class AbstractPipelineActivityNodeUi2D extends AbstractIconLabelNodeUi2D {
   protected static final TextStyle EMPTY = TextStyle.newColor(TextStyles.DefaultItalic, org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND);
   protected static final TextStyle POPULATED = TextStyles.DefaultText;
   private final AbstractPipelineActivityNodeUi2D.NodeListener nodeListener = new AbstractPipelineActivityNodeUi2D.NodeListener();

   public AbstractPipelineActivityNodeUi2D(Graph2D var1, PipelineActivityNode var2) {
      super(var1, var2);
      this.getActivity().addListener(this.nodeListener);
      this.updateContentLabelStyle();
   }

   public PipelineActivityNode getNodeObject() {
      return (PipelineActivityNode)super.getNodeObject();
   }

   public PipelineActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   public void dispose() {
      super.dispose();
      this.getActivity().removeListener(this.nodeListener);
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public void setContentString(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         super.setContentString(var1);
      } else {
         super.setContentString(this.getActivity().getRole());
      }
   }

   @Override
   public GraphNodeEditor getEditor() {
      Object var1 = this.getNodeObject().getDataObject().getContent();
      return var1 instanceof IntegrationEntity ? new NameEditor(this, (IntegrationEntity)var1) : null;
   }

   public final void updateIconLabel() {
      this.setIcon(this.getIcon());
   }

   @Override
   protected void paintBorder(Graphics var1) {
      if (this.getNodeObject() != null && this.getActivity().isEmpty()) {
         Graphics2D var2 = (Graphics2D)var1;
         Stroke var3 = var2.getStroke();
         var2.setStroke(BORDER_STYLE_WHEN_EMPTY);
         super.paintBorder(var2);
         var2.setStroke(var3);
      } else {
         super.paintBorder(var1);
      }
   }

   protected TextStyle getLabelStyle() {
      return this.getActivity().isEmpty() ? EMPTY : POPULATED;
   }

   private void updateContentLabelStyle() {
      TextStyle var1 = this.getLabelStyle();
      var1.makeOver(this.getContentLabel());
   }

   @Override
   public final String getToolTipText() {
      return this.getActivity().isEmpty() ? "Right-click or use drag & drop to populate this place holder" : this.getContentToolTip();
   }

   private class NodeListener implements PipelineActivityListener {
      private NodeListener() {
      }

      @Override
      public void contentChanged(PipelineActivityEvent var1) {
         this.updateUi();
      }

      @Override
      public void activityObjectChanged(PipelineActivityEvent var1) {
         this.updateUi();
      }

      private void updateUi() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               AbstractPipelineActivityNodeUi2D.this.updateIconLabel();
               AbstractPipelineActivityNodeUi2D.this.updateContentLabelStyle();
               NodeListener.this.updateContentString();
               AbstractPipelineActivityNodeUi2D.this.setSize(AbstractPipelineActivityNodeUi2D.this.getPreferredSize());
               AbstractPipelineActivityNodeUi2D.this.getGraph().repaint();
            }
         });
      }

      private void updateContentString() {
         AbstractPipelineActivityNodeUi2D.this.getContentLabel().setText(AbstractPipelineActivityNodeUi2D.this.getContentString());
      }
   }
}

