/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <iostream>

#include <konga/SysInfo/SysInfo.h>		                                

#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/SimpleFileFilterFileMatcher.h>

#include <konga/ServerVersion/ServerVersion.h>

#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/PluginManager/PipelinePluginManifest.h>
#include <konga/PluginManager/PipelinePluginInput.h>
#include <konga/PluginManager/PluginSerializer.h>
#include <konga/PluginManager/PipelinePluginParser.h>
#include <konga/PluginManager/PipelinePosition.h>

#include "PipelinePluginExecutor.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

namespace {
	std::string GetPipelinePluginDescription(const PipelinePluginManifest& manifest) {
		return string( "\"" + manifest.GetDisplayName() + "\" (Version="
			+ manifest.GetPipelinePluginVersion().GetLongVersionString()
			+ ", UniqueName=\"" + manifest.GetUniqueName() + "\")"
			);
	}

	const string sPluginDirKey("$(plugin.directory)");

	std::string SubstituteKeys(const std::string& s, const std::string& sPluginDirectory,
		const OperationDataElementHolder& deHolder)
	{
		string sRetVal = deHolder.replaceOperationDataElementInString(s);
		string::size_type ipos = 0;
		while ( (ipos=sRetVal.find(sPluginDirKey, ipos)) != string::npos ) {
			sRetVal.replace(ipos, sPluginDirKey.size(), sPluginDirectory);
			ipos += sPluginDirectory.size();
		}

		return sRetVal;
	}
}

namespace {
	                                                                                    
	string CreateCWD() {
		string cwd(konga::getTmpDir());
		AppendPathSeparator(cwd);
		cwd += "plugin-";
		cwd += GetGUIDString();
		konga::kCreateDirectory(cwd);
		AppendPathSeparator(cwd);
		return cwd;
	}

	void AddInputDataElements(const PipelinePluginManifest& manifest,
		const OperationDataElementHolder& deHolder, PipelinePluginInput& input) {
		                                                     
		  
		const PipelinePluginManifest::DEMapT& manifestDataElements = manifest.GetDataElements();
		for( PipelinePluginManifest::DEMapT::const_iterator cit = manifestDataElements.begin();
			cit != manifestDataElements.end(); ++cit )
		{
			if ( cit->second.get() == 0 )
				continue;
			
			const string& deName = cit->first;
			if ( deName.empty() )
				continue;

			const PluginDataElement& pluginDE = *cit->second.get();

			if ( pluginDE.IsWildCard() ) {
				string pattern(deName);
				SimpleFileFilterFileMatcher matcher(pattern, false, false);
				map<string, DataElement*> filteredDataElements;
				deHolder.getMatchingOperationDataElements(matcher, filteredDataElements);

				for( map<string, DataElement*>::const_iterator filterIt = filteredDataElements.begin(); filterIt != filteredDataElements.end(); ++filterIt ) {
					DataElement& filteredDe = *filterIt->second;
					const string& filteredDeName = filterIt->first;
					boost::shared_ptr<PluginDataElement> de( new PluginDataElement(filteredDe, true) );
					input.AddDataElement(filteredDeName, de);
				}
				continue;
			}

			DataElement* pDE = 0;
			if ( !deHolder.isOperationDataElementDefined(deName) ) {
				const char* defaultValue = pluginDE.getDefaultStr();
				if ( pluginDE.IsRequired() ) {
					if ( defaultValue == 0 || *defaultValue == 0 )
						throw CrException("Failed to execute pipeline plugin "
							+ GetPipelinePluginDescription(manifest)
							+ ". The required data element \"" + deName
							+ "\" is missing and the manifest provides no default.");				
					pDE = deHolder.getOperationDataElement(deName);
					pDE->set(&pluginDE);
					pDE->setValue(defaultValue);
				}
				else
					continue;	                                     
			}
			else {
				pDE = deHolder.getOperationDataElement(deName);
			}
			assert(pDE);

			                                                                        
			                                                   
			if ( pDE->isCollection() || pluginDE.isCollection() )
				continue;

			                                                                                                                
			if ( pluginDE.getType() != pDE->getType() ) {
				DataElement::Type oldType = pDE->getType();
				                                                     
				int err = 0;
				string sOldValue( (const char*)pDE->GetString(err) );
				pDE->setType(pluginDE.getType());
				pDE->setValue(sOldValue.c_str());
				const int iErr = pDE->getError();
				if ( pluginDE.IsRequired() && iErr != 0 )
					throw CrException("Failed to execute pipeline plugin "
						+ GetPipelinePluginDescription(manifest)
						+ ". The required data element of type " + string(DataElement::get_type_name(oldType))
						+ " with value \"" + sOldValue + " could not be converted to type "
						+ string(DataElement::get_type_name(pluginDE.getType()))
						+ " as required by the plugin manifest.");
			}

			boost::shared_ptr<PluginDataElement> de( new PluginDataElement(*pDE, pluginDE.IsRequired()) );
			input.AddDataElement(deName, de);
		}

		                                                    
		if ( manifest.GetRequestsAllDataElements() ) {
			const PluginDataElementHolder::DEMapT& alreadyAddedDataElements =
				input.GetDataElements();
			const map<string, DataElement*>& allDataElements =
				deHolder.getOperationDataElements();
			for( map<string, DataElement*>::const_iterator cit = allDataElements.begin();
				cit != allDataElements.end(); ++cit )
			{
				const string& deName = cit->first;
				DataElement* pDE = cit->second;
				
				if ( deName.empty() || pDE == 0 || pDE->IsNull() || pDE->getType() == DataElement::t_unknown )
					continue;

				if ( alreadyAddedDataElements.find(deName) == alreadyAddedDataElements.end() ) {
					boost::shared_ptr<PluginDataElement> pluginDe(
						new PluginDataElement(*pDE, true)
						);
					pluginDe->setName(deName.c_str());
					input.AddDataElement(deName, pluginDe);
				}
			}
		}
	}

	                                                        
	                                                                   
