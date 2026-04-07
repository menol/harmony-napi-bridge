import { describe, it, expect, beforeEach } from '@ohos/hypium';
import http from '@ohos.net.http';
import { hello_world_plugin, koin_plugin, storage_plugin, ktor_plugin, IndexPresenter, Student, KeyValueStorage, ArkTsHttpFetcher, ArkTsHttpRequest,
  User,
  TreeNode,
  MutableData,
  TripleBox} from 'khn';
import type { Role, NetworkResult, TestInterface, DataSealed } from 'khn';
import { UserUtilsV2 } from 'khn/src/main/ets/components/LogicKn';

export default function khnTests() {
  describe('KhnNapiTests', () => {
    
    beforeEach(async () => {
      // Yield to the event loop to prevent UI freezing and ANR (APP_INPUT_BLOCK)
      await new Promise<void>(resolve => setTimeout(resolve, 10));
    });

    it('should add two numbers', 0, () => {
      const result = hello_world_plugin.add(12, 30);
      expect(result).assertEqual(42);
    });

    it('should greet with name', 0, () => {
      const result = hello_world_plugin.greet('Harmony');
      // The exact string depends on the Kotlin implementation, but it usually returns a greeting string
      expect(typeof result).assertEqual('string');
      expect(result.length > 0).assertTrue();
    });

    it('should process list', 0, () => {
      const result = hello_world_plugin.processList(['A', 'B']);
      expect(Array.isArray(result)).assertTrue();
    });

    it('should process map', 0, () => {
      const result = hello_world_plugin.processMap({ 'key': 'value' });
      expect(typeof result).assertEqual('object');
      expect(result !== null).assertTrue();
    });


    it('should process specific primitive lists', 0, () => {
      
      const intList = hello_world_plugin.processIntList([1, 2, 3]);
      expect(intList[0]).assertEqual(2);
      expect(intList[2]).assertEqual(6);

      
      const doubleList = hello_world_plugin.processDoubleList([1.5, 2.5]);
      expect(doubleList[0]).assertEqual(3.0);
      expect(doubleList[1]).assertEqual(5.0);

      
      const boolList = hello_world_plugin.processBooleanList([true, false]);
      expect(boolList[0]).assertEqual(false);
      expect(boolList[1]).assertEqual(true);
    });

    it('should process specific primitive maps', 0, () => {
      
      const intMap = hello_world_plugin.processStringIntMap({ 'a': 10, 'b': 20 });
      expect(intMap['a']).assertEqual(20);
      expect(intMap['b']).assertEqual(30);

      
      const doubleMap = hello_world_plugin.processStringDoubleMap({ 'a': 10.5, 'b': 20.5 });
      expect(doubleMap['a']).assertEqual(20.5);
      expect(doubleMap['b']).assertEqual(30.5);

      
      const boolMap = hello_world_plugin.processStringBooleanMap({ 'a': true, 'b': false });
      expect(boolMap['a']).assertEqual(false);
      expect(boolMap['b']).assertEqual(true);
    });

    it('should process data classes and enums', 0, () => {
      
      const result = hello_world_plugin.processUser({ name: "Test", age: 20 }, "ADMIN");
      expect(result.name).assertEqual("Test-ADMIN");
      expect(result.age).assertEqual(20);
    });

    it('should process sealed classes and discriminated unions', 0, () => {
      const successInput: NetworkResult.Success = { type: "Success", data: "hello" };
      const successResult = hello_world_plugin.processResult(successInput);
      expect(successResult.type).assertEqual("Success");
      expect((successResult as NetworkResult.Success).data).assertEqual("hello processed");

      const errorInput: NetworkResult.Error = { type: "Error", message: "failed" };
      const errorResult = hello_world_plugin.processResult(errorInput);
      expect(errorResult.type).assertEqual("Error");
      expect((errorResult as NetworkResult.Error).message).assertEqual("failed processed");
    });

    it('should handle complex coroutines scheduling and background callbacks', 0, async () => {
      const presenter = new IndexPresenter();
      let callbacks: string[] = [];

      presenter.attach({
        showUser: (name: string) => {
          console.log(`[Test Callback] showUser: ${name}`);
          callbacks.push(name);
        },
        showStudent: (student: Student<string>) => {
          console.log(`[Test Callback] showStudent: ${student.name}`);
          callbacks.push(`Student: ${student.name}, Age: ${student.age}`);
        }
      });

      const student: Student<string> = { name: "Alice", age: 20, data: "CS" };
      await presenter.showStudent(student);

      expect(callbacks.length).assertEqual(4);
      expect(callbacks[0]).assertEqual("Starting process for Alice...");
      expect(callbacks[1]).assertEqual("Still computing in background...");
      expect(callbacks[2]).assertEqual("Student: ALICE, Age: 21");
      expect(callbacks[3]).assertEqual("Process finished!");
    });

    it('should support async executing multiple tasks concurrently', 0, async () => {
      const startTime = Date.now();
      const taskCount = 5;
      const delayMs = 1000;
      
      // Execute 5 tasks concurrently, each taking 1 second
      const results = await hello_world_plugin.executeMultipleTasksAsync(taskCount, delayMs);
      
      const endTime = Date.now();
      const duration = endTime - startTime;
      
      console.log(`[Concurrent Test] Executed ${taskCount} tasks in ${duration} ms`);
      
      // Verify all tasks returned results
      expect(results.length).assertEqual(taskCount);
      for (let i = 0; i < taskCount; i++) {
        expect(results[i]).assertEqual(`Task ${i + 1} completed`);
      }
      
      // If it was sequential, it would take 5000ms. 
      // If concurrent, it should take ~1000ms. Let's assert it takes less than 2000ms.
      expect(duration < 2000).assertTrue();
    });

    it('should support Koin dependency injection', 0, () => {
      // 1. Initialize Koin
      koin_plugin.initKoin();
      
      // 2. Call method that uses injected dependency
      const greeting = koin_plugin.getGreeting("HarmonyOS Developer");
      
      console.log(`[Koin Test] Result: ${greeting}`);
      
      // 3. Verify injection works
      expect(greeting).assertEqual("Hello HarmonyOS Developer, injected by Koin!");
    });

    it('should inject ArkTS storage into Kotlin via Koin', 0, () => {
      // 1. Create an ArkTS-side storage implementation (using Map to simulate KV DB)
      const mockStorage = new Map<string, string>();
      const arkTsStorage: KeyValueStorage = {
        saveString: (key: string, value: string) => {
          console.log(`[ArkTS Storage] Kotlin requested to save: ${key} = ${value}`);
          mockStorage.set(key, value);
        },
        getString: (key: string): string => {
          const value = mockStorage.get(key) || "";
          console.log(`[ArkTS Storage] Kotlin requested to get: ${key}, returning: ${value}`);
          return value;
        }
      };

      // 2. Inject it into Kotlin's Koin container
      storage_plugin.initArkTsStorage(arkTsStorage);

      // 3. Trigger Kotlin business logic. 
      // Kotlin will resolve SettingsRepository from Koin, and call saveTheme and getTheme on it.
      // This will call back into our ArkTS mockStorage!
      const result = storage_plugin.testStorageRoundTrip();

      // 4. Verify the round trip works
      expect(result).assertEqual("Dark Mode from Kotlin");
      expect(mockStorage.get("theme")).assertEqual("Dark Mode from Kotlin");
    });
    it('should use Ktor with custom Engine calling ArkTS HTTP', 0, async () => {
      // 1. Create ArkTS HTTP fetcher implementation using real @ohos.net.http
      const arkTsFetcher: ArkTsHttpFetcher = {
        fetch: (requestId: string, request: ArkTsHttpRequest): void => {
          console.log(`[ArkTS HttpFetcher] Real network request from Ktor Engine: ${request.method} ${request.url}`);
          
          let httpRequest = http.createHttp();
          
          let method = http.RequestMethod.GET;
          if (request.method.toUpperCase() === 'POST') method = http.RequestMethod.POST;
          else if (request.method.toUpperCase() === 'PUT') method = http.RequestMethod.PUT;
          else if (request.method.toUpperCase() === 'DELETE') method = http.RequestMethod.DELETE;

          let options: http.HttpRequestOptions = {
            method: method,
            header: {
              'Content-Type': 'application/json'
            },
            readTimeout: 10000,
            connectTimeout: 10000
          };

          if (request.body && request.body.length > 0) {
            options.extraData = request.body;
          }

          try {
            httpRequest.request(request.url, options)
              .then((data: http.HttpResponse) => {
                console.log(`[ArkTS HttpFetcher] Request success: ${requestId}, status: ${data.responseCode}`);
                let bodyStr = typeof data.result === 'string' ? data.result : JSON.stringify(data.result);
                ktor_plugin.onFetchSuccess(requestId, data.responseCode, bodyStr);
              })
              .catch((err: Error) => {
                console.error(`[ArkTS HttpFetcher] Request failed: ${requestId}, error: ${JSON.stringify(err)}`);
                ktor_plugin.onFetchError(requestId, err.message || JSON.stringify(err));
              })
              .finally(() => {
                httpRequest.destroy();
              });
          } catch (err) {
            console.error(`[ArkTS HttpFetcher] Request synchronous throw: ${requestId}, error: ${JSON.stringify(err)}`);
            ktor_plugin.onFetchError(requestId, err.message || JSON.stringify(err));
            httpRequest.destroy();
          }
        }
      };

      // 2. Inject it into Ktor Plugin
      ktor_plugin.initKtor(arkTsFetcher);
      
      // 3. Trigger Ktor request - Success case (200 OK)
      console.log(`[Ktor Test] Starting API fetch for user 1 (Success Case)...`);
      const startTime = Date.now();
      const response = await ktor_plugin.fetchUserFromApi("1");
      const duration = Date.now() - startTime;
      console.log(`[Ktor Test] Fetch complete in ${duration}ms. Response: \n${response}`);
      
      expect(response).assertContain("Status: 200");
      expect(response).assertContain("Leanne Graham");
      expect(response).assertContain("Bret");
      
      // 4. Trigger Ktor request - Failure case (404 Not Found)
      console.log(`[Ktor Test] Starting API fetch for invalid user (404 Case)...`);
      const notFoundResponse = await ktor_plugin.fetchUserFromApi("invalid_user_id_123");
      console.log(`[Ktor Test] 404 Fetch complete. Response: \n${notFoundResponse}`);
      expect(notFoundResponse).assertContain("Status: 404");
      expect(notFoundResponse).assertContain("{}");

      // 5. Trigger Ktor request - Network Error case (Invalid Host)
      console.log(`[Ktor Test] Starting API fetch for invalid host (Network Error Case)...`);
      try {
        await ktor_plugin.fetchFromUrl("https://invalid.domain.that.does.not.exist.com");
        expect(false).assertTrue(); // Should not reach here
      } catch (e) {
        console.log(`[Ktor Test] Expected network error caught: ${e.message}`);
        expect(e.message.length > 0).assertTrue();
      }
    });

    it('should test Kotlin extension functions', 0, () => {
      const user = { name: "Bob", age: 25 } as User;
      const fullName = UserUtilsV2.getFullName(user);
      expect(fullName).assertEqual("Bob (25)");
    });

    it('should test Kotlin Null Safety', 0, () => {
      // Test returning null
      const nullResult = hello_world_plugin.processNullableString(null);
      // We expect the Kotlin string to be converted correctly
      // Note: sometimes TS assertNull might have issues if NAPI null is returned differently, 
      // let's assert using strict equality
      expect(nullResult === null || nullResult === undefined).assertTrue();

      // Test returning non-null
      const strResult = hello_world_plugin.processNullableString("hello");
      expect(strResult).assertEqual("HELLO");

      // Test returning nullable list
      const listNull = hello_world_plugin.getNullableList(true);
      expect(listNull === null || listNull === undefined).assertTrue();

      const listNotNull = hello_world_plugin.getNullableList(false);
      expect(listNotNull !== null).assertTrue();
      expect(listNotNull?.length).assertEqual(3);
    });

    it('should test Kotlin Mutability (MutableList and MutableMap)', 0, () => {
      // List
      const inputList = ["A", "B"];
      const modifiedList = hello_world_plugin.processMutableList(inputList);
      expect(modifiedList.length).assertEqual(3);
      expect(modifiedList[2]).assertEqual("Kotlin_Added");

      // Map
      const inputMap: Record<string, number> = { "A": 1 };
      const modifiedMap = hello_world_plugin.processMutableMap(inputMap);
      expect(modifiedMap["A"]).assertEqual(1);
      expect(modifiedMap["Kotlin_Key"]).assertEqual(999);
    });

    it('should test Kotlin var vs val in Data Classes', 0, () => {
      const initialData = {
        id: 1,
        name: "Test"
        // 故意不传 readOnlyField，测试 Kotlin 数据类的默认参数反序列化
      } as MutableData;

      const result = hello_world_plugin.modifyMutableData(initialData);
      // var fields modified
      expect(result.id).assertEqual(101);
      expect(result.name).assertEqual("Test_Modified");
      // val field unchanged / fallback to default
      expect(result.readOnlyField).assertEqual("Immutable");
    });

    it('should test passing Interface as callback', 0, () => {
      const callback: TestInterface = {
        sayHello: (name: string) => {
          return `Hello ${name} from ArkTS`;
        }
      };
      
      const result = hello_world_plugin.callTestInterface(callback);
      expect(result).assertEqual("Callback returned: Hello Kotlin from ArkTS");
    });

    it('should test deeply nested Sealed Classes (Discriminated Unions)', 0, () => {
      const itemA: DataSealed.ItemA = { type: "ItemA", id: 5 };
      const resA = hello_world_plugin.processDataSealed(itemA) as DataSealed.ItemA;
      expect(resA.type).assertEqual("ItemA");
      expect((resA as DataSealed.ItemA).id).assertEqual(50);

      const itemB: DataSealed.ItemB = { type: "ItemB", name: "test" };
      const resB = hello_world_plugin.processDataSealed(itemB) as DataSealed.ItemB;
      expect(resB.type).assertEqual("ItemB");
      expect((resB as DataSealed.ItemB).name).assertEqual("TEST");
    });

    it('should test extreme recursive data structures (Tree)', 0, () => {
      const root = {
        value: "root",
        children: [
          { value: "child1", children: [] },
          { value: "child2", children: [
            { value: "grandchild", children: [] }
          ] }
        ]
      } as TreeNode;

      const result = hello_world_plugin.processTree(root);
      expect(result.value).assertEqual("ROOT");
      expect(result.children.length).assertEqual(2);
      // Because we reversed children in processTree:
      expect(result.children[0].value).assertEqual("CHILD2");
      expect(result.children[0].children[0].value).assertEqual("GRANDCHILD");
      expect(result.children[1].value).assertEqual("CHILD1");
    });

    it('should test extreme multi-generics (TripleBox)', 0, () => {
      const input = { a: "hello", b: 42, c: true } as TripleBox<string, number, boolean>;
      const result = hello_world_plugin.processTripleBox(input);
      expect(result.a).assertEqual("olleh");
      expect(result.b).assertEqual(420);
      expect(result.c).assertEqual(false);
    });

    it('should test extreme exception capturing across boundary', 0, () => {
      try {
        hello_world_plugin.throwKotlinException("Test Error");
        expect(false).assertTrue(); // Should not reach here
      } catch (err) {
        expect(err.message).assertContain("Kotlin Exception: Test Error");
      }
    });

    it('should test extreme Any? (Dynamic) mixing madness', 0, () => {
      const mixedData: ESObject = {
        "a_null": null,
        "a_string": "abc",
        "a_num": 5,
        "a_bool": true,
        "a_list": [1, false, "xy"],
        "a_map": { "inner": 10 } as Record<string, number>,
      };

      const result: ESObject = hello_world_plugin.processExtremeAny(mixedData);
      expect(result["Key_a_null"]).assertEqual("Was Null");
      expect(result["Key_a_string"]).assertEqual("cba");
      expect(result["Key_a_num"]).assertEqual(500); // 5 * 100
      expect(result["Key_a_bool"]).assertEqual(false);
      expect(result["Key_a_list"][0]).assertEqual(100);
      expect(result["Key_a_list"][1]).assertEqual(true);
      expect(result["Key_a_list"][2]).assertEqual("yx");
      expect(result["Key_a_map"]["Key_inner"]).assertEqual(1000);
    });

    it('should test extreme suspend coroutine promise rejection', 0, async () => {
      try {
        await hello_world_plugin.extremeSuspendThrow("Async Error");
        expect(false).assertTrue(); // Should not reach here
      } catch (err) {
        expect(err.message).assertContain("Suspend Exception: Async Error");
      }
    });
  });
}
