package org.etlforge.application.ui.prefs;

import javax.swing.event.ChangeListener;
import org.etlforge.ui.UiProvider;

public interface PreferenceUi extends UiProvider {
   void apply();

   void restore();

   void restoreDefault();

   boolean isDirty();

   void addChangeListener(ChangeListener var1);

   void share();
}
