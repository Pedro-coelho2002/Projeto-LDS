using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UrbanMarket.Models
{
    public class Pedido
    {
        /// <summary>
        /// Identificador único do pedido.
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Produto que é requisitado.
        /// </summary>
        public ProdutoFeirante ProdutoFeirante { get; set; }

        /// <summary>
        /// Chave estrangeira, chave primária na Tabela Produto.
        /// </summary>
        public int ProdutoFeiranteId { get; set; }

        /// <summary>
        /// Identificador do feirante de origem.
        /// </summary>
        public int IdFrom { get; set; }

        /// <summary>
        /// Identificador do feirante de destino.
        /// </summary>
        public int IdTo { get; set; }

        /// <summary>
        /// Classifica se o pedido foi aprovado.
        /// </summary>
        public bool Aprovado { get; set; }
    }
}
