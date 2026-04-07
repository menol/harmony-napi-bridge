import abilityTest from './Ability.test';
import napiWrapperTest from './NapiWrapper.test';
import memoryLeakTest from './MemoryLeak.test';
import performanceTest from './Performance.test';

export default function testsuite() {
  abilityTest();
  napiWrapperTest();
  memoryLeakTest();
  performanceTest();
}