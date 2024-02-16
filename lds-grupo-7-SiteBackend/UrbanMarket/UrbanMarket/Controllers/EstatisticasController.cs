using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using UrbanMarket.Data;
using UrbanMarket.Models;
using System.Collections.Generic;
using System.Linq;

namespace UrbanMarket.Controllers
{
    public class EstatisticasController : Controller
    {
        private readonly UrbanMarketContext dbcontext;

        public EstatisticasController(UrbanMarketContext context)
        {
            this.dbcontext = context;
        }

        public ActionResult Index()
        {
            ViewBag.TotalProdutos = dbcontext.Produtos.Count();

            List<CategoriaProduto> categorias = dbcontext.Categorias
                .Include(c => c.Produtos)
                .ToList();

            ViewBag.Categorias = categorias;

            // Calcula a distribuição de produtos por unidade de medida
            Dictionary<string, int> produtosPorUnidade = new Dictionary<string, int>();

            foreach (var produto in dbcontext.Produtos.Include(p => p.UnidMedida))
            {
                string unidadeMedida = produto.UnidMedida?.UniMedida ?? "Sem Unidade"; // Padrão para produtos sem unidade de medida

                if (!produtosPorUnidade.ContainsKey(unidadeMedida))
                {
                    produtosPorUnidade.Add(unidadeMedida, 0);
                }

                produtosPorUnidade[unidadeMedida]++;
            }

            ViewBag.TotalProdutosPorUnidade = produtosPorUnidade;

			List<Produto> produtos = dbcontext.Produtos.ToList();

			ViewBag.Produtos = produtos;
			ViewBag.TotalProdutos = dbcontext.Produtos.Count();

			List<Feirante> feirantes = dbcontext.Feirantes.ToList();

			ViewBag.Feirantes = feirantes;
			ViewBag.TotalFeirantes = dbcontext.Feirantes.Count();

			List<ProdutoFeirante> produtosFeirantes = dbcontext.ProdutosFeirantes
				.Include(produtosFeirantes => produtosFeirantes.Produto)
				.Include(produtosFeirantes => produtosFeirantes.Feirante)
				.ToList();

			ViewBag.ProdutosFeirantes = produtosFeirantes;
			ViewBag.TotalPublicacoes = dbcontext.ProdutosFeirantes.Count();

			return View();
        }
    }
}
