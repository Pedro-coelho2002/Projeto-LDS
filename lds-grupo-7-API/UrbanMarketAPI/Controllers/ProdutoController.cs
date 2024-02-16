using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarketAPI.Data;
using UrbanMarketAPI.DTO;
using UrbanMarketAPI.Models;

namespace UrbanMarketAPI.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class ProdutoController : ControllerBase
	{
		private readonly UrbanMarketContext dbContext;

		public ProdutoController(UrbanMarketContext urbanMarketContext) => this.dbContext = urbanMarketContext;

		// GET: api/Produto
		[HttpGet]
		public ActionResult<IEnumerable<Produto>> GetProdutos()
		{
			if (dbContext.Produtos == null)
				return NotFound();

			var produtosDTO = new List<ProdutoDTO>();

			dbContext.Produtos.ToList().ForEach(p =>
			{
				produtosDTO.Add(new ProdutoDTO
				{
					Id = p.Id,
					Nome = p.Nome,
					LinkImagem = p.LinkImagem,
					CategoriaProdutoId = p.CategoriaProdutoId,
					UnidMedidaId = p.UnidMedidaId
				});
			});


			return Ok(produtosDTO.ToList());
		}

		// GET: api/Produto/{id}
		[HttpGet("{id}")]
		public ActionResult<Produto> GetProdutoById(int id)
		{
			if (dbContext.Produtos == null)
				return NotFound();

			var produto = dbContext.Produtos
				.SingleOrDefault(x => x.Id == id);

			if (produto == null)
				return NotFound();

			return Ok(new ProdutoDTO
			{
				Id = produto.Id,
				Nome = produto.Nome,
				LinkImagem = produto.LinkImagem,
				CategoriaProdutoId = produto.CategoriaProdutoId,
				UnidMedidaId = produto.UnidMedidaId
			});
		}

		// POST: api/Produto
		/*
         * BODY:
         * {
         *  "nome": "nome",
         *  "linkImagem": "LINK",
         *  "categoriaProdutoId": 0,
         *  "unidMedidaId": 0
         * }
         */
		[HttpPost]
		public ActionResult<Produto> Add(Produto produto)
		{
			// Validação das regras de negócio

			produto.Id = 0;

			var categoria = dbContext.Categorias
				.SingleOrDefault(c => c.Id == produto.CategoriaProdutoId);

			if (categoria == null)
				return BadRequest("Categoria não encontrada.");

			var unidadeMedida = dbContext.UnidadesMedidas
				.SingleOrDefault(u => u.Id == produto.UnidMedidaId);

			if (unidadeMedida == null)
				return BadRequest("Unidade de medida não encontrada.");

			dbContext.Produtos.Add(produto);
			dbContext.SaveChanges();
			return CreatedAtAction(nameof(Add), new { id = produto.Id }, produto);
		}

		// PUT: api/Produto/{id}
		/*
         * BODY:
         * {
         *  "id": {id},
         *  "nome": "nome",
         *  "linkImagem": "LINK",
         *  "categoriaProdutoId": 0,
         *  "unidMedidaId": 0
         * }
         */
		[HttpPut("{id}")]
		public IActionResult Update(int id, Produto produto)
		{
			if (!produto.Id.Equals(id))
				return BadRequest();

			// Validação das regras de negócio

			if (!IsValidateProduto(produto))
				return BadRequest("O produto inserido não é válido!");

			dbContext.Entry(produto).State = EntityState.Modified;
			dbContext.SaveChanges();

			var produtoDTO = new ProdutoDTO
			{
				Id = produto.Id,
				Nome = produto.Nome,
				LinkImagem = produto.LinkImagem,
				CategoriaProdutoId = produto.CategoriaProdutoId,
				UnidMedidaId = produto.UnidMedidaId
			};

			return Ok(produtoDTO);
		}

		// DELETE: api/Produto/{id}
		[HttpDelete("{id}")]
		public ActionResult<Produto> Delete(int id)
		{
			var produto = dbContext.Produtos
				.SingleOrDefault(p => p.Id == id);

			if (produto == null)
				return NotFound();

			dbContext.Produtos.Remove(produto);
			dbContext.SaveChanges();
			return Ok(produto);
		}

		// GET: api/Produto/Feirantes/{id}
		[HttpGet("Feirantes/{id}")]
		public ActionResult<IEnumerable<FeiranteDTO>> GetFeirantesByProdutoId(int id)
		{
			var feirantes = dbContext.ProdutosFeirantes
				.Where(pf => pf.ProdutoId == id)
				.Include(pf => pf.Feirante)
				.Select(pf => new FeiranteDTO
				{
					Id = pf.Feirante.Id,
					Nome = pf.Feirante.Nome,
					Email = pf.Feirante.Email,
					DataNasc = pf.Feirante.DataNasc,
					Sexo = pf.Feirante.Sexo,
					Telem = pf.Feirante.Telem,
					Pin = pf.Feirante.Pin
				})
				.Distinct()  // Ensure unique entries
				.ToList();

			if (feirantes == null || feirantes.Count == 0)
			{
				return NotFound();
			}

			return Ok(feirantes);
		}

		/// <summary>
		/// Função que valida as regras de negócio do produto.
		/// </summary>
		/// <param name="produto">Produto inserido</param>
		/// <returns> False se o produto não corresponder às regras. True se corresponder às regras de negócio.</returns>
		private bool IsValidateProduto(Produto produto)
		{
			var categoria = dbContext.Categorias
				.SingleOrDefault(c => c.Id == produto.CategoriaProdutoId);

			if (categoria == null)
				return false;

			var unidadeMedida = dbContext.UnidadesMedidas
				.SingleOrDefault(u => u.Id == produto.UnidMedidaId);

			if (unidadeMedida == null)
				return false;

			return true;
		}
	}
}
