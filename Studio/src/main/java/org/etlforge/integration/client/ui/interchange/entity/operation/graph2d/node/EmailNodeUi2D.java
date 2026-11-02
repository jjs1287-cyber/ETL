package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.ValidationListener;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.EmailWrapper;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.node.AbstractIconLabelNodeUi2D;

public class EmailNodeUi2D extends AbstractIconLabelNodeUi2D {
   private final EmailNodeUi2D.EmailListener emailListener = new EmailNodeUi2D.EmailListener();

   public EmailNodeUi2D(Graph2D var1, EmailWrapperNode var2) {
      super(var1, var2);
      this.emailListener.install();
      this.setToolTipText(this.getContentToolTip());
   }

   public EmailWrapperNode getNodeObject() {
      return (EmailWrapperNode)super.getNodeObject();
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   private EmailWrapper getDataObject() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      return this.getDataObject().getActivity().getName();
   }

   @Override
   protected String getContentToolTip() {
      return ToolTipStringFactory.getToolTip(this.getDataObject().getActivity());
   }

   @Override
   protected Icon getIcon() {
      EntityIcons var1 = EntityIcons.forType(EntityType.EmailMessage);
      ValidationStatus var2 = this.getDataObject().getActivity().getValidationStatus();
      return var1.getIcon(EntityIcons.Category.fromValidationStatus(var2), EntityIcons.Size.LARGE);
   }

   @Override
   public GraphNodeEditor getEditor() {
      return new NameEditor(this, this.getDataObject().getActivity());
   }

   @Override
   public void dispose() {
      try {
         this.emailListener.dispose();
      } finally {
         super.dispose();
      }
   }

   private class EmailListener implements PropertyChangeListener, ValidationListener {
      private EmailListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         EmailNodeUi2D.this.setContentString(EmailNodeUi2D.this.getContentString());
         EmailNodeUi2D.this.setToolTipText(EmailNodeUi2D.this.getContentToolTip());
         EmailNodeUi2D.this.getGraph().repaint();
      }

      @Override
      public void validationComplete(IntegrationData var1, ValidationResult var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EmailNodeUi2D.this.setIcon(EmailNodeUi2D.this.getIcon());
               EmailNodeUi2D.this.getGraph().repaint();
            }
         });
      }

      public void install() {
         EmailMessage var1 = EmailNodeUi2D.this.getDataObject().getActivity();
         var1.addPropertyChangeListener("Name", this);
         var1.addValidationListener(this);
      }

      public void dispose() {
         EmailMessage var1 = EmailNodeUi2D.this.getDataObject().getActivity();
         var1.removePropertyChangeListener("Name", this);
         var1.removeValidationListener(this);
      }
   }
}