	void AppendFileContents(string& s, const string& fileName) {
		string tmp;
		konga::read_file_to_string(fileName, tmp);
		s += tmp;
	}
	void AppendToErrorString(string& sError, const string& stdoutFile,
		const string& stderrFile)
	{
		if ( konga::NonEmptyFileExists(stderrFile) ) {
			AppendFileContents(sError, stderrFile);
		}
		if ( konga::NonEmptyFileExists(stdoutFile) ) {
			AppendFileContents(sError, stdoutFile);
		}
	}

	                                                
	  
                                                                   
                           
  
                                                                   
                                                                                 
                                                                         
                   
                       
                       
                                                          
                                                                                                      
                                        
                                                
                          
                                                             
                                                
                          
                                           
                   
                                                                                     
    
                                                             
                                                 
                           
                                           
                   
                                                                                    
    

                                                    
                            
          
   
  
  
	class AutoDeletePluginDirectory {
	public:
		AutoDeletePluginDirectory(const string& sInputXmlFile,
			const string& sOutputXmlFile,
			const string& stdoutFileName,
			const string& stderrFileName,
			const string& sCWD) : m_bDelete(false),
			m_sInputXmlFile(sInputXmlFile),
			m_sOutputXmlFile(sOutputXmlFile),
			m_stdoutFileName(stdoutFileName),
			m_stderrFileName(stderrFileName),
			m_sCWD(sCWD)
		{}

		~AutoDeletePluginDirectory(void) {
			if ( m_bDelete ) {
				DeleteFileWithRetries(m_sInputXmlFile, 5, 100);
				DeleteFileWithRetries(m_sOutputXmlFile, 5, 100);
				DeleteFileWithRetries(m_stdoutFileName, 5, 100);
				DeleteFileWithRetries(m_stderrFileName, 5, 100);
				if ( konga::IsDirectoryEmpty(m_sCWD) ) {
					RemoveDirectoryWithRetries(m_sCWD, 5, 100);
				}
			}
		}

		void setDeleteFlagFromContext(const OperationContext& context) {
			m_bDelete = !context.isDebugEnabled();
		}

	private:
		bool m_bDelete;
		string m_sInputXmlFile, m_sOutputXmlFile, m_stdoutFileName, m_stderrFileName, m_sCWD;
	};

}                       

