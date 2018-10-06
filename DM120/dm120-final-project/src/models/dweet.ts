import { With } from './with';

export class Dweet {

    private this: string;
    private by: string;
    private the: string;
    private with: Array<With>;

    constructor(_this: string, by: string, the: string, _with: Array<With>) {
        this.this = _this;
        this.by = by;
        this.the = the;
        this.with = _with;
    }

    public getThis() {
        return this.this;
    }

    public setThis(_this: string) {
        this.this = _this;
    }

    public getBy(): string {
        return this.by
    }

    public setBy(by: string) {
        this.by = by;
    }

    public getThe(): string {
        return this.the
    }

    public setThe(the: string) {
        this.the = the;
    }

    public getWith(): Array<With> {
        return this.with
    }

    public setWith(_with: Array<With>) {
        this.with = _with;
    }
}