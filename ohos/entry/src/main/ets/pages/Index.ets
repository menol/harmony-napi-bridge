import { hilog } from '@kit.PerformanceAnalysisKit';
import { Core } from '@ohos/hypium';
import hidebug from '@ohos.hidebug';
import faultLogger from '@ohos.faultLogger';
import khnTests from '../test/KhnTest';

const DOMAIN = 0x0001;
const TAG = 'KhnDemo';

@Entry
@Component
struct Index {
  @State logText: string = "No Tests Executed\nTap 'Run Tests' to execute the khn.har test suite.\n";
  @State metricsText: string = 'Memory Diff: 0 KB | Time: 0 ms';
  @State isRunning: boolean = false;
  
  private testsRegistered: boolean = false;

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
  }

  private appendLog(message: string): void {
    this.logText += message + '\n';
    hilog.info(DOMAIN, TAG, '%{public}s', message);
  }

  private runTests(): void {
    if (this.isRunning) return;
    this.isRunning = true;
    
    this.logText = '';
    this.appendLog('Starting tests...');

    const startMem = hidebug.getNativeHeapAllocatedSize();
    const startTime = Date.now();

    if (!this.testsRegistered) {
      khnTests();
      this.testsRegistered = true;
    }

    const mockDelegator = {
      addAbilityMonitor: () => {},
      removeAbilityMonitor: () => {},
      waitAbilityMonitor: () => Promise.resolve(),
      getAppContext: () => null,
      getAbilityState: () => 0,
      getCurrentTopAbility: () => Promise.resolve(null),
      print: (msg: string) => { this.appendLog(msg); },
      printSync: (msg: string) => { this.appendLog(msg); },
      finishTest: (msg: string, code: number, callback: () => void) => {
        this.appendLog(`Test Finished: ${msg} (Code: ${code})`);
        const endMem = hidebug.getNativeHeapAllocatedSize();
        const endTime = Date.now();
        const memDiff = endMem - startMem;
        const timeDiff = endTime - startTime;
        this.metricsText = `Memory Diff: ${memDiff} KB | Time: ${timeDiff} ms`;
        this.isRunning = false;
        if (callback) {
          callback();
        }
      }
    };

    Core.getInstance().execute(mockDelegator as any);
  }

  build() {
    Column({ space: 16 }) {
      Text('khn.har 真机联调面板')
        .fontSize(24)
        .fontWeight(FontWeight.Bold)
        .width('100%')
        .textAlign(TextAlign.Start)

      Text(this.metricsText)
        .fontSize(14)
        .fontColor('#666666')
        .width('100%')

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
            this.metricsText = 'Memory Diff: 0 KB | Time: 0 ms';
          })
          .enabled(!this.isRunning)
          .layoutWeight(1)
      }
      .width('100%')

      Divider()
        .strokeWidth(1)
        .color('#D9D9D9')

      Scroll() {
        Column() {
          Text(this.logText)
            .fontSize(12)
            .width('100%')
            .padding(8)
        }
        .width('100%')
        .backgroundColor('#F1F3F5')
        .alignItems(HorizontalAlign.Start)
      }
      .width('100%')
      .layoutWeight(1)
    }
    .width('100%')
    .height('100%')
    .padding(16)
  }
}
