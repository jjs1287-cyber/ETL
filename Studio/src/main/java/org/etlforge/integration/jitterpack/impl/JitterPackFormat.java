package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.IOException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.client.project.jitterpack.UnpackException;
import org.etlforge.util.version.UnsupportedVersionException;

interface JitterPackFormat {
   JitterPackDescriptor extractDescriptor(File var1) throws IllegalDataFormatException, IOException, InvalidPasswordException;

   ProjectDescriptor unpack(File var1, File var2) throws IllegalDataFormatException, UnpackException, UnsupportedVersionException;

   JitterPack pack(ManagedProject var1, String var2, File var3) throws JitterPackException;
}