void PipelinePluginExecutor::ExecutePipelinePlugin(const PipelinePluginManifest& manifest,
												   const PipelinePosition& pipelinePosition,
												   const OperationContext& context,
												   const OperationDataElementHolder& deHolder,
												   const vector<string>& inputFiles,
												   bool& bHasNewFiles,
												   std::vector<OutputFile>& outputFiles)
{
	outputFiles.clear();

	                              
	  
	const Version& serverVersion = GetServerVersion();
	const string& sOsName = konga::GetPlatform();
	const string& sOsVersion = konga::GetOsVersion();
	const string& sServerHome = konga::GetServerHomeWithSlash();
	const string& sOperationName = context.getOperationName();
	const string& sOperationGuid = context.getOperationGuid();
	const long lOpId = context.getOperationId();
	const string& sPluginHome = konga::GetBranchPath( manifest.GetParsedFrom() );

	                                                                                                                           
	                                                                                                                     
	                                                                                                                      
	                                 
	PipelinePluginInput input(serverVersion, sOsName, sOsVersion, sServerHome, sOperationName.length() ? sOperationName : "[N/A]",
		sOperationGuid.length() ? sOperationGuid : "[N/A]", lOpId, sPluginHome);
	input.SetPipelinePosition(pipelinePosition);

	AddInputDataElements(manifest, deHolder, input);

	                           
	  
	for(vector<string>::const_iterator citFiles = inputFiles.begin();
		citFiles != inputFiles.end(); ++citFiles)
	{
		input.AddInputFile(*citFiles);
	}

	                                                                             
	const string& sCWD = CreateCWD();

	const string sInputXmlFile(sCWD + "input.xml");
	PluginSerializer serializer;
	const string& sInput = serializer.SerializePipelinePluginInput(input);
	konga::write_string_to_file(sInputXmlFile, sInput, true);

	                       
	  
	const string& sCommand = SubstituteKeys(manifest.GetCommand(), sPluginHome, deHolder);
	const string& sArgs = SubstituteKeys(manifest.GetArguments(), sPluginHome, deHolder);

	                                             
	const string stdoutFileName(sCWD + "stdout.txt");
	const string stderrFileName(sCWD + "stderr.txt");

	const string cmdForLogging(sCommand + " " + sArgs);

	RunProcFileHandles handles;
	ProcessIdT pid = konga::RunProc(sCommand, sArgs, sCWD, stdoutFileName, stderrFileName, &handles);
	const int iExitCode = konga::WaitForProc(pid);

	handles.Close();

	                                                                                
	                                             
	const string sOutputXmlFile(sCWD + "output.xml");
	AutoDeletePluginDirectory auto_delete_plugin_dir(sInputXmlFile, sOutputXmlFile, stdoutFileName, stderrFileName, sCWD);
	if ( !FileExists(sOutputXmlFile) ) {
		if ( iExitCode != 0 ) {
			string sErr("The pipeline plugin " + GetPipelinePluginDescription(manifest)
			+ " returned an error code " + toString(iExitCode) + ". No output.xml was generated.");
			AppendToErrorString(sErr, stdoutFileName, stderrFileName);
			sErr += "\nPlugin command: " + cmdForLogging;
			throw CrException(sErr);
		}
		auto_delete_plugin_dir.setDeleteFlagFromContext(context);
		return;
	}

	                    
	boost::shared_ptr<PipelinePluginOutput> output;
	try {
		PipelinePluginParser parser;
		output = parser.ParsePipelinePluginOutput(sOutputXmlFile);
	}
	catch( const CrException& ex ) {
		string sErr;
		if ( iExitCode == 0 ) {
			sErr = "Successfully called the pipeline plugin " + GetPipelinePluginDescription(manifest)
				+ " but ";
		} else {
			sErr = "The pipeline plugin " + GetPipelinePluginDescription(manifest)
				+ " returned an error code " + toString(iExitCode) + " and ";
		}
		sErr += "failed to parse the output xml written to the file \"" +
			sOutputXmlFile + "\". Reason:\n" + ex.GetErrorString();
		AppendToErrorString(sErr, stdoutFileName, stderrFileName);
		sErr += "\nPlugin command: " + cmdForLogging;
		throw CrException(sErr);
	}
	string sWarning, sSeverity;
	output->GetWarning(sWarning, sSeverity);

	if ( iExitCode != 0 ) {
		string sErr;
		if ( sWarning.empty() )
			sErr += "The plugin provided no error information.\n";
		else {
			sErr += sWarning + " (Severity=" + sSeverity + ")\n";
		}
		sErr += "\nThe pipeline plugin " + GetPipelinePluginDescription(manifest)
			+ " returned an error code " + toString(iExitCode) + ". ";
		AppendToErrorString(sErr, stdoutFileName, stderrFileName);
		sErr += "\nPlugin command: " + cmdForLogging;
		throw CrException(sErr);
	}

	                                                  
	const PipelinePluginOutput::DEMapT& outputDataElements = output->GetDataElements();
	for( PipelinePluginOutput::DEMapT::const_iterator citOutputDE = outputDataElements.begin();
		citOutputDE != outputDataElements.end(); ++citOutputDE)
	{
		const string& sDeName = citOutputDE->first;
		if ( sDeName.empty() )
			continue;

		const PluginDataElement& outputDE = *citOutputDE->second;
		DataElement* de = deHolder.getOperationDataElement(sDeName);
		de->set( &outputDE );
	}

	bHasNewFiles = output->GetHasFilesSection();
	if ( bHasNewFiles ) {
		const PipelinePluginOutput::OutputFilesT& ofs = output->GetOutputFiles();

		for( PipelinePluginOutput::OutputFilesT::const_iterator cit = ofs.begin();
			cit != ofs.end(); ++cit )
		{
			const PipelinePluginOutput::OutputFile& of = *cit;
			OutputFile outputFile;
			outputFile.Name = of.Name;
			outputFile.OriginFiles = of.OriginFiles;
			outputFile.Warning = FileWarning(of.Warning.Warning, of.Warning.Severity);
			outputFiles.push_back(outputFile);
		}
	}

	auto_delete_plugin_dir.setDeleteFlagFromContext(context);
}
