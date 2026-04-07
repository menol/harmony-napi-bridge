export function observeKmp<T extends Object>(value: T): T {
  // @ts-ignore - UIUtils is available in ArkUI
  return UIUtils.makeObserved(value) as T;
}

export function patchObserved<T extends Object>(target: T, next: T): void {
  if (!target || !next) return;
  
  const targetRecord = target as Record<string, Object>;
  const nextRecord = next as Record<string, Object>;
  
  for (const key in nextRecord) {
    if (Object.prototype.hasOwnProperty.call(nextRecord, key)) {
      const currentValue = targetRecord[key];
      const nextValue = nextRecord[key];
      
      const isObjectLike = (val: unknown): boolean => val !== null && typeof val === 'object';
      const isArrayLike = (val: unknown): boolean => Array.isArray(val);
      
      if (isObjectLike(currentValue) && isObjectLike(nextValue) && !isArrayLike(currentValue) && !isArrayLike(nextValue)) {
        patchObserved(currentValue as Object, nextValue as Object);
      } else {
        targetRecord[key] = nextValue;
      }
    }
  }
}