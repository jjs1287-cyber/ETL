package org.etlforge.integration.client.ui.interchange.location.fileshare;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.datalocation.cache.FileShareLocationCache;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.FileLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.ui.UiProvider;

public class FileShareLocationPanel extends DataLocationPanel {
   private final FileSharePathInputField pathField;
   private final FileLocationPanel fileLocationPanel;
   private FileShareLocationCache cache;

   public FileShareLocationPanel(FileShareLocation var1, SourceTarget var2) {
      super(var1, var2);
      byte var3 = 20;
      this.pathField = this.createPathField(var3);
      this.fileLocationPanel = new FileLocationPanel(var1, var2, true, this, var3);
      this.reset();
      this.addInputField(this.pathField, this.pathField, this.pathField);
   }

   private FileSharePathInputField createPathField(int var1) {
      String var2 = LocationResources.getString("FileShare.Path.Name");
      String var3 = LocationResources.getString("FileShare.Path.Label");
      return new FileSharePathInputField(var2, var3, var1);
   }

   public FileShareLocation getDisplayedObject() {
      return (FileShareLocation)super.getDisplayedObject();
   }

   public void setDataCache(FileShareLocationCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
      this.fileLocationPanel.setLoginNameCache(var1);
      this.pathField.installAutoCompletion(new CacheBasedPathHistory(var1));
   }

   @Override
   public void display() {
      this.doLayout();
      super.display();
   }

   private void doLayout() {
      DataLocationPanelLayout var1 = new DataLocationPanelLayout();
      var1.add(this.pathField);
      UiProvider var2 = this.fileLocationPanel.layoutComponents(var1);
      this.wrap(var2);
   }

   @Override
   public void apply() {
      FileShareLocation var1 = this.getDisplayedObject();
      var1.setPath(this.pathField.getValue().trim());
      this.fileLocationPanel.apply(var1);
      this.updatePathCache(var1);
   }

   @Override
   public void reset() {
      FileShareLocation var1 = this.getDisplayedObject();
      this.fileLocationPanel.reset(var1);
      this.updateFields(var1);
   }

   @Override
   public void setInitialFocus() {
      this.pathField.requestFocus();
   }

   private void updatePathCache(FileShareLocation var1) {
      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   private void updateFields(FileShareLocation var1) {
      this.pathField.setValue(var1.getPath());
   }
}
