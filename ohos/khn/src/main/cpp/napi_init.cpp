#include "napi/native_api.h"


// Forward declaration for generated wrappers initialization
extern "C" napi_value InitGeneratedWrappers(napi_env env, napi_value exports);

EXTERN_C_START
static napi_value Init(napi_env env, napi_value exports) {

    // 初始化所有生成的 Wrappers
    InitGeneratedWrappers(env, exports);
    
    return exports;
}
EXTERN_C_END

static napi_module demoModule = {
    .nm_version = 1,
    .nm_flags = 0,
    .nm_filename = nullptr,
    .nm_register_func = Init,
    .nm_modname = "khn",
    .nm_priv = ((void *)0),
    .reserved = {0},
};

extern "C" __attribute__((constructor)) void RegisterKhnModule(void) { napi_module_register(&demoModule); }
