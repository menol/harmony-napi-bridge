export type Role = "ADMIN" | "USER";

export interface User {
    name: string;
    age: number;
}

export interface TestInterface {
    sayHello(name: string): string;
}
export interface DemoInterface<T> {
    sayHello(name: T): string;
}
export interface DemoAbstract {
    process(item: Array<string>): Array<string>;
    sayHello(): string;
}
export interface TestAbstract<T> {
    process(item: T): T;
    sayHello(): string;
}
export declare namespace hello_world_plugin {
    function add(a: number, b: number): number;
    function greet(name: string): string;
    function processList(items: Array<string>): Array<string>;
    function processMap(data: Record<string, string>): Record<string, string>;
    function processAny(value: unknown): unknown;
    function processAnyMap(data: Record<string, unknown>): Record<string, unknown>;
    function processIntList(items: Array<number>): Array<number>;
    function processDoubleList(items: Array<number>): Array<number>;
    function processBooleanList(items: Array<boolean>): Array<boolean>;
    function processStringIntMap(data: Record<string, number>): Record<string, number>;
    function processStringDoubleMap(data: Record<string, number>): Record<string, number>;
    function processStringBooleanMap(data: Record<string, boolean>): Record<string, boolean>;
    function processUser(user: User, role: Role): User;
}
