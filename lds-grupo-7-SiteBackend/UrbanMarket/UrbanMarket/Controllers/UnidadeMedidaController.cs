using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarket.Data;
using UrbanMarket.Models;

namespace UrbanMarket.Controllers
{
    public class UnidadeMedidaController : Controller
    {

        private readonly UrbanMarketContext context;

        public UnidadeMedidaController(UrbanMarketContext context)
        {
            this.context = context;
        }

        public IActionResult Index()
        {
            List<UnidadeMedida> unidades = context.UnidadesMedidas.ToList();
            return View(unidades);
        }


        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Create(UnidadeMedida unidade)
        {

            if (context.UnidadesMedidas.Any(c => c.UniMedida == unidade.UniMedida))
            {
                ModelState.AddModelError("Error 001", "Já existe uma unidade de medida com o mesmo nome!");
            }

            context.UnidadesMedidas.Add(unidade);
            context.SaveChanges();

            return RedirectToAction("Index");
        }

        // HTTP Method: GET
        public IActionResult Edit(int? id)
        {
            if (id == null)
            {
                return BadRequest();
            }

            if (id <= 0)
            {
                return NotFound();
            }

            UnidadeMedida unidade = context.UnidadesMedidas.Find(id);

            if (unidade == null)
            {
                return NotFound();
            }

            return View(unidade);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Edit(UnidadeMedida unidade)
        {


            context.UnidadesMedidas.Update(unidade);
            context.SaveChanges();
            return RedirectToAction("Index");
        }

        // HTTP Method: GET
        public IActionResult Delete(int? id)
        {

            UnidadeMedida unidade;

            if (id == null)
            {
                return BadRequest();
            }

            if (id <= 0)
            {
                return NotFound();
            }

            unidade = context.UnidadesMedidas.Find(id);

            if (unidade == null)
            {
                return NotFound();
            }

            return View(unidade);
        }

        [HttpPost, ActionName("DeletePost")]
        [ValidateAntiForgeryToken]
        public IActionResult DeleteUnidade(int? id)
        {
            UnidadeMedida unidade = context.UnidadesMedidas.Find(id);

            if (unidade == null)
            {
                return NotFound();
            }

            context.UnidadesMedidas.Remove(unidade);
            context.SaveChanges();

            return RedirectToAction("Index");
        }

    }
}