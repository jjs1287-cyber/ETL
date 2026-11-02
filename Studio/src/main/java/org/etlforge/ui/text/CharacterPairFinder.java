package org.etlforge.ui.text;

import javax.swing.text.Document;
import org.etlforge.util.Interval;

public interface CharacterPairFinder {
   Interval findMatch(Document var1, int var2);
}
