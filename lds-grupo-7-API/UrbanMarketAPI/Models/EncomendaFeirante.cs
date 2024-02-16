namespace UrbanMarketAPI.Models
{
    public class EncomendaFeirante
    {
        public int Id { get; set; }

        // Relação com a tabela Pedido
        // public Pedido Pedido { get; set; }

        // Chave estrangeira referente ao Pedido
        // public int PedidoId { get; set; }

        public bool Pago { get; set; }

        public DateTime Data {  get; set; }

        public float ValorFinal {  get; set; }
    }
}
