using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UrbanMarket.Models
{
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
