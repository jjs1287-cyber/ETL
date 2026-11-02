package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.salesforce.wsdl.ObjectCategory;

public final class ObjectCategoryPreference extends EnumPreference<ObjectCategory> {
   public static final ObjectCategoryPreference INSTANCE = new ObjectCategoryPreference(ApplicationConfiguration.getPreferenceFactory().getApplicationClass());

   public ObjectCategoryPreference(Class<?> var1) {
      super(var1, "Salesforce Wizards", "ObjectCategory", ObjectCategory.ALL, ObjectCategory.class);
   }
}
