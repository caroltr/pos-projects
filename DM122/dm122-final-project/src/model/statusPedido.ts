export class StatusPedido {

    private idPedido: string;
    private dataEmissao: number;
    private dataAtualizacao: number;
    private vendedor: string;
    private frete: string;
    private transportadora: string;
    private status: string;

    constructor() {
    }

    setIdPedido(idPedido:string) {
        this.idPedido = idPedido;
    }

    getIdPedido(): string {
        return this.idPedido;
    }

    setDataEmissao(dataEmissao:number) {
        this.dataEmissao = dataEmissao;
    }

    getDataEmissao(): number {
        return this.dataEmissao;
    }

    setDataAtualizacao(dataAtualizacao:number) {
        this.dataAtualizacao = dataAtualizacao;
    }

    getDataAtualizacao(): number {
        return this.dataAtualizacao;
    }

    setVendedor(vendedor: string) {
         this.vendedor = vendedor;
    }

    getVendedor(): string {
        return this.vendedor;
    }

    setFrete(frete: string) {
        this.frete = frete;
   }

   getFrete(): string {
    return this.frete;
   }

    setTransportadora(transportadora: string) {
        this.transportadora = transportadora;
    }

    getTransportadora(): string {
        return this.transportadora;
    }

    setStatus(status: string) {
        this.status = status;
    }

    getStatus(): string {
        return this.status;
    }
}