# External Integrations

## External APIs
- **None currently implemented as third-party REST APIs.**
- The project primarily focuses on bridging native Kotlin code with OpenHarmony's **Node-API (NAPI)**. It defines a custom networking bridge (`registerNetworkCallback`) to allow native code to trigger ArkTS/JS-side `apiRequest` HTTP calls.

## Databases
- **None**

## Auth Providers
- **None**

## Webhooks
- **None**

## Platform Integrations
- **HarmonyOS NAPI (Node-API)**: The core system integration allowing bidirectional calls between Kotlin Native and ArkTS.
- **Hilog (HarmonyOS Logging)**: Interop via `hilog.def` to allow Kotlin code to log directly to the HarmonyOS system log.