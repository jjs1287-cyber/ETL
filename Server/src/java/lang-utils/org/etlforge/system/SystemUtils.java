  
                             
   
package org.etlforge.system;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

public final class SystemUtils {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final boolean IS_OS_WINDOWS_VISTA = System.getProperty("os.name").toLowerCase().contains("windows vista");
    public static final boolean IS_JAVA_1_6 = JavaVersion.access$000().is_1_6();
    public static final File USER_HOME = new File(System.getProperty("user.home", ""));

    public static String getSystemProperties(String string) {
        Properties properties = System.getProperties();
        ArrayList<Object> arrayList = new ArrayList<Object>(properties.keySet());
        Collections.sort(arrayList, new Comparator<Object>(){

            @Override
            public int compare(Object object, Object object2) {
                return object.toString().compareToIgnoreCase(object2.toString());
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (Object e : arrayList) {
            stringBuilder.append(e).append(" = ").append(properties.get(e)).append(string);
        }
        return stringBuilder.toString();
    }

    private SystemUtils() {
    }

    private static final class JavaVersion {
        private static final JavaVersion INSTANCE = new JavaVersion();
        private String version = System.getProperty("java.version");

        private JavaVersion() {
            if (this.version == null) {
                this.version = "";
            }
        }

        public boolean is_1_6() {
            return this.version.startsWith("1.6");
        }

        static                 JavaVersion access$000() {
            return INSTANCE;
        }
    }
}

