package org.etlforge.integration.data.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.target.TargetLinkCollector;
import org.etlforge.integration.data.entity.target.TargetReferencesReplacer;
import org.etlforge.integration.data.entity.target.TargetValidator;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationPersistor;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LocatableChangeEvent;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.system.annotations.InternalConstructor;
import org.etlforge.util.persist.Persistor;

public class Target extends IntegrationEntity implements Locatable {
   public static final String LOCATION = "Location";
   public static final String TARGET_ID = "target_id";
   public static final String TARGET_NAME = "target_name";
   private DataLocation location;
   private transient TargetLocationAssociation locationAssoc;
   private transient PropertyChangeSupport locationChangeListeners;
   private EnumSet<DataLocationType> allowedTypes;
   private static final String[] PROPERTIES = new String[]{
      "target_id",
      "target_name",
      "source_type_id",
      "document_id",
      "locator",
      "path",
      "file_name",
      "timeout",
      "login",
      "password",
      "smtp_message",
      "dsn",
      "transfer_type",
      "ftps_explicit",
      "ftps_auth",
      "encrypt_text",
      "encrypt_choice",
      "driver",
      "drivertype",
      "user_Client",
      "user_password",
      "user_id",
      "developer_password",
      "developer_client",
      "developer_id",
      "system_number",
      "app_server",
      "lang",
      "port",
      "passive_mode",
      "transfer_timeout",
      "secured",
      "dbname",
      "connectstring",
      "httpurl",
      "certificatename",
      "optional_connectstring",
      "transactionIsolationLevel",
      "http_verb",
      "target_target_id",
      "use_basic_http_authentication",
      "enable_content_encoding",
      "enable_chunked_transfer_encoding",
      "send_expect_100_continue",
      "httpContentType",
      "http_request_headers",
      "http_ssl_version",
      "skip_empty_files",
      "file_share_create_directories",
      "character_encoding",
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

   public Target(String var1) {
      this(null, var1, null);
   }

   @InternalConstructor
   public Target(TargetId var1, String var2) {
      this(var1, var2, null);
   }

   public Target(TargetId var1, String var2, DataLocation var3) {
      super(EntityType.Target, var1, var2);
      this.location = var3;
      if (var3 != null) {
         this.locationAssoc = TargetLocationAssociationFactory.newAssociation(this, var3);
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

         this.locationAssoc = TargetLocationAssociationFactory.newAssociation(this, var1);
         this.location = var1;
         this.firePropertyChange("Location", var2, var1);
      }
   }

   public TargetId getID() {
      return (TargetId)super.getID();
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = this.location.getPropertiesForTarget();
      var1.put("target_id", this.getID().toString());
      var1.put("target_name", this.getName());
      var1.put("source_type_id", Integer.toString(this.location.getDataLocationType().getId()));
      var1.put("salesforce_wizard_object", this.getProperty("salesforce_wizard_object"));
      return var1;
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new TargetValidator(this, var1).run();
   }

   public Target clone() {
      Target var1 = (Target)super.clone();
      if (this.location != null) {
         var1.location = this.location.clone();
         var1.locationAssoc = TargetLocationAssociationFactory.newAssociation(var1, var1.location);
      }

      var1.locationChangeListeners = new PropertyChangeSupport(var1);
      var1.setAllowedLocationTypes(this.allowedTypes);
      return var1;
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      TargetLinkCollector var2 = new TargetLinkCollector(this, var1);
      return var2.getLinks();
   }

   void onLocationPropertyChanged(PropertyChangeEvent var1) {
      this.firePropertyChange("Location", var1.getOldValue(), var1.getNewValue());
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

   public boolean canBeTargetForArchive() {
      return this.location != null && this.location.isFileBased();
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      Target var2 = (Target)var1;
      if (this.locationAssoc != null) {
         this.locationAssoc.dispose();
      }

      synchronized (var1.getDataLock()) {
         if (var2.location != null) {
            this.location = var2.location.clone();
            this.locationAssoc = TargetLocationAssociationFactory.newAssociation(this, this.location);
         } else {
            this.location = null;
            this.locationAssoc = null;
         }
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

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         TargetReferencesReplacer var3 = new TargetReferencesReplacer(var1);
         var3.replaceReferences(this);
      }
   }

   @Override
   public boolean isDeepCopyable() {
      DataLocation var1 = this.getLocation();
      if (var1 instanceof HttpLocation) {
         TargetId var2 = ((HttpLocation)var1).getTargetIdForResponse();
         return var2 != null;
      } else {
         return false;
      }
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this);
   }

   @Override
   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
   }
}
