import { describe, it, expect } from '@ohos/hypium';
import { TestClass, hello_world_plugin } from 'khn';

export default function performanceTest() {
  describe('PerformanceTest', () => {
    it('should execute 10000 primitive conversions within threshold', 0, async () => {
      const iterations = 10000;
      const chunkSize = 1000;
      const start = Date.now();
      for (let i = 0; i < iterations; i += chunkSize) {
        for (let j = 0; j < chunkSize; j++) {
          if (i + j < iterations) {
            hello_world_plugin.add(i + j, i + j);
          }
        }
        await new Promise<void>(resolve => setTimeout(resolve, 10));
      }
      const end = Date.now();
      const duration = end - start;
      // Expect 10000 simple calls to take less than 500ms
      expect(duration).assertLess(500);
    });

    it('should execute 10000 object instantiations within threshold', 0, async () => {
      const iterations = 10000;
      const chunkSize = 1000;
      const start = Date.now();
      for (let i = 0; i < iterations; i += chunkSize) {
        for (let j = 0; j < chunkSize; j++) {
          if (i + j < iterations) {
            const tc = new TestClass(i + j);
            tc.increment();
          }
        }
        await new Promise<void>(resolve => setTimeout(resolve, 10));
      }
      const end = Date.now();
      const duration = end - start;
      // Expect 10000 object creations and calls to take less than 1500ms
      expect(duration).assertLess(1500);
    });
  });
}
