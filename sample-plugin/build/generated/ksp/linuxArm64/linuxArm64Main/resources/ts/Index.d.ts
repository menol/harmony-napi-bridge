export type Role = "ADMIN" | "USER";

export type NetworkResult = NetworkResult.Error | NetworkResult.Success;
export namespace NetworkResult {
    export interface Error {
        type: "Error";
        message: string;
    }
    export interface Success {
        type: "Success";
        data: string;
    }
}

export interface Student<T> {
    name: string;
    age: number;
    data: T;
}

export interface User {
    name: string;
    age: number;
}

export interface ArkTsHttpRequest {
    url: string;
    method: string;
    body: string;
}

export interface BasePageState {
}
export interface BaseView {
}
export interface IndexView extends BaseView {
    showUser(name: string): void;
    showStudent(student: Student<string>): void;
}
export declare class IndexPresenter {
    constructor();
    attach(view: IndexView): void;
    showUser(user: User): void;
    showStudent(student: Student<string>): Promise<void>;
    detach(): void;
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
export declare class TestClass {
    constructor(value: number);
    fetchValue(): number;
    increment(): void;
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
    function processResult(result: NetworkResult): NetworkResult;
    function fetchDataAsync(id: string): Promise<string>;
    function executeMultipleTasksAsync(count: number, delayMs: number): Promise<Array<string>>;
    function getTestClass(): TestClass;
}
export declare namespace koin_plugin {
    function initKoin(): void;
    function getGreeting(name: string): string;
    function getKoin(): unknown;
}
export interface ArkTsHttpFetcher {
    fetch(requestId: string, request: ArkTsHttpRequest): void;
}
export declare namespace ktor_plugin {
    function initKtor(fetcher: ArkTsHttpFetcher): void;
    function onFetchSuccess(requestId: string, status: number, body: string): void;
    function onFetchError(requestId: string, error: string): void;
    function fetchFromUrl(url: string): Promise<string>;
    function fetchUserFromApi(userId: string): Promise<string>;
}
export interface KeyValueStorage {
    saveString(key: string, value: string): void;
    getString(key: string): string;
}
export declare namespace storage_plugin {
    function initArkTsStorage(storageImpl: KeyValueStorage): void;
    function testStorageRoundTrip(): string;
    function getKoin(): unknown;
}
export declare namespace UserUtilsV2 {
    function getFullName(receiver: User): string;
}
