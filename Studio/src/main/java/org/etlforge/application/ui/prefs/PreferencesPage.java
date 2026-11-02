package org.etlforge.application.ui.prefs;

import java.awt.Container;
import javax.swing.event.ChangeListener;

public interface PreferencesPage {
   String[] getPath();

   void apply();

   void restore();

   void restoreDefaults();

   boolean isDirty();

   void addChangeListener(ChangeListener var1);

   void requestFocus();

   void display(Container var1);
}
