export class Content {

    private temperatura: string = '0';
    private luminosidade: string = '0';
    private botao: string = '0';
    private toque: string = '0';
    private status: string = '';

    constructor(temperatura: string, luminosidade: string, botao: string, toque: string, status: string) {
        this.temperatura = temperatura;
        this.luminosidade = luminosidade;
        this.botao = botao;
        this.toque = toque;
        this.status = status;
    }

    public getTemperatura(): string {
        return this.temperatura
    }

    public setTemperatura(temperatura: string) {
        this.temperatura = temperatura;
    }

    public getLuminosidade(): string {
        return this.luminosidade
    }

    public setLuminosidade(luminosidade: string) {
        this.luminosidade = luminosidade;
    }

    public getBotao(): string {
        return this.botao
    }

    public setBotao(botao: string) {
        this.botao = botao;
    }

    public getToque(): string {
        return this.toque
    }

    public setToque(toque: string) {
        this.toque = toque;
    }

    public getStatus(): string {
        return this.status
    }

    public setStatus(status: string) {
        this.status = status;
    }
}