package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.Empty;
import org.etlforge.ui.util.UiUtils;

public abstract class DataLocationPanel extends IntegrationDataPanel {
   private final DataLocation location;
   private final SourceTarget sourceTarget;
   private BoundProperty[] boundPropertyControls;

   public DataLocationPanel(DataLocation var1, SourceTarget var2) {
      this.location = Preconditions.checkNotNull(var1, "location");
      this.sourceTarget = Preconditions.checkNotNull(var2, "st");
      this.createBoundPropertyControls(var2);
      this.setBorder(Empty.border(0, 10, 0, 0));
   }

   public final SourceTarget getSourceTarget() {
      return this.sourceTarget;
   }

   public DataLocation getDisplayedObject() {
      return this.location;
   }

   public final DataLocationType getLocationType() {
      return this.location.getDataLocationType();
   }

   @Override
   public void setInitialFocus() {
   }

   protected abstract void reset();

   public BoundProperty[] getBoundPropertyControls() {
      return (BoundProperty[])this.boundPropertyControls.clone();
   }

   private void createBoundPropertyControls(final SourceTarget var1) {
      BoundProperty var2 = new BoundProperty() {
         @Override
         public String getName() {
            return var1 == SourceTarget.Source ? "location" : "Location";
         }

         @Override
         public void newValue(Object var1x) {
            UiUtils.runOnEventThread(new Runnable() {
               @Override
               public void run() {
                  DataLocationPanel.this.resetSilently();
               }
            });
         }
      };
      this.boundPropertyControls = new BoundProperty[]{var2};
   }

   private void resetSilently() {
      this.setSendInputChangeEvents(false);
      this.setSendUndoEvents(false);

      try {
         this.reset();
      } finally {
         this.setSendInputChangeEvents(true);
         this.setSendUndoEvents(true);
      }
   }
}
