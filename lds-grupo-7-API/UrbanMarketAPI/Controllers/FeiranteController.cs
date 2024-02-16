using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Text.RegularExpressions;
using UrbanMarketAPI.Data;
using UrbanMarketAPI.DTO;
using UrbanMarketAPI.Models;

namespace UrbanMarketAPI.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class FeiranteController : ControllerBase
	{
		private readonly UrbanMarketContext dbContext;

		public FeiranteController(UrbanMarketContext urbanMarketContext) => this.dbContext = urbanMarketContext;

		// GET: api/Feirante
		[HttpGet]
		public ActionResult<IEnumerable<Feirante>> GetFeirantes()
		{
			if (dbContext.Feirantes == null)
				return NotFound();

			var feirantes = dbContext.Feirantes
				.Include(feirante => feirante.ProdutosPublicados)
				.ToList();

			var feirantesDTO = new List<FeiranteDTO>();

			foreach (var item in feirantes)
			{
				feirantesDTO.Add(new FeiranteDTO
				{
					Id = item.Id,
					Nome = item.Nome,
					Email = item.Email,
					DataNasc = item.DataNasc,
					Sexo = item.Sexo,
					Telem = item.Telem,
					Pin = item.Pin
				});

			}

			return Ok(feirantesDTO.ToList());
		}

		// GET: api/Feirante/{id}
		[HttpGet("ById/{id}")]
		public ActionResult<Feirante> GetFeiranteById(int id)
		{
			if (dbContext.Feirantes == null)
				return NotFound();

			var feirante = dbContext.Feirantes
				.Include(feirante => feirante.ProdutosPublicados)
				.SingleOrDefault(f => f.Id == id);

			if (feirante == null)
				return NotFound();

			return Ok(new FeiranteDTO
			{
				Id = feirante.Id,
				Nome = feirante.Nome,
				Email = feirante.Email,
				DataNasc = feirante.DataNasc,
				Sexo = feirante.Sexo,
				Telem = feirante.Telem,
				Pin = feirante.Pin
			});
		}

		// GET: api/Feirante/{telem}
		[HttpGet("ByTelem/{telem}")]
		public ActionResult<Feirante> GetFeiranteByTelem(int telem)
		{
			if (dbContext.Feirantes == null)
				return NotFound();

			var feirante = dbContext.Feirantes
				.Include(feirante => feirante.ProdutosPublicados)
				.SingleOrDefault(f => f.Telem == telem);

			if (feirante == null)
				return NotFound();

			return Ok(new FeiranteDTO
			{
				Id = feirante.Id,
				Nome = feirante.Nome,
				Email = feirante.Email,
				DataNasc = feirante.DataNasc,
				Sexo = feirante.Sexo,
				Telem = feirante.Telem,
				Pin = feirante.Pin
			});
		}

		// POST: api/Feirante
		/*
         * BODY:
         * {
         *  "nome": "nome",
         *  "email": "email",
         *  "dataNasc": "yyyy-MM-dd",
         *  "sexo": "M/F",
         *  "telem": 0,
         *  "pin": 0
         * }
         */
		[HttpPost]
		public ActionResult<Feirante> Add(Feirante feirante)
		{
			// Validação das regras de negócio

			feirante.Id = 0;

			if (!IsValidEmail(feirante.Email))
				return BadRequest("Email do feirante inválido!");

			if (feirante.DataNasc > DateTime.Now)
				return BadRequest("Data de nascimento do feirante inválida!");

			if (!(feirante.Sexo == 'M'
				|| feirante.Sexo == 'm'
				|| feirante.Sexo == 'F'
				|| feirante.Sexo == 'f'))
				return BadRequest("Sexo do feirante inválida!");

			if (feirante.Telem > 999999999)
				return BadRequest("Número de telefone do feirante inválida!");

			if (feirante.Pin > 9999)
				return BadRequest("Código PIN do feirante inválida!");

			dbContext.Feirantes.Add(feirante);
			dbContext.SaveChanges();
			return CreatedAtAction(nameof(Add), new { id = feirante.Id }, feirante);
		}

		// PUT: api/Feirante/{id}
		/*
         * BODY:
         * {
         *  "id": {id},
         *  "nome": "nome",
         *  "email": "email",
         *  "dataNasc": "yyyy-MM-dd",
         *  "sexo": "M/F",
         *  "telem": 0,
         *  "pin": 0
         * }
         */
		[HttpPut("{id}")]
		public IActionResult Update(int id, Feirante feirante)
		{
			if (!feirante.Id.Equals(id))
				return BadRequest();

			// Validação das regras de negócio

			if (!IsValidateFeirante(feirante))
				return BadRequest("O produto inserido não é válido!");

			dbContext.Feirantes.Entry(feirante).State = EntityState.Modified;
			dbContext.SaveChanges();
			return NoContent();
		}

		// DELETE: api/Feirante/{id}
		[HttpDelete("{id}")]
		public IActionResult Delete(int id)
		{
			if (dbContext.Feirantes == null)
				return NotFound();

			var feirante = dbContext.Feirantes.SingleOrDefault(f => f.Id == id);

			if (feirante == null)
				return NotFound();

			// São removidos todos os produtos associados ao feirante.

			dbContext.Feirantes.Remove(feirante);
			dbContext.SaveChanges();
			return NoContent();
		}

		private bool IsValidateFeirante(Feirante feirante)
		{
			if (!IsValidEmail(feirante.Email))
				return false;

			if (feirante.DataNasc > DateTime.Now)
				return false;

			if (!(feirante.Sexo == 'M'
				|| feirante.Sexo == 'm'
				|| feirante.Sexo == 'F'
				|| feirante.Sexo == 'f'))
				return false;

			if (feirante.Telem > 999999999)
				return false;

			if (feirante.Pin > 9999)
				return false;

			return true;
		}

		private bool IsValidEmail(string email)
		{
			// Defina a expressão regular para validação de e-mail
			string pattern = @"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$";

			// Crie um objeto Regex com a expressão regular
			Regex regex = new Regex(pattern);

			// Verifique se o e-mail corresponde à expressão regular
			return regex.IsMatch(email);
		}
	}
}
