namespace UrbanMarketAPI.DTO
{
	public class FeiranteDTO
	{
		/// <summary>
		/// Identificador único do Feirante/Expositor.
		/// </summary>
		public int Id { get; set; }

		/// <summary>
		/// Nome do Feirante/Expositor.
		/// </summary>
		public string? Nome { get; set; }

		/// <summary>
		/// Email do Feirante/Expositor.
		/// </summary>
		public string? Email { get; set; }

		/// <summary>
		/// Data de Nascimento do Feirante/Expositor.
		/// </summary>
		public DateTime? DataNasc { get; set; }

		/// <summary>
		/// Sexo do Feirante.
		/// </summary>
		public char? Sexo { get; set; }

		/// <summary>
		/// Número de telemóvel do Feirante/Expositor.
		/// </summary>
		public int Telem { get; set; }

		/// <summary>
		/// Código PIN que o Feirante/Expositor define na aplicação para realizar o login.
		/// </summary>
		public int Pin { get; set; }
	}
}
