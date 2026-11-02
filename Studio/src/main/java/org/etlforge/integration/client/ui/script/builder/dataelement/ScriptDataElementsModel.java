package org.etlforge.integration.client.ui.script.builder.dataelement;

import java.util.Collection;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementMap;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ScriptDataElementsModel extends AbstractPropertyBasedModel {
   public static final String DATA_ELEMENTS = "allDataElements";
   private final Property<DataElementMap> dataElements = this.newProperty("allDataElements", new DataElementMap());
   private final Object lock;

   public ScriptDataElementsModel() {
      this.dataElements.setAllowsNull(false);
      this.lock = new Object();
   }

   public Collection<DataElement> getDataElements() {
      synchronized (this.lock) {
         return this.dataElements.get().getVisibleDataElements();
      }
   }

   public void stopTest() {
      synchronized (this.lock) {
         DataElementMap var2 = new DataElementMap(this.dataElements.get());
         var2.stopTest();
         this.dataElements.set(var2);
      }
   }

   public void setDataElementsReturnedFromServer(Collection<DataElement> var1) {
      synchronized (this.lock) {
         DataElementMap var3 = new DataElementMap(this.dataElements.get());
         var3.processDataElementsReturnedFromServer(var1);
         this.dataElements.set(var3);
      }
   }

   public void addDataElementsScannedFromScript(Iterable<DataElement> var1, boolean var2) {
      synchronized (this.lock) {
         DataElementMap var4 = new DataElementMap(this.dataElements.get());
         var4.addScannedDataElements(var1, var2);
         this.dataElements.set(var4);
      }
   }

   public void setDataElementsScannedFromScript(Iterable<DataElement> var1) {
      synchronized (this.lock) {
         DataElementMap var3 = new DataElementMap(this.dataElements.get());
         var3.replaceScannedDataElements(var1);
         this.dataElements.set(var3);
      }
   }

   public void resetDataElements() {
      synchronized (this.lock) {
         DataElementMap var2 = new DataElementMap(this.dataElements.get());
         var2.reset();
         this.dataElements.set(var2);
      }
   }

   public DataElement lookup(String var1, DataElementScope var2) {
      return this.dataElements.get().lookup(var1, var2);
   }
}
