package model.seletor;

import java.util.Calendar;

public class ComentarioSeletor extends AbstractBaseSeletor{

	private Integer id;
	private String conteudo;
	private Calendar dataPostagemInicial;
	private Calendar dataPostagemFinal;
	private Integer idUsuario;
	private Integer idTweet;
	private Calendar dataPostagem;
	
	public boolean possuiFiltro() {
		return ( this.id != null ) || 
			   ( this.conteudo != null && !this.conteudo.trim().isEmpty() ) ||
			   ( this.dataPostagemInicial != null ) ||
			   ( this.dataPostagemFinal != null ) ||
			   ( this.idTweet != null ) ||
			   ( this.idUsuario != null ) ||
			   ( this.dataPostagem != null);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Calendar getDataPostagemInicial() {
		return dataPostagemInicial;
	}

	public void setDataPostagemInicial(Calendar dataInicial) {
		this.dataPostagemInicial = dataInicial;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdTweet() {
		return idTweet;
	}

	public void setIdTweet(Integer idTweet) {
		this.idTweet = idTweet;
	}

	public Calendar getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(Calendar dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public Calendar getDataPostagemFinal() {
		return dataPostagemFinal;
	}

	public void setDataPostagemFinal(Calendar dataPostagemFinal) {
		this.dataPostagemFinal = dataPostagemFinal;
	}

}
