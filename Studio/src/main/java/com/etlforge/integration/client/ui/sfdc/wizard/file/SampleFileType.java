package com.etlforge.integration.client.ui.sfdc.wizard.file;

import java.io.IOException;
import org.etlforge.integration.data.structure.text.InvalidTemplateException;

public interface SampleFileType {
   SourceFileStructure createFileStructure(SampleFile var1, String var2) throws IOException, InvalidTemplateException;
}
