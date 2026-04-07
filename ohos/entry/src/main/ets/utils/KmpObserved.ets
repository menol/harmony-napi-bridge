import { UIUtils } from '@kit.ArkUI';

export function observeKmp<T extends object>(value: T): T {
  return UIUtils.makeObserved(value);
}

export function patchObserved<T extends object>(target: T, next: T): void {
  if (!target || !next) {
    return;
  }
  
  const targetRecord = target as Record<string, Object | null | undefined>;
  const nextRecord = next as Record<string, Object | null | undefined>;
  
  const keys = Object.keys(nextRecord);
  for (let i = 0; i < keys.length; i++) {
    const key = keys[i];
    const currentValue = targetRecord[key];
    const nextValue = nextRecord[key];
    
    const isCurrentObject = currentValue !== null && typeof currentValue === 'object' && !Array.isArray(currentValue);
    const isNextObject = nextValue !== null && typeof nextValue === 'object' && !Array.isArray(nextValue);
    
    if (isCurrentObject && isNextObject) {
      patchObserved(currentValue as object, nextValue as object);
    } else {
      targetRecord[key] = nextValue;
    }
  }
}