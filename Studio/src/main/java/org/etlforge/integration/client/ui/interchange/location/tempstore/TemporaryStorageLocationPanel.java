package org.etlforge.integration.client.ui.interchange.location.tempstore;

import java.util.Arrays;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.datalocation.cache.TemporaryStorageLocationCache;
import org.etlforge.integration.client.ui.interchange.location.local.FolderPathField;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.FileLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.ui.Size;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class TemporaryStorageLocationPanel extends DataLocationPanel {
   private final FolderPathField pathField;
   private final FileLocationPanel fileLocationPanel;
   private TemporaryStorageLocationCache cache;

   public TemporaryStorageLocationPanel(TemporaryStorageLocation var1, SourceTarget var2) {
      super(var1, var2);
      byte var3 = 40;
      this.pathField = new FolderPathField("Path", LocationResources.getString("TempStore.Path"), Size.columns(var3));
      this.pathField.setRequired(false);
      this.fileLocationPanel = new FileLocationPanel(var1, var2, false, this, var3) {
         @Override
         protected boolean needsLoginControls() {
            return false;
         }
      };
      this.reset();
      this.addInputField(this.pathField, this.pathField, this.pathField);
   }

   public void setDataCache(TemporaryStorageLocationCache var1) {
      this.cache = var1;
      this.pathField.installAutoCompletion(new TemporaryStorageLocationPanel.CacheBasedPathHistory(var1));
   }

   public TemporaryStorageLocation getDisplayedObject() {
      return (TemporaryStorageLocation)super.getDisplayedObject();
   }

   @Override
   public void apply() {
      TemporaryStorageLocation var1 = this.getDisplayedObject();
      var1.setPath(this.pathField.getValue());
      this.fileLocationPanel.apply(var1);
      this.updatePathCache(var1);
   }

   private void updatePathCache(TemporaryStorageLocation var1) {
      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   @Override
   public void reset() {
      TemporaryStorageLocation var1 = this.getDisplayedObject();
      this.fileLocationPanel.reset(var1);
      this.pathField.setValue(var1.getPath());
   }

   @Override
   public void display() {
      super.display();
      this.doLayout();
   }

   private void doLayout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(3, 5, 10, 10);
      var1.add(InputFieldFactory.getCapsule(this.pathField));
      JPanel var2 = InvisiblePanel.newPanel();
      this.fileLocationPanel.layoutComponents(var2, null);
      var1.add(var2);
      this.wrap(var1);
   }

   @Override
   public void setInitialFocus() {
      this.pathField.requestFocus();
   }

   private static class CacheBasedPathHistory implements StringValueHistory {
      private final TemporaryStorageLocationCache cache;

      public CacheBasedPathHistory(TemporaryStorageLocationCache var1) {
         this.cache = var1;
      }

      @Override
      public boolean delete(String var1) {
         return this.cache.removePath(var1);
      }

      @Override
      public String[] values() {
         String[] var1 = this.cache.getPaths();
         Arrays.sort(var1, String.CASE_INSENSITIVE_ORDER);
         return var1;
      }
   }
}
