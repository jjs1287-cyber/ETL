/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include <konga/SystemVariables/SystemVariables.h>
#include "SystemVariableDefs.h"

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include <konga/JavaBackend/JsonEngine/JsonEngineWrapper.h>

#include "TransformationEngine.h"

using namespace konga;
using std::string;

void TransformationEngine::HandleSpecialSourceStructures(const std::string& sourceFilePath, bool keepOriginal, konga::DbConnection* pTranDb) {
	if ( m_sourceType == JsonTypeId ) {
		if ( !konga::FileExists(sourceFilePath) ) {
			throw InternalErrorException("The JSON source file '" + sourceFilePath + "' does not exist.");
		}
		const string jsonSourceFilePath = sourceFilePath + "-json-original";
		konga::kMoveFile(sourceFilePath, jsonSourceFilePath, true);

		SmartDbPtr<DbConnection> pTranDb_local(0);
		if ( pTranDb == 0 ) {
			pTranDb_local.Attach( konga::GetBackEndDbConnection("TranDb") );
			pTranDb = pTranDb_local;
		}

		std::auto_ptr<JsonEngineWrapper> jsonEngine( JsonEngineWrapper::createJsonEngineWrapper(*pTranDb, getOperationContext()->getCurrentUser()) );
		jsonEngine->startNewSession();
		try {
			etlforge::Utf8TextInput utf8(jsonSourceFilePath,GetSystemVariable(TEXT_SOURCE_ENC,getOperationDataElementHolder(),""));
			jsonEngine->jsonToXml(utf8.path(), sourceFilePath, m_TransformationID);
		} catch(const std::exception& failure) {throw CrException(failure.what());}
		jsonEngine->closeSession();

		if( !keepOriginal ) {
			konga::kDeleteFile(jsonSourceFilePath);
		}

	}
}

void TransformationEngine::HandleSpecialTargetStructures(const std::string& targetFilePath, long transformationId) {
	if ( m_targetType == JsonTypeId ) {
		if ( !konga::FileExists(targetFilePath) ) {
			throw InternalErrorException("The JSON target file '" + targetFilePath + "' does not exist.");
		}
		const string xmlTargetFilePath = targetFilePath + "-xml-intermediate";
		konga::kMoveFile(targetFilePath, xmlTargetFilePath, true);

		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		std::auto_ptr<JsonEngineWrapper> jsonEngine( JsonEngineWrapper::createJsonEngineWrapper(*pTranDb, getOperationContext()->getCurrentUser()) );
		jsonEngine->startNewSession();
		jsonEngine->xmlToJson(xmlTargetFilePath, targetFilePath, transformationId);

		jsonEngine->closeSession();
	}
}
