package org.etlforge.integration.client.ui.interchange.location;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.client.ui.interchange.DefaultPredefinedVariable;
import org.etlforge.integration.client.ui.interchange.PredefinedVariable;

public final class TargetFileNameVariables {
   private static final PredefinedVariable DATE = new DefaultPredefinedVariable(
      "[date]", "<b>[date]</b> is substituted with the current date in the format YYYY-Mon-DD."
   );
   private static final PredefinedVariable DATE_TIME = new DefaultPredefinedVariable(
      "[date]_[time]",
      "<b>[date]</b> is substituted with the current date in the format YYYY-Mon-DD.<br><b>[time]</b> is substituted with the current time in the format HHMMSS."
   );
   private static final PredefinedVariable EXT = new DefaultPredefinedVariable("[ext]", "<b>[ext]</b> is substituted with the file extension, if any.");
   private static final PredefinedVariable FILE = new DefaultPredefinedVariable(
      "[file]", "<b>[file]</b> is substituted with the base file name (i.e. excluding the file extension, if any)."
   );
   private static final PredefinedVariable SEQUENCE = new DefaultPredefinedVariable(
      "[sequence]", "<b>[sequence]</b> is substituted with the sequence of the file, 1 for the first, 2 for the second and so on."
   );
   private static final PredefinedVariable TIME = new DefaultPredefinedVariable(
      "[time]", "<b>[time]</b> is substituted with the current time in the format HHMMSS."
   );
   private static final PredefinedVariable UNIQUE = new DefaultPredefinedVariable(
      "[unique]", "<b>[unique]</b> is substituted with a GUID which is a random unique identifier. This can be used to construct a unique file name."
   );

   public static ImmutableList<PredefinedVariable> getVariables() {
      return ImmutableList.of(DATE, DATE_TIME, EXT, FILE, TIME, SEQUENCE, UNIQUE);
   }

   private TargetFileNameVariables() {
   }
}
