import { describe, it, expect } from '@ohos/hypium';
import { TestClass, hello_world_plugin } from 'khn';

export default function performanceTest() {
  describe('PerformanceTest', () => {
    it('should execute 10000 primitive conversions within threshold', 0, () => {
      const iterations = 10000;
      const start = Date.now();
      for (let i = 0; i < iterations; i++) {
        hello_world_plugin.add(i, i);
      }
      const end = Date.now();
      const duration = end - start;
      // Expect 10000 simple calls to take less than 500ms
      expect(duration).assertLess(500);
    });

    it('should execute 10000 object instantiations within threshold', 0, () => {
      const iterations = 10000;
      const start = Date.now();
      for (let i = 0; i < iterations; i++) {
        const tc = new TestClass(i);
        tc.increment();
      }
      const end = Date.now();
      const duration = end - start;
      // Expect 10000 object creations and calls to take less than 1500ms
      expect(duration).assertLess(1500);
    });
  });
}
