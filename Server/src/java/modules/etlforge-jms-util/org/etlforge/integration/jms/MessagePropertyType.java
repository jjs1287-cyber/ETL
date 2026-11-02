/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import javax.jms.JMSException;
import javax.jms.Message;

   
                                                               
   
             
  
   
public enum MessagePropertyType {

    BOOLEAN(false) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            if (s.equals("1")) {
                return true;
            }
            if (s.equals("0")) {
                return false;
            }
            String lower = s.toLowerCase();
            if (lower.equals("true")) {
                return true;
            }
            if (lower.equals("false")) {
                return false;
            }
            throw new IllegalArgumentException("Invalid value for a BOOLEAN property. " +
                    "Valid values are \"true\" or \"1\" (for true), and \"false\" or \"0\" (for false)");
        }
    },
    
    BYTE(true) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            try {
                return Byte.valueOf(s);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    },
    
    DOUBLE(true) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            try {
                return Double.valueOf(s);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    },
    
    FLOAT(true) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            try {
                return Float.valueOf(s);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    },
    
    INT(true) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            try {
                return Integer.valueOf(s);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    },
    
    LONG(true) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            try {
                return Long.valueOf(s);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    },
    
    SHORT(true) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            try {
                return Short.valueOf(s);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    },
    
    STRING(false) {

        @Override
        protected Object getValueFromString(String s) throws IllegalArgumentException {
            return s;
        }
    };
    
    
    private final boolean numeric;
    
    private MessagePropertyType(boolean numeric) {
        this.numeric = numeric;
    }
    
 
    protected final void applyTo(Message msg, String name, String value) throws JMSException, IllegalArgumentException {
        msg.setObjectProperty(name, convertStringToObjectValue(value));
    }
    
    private Object convertStringToObjectValue(String s) throws IllegalArgumentException {
        if ((s == null) || (s.length() == 0)) {
                                                                                 
                                                                                       
                                                                                   
            return null;
        }
        if (numeric) {
            s = s.trim();
            if (s.length() == 0) {
                return null;
            }
        }
        return getValueFromString(s);
    }
    
    protected abstract Object getValueFromString(String s) throws IllegalArgumentException;
    
    public final boolean isValidValue(String s) {
        if ((s == null) || (s.length() == 0)) {
            return true;
        }
        if (numeric) {
            s = s.trim();
            if (s.length() == 0) {
                return true;
            }
        }
        try {
            getValueFromString(s);
            return true;
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
    }
    
   public String getBackendStringDescription() {
	   switch(this) {
	   case BOOLEAN:
		   return "bool";
	   case SHORT:
		   return "int";	                                       
       default:
    	   return this.toString().toLowerCase();
			   
	   }
   }
    
    public boolean isNumeric() {
        return numeric;
    }
}
