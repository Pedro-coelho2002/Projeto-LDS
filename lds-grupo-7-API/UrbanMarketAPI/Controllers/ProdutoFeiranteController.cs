using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarketAPI.Data;
using UrbanMarketAPI.DTO;
using UrbanMarketAPI.Models;

namespace UrbanMarketAPI.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class ProdutoFeiranteController : ControllerBase
	{
		private readonly UrbanMarketContext dbContext;

		public ProdutoFeiranteController(UrbanMarketContext urbanMarketContext) => this.dbContext = urbanMarketContext;

		// GET: api/ProdutoFeirante (Todas as publicações na aplicação)
		[HttpGet]
		public ActionResult<IEnumerable<ProdutoFeirante>> GetProdutosFeirantes()
		{
			if (dbContext.ProdutosFeirantes == null)
				return NotFound();

			var produtosFeirantes = dbContext.ProdutosFeirantes
				.ToList();

			var produtosFeirantesDTO = new List<ProdutoFeiranteDTO>();

			foreach (var item in produtosFeirantes)
			{
				produtosFeirantesDTO.Add(new ProdutoFeiranteDTO
				{
					Id = item.Id,
					ProdutoId = item.ProdutoId,
					FeiranteId = item.FeiranteId,
					Quantidade = item.Quantidade,
					PrecoUni = item.PrecoUni
				});
			}


			return Ok(produtosFeirantesDTO.ToList());
		}

		// GET: api/ProdutoFeirante/{id} (Detalhes de uma publicação)
		[HttpGet("{id}")]
		public ActionResult<ProdutoFeirante> GetProdutoFeiranteById(int id)
		{
			if (dbContext.ProdutosFeirantes == null)
				return NotFound();

			var produtoFeirante = dbContext.ProdutosFeirantes
				.SingleOrDefault(x => x.Id == id);

			if (produtoFeirante == null)
				return NotFound();

			return Ok(new ProdutoFeiranteDTO
			{
				Id = produtoFeirante.Id,
				ProdutoId = produtoFeirante.ProdutoId,
				FeiranteId = produtoFeirante.FeiranteId,
				Quantidade = produtoFeirante.Quantidade,
				PrecoUni = produtoFeirante.PrecoUni
			});
		}

		// GET: api/ProdutoFeirante/Feirante/{id} (Todas as publicações de um feirante)
		[HttpGet("Feirante/{id}")]
		public ActionResult<ProdutoFeirante> GetProdutoFeiranteByFeiranteId(int id)
		{
			if (dbContext.ProdutosFeirantes == null)
				return NotFound();

			// Criara uma lista de produtosFeirantes
			var produtosFeirantes = dbContext.ProdutosFeirantes
				.Where(x => x.FeiranteId == id)
				.ToList();

			if (produtosFeirantes == null)
				return NotFound();

			var produtosFeirantesDTO = new List<ProdutoFeiranteDTO>();

			foreach (var item in produtosFeirantes)
			{
				produtosFeirantesDTO.Add(new ProdutoFeiranteDTO
				{
					Id = item.Id,
					ProdutoId = item.ProdutoId,
					FeiranteId = item.FeiranteId,
					Quantidade = item.Quantidade,
					PrecoUni = item.PrecoUni
				});
			}
			return Ok(produtosFeirantesDTO);
		}


		[HttpPost]
		public ActionResult<ProdutoFeirante> Add(ProdutoFeirante produtoFeirante)
		{
			produtoFeirante.Id = 0;

			if (dbContext.ProdutosFeirantes == null)
				return NotFound();

			var produto = dbContext.Produtos
				.SingleOrDefault(x => x.Id == produtoFeirante.ProdutoId);

			if (produto == null)
				return BadRequest("Produto não encontrado.");

			var feirante = dbContext.Feirantes
				.SingleOrDefault(x => x.Id == produtoFeirante.FeiranteId);

			if (feirante == null)
				return BadRequest("Feirante não encontrado."); ;

			dbContext.ProdutosFeirantes.Add(produtoFeirante);
			dbContext.SaveChanges();

			var produtosFeirante = new ProdutoFeiranteDTO
			{
				Id = produtoFeirante.Id,
				ProdutoId = produtoFeirante.ProdutoId,
				FeiranteId = produtoFeirante.FeiranteId,
				Quantidade = produtoFeirante.Quantidade,
				PrecoUni = produtoFeirante.PrecoUni
			};


			return Ok(produtosFeirante);
		}

		[HttpPut("{id}")]
		public ActionResult<ProdutoFeirante> Update(int id, ProdutoFeirante produtoFeirante)
		{
			if (id != produtoFeirante.Id)
				return BadRequest();


			if (dbContext.ProdutosFeirantes == null)
				return NotFound();

			dbContext.Entry(produtoFeirante).State = EntityState.Modified;
			dbContext.SaveChanges();

			return Ok(produtoFeirante);
		}

		[HttpDelete("{id}")]
		public ActionResult<ProdutoFeirante> Delete(int id)
		{
			if (dbContext.ProdutosFeirantes == null)
				return NotFound();

			var produtoFeirante = dbContext.ProdutosFeirantes
				.SingleOrDefault(x => x.Id == id);

			if (produtoFeirante == null)
				return NotFound();

			dbContext.ProdutosFeirantes.Remove(produtoFeirante);
			dbContext.SaveChanges();

			return Ok(produtoFeirante);
		}
	}
}
