/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_OPERATION_OBJECTS_EMAIL_MESSAGE_SENDER_H
#define ETLFORGE_OPERATION_OBJECTS_EMAIL_MESSAGE_SENDER_H

namespace konga {

	class DbConnection;
	class OperationDataElementHolder;

	void SendEmailMessage(long lMessageId, DbConnection& dbConnection, OperationDataElementHolder& deHolder, bool bUseSystemEmailSettingsAsDefault);

}

#endif                                                     

