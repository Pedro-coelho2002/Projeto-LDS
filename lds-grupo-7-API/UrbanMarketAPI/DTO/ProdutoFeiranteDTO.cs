namespace UrbanMarketAPI.DTO
{
    public class ProdutoFeiranteDTO
    {
        /// <summary>
        /// Identificador único da publicação do produto.
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Identificador do produto que é publicado.
        /// </summary>
        public int ProdutoId { get; set; }

        /// <summary>
        /// Identificador do feirante que publica o produto.
        /// </summary>
        public int FeiranteId { get; set; }

        /// <summary>
        /// Quantidade de produto que o feirante publica.
        /// </summary>
        public float Quantidade { get; set; }

        /// <summary>
        /// Preço por unidade.
        /// </summary>
        public float PrecoUni { get; set; }

        /// <summary>
        /// Preço total calculado com base na quantidade e preço por unidade.
        /// </summary>
        public float PrecoTotal
        {
			get { return Quantidade * PrecoUni; }
		}
    }
}
