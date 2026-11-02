/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_ENGINE_OPERATION_OBJECT_CREATOR_KEY_VALUES_H
#define KONGA_OPERATION_ENGINE_OPERATION_OBJECT_CREATOR_KEY_VALUES_H

namespace konga {

	class FileTypeSourceLocation;
	class FileTypeTargetLocation;
	class DatabaseLocation;
	class LdapLocation;
	class KeyValues;
	class OperationDataElementHolder;

	   
                                                     
                                                                         
    
                                                                
   
    
	class OperationObjectCreatorKeyValues {
	public:
		OperationObjectCreatorKeyValues(void) {}
		~OperationObjectCreatorKeyValues(void) {}

		FileTypeSourceLocation* getFileTypeSourceLocation(const KeyValues& keyValues, OperationDataElementHolder& deHolder);
		FileTypeTargetLocation* getFileTypeTargetLocation(const KeyValues& keyValues, OperationDataElementHolder& deHolder);

		DatabaseLocation* getDatabaseLocation(const KeyValues& keyValues, OperationDataElementHolder& deHolder, bool bIsSource);
		LdapLocation* getLdapLocation(const KeyValues& keyValues, OperationDataElementHolder& deHolder, bool bIsSource);
	};
}

#endif                                                                

