import { describe, it, expect } from '@ohos/hypium';
import hidebug from '@ohos.hidebug';
import { TestClass } from 'khn';

export default function memoryLeakTest() {
  describe('MemoryLeakTest', () => {
    it('should not leak memory after many object creations', 0, async () => {
      const iterations = 10000;
      const chunkSize = 1000;
      const initialMemory = Number(hidebug.getNativeHeapAllocatedSize());
      
      for (let i = 0; i < iterations; i += chunkSize) {
        for (let j = 0; j < chunkSize; j++) {
          if (i + j < iterations) {
            let tc = new TestClass(i + j);
            tc.increment();
            tc = null as any;
          }
        }
        // Yield the event loop to prevent ANR
        await new Promise<void>(resolve => setTimeout(resolve, 0));
      }
      
      // Force Garbage Collection to ensure unreferenced objects are freed
      // Note: In ArkTS, we can use global gc() if exposed, or rely on ArkTools.
      // For standard API, we might need to use a short delay or system GC trigger if available.
      // Assuming gc() is available in the test environment.
      if (typeof gc === 'function') {
        gc();
      }

      // Allow some time for Kotlin/Native background GC and NAPI finalizers to complete
      await new Promise<void>(resolve => setTimeout(resolve, 500));
      
      const finalMemory = Number(hidebug.getNativeHeapAllocatedSize());
      const diff = finalMemory - initialMemory;
      
      // Assert the difference is less than 2MB (2 * 1024 * 1024 bytes) to ensure no leak.
      // 50MB was too permissive and could hide small leaks.
      expect(diff).assertLess(2 * 1024 * 1024);
    });
  });
}
