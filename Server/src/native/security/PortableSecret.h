#pragma once

#include <openssl/crypto.h>
#include <openssl/evp.h>
#include <openssl/rand.h>
#include <array>
#include <memory>
#include <stdexcept>
#include <string>
#include <vector>

namespace etlforge::security {
inline std::array<unsigned char,32> secretKey(const std::string& master) {
    if(master.size()<32)throw std::runtime_error("Security/KeyVaultMasterKey must contain at least 32 characters on every cluster node");
    std::array<unsigned char,32> key{};unsigned int count=0;
    if(EVP_Digest(master.data(),master.size(),key.data(),&count,EVP_sha256(),nullptr)!=1||count!=key.size())throw std::runtime_error("Cannot derive secret-encryption key");
    return key;
}
inline std::string encode64(const std::vector<unsigned char>& bytes) {
    std::string output(4*((bytes.size()+2)/3),'\0');int size=EVP_EncodeBlock(reinterpret_cast<unsigned char*>(output.data()),bytes.data(),static_cast<int>(bytes.size()));output.resize(size);return output;
}
inline std::vector<unsigned char> decode64(const std::string& text) {
    if(text.empty()||text.size()%4)throw std::runtime_error("Invalid protected credential encoding");
    std::vector<unsigned char> output(3*(text.size()/4));int size=EVP_DecodeBlock(output.data(),reinterpret_cast<const unsigned char*>(text.data()),static_cast<int>(text.size()));
    if(size<0)throw std::runtime_error("Invalid protected credential encoding");
    size-=text.size()>=1&&text.back()=='=';size-=text.size()>=2&&text[text.size()-2]=='=';output.resize(size);return output;
}
inline std::string seal(const std::string& plain,const std::string& master) {
    if(plain.empty())return {};
    auto key=secretKey(master);std::vector<unsigned char> envelope(28+plain.size());
    if(RAND_bytes(envelope.data(),12)!=1)throw std::runtime_error("Cannot create credential nonce");
    EVP_CIPHER_CTX* raw=EVP_CIPHER_CTX_new();if(!raw)throw std::runtime_error("Cannot create credential cipher");
    std::unique_ptr<EVP_CIPHER_CTX,decltype(&EVP_CIPHER_CTX_free)> context(raw,EVP_CIPHER_CTX_free);int size=0,total=0;
    if(EVP_EncryptInit_ex(raw,EVP_aes_256_gcm(),nullptr,nullptr,nullptr)!=1||EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_SET_IVLEN,12,nullptr)!=1||EVP_EncryptInit_ex(raw,nullptr,nullptr,key.data(),envelope.data())!=1||EVP_EncryptUpdate(raw,envelope.data()+28,&size,reinterpret_cast<const unsigned char*>(plain.data()),static_cast<int>(plain.size()))!=1)throw std::runtime_error("Cannot encrypt credential");
    total=size;if(EVP_EncryptFinal_ex(raw,envelope.data()+28+total,&size)!=1)throw std::runtime_error("Cannot finish credential encryption");total+=size;
    if(EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_GET_TAG,16,envelope.data()+12)!=1)throw std::runtime_error("Cannot authenticate encrypted credential");envelope.resize(28+total);OPENSSL_cleanse(key.data(),key.size());return "gcm1:"+encode64(envelope);
}
inline std::string open(const std::string& protectedValue,const std::string& master) {
    if(protectedValue.empty())return {};if(protectedValue.rfind("gcm1:",0)!=0)throw std::runtime_error("Legacy machine-protected cloud credential requires one-time migration");
    auto envelope=decode64(protectedValue.substr(5));if(envelope.size()<28)throw std::runtime_error("Invalid protected credential envelope");auto key=secretKey(master);std::vector<unsigned char> plain(envelope.size()-28);
    EVP_CIPHER_CTX* raw=EVP_CIPHER_CTX_new();if(!raw)throw std::runtime_error("Cannot create credential cipher");std::unique_ptr<EVP_CIPHER_CTX,decltype(&EVP_CIPHER_CTX_free)> context(raw,EVP_CIPHER_CTX_free);int size=0,total=0;
    if(EVP_DecryptInit_ex(raw,EVP_aes_256_gcm(),nullptr,nullptr,nullptr)!=1||EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_SET_IVLEN,12,nullptr)!=1||EVP_DecryptInit_ex(raw,nullptr,nullptr,key.data(),envelope.data())!=1||EVP_DecryptUpdate(raw,plain.data(),&size,envelope.data()+28,static_cast<int>(envelope.size()-28))!=1)throw std::runtime_error("Cannot decrypt credential");total=size;
    if(EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_SET_TAG,16,envelope.data()+12)!=1||EVP_DecryptFinal_ex(raw,plain.data()+total,&size)!=1)throw std::runtime_error("Credential authentication failed; verify the cluster vault key");total+=size;OPENSSL_cleanse(key.data(),key.size());std::string result(reinterpret_cast<char*>(plain.data()),total);OPENSSL_cleanse(plain.data(),plain.size());return result;
}
}
