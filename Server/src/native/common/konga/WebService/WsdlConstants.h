/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef CR_WSDLCONSTANTS_H
#define CR_WSDLCONSTANTS_H

namespace konga
{
    namespace WsdlConstants
    {
    	enum WsdlSchemaStatus { Unknown = -1, Valid, Warning, Error };
    
    	inline WsdlSchemaStatus GetWsdlSchemaStatusFromInt(int i)
    	{
    		switch(i)
    		{
    		case 0:
    			return Valid;
    		case 1:
    			return Warning;
    		case 2:
    			return Error;
    		default:
    			return Unknown;
    		}
    	}
    }
}
#endif

