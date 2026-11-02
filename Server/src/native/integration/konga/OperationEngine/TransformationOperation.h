/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_OPERATION_H
#define KONGA_TRANSFORMATION_OPERATION_H

#include <konga/OperationObjects/Operation.h>

namespace konga {

	                                    
	                                                      
	class TransformationOperation : public Operation {
	private:
		TransformationOperation(void) {}

	public:
		~TransformationOperation(void) {}

		static std::string FINAL_TARGET_TYPE_DE;

		virtual ActivityType getActivityType() const  { return ActivityType_Transformation; }

		void doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder);

		friend class OperationCreatorDb;	                                
	};
}
#endif                                    


