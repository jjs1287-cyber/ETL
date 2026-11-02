/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_OPERATION_ENGINE_DEBUG_LOG_H
#define OPERATION_OPERATION_ENGINE_DEBUG_LOG_H

                                                                        
               
#define OP_DEBUG_LOG(X) if(OperationEngine::m_bIsDebugEnabled){ m_Logger.Debug(std::string("OpId=") + toString(m_lOperationId) + std::string(": ") + X); }

#endif                                          

