using Microsoft.EntityFrameworkCore;
using UrbanMarketAPI.Models;

namespace UrbanMarketAPI.Data
{
    public class UrbanMarketContext : DbContext
    {

        public UrbanMarketContext(DbContextOptions<UrbanMarketContext> options) : base(options)
        { }

        public DbSet<CategoriaProduto> Categorias { get; set; }

        public DbSet<EncomendaFeirante> EncomendasFeirantes { get; set; }

        public DbSet<Feirante> Feirantes { get; set; }

        public DbSet<Pedido> Pedidos { get; set; }

        public DbSet<Produto> Produtos { get; set; }

        public DbSet<UnidadeMedida> UnidadesMedidas { get; set; }

        public DbSet<ProdutoFeirante> ProdutosFeirantes { get; set; }
	}
}
