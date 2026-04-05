export type Role = "ADMIN" | "USER";

export type NetworkResult<T> = NetworkResult.Error<T> | NetworkResult.Success<T>;
export namespace NetworkResult {
    export interface Error<T> {
        type: "Error";
        message: string;
    }
    export interface Success<T> {
        type: "Success";
        data: T;
    }
}

export interface User {
    name: string;
    age: number;
}

export interface BasePageState {
}
export interface BaseView {
}
export interface IndexView extends BaseView {
    showUser(name: string): void;
}
export interface PageState extends BasePageState {
}
export namespace PageState {
    export interface Error extends PageState {
        type: "Error";
        message: string;
    }
    export interface Loading extends PageState {
        type: "Loading";
        isRefreshing: boolean;
    }
    export interface Success<T> extends PageState {
        type: "Success";
        data: T;
    }
}
export interface TestSealed<T> {
    process(item: T): T;
}
export interface TestInterface {
    sayHello(name: string): string;
}
export interface DemoInterface<T> {
    sayHello(name: T): string;
}
export declare abstract class DemoAbstract {
    abstract process(item: Array<string>): Array<string>;
    sayHello(): string;
}
export declare abstract class TestAbstract<T> {
    abstract process(item: T): T;
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
    function processResult(result: NetworkResult<string>): NetworkResult<string>;
}
export declare namespace UserUtils {
    function getFullName(receiver: User): string;
}
