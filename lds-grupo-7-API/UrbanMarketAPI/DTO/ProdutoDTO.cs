using System.ComponentModel.DataAnnotations;

namespace UrbanMarketAPI.DTO
{
    public class ProdutoDTO
    {
        /// <summary>
        /// Identificador único do produto.
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Nome do produto.
        /// </summary>
        [MaxLength(50)]
        public string Nome { get; set; }

        /// <summary>
        /// Link para a imagem que é exibida para a representação do produto.
        /// </summary>
        public string LinkImagem { get; set; }

        /// <summary>
        /// Chave estrangeira, chave primária na Tabela CategoriaProduto.
        /// </summary>
        public int CategoriaProdutoId { get; set; }

        /// <summary>
        /// Chave estrangeira, chave primária na Tabela UnidadeMedida.
        /// </summary>
        public int UnidMedidaId { get; set; }
    }
}
