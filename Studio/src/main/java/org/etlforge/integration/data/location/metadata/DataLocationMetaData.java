package org.etlforge.integration.data.location.metadata;

import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.location.DataLocationType;

public abstract class DataLocationMetaData {
   private static DataLocationMetaData instance;
   private static final CountDownLatch initializationLatch = new CountDownLatch(1);

   public static synchronized DataLocationMetaData getInstance() {
      if (instance == null) {
         initialize();
      }

      return instance;
   }

   private static synchronized void initialize() {
      if (instance == null) {
         try {
            DataLocationMetaData.XmlBasedMetaData var0 = new DataLocationMetaData.XmlBasedMetaData();
            var0.loadData();
            instance = var0;
            initializationLatch.countDown();
         } catch (DataLocationMetaDataXmlException var1) {
            ErrorLog.log(DataLocationMetaData.class, null, var1);
            instance = new DataLocationMetaData.NoConfigMetaData();
         }
      }
   }

   public abstract DataLocationType[] getSupportedSourceTypes();

   public abstract boolean isSupportedSourceType(DataLocationType var1);

   public abstract DataLocationType[] getSupportedTargetTypes();

   public abstract boolean isSupportedTargetType(DataLocationType var1);

   private static final class NoConfigMetaData extends DataLocationMetaData {
      private NoConfigMetaData() {
      }

      @Override
      public DataLocationType[] getSupportedSourceTypes() {
         return DataLocationType.values();
      }

      @Override
      public boolean isSupportedSourceType(DataLocationType var1) {
         return true;
      }

      @Override
      public DataLocationType[] getSupportedTargetTypes() {
         return DataLocationType.values();
      }

      @Override
      public boolean isSupportedTargetType(DataLocationType var1) {
         return true;
      }
   }

   private static class XmlBasedMetaData extends DataLocationMetaData {
      private DataLocationMetaDataLoader dataLoader;

      private XmlBasedMetaData() {
      }

      private void loadData() throws DataLocationMetaDataXmlException {
         this.dataLoader = new DataLocationMetaDataLoader();
         this.dataLoader.load();
      }

      @Override
      public DataLocationType[] getSupportedSourceTypes() {
         return this.dataLoader.getSupportedSourceTypes();
      }

      @Override
      public boolean isSupportedSourceType(DataLocationType var1) {
         return this.dataLoader.isSupportedSourceType(var1);
      }

      @Override
      public DataLocationType[] getSupportedTargetTypes() {
         return this.dataLoader.getSupportedTargetTypes();
      }

      @Override
      public boolean isSupportedTargetType(DataLocationType var1) {
         return this.dataLoader.isSupportedTargetType(var1);
      }
   }
}
