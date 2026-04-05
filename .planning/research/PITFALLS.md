# Pitfalls Research

**Domain:** HarmonyOS NAPI Unit Testing & Kotlin Native Bridge
**Researched:** 2026-04-05
**Confidence:** HIGH

## Critical Pitfalls

### Pitfall 1: Manual Glue Code Desync (The "Fake Test" Pitfall)

**What goes wrong:**
Unit tests pass, but they are testing manually written C++ bindings instead of the actual KSP-generated wrappers. The generated code remains completely disconnected from runtime execution.

**Why it happens:**
Setting up the build system to link KSP-generated C++ code into the HarmonyOS test module is complex. Developers temporarily write manual bindings to get tests running and forget to switch over to the generated output.

**How to avoid:**
Enforce that the test module's build configuration (e.g., `CMakeLists.txt`) ONLY includes source files from the `build/generated/ksp` directory. Forbid manual `napi_module_register` in test code.

**Warning signs:**
Manual N-API registration code (like `Add` or `InitOpenMiniEffect`) exists in the test module setup. The tests run, but deleting the generated code doesn't break them.

**Phase to address:**
Build System Integration & Test Setup

---

### Pitfall 2: Memory Safety & Use-After-Free in Initialization

**What goes wrong:**
The N-API module crashes randomly upon load or shows garbage strings because module properties (like `nm_modname`) point to freed memory.

**Why it happens:**
Using Kotlin Native's `memScoped` blocks for N-API structs. When the block exits, the memory is freed, but N-API expects pointers (like the module name) to outlive the initialization function.

**How to avoid:**
Allocate N-API module structs and strings on the heap or use static memory that outlives the initialization function.

**Warning signs:**
`memScoped` blocks used around `napi_module_register`. Intermittent crashes or garbage strings in crash logs during module load.

**Phase to address:**
Core N-API Wrapper Generation

---

### Pitfall 3: Missing FFI Exception Boundary

**What goes wrong:**
A simple Kotlin exception (e.g., `IllegalArgumentException`) crashes the entire HarmonyOS application or test runner.

**Why it happens:**
Kotlin Native exceptions cannot propagate across the C FFI boundary. Unhandled exceptions at this boundary cause immediate process termination.

**How to avoid:**
Generate a `try-catch` block around every Kotlin function call inside the C++ wrapper. On catch, use `napi_throw_error` to throw a proper JavaScript exception.

**Warning signs:**
The app crashes instantly without a JavaScript stack trace when passing invalid arguments from JS to Kotlin.

**Phase to address:**
Core N-API Wrapper Generation

---

### Pitfall 4: Ignoring N-API Status Codes & Uninitialized Memory

**What goes wrong:**
N-API functions fail silently, leading to the use of uninitialized variables, buffer overflows, or Out-Of-Memory (OOM) crashes.

**Why it happens:**
Developers ignore the `napi_status` returned by N-API functions (like `napi_get_value_string_utf8`) to write shorter code, assuming happy-path execution.

**How to avoid:**
Implement a macro or utility function that strictly checks `status == napi_ok` for every N-API call and handles failures safely (e.g., throwing a JS error).

**Warning signs:**
N-API functions are called but their return values are not assigned or checked. Variables like `lengthVar` are used without guaranteeing they were successfully populated.

**Phase to address:**
Core N-API Wrapper Generation

---

### Pitfall 5: Uninitialized Array Dereference (Argc/Argv Mismatch)

**What goes wrong:**
Blindly accessing `argv[index]` without verifying the actual argument count, leading to null pointer dereferences or undefined behavior.

**Why it happens:**
Assuming JavaScript callers will always provide the exact number of arguments defined in the Kotlin signature.

**How to avoid:**
Generate validation code that checks `argc` against the expected number of arguments before accessing `argv`. Throw a JS `TypeError` if there's a mismatch.

**Warning signs:**
Hardcoded array access `argv[index]!!` in generated wrappers without prior `if (argc < expected)` checks.

**Phase to address:**
Core N-API Wrapper Generation

---

## Technical Debt Patterns

Shortcuts that seem reasonable but create long-term problems.

| Shortcut | Immediate Benefit | Long-term Cost | When Acceptable |
|----------|-------------------|----------------|-----------------|
| Incomplete Type Mapping | Faster generation of simple functions | Functions returning `Unit` or using `Boolean` fail to compile | MVP only, must be fixed before release |
| Defining properties one-by-one | Simpler generator logic | Performance overhead during module initialization | Never acceptable for production |
| Assuming `@HarmonyModule` is on `object` | Skips validation logic | Cryptic compilation errors for users annotating `class` | Prototyping phase only |

