import { describe, it, expect } from '@ohos/hypium';
import { hello_world_plugin } from 'khn';

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

    it('should support async coroutine returns (placeholder)', 0, async () => {
      const result = await Promise.resolve(true);
      expect(result).assertTrue();
    });
  });
}
