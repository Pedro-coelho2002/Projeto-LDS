using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace UrbanMarketAPI.Models
{
	public class Produto
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
		/// Categoria do produto.
		/// </summary>
		public CategoriaProduto? Categoria { get; set; }

		/// <summary>
		/// Identificador da categoria do produto.
		/// Chave estrangeira, chave primária na Tabela CategoriaProduto.
		/// </summary>
		public int CategoriaProdutoId { get; set; }

		/// <summary>
		/// Unidade de medida do produto.
		/// </summary>
		public UnidadeMedida? UnidMedida { get; set; }

		/// <summary>
		/// Chave estrangeira, chave primária na Tabela UnidadeMedida.
		/// </summary>
		public int UnidMedidaId { get; set; }

		public ICollection<ProdutoFeirante> PublicacoesDoProduto { get; set; } = new List<ProdutoFeirante>();
	}
}
