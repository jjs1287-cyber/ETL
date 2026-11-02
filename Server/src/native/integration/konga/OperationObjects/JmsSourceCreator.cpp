/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <memory>
#include <string>

#include <konga/Utils/FileUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "OperationEngineException.h"
#include "LocationCreatorDb.h"
#include "JmsSource.h"

using namespace konga;
using std::string;

JmsSource* LocationCreatorDb::getJmsSource(OperationDataElementHolder& deHolder) {
	DataElement* const de = deHolder.getOperationDataElement("__JmsBaseFileName");
	if ( de == 0 )
		throw OperationEngineException("Internal Error: No JMS base file name was provided for an incoming JMS message.");
	int err = 0;
	string sPayloadPath((const char*)de->GetString(err));
	sPayloadPath += ".payload";
	if ( !FileExists(sPayloadPath) )
		throw OperationEngineException("The downloaded jms payload does not exist." \
		" It is supposed to be located here: \"" + sPayloadPath + "\".");

	std::auto_ptr<JmsSource> jmsSource( new JmsSource(sPayloadPath) );

	return jmsSource.release();
}

