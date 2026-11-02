/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef AXIS2CIMPL_BASE_H
#define AXIS2CIMPL_BASE_H

#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>

#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>

                                                                                          

namespace {
	axis2_char_t* allocate_axis2_char_t(const std::string& s) {
		axis2_char_t* pc = new axis2_char_t[s.size()+1];
		strcpy(pc, s.c_str());
		return pc;
	}

	                                                 
	void throwOnError(const axutil_env_t* env, const std::string& header) {
		const axis2_status_t status = axutil_env_check_status(env);
		if ( status != AXIS2_SUCCESS ) {
			const axis2_char_t* err = axutil_error_get_message(env->error);
			if ( err ) {
				throw konga::CrException(header + " " + err);
			}
			else {
				throw konga::CrException(header + " Axis2 returned non-success status code but no error was reported.");
			}
		}
	}

#ifdef _DEBUG
	const axutil_log_levels_t AXIS2_LOG_LEVEL = AXIS2_LOG_LEVEL_DEBUG;
#else
	const axutil_log_levels_t AXIS2_LOG_LEVEL = AXIS2_LOG_LEVEL_WARNING;
#endif
}

namespace konga {

	class Axis2CImplBase {
	public:
		Axis2CImplBase(const std::string& serviceName) : m_env(0), m_stub(0), m_home(0), m_uri(0), m_fail_counter(1), m_ServiceName(serviceName) {}
		virtual ~Axis2CImplBase(void) {
			CleanAll();
		}

		virtual axis2_stub_t* create_axis2_stub_t() = 0;

		void InitCurrent() {
			try {
				                                   
				if( m_logFile.empty() ) {
					m_logFile = getTmpDir();
					m_logFile += "axis2_log";
					kCreateDirectory(m_logFile);
					m_logFile = getTmpFileName("axis2log-", m_logFile);
					m_logFile += ".txt";
				}
				
				if ( m_env == 0 ) {
					m_env = axutil_env_create_all(m_logFile.c_str(), AXIS2_LOG_LEVEL);
				}
				if ( m_env == 0 ) {
					throw InternalErrorException("Failed to allocate Axis2C environment. Check the file '" + m_logFile
						+ "' for possible error messages.");
				}

				setHome();
				setUri();

				if ( m_stub == 0 ) {
					m_stub = this->create_axis2_stub_t();

					axis2_status_t status = axutil_env_check_status(m_env);
					throwOnError(m_env, "axutil_env_check_status failed.");

					if ( m_stub == 0 ) {
						std::string fatalErr("Failed to allocate Axis2C stub for " + m_ServiceName +
							". No failure status was reported. This probably means that the Axis2C installation/configuration is wrong.");
						if ( m_home  == 0 || *m_home == 0 ) {
							fatalErr += " No Axis2C directory was set. This is probably the issue.";
						}
						else {
							const std::string home_dir(m_home);
							if ( konga::IsDirectory(home_dir) ) {
								fatalErr += "The selected home directory " + home_dir + " exists. Make sure the required files are there.";
							}
							else {
								fatalErr += "The selected home directory " + home_dir + " does not exist or is not a directory.";
							}
						}
						throw InternalErrorException(fatalErr);
					}

					                      
					long timeOutMs = konga::GetOptionAsInt("JdbcEngine", "ClientSessionTimeout", 180);	                      
					                               
					timeOutMs*=60000;
					status = axis2_options_set_timeout_in_milli_seconds(
						axis2_stub_get_options(m_stub, m_env),
						m_env, timeOutMs);
				}
			}
			catch(...) {
				CleanCurrent();
				throw;
			}
		}

		void CleanCurrent() {
			if ( m_stub != 0 ) {
				axis2_stub_free(m_stub, m_env);
				m_stub = 0;
			}
		}

		void throwOnNullResponse(void* p, const std::string& header) {
			if ( p == 0 ) {
				CleanCurrent();
				throw InternalErrorException(header + std::string(" NULL response was returned. The operation was attempted " + konga::toString(m_fail_counter) + " times.\n")
					+ "Check for errors in logfile on server: '" + m_logFile + "'.");
			}
		}

	private:
		void CleanAll() {

			CleanCurrent();

			if ( m_env != 0 ) {
				axutil_env_free(m_env);
				m_env = 0;
			}

			if ( m_home != 0 ) {
				delete[] m_home;
				m_home = 0;
			}

			if ( m_uri != 0 ) {
				delete[] m_uri;
				m_uri = 0;
			}

			                            
			if ( !NonEmptyFileExists(m_logFile) ) {
				try {
					kDeleteFile(m_logFile);
				} catch(...) {
					                
				}
			}
		}

		void setHome() {
			if( m_home == 0 ) {
				m_home = allocate_axis2_char_t( konga::GetAXIS2C_HOME() );
			}
		}

		void setUri() {
			if ( m_uri == 0 ) {
				m_uri = allocate_axis2_char_t( konga::GetEndpointUrlForService(m_ServiceName) );
			}
		}

	protected:
		axutil_env_t* m_env;
		axis2_stub_t* m_stub;	           

		axis2_char_t* m_home;
		axis2_char_t * m_uri;

		int m_fail_counter;

		std::string m_logFile;
		std::string m_userName, m_md5Pwd;
		std::string m_sessionGuid;

		const std::string m_ServiceName;
	};
}

#endif                     
