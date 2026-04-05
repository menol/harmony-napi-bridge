import { describe, it, expect } from '@ohos/hypium';
import { TestClass, hello_world_plugin } from 'khn';

export default function napiWrapperTest() {
  describe('NapiWrapperTest', () => {
    it('should test normal class export', 0, () => {
      // 1. Instantiating TestClass from ArkTS
      const tc = new TestClass(10);
      expect(tc.getValue()).assertEqual(10);
      
      // 2. Calling its instance methods
      tc.increment();
      expect(tc.getValue()).assertEqual(11);
      
      // 3. Getting an existing instance from Kotlin and verifying its methods work
      const existingTc = hello_world_plugin.getTestClass();
      expect(existingTc.getValue()).assertEqual(42);
      existingTc.increment();
      expect(existingTc.getValue()).assertEqual(43);
    });
  });
}
