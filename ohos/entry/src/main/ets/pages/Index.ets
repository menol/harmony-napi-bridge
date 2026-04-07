import { hilog } from '@kit.PerformanceAnalysisKit';
import { Core, Hypium } from '@ohos/hypium';
import hidebug from '@ohos.hidebug';
import faultLogger from '@ohos.faultLogger';
import khnTests from '../test/KhnTest';
import { observeKmp, patchObserved } from '../utils/KmpObserved';
import { DemoAbstract, IndexPresenter, IndexView, Student } from 'khn';
import type { DemoInterface, TestInterface, User } from 'khn';

const DOMAIN = 0x0001;
const TAG = 'KhnDemo';

interface MockDelegator {
  addAbilityMonitor: () => void;
  removeAbilityMonitor: () => void;
  waitAbilityMonitor: () => Promise<void>;
  getAppContext: () => Object | null;
  getAbilityState: () => number;
  getCurrentTopAbility: () => Promise<Object | null>;
  print: (msg: string) => void;
  printSync: (msg: string) => void;
  executeShellCommand: (cmd: string, timeout: number, callback: Function) => void;
  finishTest: (msg: string, code: number, callback: Function) => void;
}

interface MockArguments {
  bundleName: string;
  parameters: Record<string, string>;
}

class TestCall implements TestInterface {
  sayHello(name: string): string {
    hilog.info(DOMAIN, TAG, '%{public}s', name);
    return `Hello, ${name}!`;
  }
}

class DemoCall implements DemoInterface<User> {
  sayHello(user: User): string {
    hilog.info(DOMAIN, TAG, '%{public}s', user.name);
    return `Hello, ${user.name}!`;
  }
}


class DemoAbsCall extends DemoAbstract {

  process(item: string[]): string[] {
    return item.map(item => item.toUpperCase());
  }

}

@ObservedV2
class IndexViewModel implements IndexView {


  showStudent(student: Student<string>): void {
    console.log(`[JS Callback] showStudent called with: ${JSON.stringify(student)}`)
    patchObserved(this.student, student)
  }

  private present: IndexPresenter = new IndexPresenter()
  @Trace user: User = observeKmp({} as User)

  @Trace student: Student<string> = observeKmp({
    name: 'World',
    age: 42,
    data: '123123123'
  } as Student<string>)

  @Trace userName: string = 'World'

  onCreate() {
    this.present.attach(this)
  }

  request() {
    hilog.info(DOMAIN, TAG, '%{public}s', 'IndexViewModel request');

    this.present.showUser({
      name: '123123',
      age: 42
    })

    this.present.showStudent({
      name: '123123',
      age: 42,
      data: '123123123'
    })
  }

  showUser(name: string): void {
    console.log(`[JS Callback] showUser called with: ${name}`)
    this.userName = name
    hilog.info(DOMAIN, TAG, '%{public}s', 'IndexViewModel '+  name);

    this.user.name = name
  }

  onDestroy() {
    this.present.detach()
  }

}

@Entry
@ComponentV2
struct Index {
  @Local logText: string = "";
  @Local metricsText: string = 'Memory: -- KB | Time: -- ms';
  @Local isRunning: boolean = false;
  
  // Dashboard Status
  @Local testStatus: 'WAITING' | 'RUNNING' | 'PASSED' | 'FAILED' = 'WAITING';
  @Local passCount: number = 0;
  @Local failCount: number = 0;

  private testsRegistered: boolean = false;
  private startTime: number = 0;
  private startMem: bigint = BigInt(0);
  private demoCall = new DemoCall()
  private demoAbsCall = new DemoAbsCall()
  @Local viewModel: IndexViewModel = new IndexViewModel()


  async aboutToAppear() {
    try {
      const crashes = await faultLogger.query(faultLogger.FaultType.CPP_CRASH);
      if (crashes && crashes.length > 0) {
        const crash = crashes[0];
        this.appendLog(`Crash Detected: ${crash.reason} in ${crash.module}`);
      }
    } catch (e) {
      this.appendLog(`Error querying faultLogger: ${e}`);
    }
    this.viewModel.onCreate()
  }

  aboutToDisappear() {
    this.viewModel.onDestroy()
  }

  private pendingLogs: string = '';
  private logUpdateTimer: number = -1;

