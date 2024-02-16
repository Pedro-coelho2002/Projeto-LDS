using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarket.Data;
using UrbanMarket.Models;

namespace UrbanMarket.Controllers
{
    public class ProdutoController : Controller
    {

        private readonly UrbanMarketContext context;

        public ProdutoController(UrbanMarketContext context)
        {
            this.context = context;
        }


        public IActionResult Index()
        {
            List<Produto> produtos = context.Produtos.ToList();
            return View(produtos);
        }

        public IActionResult Details(int? id)
        {
            if (id == null || id <= 0)
            {
                return NotFound();
            }

            var produto = context.Produtos
                .Include(p => p.Categoria)
                .Include(p => p.UnidMedida)
                .FirstOrDefault(p => p.Id == id);

            if (produto == null)
            {
                return NotFound();
            }

            ViewBag.Categorias = context.Categorias.ToList();
            ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();

            return View(produto);
        }

        public IActionResult Create()
        {
            ViewBag.Categorias = context.Categorias.ToList();
            ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Create(Produto produto)
        {
            if (ModelState.IsValid)
            {
                // Validar se as UnidadesMedidas e Categorias existem antes de atribuir
                var categoriaExistente = context.Categorias.Any(c => c.Id == produto.CategoriaProdutoId);
                var unidadeMedidaExistente = context.UnidadesMedidas.Any(u => u.Id == produto.UnidMedidaId);

                if (!categoriaExistente || !unidadeMedidaExistente)
                {
                    ModelState.AddModelError("", "Categoria ou Unidade de Medida inválida.");
                    ViewBag.Categorias = context.Categorias.ToList();
                    ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();
                    return View(produto);
                }

                context.Produtos.Add(produto);
                context.SaveChanges();

                return RedirectToAction("Index");
            }

            ViewBag.Categorias = context.Categorias.ToList();
            ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();
            return View(produto);
        }

		public IActionResult Edit(int? id)
		{
			if (id == null || id <= 0)
			{
				return NotFound();
			}

			var produto = context.Produtos
				.Include(p => p.Categoria)
				.Include(p => p.UnidMedida)
				.FirstOrDefault(p => p.Id == id);

			if (produto == null)
			{
				return NotFound();
			}

			ViewBag.Categorias = context.Categorias.ToList();
			ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();

			return View(produto);
		}

		[HttpPost]
		[ValidateAntiForgeryToken]
		public IActionResult Edit(Produto produto)
		{
			if (ModelState.IsValid)
			{
				// Validar se as UnidadesMedidas e Categorias existem antes de atribuir
				var categoriaExistente = context.Categorias.Any(c => c.Id == produto.CategoriaProdutoId);
				var unidadeMedidaExistente = context.UnidadesMedidas.Any(u => u.Id == produto.UnidMedidaId);

				if (!categoriaExistente || !unidadeMedidaExistente)
				{
					ModelState.AddModelError("", "Categoria ou Unidade de Medida inválida.");
					ViewBag.Categorias = context.Categorias.ToList();
					ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();
					return View(produto);
				}

				context.Produtos.Update(produto);
				context.SaveChanges();

				return RedirectToAction("Index");
			}

			ViewBag.Categorias = context.Categorias.ToList();
			ViewBag.UnidadesMedidas = context.UnidadesMedidas.ToList();
			return View(produto);
		}

        public IActionResult Delete(int? id)
        {
            if (id == null || id <= 0)
            {
                return NotFound();
            }

            var produto = context.Produtos
                .Include(p => p.Categoria)
                .Include(p => p.UnidMedida)
                .FirstOrDefault(p => p.Id == id);

            if (produto == null)
            {
                return NotFound();
            }

            return View(produto);
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public IActionResult DeleteConfirmed(int id)
        {
            var produto = context.Produtos.Find(id);

            if (produto == null)
            {
                return NotFound();
            }

            context.Produtos.Remove(produto);
            context.SaveChanges();

            return RedirectToAction("Index");
        }

    }
}