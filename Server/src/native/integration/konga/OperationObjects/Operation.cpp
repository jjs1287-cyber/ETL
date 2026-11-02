/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/ItemProperties/Definitions.h>

#include "SourceDataLocation.h"
#include "TargetDataLocation.h"
#include "Transformation.h"
#include "Operation.h"

using namespace konga;
using std::string;

Operation::Operation(void) { }

Operation::~Operation(void) { }

long Operation::getId() const {
	return m_OperationData.m_lOperationID;
}

string Operation::getName() const {
	return m_OperationData.m_stOperationName;
}

OperationType Operation::getOperationType() const {
	return m_OperationData.m_OperationType;
}

bool Operation::isDebugOn() const {
	return m_extendedProperties.GetLong(OPERATION_PROPERTY_WRITE_LOG, 0) == 1;
}

bool Operation::ignoreHistory() const {
	return m_OperationData.m_bIgnoreHistory;
}

bool Operation::isDiff() const {
	return m_OperationData.m_bDiff;
}

bool Operation::doLogging() const {
	return m_OperationData.m_bTrace;
}

long Operation::getSuccessOperationId() const {
	return ( m_OperationData.m_lSuccessOperationID > 0 ) ?
		m_OperationData.m_lSuccessOperationID : 0;
}

long Operation::getFailureOperationId() const {
	return ( m_OperationData.m_lFailureOperationID > 0 ) ?
		m_OperationData.m_lFailureOperationID : 0;
}

long Operation::getSuccessMessageId() const {
	return m_OperationData.m_lSuccessMessageID;
}

long Operation::getFailureMessageId() const {
	return m_OperationData.m_lFailureMessageID;
}

long Operation::getSourceId() const {
	return m_OperationData.m_lSourceId;
}

long Operation::getTargetId() const {
	return m_OperationData.m_lTargetId;
}

bool Operation::shouldWriteHistoryLog() const {
	return m_extendedProperties.GetBool(OPERATION_PROPERTY_HISTORY_LOG, true);
}

int Operation::getOperationLogLevel() const {
	return m_extendedProperties.GetInt(OPERATION_PROPERTY_LOG_LEVEL, 1);
}

const Transformation* Operation::getTransformation() const {
	return m_pTransformation.get();
}

const SourceDataLocation* Operation::getSource() const {
	return m_pSource.get();
}

const TargetDataLocation* Operation::getTarget() const {
	return m_pTarget.get();
}

