import { Content } from './content';

export class With {

    private thing: string;
    private created: string;
    private content: Content;
    private date: string;
    private time: string;

    constructor(thing: string, created: string, content: Content, date: string, time: string) {
        this.thing = thing;
        this.created = created;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public getThing(): string {
        return this.thing
    }

    public setThing(thing: string) {
        this.thing = thing;
    }

    public getCreated(): string {
        return this.created
    }

    public setCreated(created: string) {
        this.created = created;
    }

    public getContent(): Content {
        return this.content
    }

    public setContent(content: Content) {
        this.content = content;
    }

    public getDate(): string {
        return this.date
    }

    public setDate(date: string) {
        this.date = date;
    }

    public getTime(): string {
        return this.time
    }

    public setTime(time: string) {
        this.time = time;
    }
}