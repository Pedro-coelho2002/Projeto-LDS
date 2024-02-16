using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using NuGet.DependencyResolver;
using System;
using UrbanMarket.Data;
using UrbanMarket.Models;

namespace UrbanMarket.Controllers
{
    public class CategoriaProdutoController : Controller
    {
        private readonly UrbanMarketContext dbContext;

        public CategoriaProdutoController(UrbanMarketContext urbanMarketContext)
        {
            this.dbContext = urbanMarketContext;
        }

        public IActionResult Index()
        {
            List<CategoriaProduto> categorias = dbContext.Categorias.ToList();
            return View(categorias);
        }

        // HTTP Method: GET
        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Create(CategoriaProduto categoriaProduto)
        {
            if (dbContext.Categorias.Any(c => c.Categoria == categoriaProduto.Categoria))
            {
                ModelState.AddModelError("Error 001","Uma categoria com o mesmo nome já existe.");
            }

            if (!ModelState.IsValid)
            {
                return View(categoriaProduto);
            }

            categoriaProduto.Id = 0;
            dbContext.Categorias.Add(categoriaProduto);
            dbContext.SaveChanges();
            return RedirectToAction("Index");

        }

        //HTTP Method: GET
        public IActionResult Edit(int? id)
        {
            CategoriaProduto categoriaProduto;

            if (id == null)
            {
                return BadRequest();
            }

            if (id <= 0)
            {
                return NotFound();
            }

            categoriaProduto = dbContext.Categorias.Find(id);

            if (categoriaProduto == null)
            {
                return NotFound();
            }

            return View(categoriaProduto);

        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Edit( CategoriaProduto categoriaProduto)
        {
            if (dbContext.Categorias.Any(c => c.Categoria == categoriaProduto.Categoria))
            {
                // Retorna um BadRequest indicando que a categoria já existe
                ModelState.AddModelError("Error 001", "Uma categoria com o mesmo nome já existe.");
            }

            //validations 
            if (!ModelState.IsValid) { return View(categoriaProduto); }

            //update data
            dbContext.Categorias.Update(categoriaProduto);
            dbContext.SaveChanges();
            return RedirectToAction("Index");
        }

        //HTTP Method: GET

        public IActionResult Delete(int? id)
        {
            CategoriaProduto categoriaProduto;

            categoriaProduto = dbContext.Categorias.Find(id);

            if (categoriaProduto == null)
            {
                return NotFound();
            }

            return View(categoriaProduto);

        }

        [HttpPost, ActionName("DeletePost")]
        [ValidateAntiForgeryToken]

        public IActionResult DeleteCategoria(int? id)
        {
            CategoriaProduto categoriaProduto = dbContext.Categorias.Find(id);

            if (categoriaProduto == null)
                return NotFound();

            dbContext.Categorias.Remove(categoriaProduto);
            dbContext.SaveChanges();
            return RedirectToAction("Index");
        }
    }
}