  private appendLog(message: string): void {
    hilog.info(DOMAIN, TAG, '%{public}s', message);

    // Accumulate logs in memory
    this.pendingLogs += message + '\n';
    
    // Keep only the last 10000 characters to prevent Text component layout from freezing the UI
    if (this.pendingLogs.length > 10000) {
      this.pendingLogs = this.pendingLogs.substring(this.pendingLogs.length - 10000);
    }

    // Debounce UI updates to prevent layout thrashing and ANR
    if (this.logUpdateTimer === -1) {
      this.logUpdateTimer = setTimeout(() => {
        this.logText = this.pendingLogs;
        this.logUpdateTimer = -1;
      }, 100);
    }

    // Simple Hypium log parser for beginner-friendly dashboard
    const passMatch = message.match(/(?:^|\b)["']?passes["']?\s*[:=]\s*(\d+)/i) || message.match(/(?:^|\b)pass(?:ed)?\s*[:=]\s*(\d+)/i);
    const failMatch = message.match(/(?:^|\b)["']?fails["']?\s*[:=]\s*(\d+)/i) || message.match(/(?:^|\b)fail(?:ed)?\s*[:=]\s*(\d+)/i);
    if (passMatch) {
      this.passCount = parseInt(passMatch[1], 10);
    }
    if (failMatch) {
      this.failCount = parseInt(failMatch[1], 10);
    }
  }

  private runTests(): void {
    if (this.isRunning) return;
    this.isRunning = true;
    
    // Reset Status
    this.testStatus = 'RUNNING';
    this.passCount = 0;
    this.failCount = 0;
    this.logText = '';
    this.pendingLogs = '';
    
    // Asynchronous call wrapper to ensure UI renders the 'RUNNING' state before blocking operations
    setTimeout(() => {
      this.viewModel.request()

      let testResult = this.demoCall.sayHello({
        name: 'World123123',
        age: 42
      })

      this.appendLog(testResult)
      this.appendLog(JSON.stringify(this.demoAbsCall.process(['asdff', 'asdf'])))
      this.appendLog('Starting tests...');

      this.startMem = hidebug.getNativeHeapAllocatedSize();
      this.startTime = Date.now();

      const mockDelegator: MockDelegator = {
        addAbilityMonitor: () => {},
        removeAbilityMonitor: () => {},
        waitAbilityMonitor: () => Promise.resolve(),
        getAppContext: () => null,
        getAbilityState: () => 0,
        getCurrentTopAbility: () => Promise.resolve(null),
        print: (msg: string) => { this.appendLog(msg); },
        printSync: (msg: string) => { this.appendLog(msg); },
        executeShellCommand: (cmd: string, timeout: number, callback: Function) => { callback(null, { stdResult: '' }); },
        finishTest: async (msg: string, code: number, callback: Function) => {
          this.appendLog(`Test Finished: ${msg} (Code: ${code})`);
          
          // Force GC to free JS wrappers, triggering NAPI finalizers
          if (typeof gc === 'function') {
            gc();
          }
          
          // Wait for Kotlin/Native background GC to complete cleanup
          await new Promise<void>(resolve => setTimeout(resolve, 800));

          const endMem = hidebug.getNativeHeapAllocatedSize();
          const endTime = Date.now();
          const memDiff = Number((endMem - this.startMem) / 1024n);
          const timeDiff = endTime - this.startTime;
          this.metricsText = `Memory Diff: ${memDiff} KB | Time: ${timeDiff} ms`;
          
          // Update Test Status Banner
          if (this.failCount > 0 || code !== 0) {
             this.testStatus = 'FAILED';
          } else if (this.passCount > 0) {
             this.testStatus = 'PASSED';
          } else {
             this.testStatus = 'WAITING'; 
          }
          
          this.isRunning = false;
          if (callback) {
            callback();
          }
        }
      };

      const mockArguments: MockArguments = {
        bundleName: 'com.example.harmony_napi_bridge',
        parameters: {}
      };

      if (!this.testsRegistered) {
        this.testsRegistered = true;
        Hypium.hypiumTest(mockDelegator, mockArguments, khnTests);
      } else {
        Core.getInstance().execute(mockDelegator);
      }
    }, 50); // Small delay to let the UI thread breathe and render the loading state
  }

  build() {
    Column({ space: 16 }) {
      
      // Dashboard Header
      Text('KMP/NAPI Test Dashboard')
        .fontSize(24)
        .fontWeight(FontWeight.Bold)
        .width('100%')
        .textAlign(TextAlign.Start)

      // Status Card (Beginner Friendly)
      Column({ space: 8 }) {
        Text(this.testStatus === 'WAITING' ? 'Ready to Test 🚥' :
             this.testStatus === 'RUNNING' ? 'Running Tests... 🏃' :
             this.testStatus === 'PASSED' ? 'All Tests Passed! ✅' : 'Tests Failed ❌')
          .fontSize(20)
          .fontWeight(FontWeight.Bold)
          .fontColor(this.testStatus === 'PASSED' ? '#00C853' :
                     this.testStatus === 'FAILED' ? '#E84026' :
                     this.testStatus === 'RUNNING' ? '#007DFF' : '#333333')
        
        Row({ space: 20 }) {
          Text(`Passed: ${this.passCount}`)
            .fontColor('#00C853')
            .fontWeight(FontWeight.Medium)
            .fontSize(16)
          Text(`Failed: ${this.failCount}`)
            .fontColor('#E84026')
            .fontWeight(FontWeight.Medium)
            .fontSize(16)
        }
        .width('100%')
        .justifyContent(FlexAlign.Start)

        Text(this.metricsText)
          .fontSize(12)
          .fontColor('#666666')
          .width('100%')
          .margin({ top: 4 })
      }
      .width('100%')
      .padding(16)
      .backgroundColor('#F1F3F5')
      .borderRadius(12)

      // Controls
      Row({ space: 12 }) {
        Button('Run Tests')
          .backgroundColor('#007DFF')
          .onClick(() => this.runTests())
          .enabled(!this.isRunning)
          .layoutWeight(1)

        Button('Clear Logs')
          .backgroundColor('#E84026')
          .onClick(() => {
            this.logText = '';
            this.pendingLogs = '';
            this.metricsText = 'Memory: -- KB | Time: -- ms';
            this.testStatus = 'WAITING';
            this.passCount = 0;
            this.failCount = 0;
          })
          .enabled(!this.isRunning)
          .layoutWeight(1)
      }
      .width('100%')

      Divider()
        .strokeWidth(1)
        .color('#D9D9D9')

      // Raw Logs (Scrollable)
      Text('Execution Logs:')
        .fontSize(14)
        .fontWeight(FontWeight.Medium)
        .width('100%')

      Scroll() {
        Column() {
          Text(this.logText)
            .fontSize(12)
            .fontFamily('HarmonyOS Sans')
            .width('100%')
            .padding(8)
        }
        .width('100%')
        .backgroundColor('#F8F9FA')
        .alignItems(HorizontalAlign.Start)
      }
      .width('100%')
      .layoutWeight(1)
      .borderRadius(8)
    }
    .width('100%')
    .height('100%')
    .padding(16)
    .backgroundColor('#FFFFFF')
  }
}