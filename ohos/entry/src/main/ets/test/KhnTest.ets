import { describe, it, expect } from '@ohos/hypium';
import { hello_world_plugin } from 'khn';
import type { Role, NetworkResult } from 'khn';

export default function khnTests() {
  describe('KhnNapiTests', () => {
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
      const successInput: NetworkResult.Success<string> = { type: "Success", data: "hello" };
      const successResult = hello_world_plugin.processResult(successInput);
      expect(successResult.type).assertEqual("Success");
      expect((successResult as NetworkResult.Success<string>).data).assertEqual("hello processed");

      const errorInput: NetworkResult<string> = { type: "Error", message: "failed" };
      const errorResult = hello_world_plugin.processResult(errorInput);
      expect(errorResult.type).assertEqual("Error");
      expect((errorResult as NetworkResult.Error<string>).message).assertEqual("failed processed");
    });

    it('should support async coroutine returns (placeholder)', 0, async () => {
      const result = await Promise.resolve(true);
      expect(result).assertTrue();
    });
  });
}
