using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarketAPI.Data;
using UrbanMarketAPI.DTO;
using UrbanMarketAPI.Models;

namespace UrbanMarketAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UnidadeMedidaController : ControllerBase
    {
        private readonly UrbanMarketContext dbContext;

        public UnidadeMedidaController(UrbanMarketContext urbanMarketContext) => this.dbContext = urbanMarketContext;

        // GET: api/UnidadeMedida
        [HttpGet]
        public ActionResult<IEnumerable<UnidadeMedida>> GetUnidadesMedidas()
        {
            if (dbContext.UnidadesMedidas == null)
                return NotFound();

            var unidadesMedidas = dbContext.UnidadesMedidas
				.Include(unidade => unidade.Produtos)
				.ToList();

            var unidadesMedidasDTO = new List<UnidadeMedidaDTO>();

            foreach (var item in unidadesMedidas)
            {
				unidadesMedidasDTO.Add(new UnidadeMedidaDTO
                {
					Id = item.Id,
					UniMedida = item.UniMedida
				});
			}

            return Ok(unidadesMedidasDTO.ToList());
        }

        // GET: api/UnidadeMedida/{id}
        [HttpGet("{id}")]
        public ActionResult<UnidadeMedida> GetUnidadeMedidaById(int id)
        {
            if (dbContext.UnidadesMedidas == null)
                return NotFound();

            var unidadeMedida = dbContext.UnidadesMedidas
                .SingleOrDefault(u => u.Id == id);

            if (unidadeMedida == null)
                return NotFound();

            return Ok(new UnidadeMedidaDTO { Id = unidadeMedida.Id, UniMedida = unidadeMedida.UniMedida });
        }

        // POST: api/UnidadeMedida
        /*
         * BODY:
         * {
         *  "uniMedida": "nome"
         * }
         */
        [HttpPost]
        public ActionResult<UnidadeMedida> Add(UnidadeMedida unidadeMedida)
        {
            // Validação das regras de negócio

            // Verificar se já existe uma medida com o mesmo nome
            if (dbContext.UnidadesMedidas.Any(c => c.UniMedida == unidadeMedida.UniMedida))
            {
                return BadRequest("Já existe uma unidade de medida com o mesmo nome!");
            }

            unidadeMedida.Id = 0;
            dbContext.UnidadesMedidas.Add(unidadeMedida);
            dbContext.SaveChanges();
            return CreatedAtAction(nameof(Add), new { id = unidadeMedida.Id }, unidadeMedida);
        }

        // PUT: api/UnidadeMedida/{id}
        /*
         * BODY:
         * {
         *  "id": {id},
         *  "uniMedida": "novo nome"
         * }
        */
        [HttpPut("{id}")]
        public IActionResult Update(int id, UnidadeMedida unidadeMedida)
        {
            if (!unidadeMedida.Id.Equals(id))
                return BadRequest();

            // Validação das regras de negócio

            dbContext.UnidadesMedidas.Entry(unidadeMedida).State = EntityState.Modified;
            dbContext.SaveChanges();
            return NoContent();
        }

        // DELETE: api/UnidadeMedida/{id}       
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            if (dbContext.UnidadesMedidas == null)
                return NotFound();

            var unidadeMedida = dbContext.UnidadesMedidas
                .Include(unidade => unidade.Produtos)
                .SingleOrDefault(c => c.Id == id);

            if (unidadeMedida == null)
                return NotFound();

            // Se a unidade de medida possuir produtos, não é possível excluí-la
            if (unidadeMedida.Produtos.Count() != 0)
                return BadRequest("Esta unidade possui produtos.");

            dbContext.UnidadesMedidas.Remove(unidadeMedida);
            dbContext.SaveChanges();
            return NoContent();
        }
    }
}
