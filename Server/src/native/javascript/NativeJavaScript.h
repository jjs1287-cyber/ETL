#pragma once
#include <string>
#include <vector>
#include <map>
#include <functional>
#include <memory>
#include <atomic>

namespace etlforge { namespace javascript {
struct Value {
    enum Type { Undefined, Null, Boolean, Number, String, Array, Object } type = Undefined;
    bool boolean = false;
    double number = 0;
    std::string text;
    std::vector<Value> array;
    std::map<std::string, Value> object;
    static Value string(const std::string& s) { Value v; v.type=String; v.text=s; return v; }
};
struct Limits {
    size_t heapBytes = 64*1024*1024;
    size_t stackBytes = 256*1024;
    size_t valueBytes = 16*1024*1024;
    unsigned deadlineMs = 5000;
    const std::atomic<bool>* cancelled = nullptr;
};
using Host = std::function<Value(const std::string&, const std::vector<Value>&)>;
class Invocation {
public:
    explicit Invocation(Host host = Host(), Limits limits = Limits());
    ~Invocation();
    Invocation(const Invocation&) = delete;
    Invocation& operator=(const Invocation&) = delete;
    Value evaluate(const std::string& source, const std::string& filename = "javascript-block");
    void refreshGlobals();
    static void validate(const std::string& source);
    static void validateSyntax(const std::string& source);
private:
    struct Impl;
    std::unique_ptr<Impl> impl;
};
} }
