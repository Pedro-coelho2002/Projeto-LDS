using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarketAPI.Data;
using UrbanMarketAPI.DTO;
using UrbanMarketAPI.Models;

namespace UrbanMarketAPI.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class CategoriaProdutoController : ControllerBase
	{
		private readonly UrbanMarketContext dbContext;

		public CategoriaProdutoController(UrbanMarketContext urbanMarketContext) => this.dbContext = urbanMarketContext;

		// GET: api/CategoriaProduto
		[HttpGet]
		public ActionResult<IEnumerable<CategoriaProduto>> GetCategoriasProdutos()
		{
			if (dbContext.Categorias == null)
				return NotFound();

			var categorias = dbContext.Categorias
				.Include(categoria => categoria.Produtos)
				.ToList();

			var categoriasDTO = new List<CategoriaProdutoDTO>();

			foreach (var item in categorias)
			{
				categoriasDTO.Add(new CategoriaProdutoDTO
				{
					Id = item.Id,
					Categoria = item.Categoria
				});
			}

			return Ok(categoriasDTO.ToList());
		}

		// GET: api/CategoriaProduto/{id}
		[HttpGet("{id}")]
		public ActionResult<CategoriaProduto> GetCategoriaProdutoById(int id)
		{
			if (dbContext.Categorias == null)
				return NotFound();

			var categoria = dbContext.Categorias
				.SingleOrDefault(c => c.Id == id);

			if (categoria == null)
				return NotFound();

			return Ok(new CategoriaProdutoDTO { Id = categoria.Id, Categoria = categoria.Categoria });
		}

		// POST: api/CategoriaProduto
		/*
        * BODY:
        * {
        *  "categoria": "nome"
        * }
        */
		[HttpPost]
		public ActionResult<CategoriaProduto> Add(CategoriaProduto categoriaProduto)
		{
			// Validação das regras de negócio

			// Verificar se já existe uma categoria com o mesmo nome
			if (dbContext.Categorias.Any(c => c.Categoria == categoriaProduto.Categoria))
			{
				// Retorna um BadRequest indicando que a categoria já existe
				return BadRequest("Uma categoria com o mesmo nome já existe.");
			}

			categoriaProduto.Id = 0;
			dbContext.Categorias.Add(categoriaProduto);
			dbContext.SaveChanges();
			return CreatedAtAction(nameof(Add), new { id = categoriaProduto.Id }, categoriaProduto);
		}

		// PUT: api/CategoriaProduto/{id}
		/*
         * BODY:
         * {
         *  "id": {id},
         *  "categoria": "novo nome"
         * }
         * 
        */
		[HttpPut("{id}")]
		public IActionResult Update(int id, CategoriaProduto categoriaProduto)
		{
			if (!categoriaProduto.Id.Equals(id))
				return BadRequest();

			// Validação das regras de negócio

			dbContext.Categorias.Entry(categoriaProduto).State = EntityState.Modified;
			dbContext.SaveChanges();
			return NoContent();
		}

		// DELETE: api/CategoriaProduto/{id}
		[HttpDelete("{id}")]
		public IActionResult Delete(int id)
		{
			if (dbContext.Categorias == null)
				return NotFound();

			var categoria = dbContext.Categorias
				.Include(categoria => categoria.Produtos)
				.SingleOrDefault(c => c.Id == id);

			if (categoria == null)
				return NotFound();

			// Se a categoria possuir produtos, não é possível excluí-la
			if (categoria.Produtos.Count() != 0)
				return BadRequest("Esta categoria possui produtos.");

			dbContext.Categorias.Remove(categoria);
			dbContext.SaveChanges();
			return NoContent();
		}
	}
}
