package org.etlforge.integration.data.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumSet;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.source.SourceValidator;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationPersistor;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocatableChangeEvent;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.system.annotations.InternalConstructor;
import org.etlforge.util.persist.Persistor;

public class Source extends IntegrationEntity implements Locatable {
   public static final String LOCATION = "location";
   public static final String SOURCE_ID = "source_id";
   public static final String SOURCE_NAME = "source_name";
   public static final String SOURCE_TYPE_ID = "source_type_id";
   private DataLocation location;
   private transient SourceLocationAssociation locationAssoc;
   private transient PropertyChangeSupport locationChangeListeners;
   private EnumSet<DataLocationType> allowedTypes;
   private static final String[] PROPERTIES = new String[]{
      "source_id",
      "source_name",
      "source_type_id",
      "document_id",
      "locator",
      "path",
      "file_name",
      "timeout",
      "login",
      "password",
      "filter",
      "ignore_first",
      "ignore_last",
      "dsn",
      "transfer_type",
      "encrypt_text",
      "encrypt_choice",
      "driver",
      "drivertype",
      "port",
      "passive_mode",
      "ftp.nlst",
      "ftps_explicit",
      "ftps_auth",
      "dbname",
      "connectstring",
      "renamefileto",
      "secured",
      "httpurl",
      "certificatename",
      "optional_connectstring",
      "transactionIsolationLevel",
      "fetch_size",
      "http_verb",
      "use_basic_http_authentication",
      "jms_message_selector",
      "enable_content_encoding",
      "enable_chunked_transfer_encoding",
      "send_expect_100_continue",
      "http_request_headers",
      "http_ssl_version",
      "filterOption.ignoreCase",
      "filterOption.regularExpression",
      "db_begin_quote",
      "db_end_quote",
      "driver_begin_quote",
      "driver_end_quote",
      "driver_class",
      "jdbc_subprotocol",
      "manual_mode",
      "salesforce_wizard_object"
   };

   @Override
   public String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public Source(String var1) {
      this(null, var1, null);
   }

   @InternalConstructor
   public Source(SourceId var1, String var2) {
      this(var1, var2, null);
   }

   public Source(SourceId var1, String var2, DataLocation var3) {
      super(EntityType.Source, var1, var2);
      this.location = var3;
      if (var3 != null) {
         this.locationAssoc = SourceLocationAssociationFactory.newAssociation(this, var3);
      }

      this.locationChangeListeners = new PropertyChangeSupport(this);
   }

   @Override
   public DataLocation getLocation() {
      return this.location;
   }

   @Override
   public void setLocation(DataLocation var1) {
      this.allowedTypes = null;
      if (!var1.equals(this.location)) {
         DataLocation var2 = this.location;
         if (this.locationAssoc != null) {
            this.locationAssoc.dispose();
         }

         this.location = var1;
         this.locationAssoc = SourceLocationAssociationFactory.newAssociation(this, var1);
         this.firePropertyChange("location", var2, var1);
      }
   }

   public SourceId getID() {
      return (SourceId)super.getID();
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new SourceValidator(this, var1).run();
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = this.location.getPropertiesForSource();
      var1.put("source_id", this.getID().toString());
      var1.put("source_name", this.getName());
      var1.put("source_type_id", Integer.toString(this.location.getDataLocationType().getId()));
      var1.put("salesforce_wizard_object", this.getProperty("salesforce_wizard_object"));
      return var1;
   }

   public Source clone() {
      Source var1 = (Source)super.clone();
      if (this.location != null) {
         var1.location = this.location.clone();
         var1.locationAssoc = SourceLocationAssociationFactory.newAssociation(var1, var1.location);
      }

      var1.locationChangeListeners = new PropertyChangeSupport(var1);
      var1.setAllowedLocationTypes(this.allowedTypes);
      return var1;
   }

   public boolean canHaveHistory() {
      if (this.location == null) {
         return false;
      }

      DataLocationType var1 = this.location.getDataLocationType();
      return var1 != DataLocationType.Database && var1 != DataLocationType.LDAP;
   }

   void onLocationPropertyChanged(PropertyChangeEvent var1) {
      this.firePropertyChange("location", var1.getOldValue(), var1.getNewValue());
      this.locationChangeListeners.firePropertyChange(new LocatableChangeEvent(this, var1));
   }

   @Override
   public void addLocationListener(PropertyChangeListener var1) {
      this.locationChangeListeners.addPropertyChangeListener(var1);
   }

   @Override
   public void addLocationListener(String var1, PropertyChangeListener var2) {
      this.locationChangeListeners.addPropertyChangeListener(var1, var2);
   }

   @Override
   public void removeLocationListener(PropertyChangeListener var1) {
      this.locationChangeListeners.removePropertyChangeListener(var1);
   }

   @Override
   public void removeLocationListener(String var1, PropertyChangeListener var2) {
      this.locationChangeListeners.removePropertyChangeListener(var1, var2);
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      DataLocationPersistor.save(this.location, var1.createChild("Location"));
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      this.setLocation(DataLocationPersistor.restore(var1.getFirstChild("Location")));
   }

   public boolean isArchivable() {
      return this.location != null && this.location.isFileBased();
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      Source var2 = (Source)var1;
      if (this.locationAssoc != null) {
         this.locationAssoc.dispose();
      }

      if (var2.location != null) {
         this.location = var2.location.clone();
         this.locationAssoc = SourceLocationAssociationFactory.newAssociation(this, this.location);
      } else {
         this.location = null;
         this.locationAssoc = null;
      }
   }

   @Override
   public void setAllowedLocationTypes(EnumSet<DataLocationType> var1) {
      this.allowedTypes = var1 == null ? null : var1.clone();
   }

   @Override
   public boolean isLocationTypeAllowed(DataLocationType var1) {
      return this.allowedTypes == null || this.allowedTypes.contains(var1);
   }

   public boolean isSchedulable() {
      return isSchedulable(this.getID());
   }

   public static boolean isSchedulable(SourceId var0) {
      return !SystemSource.SOURCE_FROM_PREVIOUS_OPERATION.getID().equals(var0) && !SystemSource.FROM_HTTP_ENDPOINT.getID().equals(var0);
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this);
   }

   @Override
   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
   }
}