## Integration Gotchas

Common mistakes when connecting to external services.

| Integration | Common Mistake | Correct Approach |
|-------------|----------------|------------------|
| CMake/Hvigor | Hardcoding paths to generated `.so` or `.cpp` | Use Gradle tasks to pass dynamic paths to CMake via arguments |
| KSP Output | Committing generated code to Git | Exclude `build/` and read directly from KSP output dirs during build |
| ArkTS Tests | Testing only happy paths | Test type mismatches, missing args, and exceptions to ensure stability |

## Performance Traps

Patterns that work at small scale but fail as usage grows.

| Trap | Symptoms | Prevention | When It Breaks |
|------|----------|------------|----------------|
| One-by-one Property Definition | Slow module load time | Allocate a single `napi_property_descriptor` array and call `napi_define_properties` once | Modules with 50+ exported functions |
| Redundant String Conversions | High CPU usage on FFI calls | Cache string conversions or use direct buffer access if possible | High-frequency function calls |

## Security Mistakes

Domain-specific security issues beyond general web security.

| Mistake | Risk | Prevention |
|---------|------|------------|
| Missing argc validation | Memory corruption / Out-of-bounds read | Always validate `argc` before accessing `argv` array |
| Unchecked N-API string length | Buffer overflow / OOM | Always check `napi_status` before allocating byte arrays for strings |
| Leaking Kotlin Exceptions | Process crash (Denial of Service) | Catch all Kotlin exceptions at the FFI boundary and convert to JS errors |

## UX Pitfalls

Common user experience mistakes in this domain.

| Pitfall | User Impact | Better Approach |
|---------|-------------|-----------------|
| Cryptic KSP compilation errors | Developers waste time debugging why generated code fails | Add explicit validation in KSP processor (e.g., verify `@HarmonyModule` target is an `object`) |
| Unhandled JS Exceptions | App crashes silently without context | Provide descriptive error messages in `napi_throw_error` detailing the expected vs actual types |

## "Looks Done But Isn't" Checklist

Things that appear complete but are missing critical pieces.

- [ ] **Generated Code Integration:** Often missing automated linking — verify CMake actually compiles `build/generated/ksp/...`
- [ ] **Exception Handling:** Often missing try-catch at FFI — verify throwing a Kotlin exception results in a catchable JS error.
- [ ] **Argument Validation:** Often missing argc checks — verify calling JS function with 0 arguments throws a TypeError instead of crashing.
- [ ] **Type Support:** Often missing `Unit` or `Boolean` — verify functions returning `Unit` compile and execute correctly.

## Recovery Strategies

When pitfalls occur despite prevention, how to recover.

| Pitfall | Recovery Cost | Recovery Steps |
|---------|---------------|----------------|
| Use-After-Free in Init | HIGH | Rewrite N-API initialization to use heap allocation or static variables, which may require refactoring the Kotlin Native entry point. |
| Manual Glue Code Desync | MEDIUM | Delete manual bindings, update CMake to point to KSP output, and fix any generation bugs that the manual bindings were hiding. |
| Missing FFI Boundary | HIGH | Modify the KSP generator to wrap all calls in try-catch, requiring changes to the core code generation templates. |

## Pitfall-to-Phase Mapping

How roadmap phases should address these pitfalls.

| Pitfall | Prevention Phase | Verification |
|---------|------------------|--------------|
| Manual Glue Code Desync | Build System Integration | Verify CMakeLists.txt points to KSP output directory and no manual `napi_module_register` exists. |
| Use-After-Free in Init | Core Wrapper Generation | Code review of the initialization function to ensure memory outlives the scope. |
| Missing FFI Boundary | Core Wrapper Generation | Write an ArkTS test that intentionally triggers a Kotlin exception and catches it in JS. |
| Uninitialized Array Dereference | Core Wrapper Generation | Write an ArkTS test that passes fewer arguments than expected and catches the TypeError. |
| Ignoring N-API Status Codes | Core Wrapper Generation | Code review to ensure all N-API calls check the return status. |

## Sources

- [CONCERNS.md](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/codebase/CONCERNS.md) (Project Technical Debt)
- N-API Documentation (Node.js/HarmonyOS)
- Kotlin Native C Interop Documentation

---
*Pitfalls research for: HarmonyOS NAPI Unit Testing & Kotlin Native Bridge*
*Researched: 2026-04-05*