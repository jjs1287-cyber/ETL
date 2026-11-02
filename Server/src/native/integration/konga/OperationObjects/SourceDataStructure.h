/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SOURCE_DATA_STRUCTURE_H
#define KONGA_OPERATION_SOURCE_DATA_STRUCTURE_H

namespace konga {

	class OperationEngineContext;

	   
                                                                  
    
	class SourceDataStructure {
	protected:
		SourceDataStructure(void) {}
	public:
		virtual ~SourceDataStructure(void) { }

		virtual void doPreTransformation(const OperationEngineContext& context) const = 0;
	};
}
#endif                                           

