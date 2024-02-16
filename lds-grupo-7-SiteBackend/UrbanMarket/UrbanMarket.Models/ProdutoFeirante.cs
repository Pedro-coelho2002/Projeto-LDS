using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UrbanMarket.Models
{
	public class ProdutoFeirante
    {
        /// <summary>
        /// Identificador único da publicação do produto.
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Produto que é publicado.
        /// </summary>
        public Produto? Produto { get; set; }

        /// <summary>
        /// Identificador do produto que é publicado.
        /// </summary>
        public int ProdutoId { get; set; }

        /// <summary>
        /// Feirante que publica o produto.
        /// </summary>
        public Feirante? Feirante { get; set; }

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

        public ICollection<Pedido> Pedidos { get; set; } = new List<Pedido>();
    }
}
