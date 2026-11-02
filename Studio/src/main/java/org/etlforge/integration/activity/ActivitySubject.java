package org.etlforge.integration.activity;

import java.beans.PropertyChangeListener;
import org.etlforge.integration.activity.serverapi.ActivitySubjectId;
import org.etlforge.system.Disposable;

public interface ActivitySubject extends Disposable {
   ActivitySubjectId getId();

   boolean isReady();

   String getName();

   String getJobName();

   void addNameChangeListener(PropertyChangeListener var1);

   void hasBeenUpdated();

   @Override
   void dispose();
}
