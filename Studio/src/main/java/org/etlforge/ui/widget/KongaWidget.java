package org.etlforge.ui.widget;

import org.etlforge.ui.Enablable;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.focus.Focusable;

public interface KongaWidget extends CustomPaintable, Enablable, Focusable {
   MnemonicStringFactory MNEMONIC_FACTORY = UiResourceBundle.MNEMONIC_FACTORY;

   UiProvider asUiProvider();
}
