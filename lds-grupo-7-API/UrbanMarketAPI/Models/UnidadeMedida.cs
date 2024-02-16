using Microsoft.Extensions.Hosting;
using System.ComponentModel.DataAnnotations;

namespace UrbanMarketAPI.Models
{
    /// <summary>
    /// Classe que representa a unidade de medida de um produto.
    /// </summary>
    public class UnidadeMedida
    {
        /// <summary>
        /// Identificador único da unidade de medida.
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Nome da unidade de medida.
        /// </summary>
        [MaxLength(50)]
        public string UniMedida { get; set; }

        // Relação com a tabela Produto (representada por uma coleção de produtos)
        /// <summary>
        /// Coleção de produtos que têm a unidade de medida.
        /// </summary>
        public ICollection<Produto> Produtos { get; set; } = new List<Produto>();

    }
}
