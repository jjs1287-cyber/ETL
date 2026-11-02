package org.etlforge.application.prefs;

public interface PreferenceListener<T> {
   void valueChanged(Preference<T> var1, T var2, T var3);
}
