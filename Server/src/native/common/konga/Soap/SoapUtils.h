/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef CRSOAPUTILS_H
#define CRSOAPUTILS_H

#include <string>

#include <konga/Utils/StringUtils.h>

#include "SoapConstants.h"

namespace konga
{
	namespace {
		SoapConstants::SoapStyle GetSoapStyle(const std::string& stSoapStyle)
		{ 
			return konga::EqualsIgnoreCase(stSoapStyle, SoapConstants::RPC) ? 
				SoapConstants::Rpc : SoapConstants::Document; 
		}
		
		SoapConstants::SoapBodyUse GetSoapBodyUse(const std::string& stSoapBodyUse)
		{ 
			return konga::EqualsIgnoreCase(stSoapBodyUse, SoapConstants::ENCODED) ? 
				SoapConstants::Encoded : SoapConstants::Literal; 
		}
	}
}
#endif                 

