/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_Target_DATA_STRUCTURE_H
#define KONGA_OPERATION_Target_DATA_STRUCTURE_H

namespace konga {

	class OperationEngineContext;

	   
                                                                  
    
	class TargetDataStructure {
	protected:
		TargetDataStructure(void) {}
	public:
		virtual ~TargetDataStructure(void) { }

		virtual void doPreTransformation(const OperationEngineContext& context) const = 0;
	};
}
#endif                                           

