package model.seletor;

import java.util.Calendar;

public class ComentarioSeletor {

	private Integer id;
	private String conteudo;
	private Calendar dataPostagemInicial;
	private Calendar dataPostagemFinal;
	private Integer idUsuario;
	private Integer idTweet;
	private Calendar dataPostagem;

	private Integer limite;
	private Integer pagina; 
	
	public boolean possuiFiltro() {
		return ( this.id != null ) || 
			   ( this.conteudo != null && !this.conteudo.trim().isEmpty() ) ||
			   ( this.dataPostagemInicial != null ) ||
			   ( this.dataPostagemFinal != null ) ||
			   ( this.idTweet != null ) ||
			   ( this.idUsuario != null ) ||
			   ( this.dataPostagem != null);
	}
	
	public boolean possuiPaginacao() {
		return this.pagina > 0 && this.limite > 0;
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

	public Integer getLimmite() {
		return limite;
	}

	public void setLimmite(Integer limmite) {
		this.limite = limmite;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	public Calendar getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(Calendar dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public Integer getLimite() {
		return limite;
	}

	public void setLimite(Integer limite) {
		this.limite = limite;
	}

	public Calendar getDataPostagemFinal() {
		return dataPostagemFinal;
	}

	public void setDataPostagemFinal(Calendar dataPostagemFinal) {
		this.dataPostagemFinal = dataPostagemFinal;
	}

}
